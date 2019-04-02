package org.gooru.dap.deps.competency;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.preprocessors.AssessmentScoreEventPreProcessor;
import org.gooru.dap.deps.competency.preprocessors.DCAContentModel;
import org.gooru.dap.deps.competency.processors.AssessmentScoreEventProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.gooru.dap.infra.KafkaMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 03-May-2018
 */
public class AssessmentScoreEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.competency.AssessmentScoreEventConsumer";
  private static final String DIAGNOSTIC = "diagnostic";

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");
  public static final String PRODUCER_TOPIC = "org.gooru.da.sink.dap.competency.stats";


  public AssessmentScoreEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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
    LOGGER.debug("Received Event ==> {}", event);
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Convert event json into java object
      AssessmentScoreEventMapper assessmentScore =
          mapper.readValue(event, AssessmentScoreEventMapper.class);
      LOGGER.debug("event has been mapped to object:== {}", assessmentScore.toString());
      //DO NOT STORE COMPETENCY STATUS IF THIS IS A DIAGNOSTIC ASSESSMENT
      if (assessmentScore.getContext().getContentSource() != null && 
          assessmentScore.getContext().getContentSource().equalsIgnoreCase(DIAGNOSTIC)) {
        LOGGER.info("Diagnostic Assesment {}, no further processing", assessmentScore.getCollectionId());
        return;
      } else {
        if (assessmentScore.getContext().getAdditionalContext() != null) {
          DCAContentModel dcaContent = new AssessmentScoreEventPreProcessor(assessmentScore).process();
          if (dcaContent != null) {            
            assessmentScore.setCollectionId(dcaContent.getContentId());
          } else {
            LOGGER.info("DCA Content Info cannot be obtained. No further processing for this event {}", event);
            return;
          }
        }        
        new AssessmentScoreEventProcessor(assessmentScore).process();
        // Produce Event for the CompetencyStatsConsumer
        KafkaMessageProducer.getInstance().sendEvents(PRODUCER_TOPIC, event);
        LOGGER.info("Successfully dispatched Collection Assessment Score Event to Kafka.."); 
      }
    } catch (IOException e) {
      LOGGER.error("unable to parse the event", e);
      // Just in case if we need the event
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }
}
