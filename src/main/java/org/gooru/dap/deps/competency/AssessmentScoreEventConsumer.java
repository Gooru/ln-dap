package org.gooru.dap.deps.competency;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.processors.AssessmentScoreEventProcessor;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 03-May-2018
 */
public class AssessmentScoreEventConsumer extends ConsumerTemplate<String, String> {

	private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.competency.AssessmentScoreEventConsumer";

	private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
	private static final Logger XMISSION_ERROR_LOGGER = LoggerFactory.getLogger("xmission.error");

	public AssessmentScoreEventConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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
	public void processingRecordExceptionHandler(ConsumerRecord<String, String> record, Exception e) {
		XMISSION_ERROR_LOGGER.error(record.value());
	}

	@Override
	public void commitExceptionHandler(Exception e) {
	}

	@Override
	public void processRecord(ConsumerRecord<String, String> record) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert event json into java object
			AssessmentScoreEventMapper assessmentScore = mapper.readValue(record.value(),
					AssessmentScoreEventMapper.class);
			LOGGER.debug("event has been mapped to object:== {}", assessmentScore.toString());

			new AssessmentScoreEventProcessor(assessmentScore).process();
		} catch (IOException e) {
			LOGGER.error("unable to parse the event", e);
			// Just in case if we need the event 
			XMISSION_ERROR_LOGGER.error(record.value());
		}
	}
}
