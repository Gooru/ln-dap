package org.gooru.dap.deps.competency.learnerprofile;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceProcessor {

	// TODO: Move this to the config
	private final double MASTERY_SCORE = 80.00;

	private final AssessmentScoreEventMapper assessmentScore;
	private final String gutCode;

	private LearnerProfileCompetencyEvidenceService service = new LearnerProfileCompetencyEvidenceService(
			DBICreator.getDbiForDefaultDS());

	public LearnerProfileCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore, String gutCode) {
		this.assessmentScore = assessmentScore;
		this.gutCode = gutCode;
	}

	public void process() {

		final double score = this.assessmentScore.getResult().getScore();

		// If the score is greater than the mastry/completion score then only persist
		// the evidence. Below mastry/completion score we are not treating the
		// competency as mastered or completed hence no need to persist the evidence.
		if (score >= MASTERY_SCORE) {
			LearnerProfileCompetencyEvidenceCommand command = LearnerProfileCompetencyEvidenceCommandBuilder
					.build(this.assessmentScore);
			LearnerProfileCompetencyEvidenceBean bean = new LearnerProfileCompetencyEvidenceBean(command);

			bean.setGutCode(gutCode);
			service.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
		}
	}
}
