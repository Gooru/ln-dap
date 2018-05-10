package org.gooru.dap.deps.competency.score;

import java.util.List;

import org.gooru.dap.deps.competency.score.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.score.mapper.GutCode;
import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyCollectionScoreService {

	private final CompetencyCollectionScoreDao competencyCollectionScoreDao;
	private final CompetencyAssessmentDao competencyAssessmentDao;
	
	public CompetencyCollectionScoreService(DBI defaultDbi, DBI coreDbi) {
		competencyCollectionScoreDao = defaultDbi.onDemand(CompetencyCollectionScoreDao.class);
		competencyAssessmentDao = coreDbi.onDemand(CompetencyAssessmentDao.class);
	}
	
	public void insertOrUpdateAssessmentCompetencyScore(CompetencyCollectionScoreBean bean) {
		competencyCollectionScoreDao.insertOrUpdate(bean);
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
