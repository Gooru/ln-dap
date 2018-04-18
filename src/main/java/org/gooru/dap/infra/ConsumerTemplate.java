package org.gooru.dap.infra;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the base class on which all DEPs will be built. It encapsulates the infra code needed to create and configure
 * Kafka consumer, deploy it and assign a thread to it.
 *
 * @author ashish on 17/4/18.
 */
public abstract class ConsumerTemplate<K, V> implements Runnable, Deployable {
    protected KafkaConsumer<K, V> consumer;
    protected final int id;
    protected final KafkaConsumerConfig kafkaConsumerConfig;
    protected Properties props;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTemplate.class);
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);

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

    /**
     * Concrete classes should implement this method to setup the props correctly by calling {@link #initialize}
     */
    public abstract void init();

    /**
     * The handler which should be used in case an exception is thrown from {@link #processRecord(ConsumerRecord)}
     *
     * @param record
     * @param e
     */
    public abstract void processingRecordExceptionHandler(ConsumerRecord record, Exception e);

    /**
     * The handler which will be invoked when the commit failed after processing the batch of records received in poll
     *
     * @param e
     */
    public abstract void commitExceptionHandler(Exception e);

    /**
     * Concrete classes should provide implementation for this method which will be called for every
     * {@link ConsumerRecord} that is read on the topic. Note that in case of any exception that will happen during
     * processing, concrete classes should handle them and take actions like logging or putting it to error topic.
     * The default behavior in base class is to catch error and *commit* the record as the loop needs to continue
     * processing the next records.
     *
     * @param record
     */
    public abstract void processRecord(ConsumerRecord<K, V> record);

    @Override
    public void run() {
        verifySetup();
        try {
            consumer.subscribe(kafkaConsumerConfig.getTopics());

            while (!shuttingDown.get()) {
                ConsumerRecords<K, V> records = consumer.poll(Long.MAX_VALUE);
                try {
                    for (ConsumerRecord<K, V> record : records) {
                        try {
                            processRecord(record);
                        } catch (Exception e) {
                            LOGGER.warn("Failure in handling message for topic '{}' offset '{}' partition '{}",
                                record.topic(), record.offset(), record.partition(), e);
                            processingRecordExceptionHandler(record, e);
                        }
                    }
                    consumer.commitSync();
                } catch (CommitFailedException e) {
                    LOGGER.warn("Failed to do commit.", e);
                    commitExceptionHandler(e);
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown, else throw
            if (!shuttingDown.get()) {
                LOGGER.warn("Got wakeup while not shutting down. ", e);
                throw e;
            }
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
        shuttingDown.set(true);
        consumer.wakeup();
    }
}
