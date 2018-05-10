package org.gooru.dap.deps.competency.usermatrix;

import org.gooru.dap.components.jdbi.DBICreator;

/**
 * @author gooru on 04-May-2018
 */
public class UserCompetencyMatrixProcessor {

	// TODO: Move this to the config
	private final int MASTERY_SCORE = 80;

	private final String userId;
	private final String gutCode;
	private final double score;
	private final boolean isSignature;
	private final long activityTime;

	UserCompetencyMatrixService service = new UserCompetencyMatrixService(DBICreator.getDbiForDefaultDS());

	public UserCompetencyMatrixProcessor(String userId, String gutCode, double score, boolean isSignature,
			long activityTime) {
		this.userId = userId;
		this.gutCode = gutCode;
		this.score = score;
		this.isSignature = isSignature;
		this.activityTime = activityTime;
	}

	public void process() {
		UserCompetencyMatrixCommand command = UserCompetencyMatrixCommandBuilder.build(this.userId, this.gutCode,
				this.activityTime);
		UserCompetencyMatrixBean bean = new UserCompetencyMatrixBean(command);

		/*
		 * if score > 80 and signature assessment update to mastered. If score > 80 and
		 * regular assessment update to completed. If score < 80 and regular assessment
		 * update to in_progress
		 */

		if (this.score >= MASTERY_SCORE) {
			if (this.isSignature) {
				service.updateUserCompetencyToMastered(bean);
			} else {
				service.updateUserCompetencyToCompleted(bean);
			}
		} else {
			if (!this.isSignature) {
				service.updateUserCompetencyToInprogress(bean);
			}
		}
	}
}
