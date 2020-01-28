package org.gooru.dap.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.databind.JsonNode;

public class KafkaProducerConfigImpl implements KafkaProducerConfig {

  private final List<String> topics;
  private final Properties properties;

  public KafkaProducerConfigImpl(JsonNode config) {
    JsonNode kafkaConfig = config.get("kafka.producer.config");
    JsonNode topicsNode = kafkaConfig.get("producer.topics");

    topics = new ArrayList<>(topicsNode.size());
    for (int index = 0; index < topicsNode.size(); index++) {
      topics.add(topicsNode.get(index).textValue());
    }

    properties = new Properties();
    JsonNode propsConfig = kafkaConfig.get("props");
    for (Iterator<Map.Entry<String, JsonNode>> it = propsConfig.fields(); it.hasNext();) {
      Map.Entry<String, JsonNode> propsNode = it.next();
      properties.setProperty(propsNode.getKey(), propsNode.getValue().asText());

    }
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

}
