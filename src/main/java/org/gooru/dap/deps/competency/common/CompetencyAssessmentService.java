package org.gooru.dap.deps.competency.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.db.mapper.GutCode;
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

	public Map<String, String> getGutCodeMapping(String taxonomyIds) {
		List<GutCode> gutCodes = competencyAssessmentDao.fetchGutCodes(taxonomyIds);
		Map<String, String> fwCodeMap = new HashMap<>();
		
		gutCodes.forEach(gutcode -> {
			fwCodeMap.put(gutcode.getTaxonomyCode(), gutcode.getGutCode());
		});
		
		return fwCodeMap;
	}

	public boolean isSignatureAssessment(String assessmentId) {
		int count = competencyAssessmentDao.fetchSignatureAssessment(assessmentId);
		return count >= 1 ? true : false;
	}
}
