package com.xuyux.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider-a")
public interface ProviderFeignClient {

    @GetMapping("/hello")
    String hello();

}
