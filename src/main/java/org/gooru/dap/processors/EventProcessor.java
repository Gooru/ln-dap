package org.gooru.dap.processors;

import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.events.EventProcessorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class EventProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private final String message;
    private String eventName;
    private JsonNode event;

    public EventProcessor(String message) {
        this.message = message;
    }

    @Override
    public void process() {
        try {
            ExecutionStatus executionStatus = validateAndInitialize();
            if (executionStatus.isSuccessFul()) {
               List<Processor> processors =  EventProcessorBuilder.lookupBuilder(eventName).build(createContext());
               processors.forEach(processor -> {
                   processor.process();
               });
            }
        } catch (Throwable e) {
            LOGGER.error("Unhandled exception in processing", e);
        }
    }

    private ProcessorContext createContext() {
        return new ProcessorContext(this.event);
    }

    private ExecutionStatus validateAndInitialize() {
        if (!validateMessage().isSuccessFul() || !validateEventName().isSuccessFul()) {
            return ExecutionStatus.FAILED;
        }
        return ExecutionStatus.SUCCESSFUL;
    }

    private ExecutionStatus validateMessage() {
        if (this.message == null || !this.message.startsWith("{") || !this.message.endsWith("}")) {
            LOGGER.error("Invalid message received, either null or message is not json string.");
            return ExecutionStatus.FAILED;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode eventObj = mapper.readTree(this.message);
            this.event = eventObj;
            return ExecutionStatus.SUCCESSFUL;

        } catch (Exception e) {
            LOGGER.error("Invalid message format, messsage should be json {}", e);
            return ExecutionStatus.FAILED;
        }
    }

    private ExecutionStatus validateEventName() {
        JsonNode eventName = this.event.get(EventMessageConstant.EVENT_NAME);
        if (eventName == null) {
            LOGGER.error("Event name fieldname should not be null.");
            return ExecutionStatus.FAILED;
        }
        this.eventName = eventName.textValue();
        return ExecutionStatus.SUCCESSFUL;
    }

}
