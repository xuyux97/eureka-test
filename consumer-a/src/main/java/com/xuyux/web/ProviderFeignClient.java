package com.xuyux.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("provider-a")
public interface ProviderFeignClient {

    @GetMapping("/hello")
    String hello();

    /**
     * 需要指定 consumes，否则后端接口认为上送的不是 multipartFile
     * @param file 文件
     * @return 结果
     */
    @PostMapping(value = "/testUploadBase64Img",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String testUploadBase64Img(@RequestPart("file") MultipartFile file);

}
