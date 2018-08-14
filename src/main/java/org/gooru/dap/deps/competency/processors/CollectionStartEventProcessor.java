package org.gooru.dap.deps.competency.processors;

import java.util.Iterator;
import java.util.List;
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
import org.gooru.dap.deps.competency.events.mapper.ContextMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 18-May-2018
 */
public class CollectionStartEventProcessor implements EventProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	private final static String PATHTYPE_SYSTEM = "system";
	private final static String FORMAT_ASSESSMENT = "assessment";

	// private final CollectionStartEventMapper collectionStartEvent;
	private final AssessmentScoreEventMapper collectionStartEvent;
	private CompetencyAssessmentService coreService = new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());

	public CollectionStartEventProcessor(AssessmentScoreEventMapper collectionStartEvent) {
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
		// We do not want to persist the in progress status of the assessment if its
		// signature assessment. There is check on pathId, pathType and format of the
		// collection. We are assuming that if valid pathId is present, pathType is
		// 'system' and its assessment then its signature item and skipping it from
		// persisting status. For rest of all cases, we will persist status and evidence
		// as IN progress
		if (doProcessing()) {
			String collectionId = this.collectionStartEvent.getCollectionId();

			// Fetch competencies tagged with assessment
			LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
			AssessmentCompetency competency = coreService.getAssessmentCompetency(collectionId);

			JsonNode taxonomy = competency.getTaxonomy();
			if (taxonomy != null && taxonomy.size() > 0) {
				// Fetch gut code mapping of the competency
				Map<String, String> fwToGutCodeMapping = coreService
						.getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

				Iterator<String> fields = taxonomy.fieldNames();
				while (fields.hasNext()) {
					String tc = fields.next();
					String gc = fwToGutCodeMapping.get(tc);

					new ContentCompetencyStatusProcessor(collectionStartEvent, tc).process();
					new ContentCompetencyEvidenceProcessor(collectionStartEvent, tc, (gc != null) ? gc : null)
							.process();
				}
			} else {
				LOGGER.debug("no taxonomy tags are applied to assessment, skipping content competency status updates");
			}

			List<String> gutCodes = competency.getGutCodes();
			if (gutCodes != null && !gutCodes.isEmpty()) {
				gutCodes.forEach(gc -> {
					// Always update the status and evidence of the learner profile, logic for
					// mastery and completed will be in respective processors
					new LearnerProfileCompetencyStatusProcessor(collectionStartEvent, gc, false).process();
					new LearnerProfileCompetencyEvidenceProcessor(collectionStartEvent, gc, false).process();
				});
			} else {
				LOGGER.debug("no gut codes found for the assessment, skipping leaner profile status updates");
			}
		} else {
			LOGGER.info("signature item flow, skipping..");
		}
	}

	private boolean doProcessing() {
		String collectionType = this.collectionStartEvent.getCollectionType();
		ContextMapper context = this.collectionStartEvent.getContext();
		String pathType = context.getPathType();
		Long pathId = context.getPathId();
		return !(pathId != null && pathType != null && pathType.equalsIgnoreCase(PATHTYPE_SYSTEM)
				&& collectionType != null && collectionType.equalsIgnoreCase(FORMAT_ASSESSMENT));
	}

}
