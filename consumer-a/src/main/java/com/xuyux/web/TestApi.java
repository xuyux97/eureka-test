package com.xuyux.web;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {

    @Resource
    ProviderFeignClient providerFeignClient;

    @GetMapping("/hello")
    public String hello() {
        return providerFeignClient.hello();
    }

}
