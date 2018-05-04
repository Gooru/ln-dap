package org.gooru.dap.processors.events;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract class  EventProcessor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);
    
    abstract  String message();
    
    private String eventName;
    
    private JsonNode eventJsonNode;
    
    protected ExecutionStatus validateAndInitialize() {
        if (!validateMessage().isSuccessFul() || !validateEventName().isSuccessFul()) {
            return ExecutionStatus.FAILED;
        }
        return ExecutionStatus.SUCCESSFUL;
    }
    
    private ExecutionStatus validateEventName() {
        JsonNode eventName = getEventJsonNode().get(EventMessageConstant.EVENT_NAME);
        if (eventName == null) {
            LOGGER.error("Event name fieldname should not be null.");
            return ExecutionStatus.FAILED;
        }
        this.setEventName(eventName.textValue());
        return ExecutionStatus.SUCCESSFUL;
    }
    
    private ExecutionStatus validateMessage() {
        final String message = message();
        if (message == null || !message.startsWith("{") || !message.endsWith("}")) {
            LOGGER.error("Invalid message received, either null or message is not json string.");
            return ExecutionStatus.FAILED;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode eventJsonNode = mapper.readTree(message);
            setEventJsonNode(eventJsonNode);
            return ExecutionStatus.SUCCESSFUL;

        } catch (Exception e) {
            LOGGER.error("Invalid message format, messsage should be json {}", e);
            return ExecutionStatus.FAILED;
        }
    }
    
    protected ProcessorContext createContext() {
        return new ProcessorContext(getEventJsonNode());
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public JsonNode getEventJsonNode() {
        return eventJsonNode;
    }

    public void setEventJsonNode(JsonNode eventJsonNode) {
        this.eventJsonNode = eventJsonNode;
    }
}
