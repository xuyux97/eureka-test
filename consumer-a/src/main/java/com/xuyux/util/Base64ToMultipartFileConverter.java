package com.xuyux.util;

import cn.hutool.core.date.DateUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/**
 * 转换器
 * @author xuyux
 * @date 2025/3/25 23:39
 */
public class Base64ToMultipartFileConverter {

    /**
     * 转换方法
     * @param base64Data base64数据
     * @return 结果
     */
    public static MultipartFile convert(String base64Data) {
        // 分割 Base64 数据头和实际内容
        String[] parts = base64Data.split(",");
        String imageType = "";
        String base64Image = "";

        if (parts.length > 1) {
            // 提取文件类型
            String dataHeader = parts[0];
            imageType = dataHeader.split(";")[0].split(":")[1];
            base64Image = parts[1];
        } else {
            base64Image = base64Data; // 假设没有数据头
        }

        // 解码 Base64 数据
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        // 生成文件名和内容类型
        String fileName = DateUtil.now() + ".jpg";

        return new Base64DecodeMultipartFile(decodedBytes, fileName, imageType);
    }

}
