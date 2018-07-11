package org.gooru.dap.deps.competency.assessmentscore.content;

import java.util.regex.Pattern;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.StatusConstants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyEvidenceProcessor {
	// TODO: Move this to the config
	private final double COMPLETION_SCORE = 80.00;

	private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");
	private static final Pattern HYPHEN_PATTERN = Pattern.compile("-");
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	private final AssessmentScoreEventMapper assessmentScore;
	private final String competencyCode;
	private final String gutCode;

	private ContentCompetencyEvidenceService service = new ContentCompetencyEvidenceService(
			DBICreator.getDbiForDefaultDS());

	public ContentCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore, String competencyCode,
			String gutCode) {
		this.assessmentScore = assessmentScore;
		this.competencyCode = competencyCode;
		this.gutCode = gutCode;
	}

	public void process() {

		final double score = this.assessmentScore.getResult().getScore();
		
		ContentCompetencyEvidenceCommand command = ContentCompetencyEvidenceCommandBuilder
				.build(this.assessmentScore);
		ContentCompetencyEvidenceBean bean = new ContentCompetencyEvidenceBean(command);
		
		bean.setCompetencyCode(competencyCode);
		bean.setGutCode(gutCode);
		
		String frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
		bean.setFrameworkCode(frameworkCode);

		boolean isMicroCompetency = (HYPHEN_PATTERN.split(competencyCode).length >= 5);
		bean.setMicroCompetency(isMicroCompetency);
		
		// If the score is greater than the completion score then only persist the
		// evidence. Below completion score we are not treating the competency as
		// completed hence no need to persist the evidence.
		int status = StatusConstants.IN_PROGRESS;
		if (score >= COMPLETION_SCORE) {
			status = StatusConstants.COMPLETED;
			this.service.insertOrUpdateContentCompetencyEvidence(bean);
		}
		
		// Regardless of score always persist evidence in TS tables
		LOGGER.debug("status:={} : persisting content competency evidence in ts table", status);
		bean.setStatus(status);
		this.service.insertOrUpdateContentCompetencyEvidenceTS(bean);
	}
}
