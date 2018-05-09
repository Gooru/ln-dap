package org.gooru.dap.processors.repositories.jdbi;

import java.util.ArrayList;
import java.util.List;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public interface DBHandler {

    static final Logger LOGGER = LoggerFactory.getLogger(DBHandler.class);
    
    ExecutionStatus checkSanity();
    
    Repository getRepository();
    
    ProcessorContext getContext();
    
    default ExecutionStatus validateRequiredFields(List<String> fields) { 
        final JsonNode eventJsonNode = getContext().getEventJsonNode();
        final List<String> missingFields = new ArrayList<>();
        fields.forEach(field -> {
            if (field.startsWith("/")) {
                JsonNode fieldNode = eventJsonNode.at(field);
                if (fieldNode == null || fieldNode.isNull()) {
                    missingFields.add(field);
                }
            } else {
                JsonNode fieldNode = eventJsonNode.get(field);
                if (fieldNode == null || fieldNode.isNull()) {
                    missingFields.add(field);
                }
            }
        });
        if (missingFields.size() > 0) {
            LOGGER.error("missing required fields in event JSON {}", missingFields);
            return ExecutionStatus.FAILED;
        }
        return ExecutionStatus.SUCCESSFUL;
    }
}
