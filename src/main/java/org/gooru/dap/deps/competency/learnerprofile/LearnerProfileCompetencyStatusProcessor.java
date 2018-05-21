package org.gooru.dap.deps.competency.learnerprofile;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusProcessor {

	// TODO: Move this to the config
	private final double MASTERY_SCORE = 80.00;

	private final AssessmentScoreEventMapper assessmentScore;
	private final String gutCode;
	private boolean isSignature;

	private LearnerProfileCompetencyStatusService service = new LearnerProfileCompetencyStatusService(
			DBICreator.getDbiForDefaultDS());

	public LearnerProfileCompetencyStatusProcessor(AssessmentScoreEventMapper assessmentScore, String gutCode,
			boolean isSignature) {
		this.assessmentScore = assessmentScore;
		this.gutCode = gutCode;
		this.isSignature = isSignature;
	}

	public void process() {
		LearnerProfileCompetencyStatusCommand command = LearnerProfileCompetencyStatusCommandBuilder
				.build(assessmentScore, gutCode);
		LearnerProfileCompetencyStatusBean bean = new LearnerProfileCompetencyStatusBean(command);

		/*
		 * if score > 80 and signature assessment update to mastered. If score > 80 and
		 * regular assessment update to completed. If score < 80 and regular assessment
		 * update to in_progress
		 */

		final double score = this.assessmentScore.getResult().getScore();

		if (score >= MASTERY_SCORE) {
			if (this.isSignature) {
				service.updateLearnerProfileCompetencyStatusToMastered(bean);
			} else {
				service.updateLearnerProfileCompetencyStatusToCompleted(bean);
			}
		} else {
			if (!this.isSignature) {
				service.updateLearnerProfileCompetencyStatusToInprogress(bean);
			}
		}
	}
}
