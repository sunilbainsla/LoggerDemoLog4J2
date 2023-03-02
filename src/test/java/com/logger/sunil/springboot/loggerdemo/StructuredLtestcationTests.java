import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.core.serializer.Deserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MDCConsumerInterceptorTest {

	@Mock
	Deserializer<String> mockStringDeserializer;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testOnConsume() {
		// Create a test ConsumerRecord
		String topic = "test-topic";
		int partition = 0;
		byte[] key = "test-key".getBytes();
		byte[] value = "test-value".getBytes();
		ConsumerRecord<byte[], byte[]> record = new ConsumerRecord<>(topic, partition, 0L, key, value);

		// Create a test ConsumerRecords object with the record
		Map<TopicPartition, ConsumerRecords<byte[], byte[]>> recordsMap = new HashMap<>();
		TopicPartition topicPartition = new TopicPartition(topic, partition);
		ConsumerRecords<byte[], byte[]> records = new ConsumerRecords<>(Collections.singletonMap(topicPartition, Collections.singletonList(record)));
		recordsMap.put(topicPartition, records);

		// Mock the deserializer to return a string for the test key
		String expectedMdcValue = "test-key-string";
		when(mockStringDeserializer.deserialize(topic, key)).thenReturn(expectedMdcValue);

		// Set up the MDCConsumerInterceptor for testing
		MDCConsumerInterceptor<byte[], byte[]> interceptor = new MDCConsumerInterceptor<>();
		interceptor.stringDeserializer = mockStringDeserializer;

		// Call the onConsume method with the test records
		ConsumerRecords<byte[], byte[]> result = interceptor.onConsume(records);

		// Verify that the MDC was updated with the expected value for the test record
		String actualMdcValue = MDC.get("key");
		assertEquals(expectedMdcValue, actualMdcValue);

		// Verify that the original records object was returned unchanged
		assertEquals(recordsMap, result);
	}
}
