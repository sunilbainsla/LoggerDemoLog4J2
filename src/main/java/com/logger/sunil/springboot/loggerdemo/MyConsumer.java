package com.logger.sunil.springboot.loggerdemo;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer {

    private final RetryTemplate retryTemplate;

    public MyConsumer(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Bean
    public Consumer<KStream<byte[], String>> consumer() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(-1); // Set the maximum number of attempts to -1 for infinite retries

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(5000); // Set the backoff period between attempts (in milliseconds)

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setThrowLastExceptionOnExhausted(false); // Do not throw an exception if the maximum number of retries is reached

        return input -> input.foreach((key, value) -> {
            retryTemplate.execute(context -> {
                // Your message processing code here
                // If an exception is thrown, the RetryTemplate will retry indefinitely
                return null;
            });
        });
    }
}
