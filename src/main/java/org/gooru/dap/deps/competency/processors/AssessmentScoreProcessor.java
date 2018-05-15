package org.gooru.dap.deps.competency.processors;

import java.util.List;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.AssessmentScoreConsumer;
import org.gooru.dap.deps.competency.common.CompetencyAssessmentService;
import org.gooru.dap.deps.competency.common.CompetencyUtils;
import org.gooru.dap.deps.competency.content.ContentCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.content.ContentCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.learnerprofile.LearnerProfileCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.learnerprofile.LearnerProfileCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.score.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.score.mapper.GutCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 14-May-2018
 */
public class AssessmentScoreProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(AssessmentScoreConsumer.Constants.LOGGER_NAME);

	private final AssessmentScoreEventMapper assessmentScoreEvent;
	private CompetencyAssessmentService service = new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());

	public AssessmentScoreProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
		this.assessmentScoreEvent = assessmentScoreEvent;
	}

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
		String collectionId = this.assessmentScoreEvent.getCollectionId();

		// Fetch competencies tagged with assessment
		LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
		AssessmentCompetency competency = service.getAssessmentCompetency(collectionId);
		JsonNode taxonomy = competency.getTaxonomy();
		if (taxonomy == null) {
			LOGGER.warn("assessment '{}' is not tagged with the taxonomy", collectionId);
			return;
		}

		LOGGER.debug("assessment competency fetched from database");
		// Fetch gut code mapping of the competency
		List<GutCode> gutCodes = service
				.getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

		final boolean isSignature = service.isSignatureAssessment(collectionId);
		LOGGER.debug("'{}' signature assessment '{}', hence persist evidence and competency status", collectionId,
				isSignature);

		// TODO: Iterate this over framework taxonomies as there might be possibility
		// that framework to GUT mapping does not exists. In this case we do not want to
		// loose the competancy status
		gutCodes.forEach(gutCode -> {
			String gc = gutCode.getGutCode();
			String tc = gutCode.getTaxonomyCode();

			// If the assessment is NOT signature assessment then only update content
			// competency status and evidence. In case of signature assessment we do not
			// want to persist content competency evidence & status
			if (!isSignature) {
				new ContentCompetencyStatusProcessor(assessmentScoreEvent, tc).process();
				new ContentCompetencyEvidenceProcessor(assessmentScoreEvent, tc).process();
			}

			// Always update the status and evidence of the learner profile, logic for
			// mastery and completed will be in respective processors
			new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, isSignature).process();
			new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc).process();

		});
	}

}
