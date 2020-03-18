package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.sql.Timestamp;
import org.skife.jdbi.v2.DBI;

/**
 * @author mukul@gooru
 */
public class CompetencyStatsService {

  private final CompetencyStatsDao competencyStatsDao;

  public CompetencyStatsService(DBI dbi) {
    this.competencyStatsDao = dbi.onDemand(CompetencyStatsDao.class);
  }

  public void insertUserClassCompetencyStats(CompetencyStatsModel gradeCompetencyStatsModel) {
    competencyStatsDao.insertUserClassCompetencyStats(gradeCompetencyStatsModel,
        new Timestamp(System.currentTimeMillis()));
  }
}
