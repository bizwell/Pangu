package com.joindata.inf.common.support.swagger.core;

import java.util.Map;

import lombok.Data;

@Data
public class MockAPI
{

    /** 请求路径 */
    private String requestPath;

    /** 请求url后面参数 */
    private Map<String, String> queryParam;

    /** 请求体 */
    private String requestBody;

    /**
     * 响应信息
     */
    private String responseBody;

    private String contentType;

    private String requestMethod;

    /**
     * mock 的端口号
     */
    private int port;

    private boolean allowOrigin;

    public void setAllowOrigin(boolean allowOrigin)
    {
        this.allowOrigin = allowOrigin;
    }
}
