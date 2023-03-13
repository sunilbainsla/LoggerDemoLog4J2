package com.logger.sunil.springboot.loggerdemo;

public class DefaultTopicHandler implements TopicHandler {
    @Override
    public void handle(String topicName, String message) {
        System.out.println("this is Default handler");
    }
}