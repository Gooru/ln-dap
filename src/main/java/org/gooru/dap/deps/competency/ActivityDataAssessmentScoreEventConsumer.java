package org.gooru.dap.deps.competency;

import java.io.IOException;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.processors.ActivityDataAssessmentScoreEventProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.gooru.dap.infra.KafkaMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ActivityDataAssessmentScoreEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.competency.ActivityDataAssessmentScoreEventConsumer";

  private final KafkaConsumerConfig kafkaConsumerConfig;
  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

  public ActivityDataAssessmentScoreEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
    super(id, kafkaConsumerConfig);
    this.kafkaConsumerConfig = kafkaConsumerConfig;
  }

  @Override
  public String getDeploymentName() {
    return DEPLOYMENT_NAME;
  }

  @Override
  public void init() {
    super.initialize(new StringDeserializer(), new StringDeserializer());
  }

  @Override
  public void processingRecordExceptionHandler(ConsumerRecord<String, String> record, Exception e) {
    XMISSION_ERROR_LOGGER.error(record.value());
  }

  @Override
  public void commitExceptionHandler(Exception e) {}

  @Override
  public void processRecord(ConsumerRecord<String, String> record) {
    final String event = record.value();
    LOGGER.debug("Received Event ==> {}", event);
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Convert event json into java object
      AssessmentScoreEventMapper assessmentScore =
          mapper.readValue(event, AssessmentScoreEventMapper.class);
      LOGGER.debug("event has been mapped to object:== {}", assessmentScore.toString());
      new ActivityDataAssessmentScoreEventProcessor(assessmentScore).process();
      // Produce Event for further processing
      List<String> producerTopics = this.kafkaConsumerConfig.getProducerTopics();
      if (producerTopics != null && !producerTopics.isEmpty()) {
        producerTopics.forEach(topic -> {
          KafkaMessageProducer.getInstance().sendEvents(topic, event);
        });
      }

      LOGGER.info("Successfully dispatched Activity Assessment Score Event to Kafka..");
    } catch (IOException e) {
      LOGGER.error("unable to parse the event", e);
      // Just in case if we need the event
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }
}