package com.xuyux.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class CustomLoadBalancerConfiguration {

    @Bean
    ReactorLoadBalancer<ServiceInstance> preferZoneAndVersionBalancer(Environment environment,
                                                                      LoadBalancerClientFactory clientFactory) {
        String version = environment.getProperty("eureka.instance.metadata-map.version");
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        if (!StringUtils.hasText(version)) {
            version = "release";
        }
        return new CustomLoadBalancer(version, name, clientFactory.getLazyProvider(name,
                ServiceInstanceListSupplier.class));
    }

}
