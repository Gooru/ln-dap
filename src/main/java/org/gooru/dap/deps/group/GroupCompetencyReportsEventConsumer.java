
package org.gooru.dap.deps.group;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.group.processors.GroupCompetencyReportsEventProcessor;
import org.gooru.dap.deps.group.processors.GroupReportsEventMapper;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class GroupCompetencyReportsEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.group.GroupCompetencyReportsEventConsumer";

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportsEventConsumer.class);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

  public GroupCompetencyReportsEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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

  }

  @Override
  public void commitExceptionHandler(Exception e) {

  }

  @Override
  public void processRecord(ConsumerRecord<String, String> record) {
    final String event = record.value();
    LOGGER.debug("Received Event ==> {}", event);
    try {
      ObjectMapper mapper = new ObjectMapper();
      GroupReportsEventMapper eventMapper = mapper.readValue(event, GroupReportsEventMapper.class);
      new GroupCompetencyReportsEventProcessor(eventMapper).process();

    } catch (IOException e) {
      LOGGER.error("unable to parse the event", e);
      // Just in case if we need the event
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }

}
