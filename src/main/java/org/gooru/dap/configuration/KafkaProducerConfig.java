package org.gooru.dap.configuration;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;

public interface KafkaProducerConfig {

    Properties getProperties();

    static KafkaProducerConfig build(JsonNode config) {
        return new KafkaProducerConfigImpl(config);
    }

}
