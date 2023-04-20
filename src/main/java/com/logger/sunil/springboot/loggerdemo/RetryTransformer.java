package com.logger.sunil.springboot.loggerdemo;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

public class RetryTransformer<V> implements ValueTransformer<V, V> {
    private final RetryTemplate retryTemplate;

    public RetryTransformer(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void init(ProcessorContext context) {
        // No initialization required
    }

    @Override
    public V transform(V value) {
        try {
            return retryTemplate.execute((RetryCallback<V, RuntimeException>) context -> value);
        } catch (Exception e) {
            throw new RuntimeException("Retry failed after exhausting all attempts", e);
        }
    }

    @Override
    public void close() {
        // No resources to release
    }
}
