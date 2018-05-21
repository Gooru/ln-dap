package org.gooru.dap.deps.question;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.constants.MessageConsumerConstants;
import org.gooru.dap.infra.ConsumerTemplate;
import org.gooru.dap.processors.events.ProcessorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionConsumer extends ConsumerTemplate<String, String> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionConsumer.class);

    public QuestionConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
        super(id, kafkaConsumerConfig);
    }

    @Override
    public void init() {
        super.initialize(new StringDeserializer(), new StringDeserializer());
    }

    @Override
    public void processingRecordExceptionHandler(ConsumerRecord record, Exception e) {
        LOGGER.warn("Failure in handling message for topic '{}' offset '{}' partition '{}", record.topic(),
            record.offset(), record.partition(), e);
    }

    @Override
    public void commitExceptionHandler(Exception e) {
        LOGGER.warn("Failed to do commit.", e);
    }

    @Override
    public void processRecord(ConsumerRecord<String, String> record) {
        ProcessorBuilder.build(getDeploymentName(), record.topic(), record.value()).process();
    }

    @Override
    public String getDeploymentName() {
        return MessageConsumerConstants.QUESTION;
    }
}
