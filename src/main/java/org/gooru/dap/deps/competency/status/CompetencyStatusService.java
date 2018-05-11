package org.gooru.dap.deps.competency.status;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyStatusService {

	private final CompetencyStatusDao dao;
	
	private final static String IN_PROGRESS = "in_progress";
	private final static String COMPLETED = "completed";
	
	public CompetencyStatusService(DBI dbi) {
		this.dao = dbi.onDemand(CompetencyStatusDao.class);
	}
	
	public void updateCompetencyStatusToInprogress(CompetencyStatusBean bean) {
		bean.setStatus(IN_PROGRESS);
		this.dao.insertOrUpdateCompetencyStatus(bean);
	}
	
	public void updateCompetencyStatusToCompleted(CompetencyStatusBean bean) {
		bean.setStatus(COMPLETED);
		this.dao.insertOrUpdateCompetencyStatus(bean);
	}
}
