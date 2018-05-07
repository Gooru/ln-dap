package org.gooru.dap.deps.resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.infra.ConsumerTemplate;
import org.gooru.dap.processors.events.ProcessorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceEventConsumer extends ConsumerTemplate<String, String> {
    private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.ResourceEventConsumer";
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceEventConsumer.class);

    public ResourceEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
        super(id, kafkaConsumerConfig);
    }

    @Override
    public void init() {
        super.initialize(new StringDeserializer(), new StringDeserializer());
    }

    @Override
    public void processingRecordExceptionHandler(ConsumerRecord record, Exception e) {
 
    }

    @Override
    public void commitExceptionHandler(Exception e) {
    }

    @Override
    public void processRecord(ConsumerRecord<String, String> record) {
        System.out.println(record);
        ProcessorBuilder.buildResourceEventProcessor(record.value()).process();
    }

    @Override
    public String getDeploymentName() {
        return DEPLOYMENT_NAME;
    }
}
