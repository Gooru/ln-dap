package org.gooru.dap.deps.competency;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.score.CompetencyCollectionScoreProcessor;
import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.infra.ConsumerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 03-May-2018
 */
public class AssessmentScoreConsumer extends ConsumerTemplate<String, String> {

	private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.competency.AssessmentScoreConsumer";

	private static final Logger LOGGER = LoggerFactory.getLogger(Constants.LOGGER_NAME);

	public AssessmentScoreConsumer(int id, KafkaConsumerConfig kafkaConsumerConfig) {
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
			
			// Store competency score
			new CompetencyCollectionScoreProcessor(assessmentScore).process();
		} catch (IOException e) {
			LOGGER.error("unable to parse the event", e);
		}
	}

	public static final class Constants {
		private Constants() {
			throw new AssertionError();
		}

		public static final String LOGGER_NAME = "org.gooru.dap.deps.competency";

		public static final String EVENT_NAME = "usage.assessment.score";
	}
}
