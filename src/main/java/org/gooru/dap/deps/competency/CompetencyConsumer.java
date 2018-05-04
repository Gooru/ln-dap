package org.gooru.dap.deps.competency;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.infra.ConsumerTemplate;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 03-May-2018
 */
public class CompetencyConsumer extends ConsumerTemplate<String, String> {
	
	private static final String DEPLOYMENT_NAME = "org.gooru.dap.deps.CompetencyConsumer";
	
	private static final Logger LOGGER = LoggerFactory.getLogger("org.gooru.dap.deps.competency");
	
	private final DBI defaultDbi = DBICreator.getDbiForDefaultDS();
	private final DBI coreDbi = DBICreator.getDbiForCoreDS();

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
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode eventJson = mapper.readTree(record.value());
			
			JsonNode collection = eventJson.get("collectionId");
			if (collection == null) {
				LOGGER.warn("no collection id present in the event, aborting..");
				return;
			}
			
			String collectionId = collection.textValue();
			
			// call the specific processor from here 
		} catch (IOException e) {
			LOGGER.error("unable to parse the event", e);
		}
	}

}
