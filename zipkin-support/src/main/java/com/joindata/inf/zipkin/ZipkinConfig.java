package com.joindata.inf.zipkin;

import com.joindata.inf.common.support.dubbo.properties.DubboProperties;
import com.joindata.inf.zipkin.filter.TraceFilter;
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

    @Bean(name = "traceFilter")
    public TraceFilter initTraceFilter() {
        TraceFilter traceFilter = new TraceFilter(dubboProperties);
        return traceFilter;
    }

}
