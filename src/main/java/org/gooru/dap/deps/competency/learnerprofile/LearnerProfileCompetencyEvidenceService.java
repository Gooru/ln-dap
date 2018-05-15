package org.gooru.dap.deps.competency.learnerprofile;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceService {

	private final LearnerProfileCompetencyEvidenceDao dao;

	public LearnerProfileCompetencyEvidenceService(DBI dbi) {
		this.dao = dbi.onDemand(LearnerProfileCompetencyEvidenceDao.class);
	}

	public void insertOrUpdateLearnerProfileCompetencyEvidence(LearnerProfileCompetencyEvidenceBean bean) {
		this.dao.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
	}
}
