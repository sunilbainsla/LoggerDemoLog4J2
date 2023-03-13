package com.logger.sunil.springboot.loggerdemo;

public class ACSTopicHandler implements TopicHandler {
    @Override
    public void handle(String topicName, String message) {
     System.out.println("this is ACS handler");
    }
}