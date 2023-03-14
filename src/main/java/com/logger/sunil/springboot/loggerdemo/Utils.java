package com.logger.sunil.springboot.loggerdemo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {
    static Map<Pattern, TopicHandler> handlers = new HashMap<>();
    public static Map<Pattern, TopicHandler> registerHandlers()
    {
        registerHandler("^PFFC-ACS.*", new ACSTopicHandler());
        registerHandler(".*NOTIFICATION.*", new NotificationTopicHandler());
        registerHandler(".*changelog.*", new NoMDCTopicHandler());
        return handlers;
    }
    public  static void registerHandler(String topicRegex, TopicHandler handler) {
        Pattern pattern = Pattern.compile(topicRegex);
        handlers.put(pattern, handler);
    }
}
