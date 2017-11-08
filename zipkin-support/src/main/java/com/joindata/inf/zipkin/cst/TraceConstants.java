package com.joindata.inf.zipkin.cst;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public interface TraceConstants {

    /**
     * Trace id
     */
    String TRACE_ID = "traceId";

    /**
     * Span id
     */
    String SPAN_ID = "spanId";

    /**
     * Client Start,表示客户端发起请求
     */
    String ANNO_CS = "cs";

    /**
     * Client Received,表示客户端获取到服务端返回信息
     */
    String ANNO_CR = "cr";

    /**
     * Server Receive,表示服务端收到请求
     */
    String ANNO_SR = "sr";

    /**
     * Server Send,表示服务端完成处理，并将结果发送给客户端
     */
    String ANNO_SS = "ss";

    /**
     * The sr time
     */
    String SR_TIME = "srt";

    /**
     * The ss time
     */
    String SS_TIME = "sst";

    String ARGS = "参数";

    String APP_ID = "appId";

}
