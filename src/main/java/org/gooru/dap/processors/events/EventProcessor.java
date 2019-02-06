package org.gooru.dap.processors.events;

import java.util.List;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.Processor;
import org.gooru.dap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class EventProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);
  private final String message;
  private JsonNode eventJsonNode;
  private final String deploymentName;
  private final String eventListenerTopicName;

  public EventProcessor(String deploymentName, String eventListenerTopicName, String message) {
    this.message = message;
    this.eventListenerTopicName = eventListenerTopicName;
    this.deploymentName = deploymentName;
  }

  @Override
  public void process() {
    try {
      ExecutionStatus executionStatus = validateAndInitialize();
      if (executionStatus.isSuccessFul()) {
        List<Processor> processors = EventProcessorBuilder.lookupBuilder(deploymentName)
            .build(eventListenerTopicName, createContext());
        processors.forEach(processor -> {
          processor.process();
        });
      }
    } catch (Throwable e) {
      LOGGER.error("Unhandled exception in processing", e);
    }
  }

  protected ExecutionStatus validateAndInitialize() {
    if (!validateEventListenerTopicName().isSuccessFul() || !validateMessage().isSuccessFul()
        || !validateEventName().isSuccessFul()) {

      return ExecutionStatus.FAILED;
    }
    return ExecutionStatus.SUCCESSFUL;
  }

  private ExecutionStatus validateEventName() {
    JsonNode eventName = this.eventJsonNode.get(EventMessageConstant.EVENT_NAME);
    if (eventName == null) {
      LOGGER.error("Event name fieldname should not be null.");
      return ExecutionStatus.FAILED;
    }
    return ExecutionStatus.SUCCESSFUL;
  }

  private ExecutionStatus validateMessage() {
    final String message = this.message;
    if (message == null || !message.startsWith("{") || !message.endsWith("}")) {
      LOGGER.error("Invalid message received, either null or message is not json string.");
      return ExecutionStatus.FAILED;
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode eventJsonNode = mapper.readTree(message);
      this.eventJsonNode = eventJsonNode;
      return ExecutionStatus.SUCCESSFUL;

    } catch (Exception e) {
      LOGGER.error("Invalid message format, messsage should be json {}", e);
      return ExecutionStatus.FAILED;
    }
  }

  private ExecutionStatus validateEventListenerTopicName() {
    if (eventListenerTopicName == null) {
      LOGGER.error("Event listener should be null");
      return ExecutionStatus.FAILED;
    }
    return ExecutionStatus.SUCCESSFUL;
  }

  protected ProcessorContext createContext() {
    return new ProcessorContext(this.eventJsonNode);
  }

}
