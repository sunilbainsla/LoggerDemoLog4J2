package com.logger.sunil.springboot.loggerdemo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MDCMessageProcessor {
    private Map<Pattern, TopicHandler> handlers = new HashMap<>();
    private TopicHandler defaultHandler = new DefaultTopicHandler();
    public void setDefaultHandler(DefaultTopicHandler handler) {
        defaultHandler = handler;
    }
    public void registerHandler(String topicRegex, TopicHandler handler) {
        Pattern pattern = Pattern.compile(topicRegex);
        handlers.put(pattern, handler);
    }
    public void processMessage(String topicName, String message) {
        boolean handled = false;
        for (Pattern pattern : handlers.keySet()) {
            if (pattern.matcher(topicName).matches()) {
                TopicHandler handler = handlers.get(pattern);
                handler.handle(topicName, message);
                handled = true;
            }
        }
        if (!handled && defaultHandler != null) {
            defaultHandler.handle(topicName, message);
        }

    }

    public static void main(String[] args) {
        MDCMessageProcessor messageProcessor = new MDCMessageProcessor();

        messageProcessor.registerHandler("^PFFC-ACS.*", new ACSTopicHandler());
        messageProcessor.registerHandler("^PFFC-NOTIFICATION.*", new NotificationTopicHandler());
        messageProcessor.processMessage("PFFC-ACS", "A new sports team has been formed!");
        messageProcessor.processMessage("Pdsadad", "The election results are in!");
    }
}