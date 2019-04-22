package org.gooru.dap.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ashish on 17/4/18.
 */
public class KafkaConsumerConfigImpl implements KafkaConsumerConfig {

  private final int instances;
  private final List<String> topics;
  private final List<String> producerTopics;
  private final Properties properties;

  public KafkaConsumerConfigImpl(JsonNode config) {
    JsonNode kafkaConfig = config.get("kafka.consumer.config");
    instances = kafkaConfig.get("instances").intValue();
    JsonNode topicsNode = kafkaConfig.get("consumer.topics");

    topics = new ArrayList<>(topicsNode.size());
    for (int index = 0; index < topicsNode.size(); index++) {
      topics.add(topicsNode.get(index).textValue());
    }
    
    JsonNode producerTopicsNode = kafkaConfig.get("producer.topics");
    if (producerTopicsNode != null && producerTopicsNode.size() > 0) {
      producerTopics = new ArrayList<>(producerTopicsNode.size());
      for (int index = 0; index < producerTopicsNode.size(); index++) {
        producerTopics.add(producerTopicsNode.get(index).textValue());
      }
    } else {
      producerTopics = null;
    }

    properties = new Properties();
    JsonNode propsConfig = kafkaConfig.get("props");
    for (Iterator<Map.Entry<String, JsonNode>> it = propsConfig.fields(); it.hasNext();) {
      Map.Entry<String, JsonNode> propsNode = it.next();
      properties.setProperty(propsNode.getKey(), propsNode.getValue().asText());

    }
  }

  @Override
  public int getInstances() {
    return instances;
  }

  @Override
  public List<String> getTopics() {
    return topics;
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public List<String> getProducerTopics() {
    return producerTopics;
  }
}
