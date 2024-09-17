package com.xuyux.web;

import com.xuyux.config.CustomLoadBalancer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider-a")
@LoadBalancerClient(name = "provider-a", configuration = CustomLoadBalancer.class)
public interface ProviderFeignClient {

    @GetMapping("/hello")
    String hello();

}
