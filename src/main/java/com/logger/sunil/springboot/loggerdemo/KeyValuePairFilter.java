package com.logger.sunil.springboot.loggerdemo;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.layout.JsonLayout;

import java.util.Map;

@Plugin(name = "KeyValuePairFilter", category = "Core", elementType = "filter", printObject = true)
public class KeyValuePairFilter extends AbstractFilter {
    private  String key;
    private  String value;

    private KeyValuePairFilter(String key, String value, Result onMatch, Result onMismatch) {
        super(onMatch, onMismatch);
        this.key = key;
        this.value = value;
    }

    @Override
    public Result filter(LogEvent event) {

           if(value.equalsIgnoreCase("${ctx:orderNumber}"))
           {
               value=null;
               key=null;
        }
        return onMismatch;
    }

    @PluginFactory
    public static KeyValuePairFilter createFilter(
            @PluginAttribute("key") @Required final String key,
            @PluginAttribute("value") @Required final String value,
            @PluginAttribute(value = "onMatch", defaultString = "DENY") final Result onMatch,
            @PluginAttribute(value = "onMismatch", defaultString = "NEUTRAL") final Result onMismatch) {
        return new KeyValuePairFilter(key, value, onMatch, onMismatch);
    }


}

