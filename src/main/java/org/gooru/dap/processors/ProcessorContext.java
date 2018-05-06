package org.gooru.dap.processors;

import com.fasterxml.jackson.databind.JsonNode;

public class ProcessorContext {

    private final JsonNode eventJsonNode;

    public ProcessorContext(JsonNode eventJson) {
        this.eventJsonNode = eventJson;
    }

    public JsonNode getEventJsonNode() {
        return eventJsonNode;
    }

    
}
