package org.gooru.dap.deps.competency.processors;

import java.util.Iterator;
import java.util.Map;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusBean;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusCommand;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusCommandBuilder;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusService;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusBean;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusCommand;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusCommandBuilder;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusService;
import org.gooru.dap.deps.competency.common.CompetencyAssessmentService;
import org.gooru.dap.deps.competency.common.CompetencyUtils;
import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.events.mapper.CollectionStartEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 18-May-2018
 */
public class CollectionStartEventProcessor implements EventProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	private final CollectionStartEventMapper collectionStartEvent;

	private CompetencyAssessmentService coreService = new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());
	private ContentCompetencyStatusService contentStatusService = new ContentCompetencyStatusService(
			DBICreator.getDbiForDefaultDS());
	private LearnerProfileCompetencyStatusService learnerProfileStatusService = new LearnerProfileCompetencyStatusService(
			DBICreator.getDbiForDefaultDS());

	public CollectionStartEventProcessor(CollectionStartEventMapper collectionStartEvent) {
		this.collectionStartEvent = collectionStartEvent;
	}

	@Override
	public void process() {
		String eventName = this.collectionStartEvent.getEventName();
		LOGGER.debug("processing event: {}", eventName);
		switch (eventName) {
		case "usage.collection.start":
			processCollectionStart();
			break;

		default:
			LOGGER.warn("invalid event passed in");
			return;
		}

	}

	private void processCollectionStart() {
		LOGGER.debug("collection start event processing start");
		String collectionId = this.collectionStartEvent.getCollectionId();

		// Fetch competencies tagged with assessment
		LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
		AssessmentCompetency competency = coreService.getAssessmentCompetency(collectionId);
		JsonNode taxonomy = competency.getTaxonomy();
		if (taxonomy == null || taxonomy.size() == 0) {
			LOGGER.warn("assessment '{}' is not tagged with the taxonomy", collectionId);
			return;
		}

		// Fetch gut code mapping of the competency
		Map<String, String> fwToGutCodeMapping = coreService
				.getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

		Iterator<String> fields = taxonomy.fieldNames();
		while (fields.hasNext()) {
			String competencyCode = fields.next();

			// Persist content competency status as in progress
			LOGGER.debug("persisting content competency status as in_progress for competency '{}' and user '{}'",
					competencyCode, this.collectionStartEvent.getUserId());
			ContentCompetencyStatusCommand command = ContentCompetencyStatusCommandBuilder
					.build(this.collectionStartEvent, competencyCode);
			ContentCompetencyStatusBean bean = new ContentCompetencyStatusBean(command);
			this.contentStatusService.insertContentCompetencyStatusToInProgress(bean);

			// Persist learner profile status as in progress if gut code mapping of the
			// competency is present
			String gutCode = fwToGutCodeMapping.get(competencyCode);
			if (gutCode != null && !gutCode.isEmpty()) {
				LOGGER.debug("persisting learner profile status as in_progress for gut code '{}' and user '{}'",
						competencyCode, this.collectionStartEvent.getUserId());
				LearnerProfileCompetencyStatusCommand learnerProfileStatusCommand = LearnerProfileCompetencyStatusCommandBuilder
						.build(this.collectionStartEvent, gutCode);
				LearnerProfileCompetencyStatusBean learnerProfileStatusBean = new LearnerProfileCompetencyStatusBean(
						learnerProfileStatusCommand);
				this.learnerProfileStatusService
						.updateLearnerProfileCompetencyStatusToInprogress(learnerProfileStatusBean);
			} else {
				LOGGER.debug(
						"no gut mapping found for competency '{}', hence learner profile competency status not persisted",
						competencyCode);
			}
		}

	}

}
