package com.joindata.inf.zipkin.agent;

import com.github.kristofa.brave.SpanCollectorMetricsHandler;
import com.google.common.collect.Lists;
import com.joindata.inf.zipkin.SimpleMetricsHandler;
import com.joindata.inf.zipkin.collector.HttpSpanCollector;
import com.joindata.inf.zipkin.collector.KafkaSpanCollector;
import com.joindata.inf.zipkin.properties.ZipkinProperties;
import com.twitter.zipkin.gen.Span;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by Rayee on 2017/10/26.
 */
@Configuration
public class AgentBeanFactory {

    @Resource
    private ZipkinProperties zipkinProperties;

    @SuppressWarnings(value = "deprecation")
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
                KafkaSpanCollector.Config.builder(zipkinProperties.getKafkaServer())
                        .flushInterval(0)
                        .build();
//        return new TraceAgent(HttpSpanCollector.create(zipkinProperties.getServer(), httpConfig, metrics));
        return new TraceAgent(KafkaSpanCollector.create(kafkaConfig, metrics));
    }

}
