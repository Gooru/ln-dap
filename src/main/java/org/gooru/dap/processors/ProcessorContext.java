package org.gooru.dap.processors;

import com.fasterxml.jackson.databind.JsonNode;

public class ProcessorContext {

    private final JsonNode context;

    public ProcessorContext(JsonNode event) {
        this.context = event.get("context");
    }
}
