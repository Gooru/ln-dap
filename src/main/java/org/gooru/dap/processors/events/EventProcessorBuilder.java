package org.gooru.dap.processors.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.dap.processors.Processor;
import org.gooru.dap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gooru.dap.constants.MessageConsumerConstants;

public enum EventProcessorBuilder {

  DEFAULT("default") {
    private final Logger LOGGER = LoggerFactory.getLogger(EventProcessorBuilder.class);

    @Override
    public List<Processor> build(String eventListenerTopicName, ProcessorContext context) {
      LOGGER.error("Invalid event listener topic name passed in, not able to handle");
      return null;
    }
  },
  RESOURCE(MessageConsumerConstants.RESOURCE) {

    @Override
    public List<Processor> build(String eventTopicName, ProcessorContext context) {
      return ResourceEventProcessorBuilder.lookupBuilder(eventTopicName).build(context);
    }
  },
  QUESTION(MessageConsumerConstants.QUESTION) {

    @Override
    public List<Processor> build(String eventTopicName, ProcessorContext context) {
      return QuestionEventProcessorBuilder.lookupBuilder(eventTopicName).build(context);
    }
  };

  private String name;

  EventProcessorBuilder(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  private static final Map<String, EventProcessorBuilder> LOOKUP = new HashMap<>();

  static {
    for (EventProcessorBuilder builder : values()) {
      LOOKUP.put(builder.getName(), builder);
    }
  }

  public static EventProcessorBuilder lookupBuilder(String name) {
    EventProcessorBuilder builder = LOOKUP.get(name);
    if (builder == null) {
      return DEFAULT;
    }
    return builder;
  }

  public abstract List<Processor> build(String eventListenerTopicName, ProcessorContext context);
}
