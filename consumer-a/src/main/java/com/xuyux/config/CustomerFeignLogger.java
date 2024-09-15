package com.xuyux.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

/**
 * feign 日志打印重写
 * @author xuyux
 * @date 2024/9/12 22:47
 */
@Slf4j
public class CustomerFeignLogger extends Logger{

    @Override
    protected void log(String s, String s1, Object... objects) {
        log.info(String.format(methodTag(s) + s1, objects));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        byte[] arrBody = request.body();
        String body = arrBody == null ? "" : new String(arrBody);
        log.info("[feign request started]\n{} {}\nheaders: {}\nbody: {}\n",
                request.httpMethod(),
                request.url(),
                CombineHeaders(request.headers()),
                body);
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        int status = response.status();

        String content = "";
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData;
            try {
                bodyData = Util.toByteArray(response.body().asInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (bodyData.length > 0) {
                content = decodeOrDefault(bodyData, UTF_8, "Binary data");
            }
            response = response.toBuilder().body(bodyData).build();
        }

        log.info("[feign request ended]\ncost time(ms): {} status:{} from {} {}\nresponse:\n{}",
                elapsedTime,
                status,
                response.request().httpMethod(),
                response.request().url(),
                content);

        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }

    private static String CombineHeaders(Map<String, Collection<String>> headers) {
        StringBuilder sb = new StringBuilder();
        if (headers != null && !headers.isEmpty()) {
            sb.append("Headers:\r\n");
            for (Map.Entry<String, Collection<String>> ob : headers.entrySet()) {
                for (String val : ob.getValue()) {
                    sb.append("  ").append(ob.getKey()).append(": ").append(val).append("\r\n");
                }
            }
        }
        return sb.toString();
    }

}
