import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.MDC;
import org.springframework.core.serializer.Deserializer;

import java.util.Map;

public class MDCConsumerInterceptor<K, V> implements ConsumerInterceptor<K, V> {

    private static final String MDC_TOPIC_KEY = "kafka.topic";
    private static final String MDC_PARTITION_KEY = "kafka.partition";
  Deserializer<String> stringDeserializer;
    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        Map<TopicPartition, OffsetAndMetadata> offsets = null;



        for (ConsumerRecord<K, V> record : records.records(topicPartition)) {
            MDC.put("key",stringDeserializer.deserialize(record.topic(),(byte[])record.key()));
        }


        return records;
}

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        // Do something on commit
    }

    @Override
    public void close() {
        // Close the interceptor
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Configure the interceptor
    }
}
