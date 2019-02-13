package org.gooru.dap.deps.competency;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.CollectionStartEventMapper;
import org.gooru.dap.deps.competency.processors.CollectionStartEventProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 18-May-2018
 */
public class CollectionStartEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.competency.CollectionStartEventConsumer";
  private static final String DIAGNOSTIC = "diagnostic";

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

  public CollectionStartEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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
  public void commitExceptionHandler(Exception e) {

  }

  @Override
  public void processRecord(ConsumerRecord<String, String> record) {
    final String event = record.value();
    LOGGER.debug("Received Event ==> {}", event);
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Convert event json into java object
      /*
       * CollectionStartEventMapper collectionStartEventMapper = mapper.readValue(event,
       * CollectionStartEventMapper.class); LOGGER.debug("event has been mapped to object:== {}",
       * collectionStartEventMapper.toString());
       * 
       * new CollectionStartEventProcessor(collectionStartEventMapper).process();
       */
      //DO NOT STORE COMPETENCY STATUS IF THIS IS A DIAGNOSTIC ASSESSMENT
      AssessmentScoreEventMapper eventMapper =
          mapper.readValue(event, AssessmentScoreEventMapper.class);
      LOGGER.debug("event has been mapped to object:== {}", eventMapper.toString());
      if (eventMapper.getContext().getContentSource() != null && 
          eventMapper.getContext().getContentSource().equalsIgnoreCase(DIAGNOSTIC)) {
        LOGGER.info("Diagnostic Assesment, no further processing");
        return;                
      } else {
        new CollectionStartEventProcessor(eventMapper).process();
      }
      
    } catch (IOException e) {
      LOGGER.error("unable to parse the event", e);
      // Just in case if we need the event
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }

}
