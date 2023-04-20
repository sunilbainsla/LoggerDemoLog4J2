package com.logger.sunil.springboot.loggerdemo;

public class RetryTransformer implements Transformer<Object, Object, KeyValue<Object, Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryTransformer.class);

    private int maxAttempts = -1;
    private int retryCount = 0;

    @Override
    public void init(ProcessorContext context) {
    }

    @Override
    public KeyValue<Object, Object> transform(Object key, Object value) {
        try {
            // process the message here
            return KeyValue.pair(key, value);
        } catch (Exception ex) {
            if (ex instanceof KafkaException) {
                if (maxAttempts == -1 || retryCount < maxAttempts) {
                    LOGGER.warn("Caught KafkaException: {}. Retrying...", ex.getMessage());
                    retryCount++;
                    throw new RetryException(ex.getMessage());
                } else {
                    LOGGER.error("Reached max retry attempts. Sending to DLQ.");
                    // throw to DLQ here
                }
            } else {
                LOGGER.error("Caught non-KafkaException. Sending to DLQ.", ex);
                // throw to DLQ here
            }
            return null;
        }
    }

    @Override
    public void close() {
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public static class RetryException extends RuntimeException {
        public RetryException(String message) {
            super(message);
        }
    }
}
