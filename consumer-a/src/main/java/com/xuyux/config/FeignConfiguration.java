package com.xuyux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    CustomerFeignLogger feignLogger() {
        return new CustomerFeignLogger();
    }

}
