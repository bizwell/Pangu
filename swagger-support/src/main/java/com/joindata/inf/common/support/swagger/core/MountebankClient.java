package com.joindata.inf.common.support.swagger.core;

import org.json.simple.parser.ParseException;
import org.mbtest.javabank.fluent.ImposterBuilder;
import org.mbtest.javabank.fluent.PredicateTypeBuilder;
import org.mbtest.javabank.fluent.ResponseBuilder;
import org.mbtest.javabank.fluent.StubBuilder;
import org.mbtest.javabank.http.imposters.Imposter;

import com.alibaba.fastjson.JSON;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

import lombok.Data;

/**
 * 服务虚拟化客户端
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年4月14日
 */
@Data
public class MountebankClient
{

    private Logger logger = Logger.get();

    private static final String CONTENT_TYPE_JSON = "application/json";

    private int mockServerPort;

    /**
     * 新增一个mock 接口
     * 
     * @param mockAPI
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public Imposter createMock(MockAPI mockAPI) throws ParseException
    {
        logger.info("创建mock 服务： {}", mockAPI);
        ImposterBuilder imposterBuilder = ImposterBuilder.anImposter();
        StubBuilder stubBuilder = imposterBuilder.onPort(mockServerPort).stub();
        PredicateTypeBuilder predicateTypeBuilder = stubBuilder.predicate();
        predicateTypeBuilder.equals().method(mockAPI.getRequestMethod());
        predicateTypeBuilder.equals().path(mockAPI.getRequestPath());
        if(StringUtil.isNotEmpty(mockAPI.getRequestBody()))
        {
            String contentType = mockAPI.getContentType();
            if(StringUtil.isNotEmpty(contentType) && contentType.contains(CONTENT_TYPE_JSON))
            {
                predicateTypeBuilder.contains().header("content-type", CONTENT_TYPE_JSON);
                predicateTypeBuilder.equals().build().put("body", JSON.parse(mockAPI.getRequestBody()));
            }
            else
            {
                predicateTypeBuilder.equals().header("content-type", mockAPI.getContentType());
                predicateTypeBuilder.equals().build().withBody(mockAPI.getRequestBody());
            }

        }

        mockAPI.getQueryParam().forEach((k, v) -> {
            predicateTypeBuilder.equals().query(k, v);
        });

        // mock response
        ResponseBuilder responseBuilder = stubBuilder.response();
        responseBuilder.is().header("content-type", mockAPI.getContentType()).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE").header("Access-Control-Allow-Headers", "content-type").body(mockAPI.getResponseBody());

        if(mockAPI.isAllowOrigin())
        {
            // 增加跨域支持
            imposterBuilder.stub().predicate().equals().method("OPTIONS").end().end().response().is().header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE").header("Access-Control-Allow-Headers", "content-type");
        }

        Imposter imposter = imposterBuilder.build();

        return imposter;
    }

    public MountebankClient(int mockServerPort)
    {
        this.mockServerPort = mockServerPort;
    }
}
