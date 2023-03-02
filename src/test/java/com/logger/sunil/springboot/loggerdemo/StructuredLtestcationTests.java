import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdcConsumerInterceptorTest {

	private static final Logger LOG = LoggerFactory.getLogger(MdcConsumerInterceptorTest.class);

	@Test
	void testInterceptor() {
		MdcConsumerInterceptor<byte[], byte[]> interceptor = new MdcConsumerInterceptor<>();
		Map<String, String> props = new HashMap<>();
		interceptor.configure(props);

		String orderNumber = "12345";
		String retailer = "Amazon";

		Map<String, String> mdc = new HashMap<>();
		mdc.put("OrderNumber", orderNumber);
		mdc.put("Retailer", retailer);

		ConsumerRecord<byte[], byte[]> record = new ConsumerRecord<>("my-topic", 0, 0, "key".getBytes(),
				"value".getBytes());

		List<ConsumerRecord<byte[], byte[]>> records = Collections.singletonList(record);
		List<ConsumerRecord<byte[], byte[]>> interceptedRecords = interceptor.onConsume(records);

		assertEquals(records.size(), interceptedRecords.size());

		ConsumerRecord<byte[], byte[]> interceptedRecord = interceptedRecords.get(0);
		assertEquals(record.key(), interceptedRecord.key());
		assertEquals(record.value(), interceptedRecord.value());

		Map<String, String> interceptedMdc = MDC.getCopyOfContextMap();
		assertTrue(interceptedMdc.containsKey("OrderNumber"));
		assertTrue(interceptedMdc.containsKey("Retailer"));
		assertEquals(orderNumber, interceptedMdc.get("OrderNumber"));
		assertEquals(retailer, interceptedMdc.get("Retailer"));
	}

}
