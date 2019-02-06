package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.components.jdbi.PGArrayUtils;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor.GradeCompetencyProcessor;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * @author mukul@gooru
 */
public class AtcComputeGradeBased implements AtcCompute {

  private final DBI dbi;
  private final DBI coreDbi;
  private CompetencyPerformanceDao competencyPerformanceDao;
  private AtcEvent atcEventObject;
  private final CompetencyStatsModel gradeCompetencyStatsModel = new CompetencyStatsModel();
  List<String> gradeCompetencyList = new ArrayList<>();

  private static final Logger LOGGER = LoggerFactory.getLogger(AtcComputeGradeBased.class);

  AtcComputeGradeBased(DBI dbi, DBI coreDbi) {
    this.dbi = dbi;
    this.coreDbi = coreDbi;
  }

  public CompetencyStatsModel compute(AtcEvent atcEventObject) {
    this.atcEventObject = atcEventObject;
    try {
      return calculateStats();
    } catch (RuntimeException re) {
      LOGGER.warn(
          "Not able to compute Grade Competency Stats for the User: " + atcEventObject.getUserId());
      throw re;
    }
  }

  private CompetencyStatsModel calculateStats() {
    try {
      LOGGER.info("Compute Competency Stats for User " + atcEventObject.getUserId() + "at grade "
          + atcEventObject.getGradeId() + " for class " + atcEventObject.getClassId());
      LOGGER.debug("Compute User Competency Completion for User " + atcEventObject.getUserId());
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

    GradeCompetencyCompletionService competencyCompletionService =
        new GradeCompetencyCompletionService(DBICreator.getDbiForDefaultDS());

    GradeCompetencyProcessor courseCompetencyProcessor = new GradeCompetencyProcessor();
    gradeCompetencyList = courseCompetencyProcessor.getGradeCompetency(atcEventObject.getGradeId(),
        atcEventObject.getSubjectCode());

    if (gradeCompetencyList != null && !gradeCompetencyList.isEmpty()) {
      for (String cc : gradeCompetencyList) {
        LOGGER.debug("The list of competencies is" + cc);
      }
      totalCompetencies = gradeCompetencyList.size();

      LOGGER.debug("The UserId is" + cm);
      gradeCompetencyStatsModel.setUserId(cm);
      gradeCompetencyStatsModel.setClassId(atcEventObject.getClassId());
      gradeCompetencyStatsModel.setCourseId(atcEventObject.getCourseId());
      gradeCompetencyStatsModel.setGradeId(atcEventObject.getGradeId());
      gradeCompetencyStatsModel.setSubjectCode(atcEventObject.getSubjectCode());
      gradeCompetencyStatsModel.setTotalCompetencies(totalCompetencies);
      LocalDate today = LocalDate.now();
      gradeCompetencyStatsModel.setMonth(today.getMonthValue());
      gradeCompetencyStatsModel.setYear(today.getYear());
      LocalDate localDate = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
      Date statsDate = Date.valueOf(localDate);
      LOGGER.info("The date is " + statsDate);
      gradeCompetencyStatsModel.setStatsDate(statsDate);      
      CompetencyStatsModel stats = competencyCompletionService.fetchUserCompetencyStatus(cm,
          atcEventObject.getSubjectCode(), gradeCompetencyList);
      if (stats != null) {
        Integer compCount = stats.getCompletedCompetencies();
        gradeCompetencyStatsModel.setCompletedCompetencies(compCount);
        gradeCompetencyStatsModel.setInprogressCompetencies(stats.getInprogressCompetencies());
        LOGGER.debug("The total Competencies are " + totalCompetencies);
        gradeCompetencyStatsModel.setPercentCompletion(
            totalCompetencies > 0 ? (Double.valueOf(compCount) / totalCompetencies * 100) : 0.0);
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

    userAvgScore = competencyPerformanceDao.fetchGradeCompetencyPerformance(cm,
        PGArrayUtils.convertFromListStringToSqlArrayOfString(gradeCompetencyList));

    if (userAvgScore != null) {
      gradeCompetencyStatsModel.setPercentScore(userAvgScore);
    }
  }
}
