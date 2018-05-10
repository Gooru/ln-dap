package org.gooru.dap.deps.competency.score;

import java.util.Iterator;
import java.util.List;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.AssessmentScoreConsumer;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.score.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.score.mapper.GutCode;
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

	private final CompetencyCollectionScoreService service = new CompetencyCollectionScoreService(defaultDbi, coreDbi);

	public void process(AssessmentScoreEventMapper assessmentScoreEvent) {
		LOGGER.debug("processing of competency assessment score started");
		CompetencyCollectionScoreCommand command = CompetencyCollectionScoreCommandBuilder.build(assessmentScoreEvent);

		String eventName = assessmentScoreEvent.getEventName();
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
		LOGGER.debug("gut code mapping fetched from database: #{}", gutCodes.size());
		
		CompetencyCollectionScoreBean bean = new CompetencyCollectionScoreBean(command);
		gutCodes.forEach(gutCode -> {
			String gc = gutCode.getGutCode();
			String tc = gutCode.getTaxonomyCode();
			LOGGER.debug("persisting gutcode: '{}' and competency: '{}'", gc, tc);

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

			LOGGER.debug("ready to persist competency score");
			service.insertOrUpdateAssessmentCompetencyScore(bean);

			// Update user competency matrix
			LOGGER.debug("ready to update user competency matrix");
			boolean isSignature = service.isSignatureAssessment(command.getCollectionId());
			new UserCompetencyMatrixProcessor(command.getUserId(), gc, command.getCollectionScore(), isSignature, command.getUpdatedAt())
					.process();
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
