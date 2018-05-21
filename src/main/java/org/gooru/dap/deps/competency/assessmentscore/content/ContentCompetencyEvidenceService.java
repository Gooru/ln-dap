package org.gooru.dap.deps.competency.assessmentscore.content;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyEvidenceService {

	private final ContentCompetencyEvidenceDao dao;
	
	public ContentCompetencyEvidenceService(DBI dbi) {
		this.dao = dbi.onDemand(ContentCompetencyEvidenceDao.class);
	}
	
	public void insertOrUpdateContentCompetencyEvidence(ContentCompetencyEvidenceBean bean) {
		this.dao.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
	}
}
