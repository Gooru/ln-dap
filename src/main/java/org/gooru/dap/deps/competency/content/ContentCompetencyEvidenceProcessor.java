package org.gooru.dap.deps.competency.content;

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

	private final AssessmentScoreEventMapper assessmentScore;
	private final String competencyCode;

	private ContentCompetencyEvidenceService service = new ContentCompetencyEvidenceService(
			DBICreator.getDbiForDefaultDS());

	public ContentCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore, String competencyCode) {
		this.assessmentScore = assessmentScore;
		this.competencyCode = competencyCode;
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

			String frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
			bean.setFrameworkCode(frameworkCode);

			this.service.insertOrUpdateContentCompetencyEvidence(bean);
		}
	}
}
