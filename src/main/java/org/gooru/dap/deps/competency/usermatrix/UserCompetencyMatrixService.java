package org.gooru.dap.deps.competency.usermatrix;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 04-May-2018
 */
public class UserCompetencyMatrixService {

	private final UserCompetencyMatrixDao dao;
	
	private final static int NOT_STARTED = 0;
	private final static int IN_PROGRESS = 1;
	private final static int INFERRED = 2;
	//private final static int NOT_DEFINED = 3;
	private final static int COMPLETED = 4;
	private final static int MASTERED = 5;
	
	public UserCompetencyMatrixService(DBI dbi) {
		this.dao = dbi.onDemand(UserCompetencyMatrixDao.class);
	}
	
	public void updateUserCompetencyToInprogress(UserCompetencyMatrixBean bean) {
		bean.setStatus(IN_PROGRESS);
		this.dao.updateUserCompetencyMatrix(bean);
	}
	
	public void updateUserCompetencyToCompleted(UserCompetencyMatrixBean bean) {
		bean.setStatus(COMPLETED);
		this.dao.updateUserCompetencyMatrix(bean);
	}
	
	public void updateUserCompetencyToMastered (UserCompetencyMatrixBean bean) {
		bean.setStatus(MASTERED);
		this.dao.updateUserCompetencyMatrix(bean);
	}
}
