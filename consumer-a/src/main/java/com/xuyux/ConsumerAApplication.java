package com.xuyux;

import com.xuyux.config.CustomLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@LoadBalancerClient(name = "provider-a", configuration = CustomLoadBalancerConfiguration.class)
public class ConsumerAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerAApplication.class, args);
    }
}