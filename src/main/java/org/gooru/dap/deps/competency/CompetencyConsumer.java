package org.gooru.dap.deps.competency;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.infra.ConsumerTemplate;

/**
 * @author gooru on 03-May-2018
 */
public class CompetencyConsumer extends ConsumerTemplate<String, String> {
	
	private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.CompetencyConsumer";

	public CompetencyConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
		super(id, kafkaConsumerConfig);
	}

	@Override
	public String getDeploymentName() {
		return DEPLOYMENT_NAME;
	}

	@Override
	public void init() {
		super.initialize(new StringDeserializer(), new StringDeserializer());
	}

	@Override
	public void processingRecordExceptionHandler(ConsumerRecord record, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commitExceptionHandler(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processRecord(ConsumerRecord<String, String> record) {
		// TODO Auto-generated method stub
		
	}

}
