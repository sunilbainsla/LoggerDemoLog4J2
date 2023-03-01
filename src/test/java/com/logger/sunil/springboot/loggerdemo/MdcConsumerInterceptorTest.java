import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdcConsumerInterceptorTest {

	private static final Logger logger = LoggerFactory.getLogger(MdcConsumerInterceptorTest.class);

	@Test
	public void testMdcConsumerInterceptor() {
		// create test input data
		Map<String, Object> recordHeaders = new HashMap<>();
		recordHeaders.put("header1", "value1");
		ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 1L, "key", "value", recordHeaders);

		// create the interceptor
		MdcConsumerInterceptor<String, String> interceptor = new MdcConsumerInterceptor<>();

		// create the test MDC context
		Map<String, String> mdcContext = new HashMap<>();
		mdcContext.put("key1", "value1");
		mdcContext.put("key2", "value2");

		// set the MDC context before calling the interceptor
		MdcUtil.setMdcContext(mdcContext);

		// call the interceptor
		ConsumerRecord<String, String> interceptedRecord = interceptor.onConsume(record);

		// assert that the MDC context has been added to the record headers
		assertTrue(interceptedRecord.headers().lastHeader(MdcConsumerInterceptor.MDC_HEADER_KEY) != null, "MDC header not added");
	}
}
