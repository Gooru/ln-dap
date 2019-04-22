package org.gooru.dap.configuration;

import java.util.List;
import java.util.Properties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ashish on 17/4/18.
 */
public interface KafkaConsumerConfig {

  int getInstances();

  List<String> getTopics();
  
  List<String> getProducerTopics();

  Properties getProperties();

  static KafkaConsumerConfig build(JsonNode config) {
    return new KafkaConsumerConfigImpl(config);
  }

}
