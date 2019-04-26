
package org.gooru.dap.deps.group;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.group.processors.GroupReportsEventMapper;
import org.gooru.dap.deps.group.processors.GroupTimespentReportsEventProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 23-Apr-2019
 */
public class GroupTimespentReportsEventConsumer extends ConsumerTemplate<String, String> {

  private static final String DEPLOYMENT_NAME =
      "org.gooru.dap.deps.group.GroupTimespentReportsEventConsumer";

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GroupTimespentReportsEventConsumer.class);
  private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

  public GroupTimespentReportsEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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
    // TODO: Whether to write the message in transmission error log or not
    XMISSION_ERROR_LOGGER.error(record.value());
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
      new GroupTimespentReportsEventProcessor(eventMapper).process();

    } catch (IOException e) {
      LOGGER.error("unable to parse the event", e);
      // Just in case if we need the event
      XMISSION_ERROR_LOGGER.error(record.value());
    }
  }

}
