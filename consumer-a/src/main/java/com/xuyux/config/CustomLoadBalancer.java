package com.xuyux.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 load balancer
 * @author xuyux
 * @date 2024/9/17 10:50
 */
@Slf4j
public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * 本实例的 version
     */
    private final String instanceVersion;

    /**
     * 提供方的服务名
     */
    private final String serviceId;

    /**
     * 提供者候选
     */
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;

    /**
     * 轮训用下标
     */
    final AtomicInteger position;

    public CustomLoadBalancer(String instanceVersion, String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers) {
        this(instanceVersion, serviceId, serviceInstanceListSuppliers, (new Random()).nextInt(1000));
    }

    public CustomLoadBalancer(String instanceVersion, String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers, int position) {
        this.instanceVersion = instanceVersion;
        this.serviceId = serviceId;
        this.serviceInstanceListSuppliers = serviceInstanceListSuppliers;
        this.position = new AtomicInteger(position);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSuppliers.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) ->
                this.processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback)supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: {}", this.serviceId);
            }
            return new EmptyResponse();
        }
        List<ServiceInstance> instanceList = chooseSameVersion(instances);
        //列表中有多个值时采用轮训
        if (instanceList.size() == 1) {
            return new DefaultResponse(instanceList.getFirst());
        }
        int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }

    /**
     * 选择相同版本的服务
     * @param instances 发现的实例列表
     * @return 挑选的实例列表
     */
    private List<ServiceInstance> chooseSameVersion(List<ServiceInstance> instances) {
        //被选择的实例
        List<ServiceInstance> chosenInstances = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            //获取实例的 Metadata
            Map<String, String> metadata = instance.getMetadata();
            if (metadata.containsKey("version")) {
                //判断依据 同版本 -> release -> 其他
                String version = metadata.get("version");
                if (version.equals(this.instanceVersion)) {
                    chosenInstances.add(instance);
                    continue;
                }
                if ("release".equals(this.instanceVersion)) {
                    chosenInstances.add(instance);
                    continue;
                }
                if (chosenInstances.isEmpty()) {
                    chosenInstances.add(instance);
                }
            } else {
                //如果发现的实例列表没有 version 默认原先
                return instances;
            }
        }
        return chosenInstances;
    }


}
