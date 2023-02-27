package com.logger.sunil.springboot.loggerdemo;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class MdcConsumerInterceptorTest {

	private static final Logger LOG = LoggerFactory.getLogger(MdcConsumerInterceptorTest.class);

	private ConsumerInterceptor<String, String> interceptor;
	private Map<String, String> expectedMdc;

	@Before
	public void setUp() {
		interceptor = new MdcConsumerInterceptor<>();
		expectedMdc = new HashMap<>();
		expectedMdc.put("tenant", "acme");
		expectedMdc.put("user", "jdoe");
	}

	@Test
	public void testOnConsume() {
		// Prepare test data
		String topic = "test-topic";
		int partition = 0;
		List<String> messages = Arrays.asList("message1", "message2", "message3");
		Map<TopicPartition, List<String>> recordsMap = new HashMap<>();
		TopicPartition tp = new TopicPartition(topic, partition);
		recordsMap.put(tp, messages);

		// Set up the MDC with expected values
		MDC.setContextMap(expectedMdc);

		// Call the onConsume method of the interceptor
		ConsumerRecords<String, String> consumerRecords = new ConsumerRecords<>(recordsMap);
		ConsumerRecords<String, String> interceptedRecords = interceptor.onConsume(consumerRecords);

		// Verify that the intercepted records have the expected MDC values
		interceptedRecords.records(tp).forEach(record -> {
			Map<String, String> mdc = MDC.getCopyOfContextMap();
			LOG.info("MDC: {}", mdc);
			assert(mdc != null);
			assert(mdc.get("tenant").equals("acme"));
			assert(mdc.get("user").equals("jdoe"));
		});
	}

	@Test
	public void testInterceptor() {
		// create interceptor
		MdcConsumerInterceptor<String, String> interceptor = new MdcConsumerInterceptor<>();

		// create test record
		ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0, "key", "value");

		// create test records
		ConsumerRecords<String, String> records = new ConsumerRecords<>(Collections.singletonMap(
				new TopicPartition("test-topic", 0), Collections.singletonList(record)));

		// apply interceptor to records
		ConsumerRecords<String, String> interceptedRecords = interceptor.onConsume(records);

		// assert that intercepted records is not null and has at least one element
		assertNotNull(interceptedRecords);
		assertFalse(interceptedRecords.isEmpty());

		// assert that MDC context was set for intercepted records
		for (ConsumerRecord<String, String> interceptedRecord : interceptedRecords) {
			assertNotNull(interceptedRecord.headers().lastHeader("mdc"));
		}
	}

}