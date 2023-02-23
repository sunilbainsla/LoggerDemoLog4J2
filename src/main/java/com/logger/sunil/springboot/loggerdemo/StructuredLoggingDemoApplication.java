package com.logger.sunil.springboot.loggerdemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StructuredLoggingDemoApplication {
	private static final Logger logger = LogManager.getLogger(StructuredLoggingDemoApplication.class);
	public static void main(String[] args) {

		logger.info("test");
		SpringApplication.run(StructuredLoggingDemoApplication.class, args);
	}

}
