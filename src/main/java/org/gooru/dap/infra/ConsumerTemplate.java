package org.gooru.dap.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 17/4/18.
 */
public abstract class ConsumerTemplate<K, V> implements Runnable, Deployable {
    private KafkaConsumer<K, V> consumer;
    private final int id;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private Properties props;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTemplate.class);

    protected ConsumerTemplate(int id, KafkaConsumerConfig kafkaConsumerConfig) {
        this.id = id;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }

    protected void initialize(Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        this.props = kafkaConsumerConfig.getProperties();
        props.setProperty("key.deserializer", keyDeserializer.getClass().getName());
        props.setProperty("value.deserializer", valueDeserializer.getClass().getName());
        this.consumer = new KafkaConsumer<>(props);
    }

    /*
     * Concrete class should implement this method to setup the props correctly.
     */
    public abstract void init();

    @Override
    public void run() {
        verifySetup();
        try {
            consumer.subscribe(kafkaConsumerConfig.getTopics());

            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(Long.MAX_VALUE);
                for (ConsumerRecord<K, V> record : records) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("partition", record.partition());
                    data.put("offset", record.offset());
                    data.put("value", record.value());
                    LOGGER.info("{} : {}", this.id, data);
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    private void verifySetup() {
        if (this.consumer == null) {
            LOGGER.error("Consumer not setup for Kafka properties. Will Exit.");
            System.exit(100);
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
