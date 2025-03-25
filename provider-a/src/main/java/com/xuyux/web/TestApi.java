package com.xuyux.web;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
public class TestApi {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from " + appName + "1";
    }

    @PostMapping("/testUploadBase64Img")
    public String testUploadBase64Img(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Uploading file {}", file.getOriginalFilename());
        file.transferTo(new File("/Users/xuyux/Downloads/" + DateUtil.date() + ".jpg"));
        return file.getOriginalFilename();
    }

}
