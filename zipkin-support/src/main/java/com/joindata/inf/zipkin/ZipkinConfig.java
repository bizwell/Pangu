package com.joindata.inf.zipkin;

import com.github.kristofa.brave.SpanCollectorMetricsHandler;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.collector.HttpSpanCollector;
import com.joindata.inf.zipkin.collector.KafkaSpanCollector;
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
    private ZipkinProperties zipkinProperties;

    @Bean(name = "traceAgent")
    public TraceAgent initTraceAgent() {
        SpanCollectorMetricsHandler metrics = new SimpleMetricsHandler();
        // set flush interval to 0 so that tests can drive flushing explicitly
        HttpSpanCollector.Config httpConfig =
                HttpSpanCollector.Config.builder()
                        .compressionEnabled(true)
                        .flushInterval(0)
                        .build();
        KafkaSpanCollector.Config kafkaConfig =
                KafkaSpanCollector.Config.builder("10.10.110.48:9092,10.10.110.49:9092,10.10.110.50:9092")
                        .flushInterval(0)
                        .build();
        return new TraceAgent(HttpSpanCollector.create(zipkinProperties.getServer(), httpConfig, metrics));
//        return new TraceAgent(KafkaSpanCollector.create(kafkaConfig, metrics));
    }

}
