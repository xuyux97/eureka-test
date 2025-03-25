package com.xuyux.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 自定义 MultipartFile 实现类
 * 该类直接基于内存中的字节数组实现了 MultipartFile 接口。这个类本身不需要依赖本地文件。
 * @author xuyux
 * @date 2025/3/25 23:22
 */
public class Base64DecodeMultipartFile implements MultipartFile {

    /**
     * 文件内容
     */
    private final byte[] fileContent;

    /**
     * 文件名称
     */
    private final String fileName;

    /**
     * 文件类型
     */
    private final String contentType;

    /**
     * 构造方法
     * @param fileContent 文件内容
     * @param fileName 文件名称
     * @param contentType 文件类型
     */
    public Base64DecodeMultipartFile(byte[] fileContent, String fileName, String contentType) {
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File dest) throws IllegalStateException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(dest));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
