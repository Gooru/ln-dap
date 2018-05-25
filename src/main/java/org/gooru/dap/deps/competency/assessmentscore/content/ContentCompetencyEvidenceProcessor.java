package org.gooru.dap.deps.competency.assessmentscore.content;

import java.util.regex.Pattern;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyEvidenceProcessor {
	// TODO: Move this to the config
	private final double COMPLETION_SCORE = 80.00;

	private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");
	private static final Pattern HYPHEN_PATTERN = Pattern.compile("-");

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

		// If the score is greater than the completion score then only persist the
		// evidence. Below completion score we are not treating the competency as
		// completed hence no need to persist the evidence.
		if (score >= COMPLETION_SCORE) {
			ContentCompetencyEvidenceCommand command = ContentCompetencyEvidenceCommandBuilder
					.build(this.assessmentScore);
			ContentCompetencyEvidenceBean bean = new ContentCompetencyEvidenceBean(command);

			bean.setCompetencyCode(competencyCode);
			bean.setGutCode(gutCode);

			String frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
			bean.setFrameworkCode(frameworkCode);

			boolean isMicroCompetency = (HYPHEN_PATTERN.split(competencyCode).length >= 5);
			bean.setMicroCompetency(isMicroCompetency);

			this.service.insertOrUpdateContentCompetencyEvidence(bean);
		}
	}
}
