package org.gooru.dap.deps.competency;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.processors.CompetencyStatsProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mukul@gooru
 */
public class CompetencyStatsEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.competency.CompetencyStatsEventConsumer";

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

  public CompetencyStatsEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
    super(id, kafkaConsumerConfig);
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
    LOGGER.info("Received Event from AssessmentScoreConsumer");
    ObjectMapper mapper = new ObjectMapper();
    try {
      AssessmentScoreEventMapper assessmentScore =
          mapper.readValue(event, AssessmentScoreEventMapper.class);
      new CompetencyStatsProcessor(assessmentScore).process();
    } catch (IOException e) {
      LOGGER.error("Unable to parse the event", e);
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }
}
