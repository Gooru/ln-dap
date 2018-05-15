package org.gooru.dap.deps.competency.learnerprofile;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusService {

	private final LearnerProfileCompetencyStatusDao dao;

	private final static int NOT_STARTED = 0;
	private final static int IN_PROGRESS = 1;
	private final static int INFERRED = 2;
	// private final static int NOT_DEFINED = 3;
	private final static int COMPLETED = 4;
	private final static int MASTERED = 5;

	public LearnerProfileCompetencyStatusService(DBI dbi) {
		this.dao = dbi.onDemand(LearnerProfileCompetencyStatusDao.class);
	}

	public void updateLearnerProfileCompetencyStatusToInprogress(LearnerProfileCompetencyStatusBean bean) {
		bean.setStatus(IN_PROGRESS);
		dao.insertOrUpdateLearnerProfileCompetencyStatus(bean);
	}

	public void updateLearnerProfileCompetencyStatusToCompleted(LearnerProfileCompetencyStatusBean bean) {
		bean.setStatus(COMPLETED);
		dao.insertOrUpdateLearnerProfileCompetencyStatus(bean);
	}

	public void updateLearnerProfileCompetencyStatusToMastered(LearnerProfileCompetencyStatusBean bean) {
		bean.setStatus(MASTERED);
		dao.insertOrUpdateLearnerProfileCompetencyStatus(bean);
	}
}
