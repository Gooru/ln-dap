package org.gooru.dap.deps.competency.assessmentscore.content;

import org.skife.jdbi.v2.DBI;

public class ContentCompetencyStatusService {

	private final ContentCompetencyStatusDao dao;
	
	private final static int IN_PROGRESS = 1;
	private final static int INFERRED = 2;
	private final static int COMPLETED = 4;
	
	public ContentCompetencyStatusService(DBI dbi) {
		this.dao = dbi.onDemand(ContentCompetencyStatusDao.class);
	}
	
	public void insertContentCompetencyStatusToInProgress(ContentCompetencyStatusBean bean) {
		bean.setStatus(IN_PROGRESS);
		this.dao.insertOrUpdateContentComptencyStatus(bean);
	}
	
	public void insertOrUpdateContentCompetencyStatusToInferred(ContentCompetencyStatusBean bean) {
		bean.setStatus(INFERRED);
		this.dao.insertOrUpdateContentComptencyStatus(bean);
	}
	
	public void insertOrUpdateContentCompetencyStatusToCompleted(ContentCompetencyStatusBean bean) {
		bean.setStatus(COMPLETED);
		this.dao.insertOrUpdateContentComptencyStatus(bean);
	}
}
