package com.logger.sunil.springboot.loggerdemo;

public interface TopicHandler {
    void handle(String topicName, String message);
}