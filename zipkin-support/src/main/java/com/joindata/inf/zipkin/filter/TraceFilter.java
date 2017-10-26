package com.joindata.inf.zipkin.filter;

import com.google.common.base.Stopwatch;
import com.joindata.inf.common.basic.annotation.FilterComponent;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.dubbo.properties.DubboProperties;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.zipkin.TraceContext;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.cst.TraceConstants;
import com.joindata.inf.zipkin.util.Ids;
import com.joindata.inf.zipkin.util.ServerInfo;
import com.joindata.inf.zipkin.util.Times;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 过滤器，创建请求的唯一ID
 * Created by Rayee on 2017/10/23.
 */
@Component
@FilterComponent
public class TraceFilter implements Filter {

    private Logger logger = Logger.get();

//    @Resource
//    private ZipkinProperties zipkinProperties;

    @Resource
    private DubboProperties dubboProperties;

    private TraceAgent agent;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        if (zipkinProperties.getEnable() != 1) {
//            return;
//        }
//        agent = new TraceAgent(zipkinProperties.getServer());
        agent = new TraceAgent("http://172.168.168.153:9411");
        logger.info("init zipkin filter with config {}", JsonUtil.toJSON(new Object[]{filterConfig}));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        if (zipkinProperties.getEnable() != 1) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //do trace
        Stopwatch stopwatch = Stopwatch.createStarted();
        //root span
        Span rootSpan = startTrace(request);
        //trace context
        TraceContext.start();
        TraceContext.setTraceId(rootSpan.getTrace_id());
        TraceContext.setSpanId(rootSpan.getId());
        TraceContext.addSpan(rootSpan);

        filterChain.doFilter(servletRequest, servletResponse);

        endTrace(request, rootSpan, stopwatch);
    }

    private Span startTrace(HttpServletRequest request) {

        String apiName = BootInfoHolder.getAppId().concat(":").concat(request.getRequestURI());
        Span apiSpan = new Span();

        //调用链初始化信息
        long id = Ids.get();
        apiSpan.setId(id);
        apiSpan.setTrace_id(id);
        apiSpan.setName(apiName);
        long timestamp = Times.currentMicros();
        apiSpan.setTimestamp(timestamp);

        apiSpan.addToAnnotations(Annotation.create(timestamp, TraceConstants.ANNO_SR, Endpoint.create(apiName, ServerInfo.IP4, request.getLocalPort())));

        apiSpan.addToBinary_annotations(BinaryAnnotation.create("name", BootInfoHolder.getAppId(), null));

        apiSpan.addToBinary_annotations(BinaryAnnotation.create("owner", dubboProperties.getAppOwner(), null));

        //api描述可加可不加，毕竟也没人想一个个加，所以就不加了

        return apiSpan;
    }

    private void endTrace(HttpServletRequest request, Span span, Stopwatch stopwatch) {
        span.addToAnnotations(Annotation.create(Times.currentMicros(), TraceConstants.ANNO_SS, Endpoint.create(span.getName(), ServerInfo.IP4, request.getLocalPort())));
        span.setDuration(stopwatch.stop().elapsed(TimeUnit.MICROSECONDS));
        agent.send(TraceContext.getSpans());
    }

    @Override
    public void destroy() {
        TraceContext.clear();
    }
}
































