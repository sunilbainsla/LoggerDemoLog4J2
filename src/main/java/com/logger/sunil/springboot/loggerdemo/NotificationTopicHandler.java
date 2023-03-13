package com.logger.sunil.springboot.loggerdemo;

public class NotificationTopicHandler implements TopicHandler {
    @Override
    public void handle(String topicName, String message) {
        System.out.println("this is Notification handler");
    }
}