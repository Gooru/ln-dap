package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import org.skife.jdbi.v2.DBI;


/**
 * @author mukul@gooru
 */
public class GradeCompetencyStatsService {
	
	private final GradeCompetencyStatsDao gradeCompetencyStatsDao;
	
	public GradeCompetencyStatsService(DBI dbi) {
		this.gradeCompetencyStatsDao = dbi.onDemand(GradeCompetencyStatsDao.class);
	}

	public void insertUserClassCompetencyStats(GradeCompetencyStatsModel gradeCompetencyStatsModel) {
		gradeCompetencyStatsDao.insertUserClassCompetencyStats(gradeCompetencyStatsModel);
	}
}
