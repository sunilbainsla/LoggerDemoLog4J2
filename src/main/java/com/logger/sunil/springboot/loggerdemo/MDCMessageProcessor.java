package com.logger.sunil.springboot.loggerdemo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MDCMessageProcessor {

    private TopicHandler defaultHandler = new DefaultTopicHandler();
    Map<Pattern, TopicHandler> handlers =Utils.registerHandlers();
    public void setDefaultHandler(DefaultTopicHandler handler) {
        defaultHandler = handler;
    }

public void processMessage(String topicName, String message) {
    handlers.keySet().stream()
            .flatMap(pattern -> {
                if (pattern.matcher(topicName).matches()) {
                    TopicHandler handler = handlers.get(pattern);
                    handler.handle(topicName, message);
                    return Stream.of(topicName);
                }
                return Stream.empty();
            })
            .findFirst()
            .ifPresentOrElse(
                    handledTopic -> {},
                    () -> defaultHandler.handle(topicName, message)
            );
}

    public static void main(String[] args) {
        MDCMessageProcessor messageProcessor = new MDCMessageProcessor();


        messageProcessor.processMessage("PFFC-ACS", "A new sports team has been formed!");
        messageProcessor.processMessage("PFFC", "The election results are in!");
        messageProcessor.processMessage("changelog", "The election results are in!");
    }
}