package com.logger.sunil.springboot.loggerdemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import java.util.concurrent.CompletableFuture;

public class Test {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    public static void request(String para)  {
        CompletableFuture.runAsync(() -> {
            MDC.put("var"+ para, "value"+ para);
            try {

            } catch (Exception e) {
                //logger.warn(e.getMessage(), e);
            }finally {
                // get MDC variables
                String st = MDC.getCopyOfContextMap().entrySet().stream().map(e -> e.getKey() + "Values=" + e.getValue()).reduce((a, b) -> a + ",----" + b).orElse("");
                logger.info("----->"+st);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            request(""+i);
        }
        Thread.sleep(10000);
    }
}
