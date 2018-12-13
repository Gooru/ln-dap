package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.util.ArrayList;
import java.util.List;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.components.jdbi.PGArrayUtils;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor.GradeCompetencyProcessor;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class AtcComputeImpl implements AtcCompute {

    private final DBI dbi;
    private final DBI coreDbi;    
    private CompetencyPerformanceDao competencyPerformanceDao;
	private AtcEvent atcEventObject;
	private final GradeCompetencyStatsModel gradeCompetencyStatsModel = new GradeCompetencyStatsModel();
	List<String> gradeCompetencyList = new ArrayList<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(AtcCompute.class);
	
    AtcComputeImpl(DBI dbi, DBI coreDbi) {
        this.dbi = dbi;
        this.coreDbi = coreDbi;
    }

	public GradeCompetencyStatsModel compute(AtcEvent atcEventObject) {
		this.atcEventObject = atcEventObject;
		try {
			return calculateStats();
		} catch (RuntimeException re) {
			LOGGER.warn("Not able to compute Grade Competency Stats for the User: " + atcEventObject.getUserId());
			throw re;
		}
	}

	private GradeCompetencyStatsModel calculateStats() {
		try {
			LOGGER.debug("Compute User Competency Completion");
			computeGradeBasedUserCompetencyCompletion();
			LOGGER.debug("Compute User Competency Performance");
			computeGradeBasedUserCompetencyPerformance();
			return gradeCompetencyStatsModel;		
		} catch (IllegalStateException ex) {
			LOGGER.warn("Caught IllegalStateException ");
			return null;
		}
	}

	private void computeGradeBasedUserCompetencyCompletion() {

		Integer totalCompetencies = 0;
		String cm = atcEventObject.getUserId();

		CompetencyCompletionService competencyCompletionService = new CompetencyCompletionService(
				DBICreator.getDbiForDefaultDS());

		GradeCompetencyProcessor courseCompetencyProcessor = new GradeCompetencyProcessor();
		gradeCompetencyList = courseCompetencyProcessor.getGradeCompetency(atcEventObject.getGradeId(), atcEventObject.getSubjectCode());

		if (gradeCompetencyList != null && !gradeCompetencyList.isEmpty()) {
			for (String cc : gradeCompetencyList) {
				LOGGER.debug("The list of competencies is" + cc);
			}
			totalCompetencies = gradeCompetencyList.size();

			LOGGER.info("The UserId is" + cm);
			gradeCompetencyStatsModel.setUserId(cm);
			gradeCompetencyStatsModel.setClassId(atcEventObject.getClassId());
			gradeCompetencyStatsModel.setCourseId(atcEventObject.getCourseId());
			gradeCompetencyStatsModel.setGradeId(atcEventObject.getGradeId());
			gradeCompetencyStatsModel.setSubjectCode(atcEventObject.getSubjectCode());
			gradeCompetencyStatsModel.setTotalCompetencies(totalCompetencies);
			GradeCompetencyStatsModel stats = competencyCompletionService.fetchUserCompetencyStatus(cm, atcEventObject.getSubjectCode(),
					gradeCompetencyList);
			if (stats != null) {
				Integer compCount = stats.getCompletedCompetencies();
				gradeCompetencyStatsModel.setCompletedCompetencies(compCount);
				gradeCompetencyStatsModel.setInprogressCompetencies(stats.getInprogressCompetencies());
				gradeCompetencyStatsModel.setPercentCompletion(
						totalCompetencies != 0 ? (Double.valueOf(compCount / totalCompetencies) * 100) : 0);				
			} else {
				gradeCompetencyStatsModel.setCompletedCompetencies(0);
				gradeCompetencyStatsModel.setInprogressCompetencies(0);
				gradeCompetencyStatsModel.setPercentCompletion(0.0);
			}

		}
	}
	
	private void computeGradeBasedUserCompetencyPerformance() {
		
		String cm = atcEventObject.getUserId();
		Double userAvgScore = 0.0;
		
		competencyPerformanceDao = dbi.onDemand(CompetencyPerformanceDao.class);
		
		userAvgScore = competencyPerformanceDao.fetchGradeCompetencyPerformance (cm, 
				PGArrayUtils.convertFromListStringToSqlArrayOfString(gradeCompetencyList));   
		
		if (userAvgScore != null) {
			gradeCompetencyStatsModel.setPercentScore(userAvgScore);
		}
	}

}
