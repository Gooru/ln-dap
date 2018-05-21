package org.gooru.dap.deps.competency.common;

import java.util.List;

import org.gooru.dap.deps.competency.score.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.score.mapper.GutCode;
import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class CompetencyAssessmentService {

	private final CompetencyAssessmentDao competencyAssessmentDao;

	public CompetencyAssessmentService(DBI dbi) {
		competencyAssessmentDao = dbi.onDemand(CompetencyAssessmentDao.class);
	}

	public AssessmentCompetency getAssessmentCompetency(String assessmentId) {
		return competencyAssessmentDao.getAssessmentCompetency(assessmentId);
	}

	public List<GutCode> getGutCodeMapping(String taxonomyIds) {
		return competencyAssessmentDao.fetchGutCodes(taxonomyIds);
	}

	public boolean isSignatureAssessment(String assessmentId) {
		int count = competencyAssessmentDao.fetchSignatureAssessment(assessmentId);
		return count >= 1 ? true : false;
	}
}
