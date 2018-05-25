package org.gooru.dap.deps.competency.processors;

import java.util.Iterator;
import java.util.Map;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.common.CompetencyAssessmentService;
import org.gooru.dap.deps.competency.common.CompetencyUtils;
import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 14-May-2018
 */
public class AssessmentScoreEventProcessor implements EventProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	private final AssessmentScoreEventMapper assessmentScoreEvent;
	private CompetencyAssessmentService service = new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());

	public AssessmentScoreEventProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
		this.assessmentScoreEvent = assessmentScoreEvent;
	}

	@Override
	public void process() {
		String eventName = this.assessmentScoreEvent.getEventName();
		LOGGER.debug("processing event: {}", eventName);
		switch (eventName) {
		case "usage.assessment.score":
			processAssessmentScore();
			break;

		default:
			LOGGER.warn("invalid event passed in");
			return;
		}
	}

	/*
	 * if score >= 80 if Signature assessment Insert or Update
	 * learner_profile_competency_status to Mastered if already not mastered Insert
	 * or Update learner_profile_competency_evidence with multiple evidence else
	 * Insert or Update learner_profile_competency_status to Completed Insert or
	 * Update learner_profile_competency_evidence Insert or Update
	 * content_competency_status to Completed Insert or Update
	 * content_competency_evidence else Insert learner_profile_competency_status to
	 * If already NOT completed or mastered Insert content_competency_status to
	 * in_progress if already not completed or mastered
	 */
	private void processAssessmentScore() {
		LOGGER.debug("assessment score event processing start");
		String collectionId = this.assessmentScoreEvent.getCollectionId();

		// Fetch competencies tagged with assessment
		LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
		AssessmentCompetency competency = service.getAssessmentCompetency(collectionId);
		JsonNode taxonomy = competency.getTaxonomy();
		if (taxonomy == null || taxonomy.size() == 0) {
			LOGGER.warn("assessment '{}' is not tagged with the taxonomy", collectionId);
			return;
		}

		// Fetch gut code mapping of the competency
		Map<String, String> fwToGutCodeMapping = service
				.getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

		final boolean isSignature = service.isSignatureAssessment(collectionId);
		LOGGER.debug("'{}' is signature assessment '{}'", collectionId, isSignature);

		Iterator<String> fields = taxonomy.fieldNames();
		while (fields.hasNext()) {
			String tc = fields.next();
			String gc = fwToGutCodeMapping.get(tc);

			// If the assessment is NOT signature assessment then only update content
			// competency status and evidence. In case of signature assessment we do not
			// want to persist content competency evidence & status
			if (!isSignature) {
				LOGGER.debug(
						"assessment is not signature assessment, hence persisting content competency status and evidence");
				new ContentCompetencyStatusProcessor(assessmentScoreEvent, tc).process();
				new ContentCompetencyEvidenceProcessor(assessmentScoreEvent, tc, (gc != null) ? gc : null).process();
			}

			// Always update the status and evidence of the learner profile if the gut code
			// mapping exists, logic for mastery and completed will be in respective
			// processors
			if (gc != null && !gc.isEmpty()) {
				LOGGER.debug(
						"gut mapping found for competency '{}', persisting learner profile competency status and evidence",
						tc);
				new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, isSignature).process();
				new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc).process();
			} else {
				LOGGER.debug(
						"gut mapping not found for competency '{}', hence learner profile competency status and evidence is not persisted");
			}
		}
	}

}
