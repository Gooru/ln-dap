package org.gooru.dap.deps.testdep;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 18/4/18.
 */
public class TestDep extends ConsumerTemplate<String, String> {
    private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.TestConsumer";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDep.class);

    public TestDep(int id, KafkaConsumerConfig kafkaConsumerConfig) {
        super(id, kafkaConsumerConfig);
    }

    @Override
    public void init() {
        super.initialize(new StringDeserializer(), new StringDeserializer());
    }

    /* NOTE This is just sample implementation. In real handlers something sensible should be done */
    @Override
    public void processingRecordExceptionHandler(ConsumerRecord record, Exception e) {
        LOGGER.warn("Failure in handling message for topic '{}' offset '{}' partition '{}", record.topic(),
            record.offset(), record.partition(), e);
    }

    /* NOTE This is just sample implementation. In real handlers something sensible should be done */
    @Override
    public void commitExceptionHandler(Exception e) {
        LOGGER.warn("Failed to do commit.", e);
    }

    @Override
    public void processRecord(ConsumerRecord<String, String> record) {
        Map<String, Object> data = new HashMap<>();
        data.put("partition", record.partition());
        data.put("offset", record.offset());
        data.put("value", record.value());
        LOGGER.info("{} : {}", this.id, data);
    }

    @Override
    public String getDeploymentName() {
        return DEPLOYMENT_NAME;
    }
}
