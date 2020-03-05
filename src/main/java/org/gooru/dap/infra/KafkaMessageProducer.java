package org.gooru.dap.infra;

import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.gooru.dap.components.KafkaProducerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaMessageProducer {

  private static final KafkaMessageProducer INSTANCE = new KafkaMessageProducer();
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageProducer.class);

  private KafkaMessageProducer() {}

  public static KafkaMessageProducer getInstance() {
    return INSTANCE;
  }

  public void sendEvents(String topic, String msg) {
    sendMessage(topic, msg);
  }

  private void sendMessage(String topic, String msg) {
    Producer<String, String> producer = KafkaProducerRegistry.getInstance().getKafkaProducer();
    ProducerRecord<String, String> kafkaMsg;
    kafkaMsg = new ProducerRecord<>(topic, msg);

    try {
      if (producer != null) {
        producer.send(kafkaMsg, (metadata, exception) -> {
          if (exception == null) {
            LOGGER.info("Message Delivered Successfully: Offset : " + metadata.offset()
                + " : Topic : " + metadata.topic() + " : Partition : " + metadata.partition()
                + " : Message : " + kafkaMsg);
          } else {
            LOGGER.error("Message Could not be delivered : " + kafkaMsg + ". Cause: "
                + exception.getMessage());
          }
        });
        LOGGER.debug("Message Sent Successfully: " + kafkaMsg);
      } else {
        LOGGER.error("Not able to obtain producer instance");
      }
    } catch (InterruptException | BufferExhaustedException | SerializationException ie) {
      LOGGER.error("sendMessageToKafka: to Kafka server:", ie);
    }
  }
}
