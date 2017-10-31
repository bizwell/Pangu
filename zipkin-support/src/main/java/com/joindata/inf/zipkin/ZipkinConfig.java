package com.joindata.inf.zipkin;

import com.joindata.inf.common.support.dubbo.properties.DubboProperties;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.filter.TraceFilter;
import com.joindata.inf.zipkin.properties.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by Rayee on 2017/10/26.
 */
@Configuration
public class ZipkinConfig {

    @Resource
    private DubboProperties dubboProperties;

    @Resource
    private ZipkinProperties zipkinProperties;

    @Bean(name = "traceFilter")
    public TraceFilter initTraceFilter() {
        TraceFilter traceFilter = new TraceFilter(dubboProperties, new TraceAgent(zipkinProperties.getServer()));
        return traceFilter;
    }

}
