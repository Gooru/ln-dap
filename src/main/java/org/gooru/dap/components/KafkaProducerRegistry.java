package org.gooru.dap.components;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.gooru.dap.configuration.AppConfiguration;
import org.gooru.dap.configuration.KafkaProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;

public class KafkaProducerRegistry
    implements FinalizationAwareComponent, InitializationAwareComponent {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerRegistry.class);
  private Producer<String, String> kafkaProducer;
  private volatile boolean initialized = false;

  @Override
  public void initializeComponent() {
    LOGGER.debug("Kafka Initialization called upon..");
    if (!initialized) {
      synchronized (Holder.INSTANCE) {
        if (!initialized) {
          LOGGER.debug("Initializing KafkaRegistry now");
          JsonNode config = AppConfiguration.fetchKafkaProducers();
          if (config == null || !config.isContainerNode()) {
            throw new IllegalStateException("No valid configuaration for Kafka Producers found");
          }
          if (config != null) {
            this.kafkaProducer = initializeKafkaPublisher(KafkaProducerConfig.build(config));

          }
          this.initialized = true;
        }
      }
    }
  }

  private Producer<String, String> initializeKafkaPublisher(
      KafkaProducerConfig kafkaProducerConfig) {
    LOGGER.debug("Initialize Kafka Publisher now...");
    final Properties properties = kafkaProducerConfig.getProperties();
    properties.forEach((key, value) -> LOGGER.debug(key + " : " + value));

    Producer<String, String> producer = new KafkaProducer<>(properties);
    LOGGER.debug("Kafka producer created successfully!");
    return producer;
  }

  public Producer<String, String> getKafkaProducer() {
    if (initialized) {
      return this.kafkaProducer;
    }
    return null;
  }

  @Override
  public void finalizeComponent() {
    if (this.kafkaProducer != null) {
      this.kafkaProducer.close();
      this.kafkaProducer = null;
    }
  }

  public static KafkaProducerRegistry getInstance() {
    return Holder.INSTANCE;
  }

  private KafkaProducerRegistry() {
    // TODO Auto-generated constructor stub
  }

  private static final class Holder {
    private static final KafkaProducerRegistry INSTANCE = new KafkaProducerRegistry();
  }

}
