package com.logger.sunil.springboot.loggerdemo;



import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Plugin(name = "dynamicTimestamp", category = "Core", elementType = "layout", printObject = true)
public class DynamicTimestampPlugin extends PatternLayout {

    private final DateTimeFormatter formatter;

    private DynamicTimestampPlugin(DateTimeFormatter formatter) {
        super("%d{yyyy-MM-dd HH:mm:ss.SSS}");
        this.formatter = formatter;
    }

    @PluginFactory
    public static DynamicTimestampPlugin createPlugin(
            @PluginAttribute(value = "pattern", defaultString = "yyyy-MM-dd HH:mm:ss.SSS") String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return new DynamicTimestampPlugin(formatter);
    }

    @Override
    public String toSerializable(LogEvent event) {
        LocalDateTime timestamp = LocalDateTime.now();
        return formatter.format(timestamp);
    }
}
