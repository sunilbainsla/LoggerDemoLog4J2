package com.logger.sunil.springboot.loggerdemo;

public class NoMDCTopicHandler implements TopicHandler {
    @Override
    public void handle(String topicName, String message) {
        System.out.println("DONT ADD MDC");
    }
}