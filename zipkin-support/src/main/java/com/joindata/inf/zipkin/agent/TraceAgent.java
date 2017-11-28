package com.joindata.inf.zipkin.agent;

import com.github.kristofa.brave.AbstractSpanCollector;
import com.github.kristofa.brave.SpanCollectorMetricsHandler;
import com.joindata.inf.zipkin.SimpleMetricsHandler;
import com.joindata.inf.zipkin.collector.HttpSpanCollector;
import com.twitter.zipkin.gen.Span;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceAgent {

    private AbstractSpanCollector collector;

    private final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, r -> {
                Thread worker = new Thread(r);
                worker.setName("TRACE-AGENT-WORKER");
                worker.setDaemon(true);
                return worker;
            });

    public TraceAgent(AbstractSpanCollector collector) {
        this.collector = collector;
    }

    public void send(final List<Span> spans) {
        if (spans != null && !spans.isEmpty()) {
            executor.submit(() -> {
                for (Span span : spans) {
                    collector.collect(span);
                }
                collector.flush();
            });
        }
    }
}
