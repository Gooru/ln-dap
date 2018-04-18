package org.gooru.dap.deps.testdep;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.infra.ConsumerTemplate;

/**
 * @author ashish on 18/4/18.
 */
public class TestDep extends ConsumerTemplate<String, String> {
    private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.TestConsumer";

    public TestDep(int id, KafkaConsumerConfig kafkaConsumerConfig) {
        super(id, kafkaConsumerConfig);
    }

    @Override
    public void init() {
        super.initialize(new StringDeserializer(), new StringDeserializer());
    }

    @Override
    public String getDeploymentName() {
        return DEPLOYMENT_NAME;
    }
}
