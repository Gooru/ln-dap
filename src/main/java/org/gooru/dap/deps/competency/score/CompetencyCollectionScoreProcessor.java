package org.gooru.dap.deps.competency.score;

import java.util.Iterator;
import java.util.List;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.AssessmentScoreConsumer;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.score.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.score.mapper.GutCode;
import org.gooru.dap.deps.competency.status.CompetencyStatusProcessor;
import org.gooru.dap.deps.competency.usermatrix.UserCompetencyMatrixProcessor;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyCollectionScoreProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(AssessmentScoreConsumer.Constants.LOGGER_NAME);

	private final DBI defaultDbi = DBICreator.getDbiForDefaultDS();
	private final DBI coreDbi = DBICreator.getDbiForCoreDS();

	private final AssessmentScoreEventMapper assessmentScoreEvent;
	private final CompetencyCollectionScoreService service = new CompetencyCollectionScoreService(defaultDbi, coreDbi);

	public CompetencyCollectionScoreProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
		this.assessmentScoreEvent = assessmentScoreEvent;
	}

	public void process() {
		LOGGER.debug("processing of competency assessment score started");
		CompetencyCollectionScoreCommand command = CompetencyCollectionScoreCommandBuilder
				.build(this.assessmentScoreEvent);

		String eventName = this.assessmentScoreEvent.getEventName();
		LOGGER.debug("processing event: {}", eventName);
		switch (eventName) {
		case "usage.assessment.score":
			processAssessmentScore(command);
			break;

		default:
			LOGGER.warn("invalid event passed in");
			return;
		}
	}

	private void processAssessmentScore(CompetencyCollectionScoreCommand command) {
		String collectionId = command.getCollectionId();

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
		List<GutCode> gutCodes = service.getGutCodeMapping(toPostgresArrayString(taxonomy.fieldNames()));

		final boolean isSignature = service.isSignatureAssessment(command.getCollectionId());
		LOGGER.debug("'{}' signature assessment '{}', hence persist evidence and competency status", collectionId,
				isSignature);

		CompetencyCollectionScoreBean bean = new CompetencyCollectionScoreBean(command);
		gutCodes.forEach(gutCode -> {
			String gc = gutCode.getGutCode();
			String tc = gutCode.getTaxonomyCode();

			// If the assessment is not signature assessment then only update competency
			// status and score otherwise skip
			if (!isSignature) {
				JsonNode txNode = taxonomy.get(tc);

				bean.setGutCode(gc);
				bean.setCompetencyCode(tc);
				JsonNode displayCode = txNode.get(CompetencyConstants.TX_DISPLAY_CODE);
				if (displayCode != null) {
					bean.setCompetencyDisplayCode(displayCode.asText(null));
				}

				JsonNode txTitle = txNode.get(CompetencyConstants.TX_TITLE);
				if (txTitle != null) {
					bean.setCompetencyTitle(txTitle.asText(null));
				}

				bean.setFrameworkCode(txNode.get(CompetencyConstants.TX_FRAMEWORK_CODE).asText());

				LOGGER.debug("ready to persist evidence");
				service.insertOrUpdateAssessmentCompetencyScore(bean);

				LOGGER.debug("ready to update competency status");
				new CompetencyStatusProcessor(this.assessmentScoreEvent, tc).process();

			}

			// always update user competency matrix
			LOGGER.debug("ready to update user competency matrix");
			new UserCompetencyMatrixProcessor(command.getUserId(), gc, command.getCollectionScore(), isSignature,
					command.getUpdatedAt()).process();

		});
	}

	private String toPostgresArrayString(Iterator<String> input) {
		if (!input.hasNext()) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (;;) {
			String s = input.next();
			sb.append('"').append(s).append('"');
			if (!input.hasNext()) {
				return sb.append('}').toString();
			}
			sb.append(',');
		}
	}
}
