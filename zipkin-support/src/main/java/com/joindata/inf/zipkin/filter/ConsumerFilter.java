package com.joindata.inf.zipkin.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.joindata.inf.zipkin.TraceContext;
import com.joindata.inf.zipkin.cst.TraceConstants;
import com.joindata.inf.zipkin.util.Ids;
import com.joindata.inf.zipkin.util.Networks;
import com.joindata.inf.zipkin.util.Times;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rayee on 2017/10/23.
 */
//@Activate(group = {Constants.CONSUMER})
public class ConsumerFilter implements Filter {

    public static final String FILTER_NAME = "consumerFilter";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

//        if (TraceContext.getTraceId() == null) {
//            invoker.invoke(invocation);
//        }
        Stopwatch stopwatch = Stopwatch.createStarted();

        Span consumerSpan = startTrace(invoker, invocation);

        System.err.println("consumer invoke before: ");
        TraceContext.print();

        Result result = invoker.invoke(invocation);
        RpcResult rpcResult = (RpcResult) result;

        System.err.println("consumer invoke after: ");
        TraceContext.print();

        endTrace(invoker, rpcResult, consumerSpan, stopwatch);

        return rpcResult;
    }

    private Span startTrace(Invoker<?> invoker, Invocation invocation) {
        //consumer span data
        Span consumerSpan = new Span();
        consumerSpan.setId(Ids.get());
        consumerSpan.setTrace_id(TraceContext.getTraceId());
        consumerSpan.setParent_id(TraceContext.getSpanId());
        String serviceName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
        consumerSpan.setName(serviceName);

        long timestamp = Times.currentMicros();
        consumerSpan.setTimestamp(timestamp);

        URL provider = invoker.getUrl();
        consumerSpan.addToAnnotations(Annotation.create(timestamp, TraceConstants.ANNO_CS, Endpoint.create(serviceName, Networks.ip2Num(provider.getHost()), provider.getPort())));

        Map<String, String> attches = invocation.getAttachments();
        attches.put(TraceConstants.TRACE_ID, String.valueOf(consumerSpan.getTrace_id()));
        attches.put(TraceConstants.SPAN_ID, String.valueOf(consumerSpan.getId()));

        return consumerSpan;
    }

    private void endTrace(Invoker invoker, Result result, Span consumerSpan, Stopwatch watch) {
        consumerSpan.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));

        // cr annotation
        consumerSpan.addToAnnotations(Annotation.create(Times.currentMicros(), TraceConstants.ANNO_CR, Endpoint.create(consumerSpan.getName(), Networks.ip2Num(invoker.getUrl().getHost()), invoker.getUrl().getPort())));

        // exception catch
        Throwable throwable = result.getException();
        if (throwable != null) {
            // attach exception
            consumerSpan.addToBinary_annotations(BinaryAnnotation.create(
                    "Exception", Throwables.getStackTraceAsString(throwable), null
            ));
        }

        // collect the span
        TraceContext.addSpan(consumerSpan);
    }

}
