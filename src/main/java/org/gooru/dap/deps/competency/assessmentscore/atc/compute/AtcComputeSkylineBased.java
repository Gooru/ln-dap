package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class AtcComputeSkylineBased implements AtcCompute {

  private final DBI dbi;
  private final DBI coreDbi;
  private CompetencyPerformanceDao competencyPerformanceDao;
  private SkylineDao userSkylineDao;
  private AtcEvent atcEventObject;
  private final CompetencyStatsModel skylineCompetencyStatsModel = new CompetencyStatsModel();
  private static final int IN_PROGRESS = 1;
  private List<String> skylineCompetencyCodes = new ArrayList<>();

  private static final Logger LOGGER = LoggerFactory.getLogger(AtcComputeSkylineBased.class);

  AtcComputeSkylineBased(DBI dbi, DBI coreDbi) {
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
      LOGGER.info("Compute Skyline Competency Stats for User " + atcEventObject.getUserId()
          + " for class " + atcEventObject.getClassId());
      LOGGER.debug("Compute User Competency Completion");
      computeSkylineBasedCompetencyCompletion();
      LOGGER.debug("Fetch Skyline Competencies");
      fetchSkylineComptencies();
      LOGGER.debug("Compute User Competency Performance");      
      computeSkylineBasedUserCompetencyPerformance();
      return skylineCompetencyStatsModel;
    } catch (IllegalStateException ex) {
      LOGGER.warn("Caught IllegalStateException ");
      return null;
    }
  }

  private void computeSkylineBasedCompetencyCompletion() {

    Integer totalCompetencies = 0;

    SkylineCompetencyCompletionService competencyCompletionService =
        new SkylineCompetencyCompletionService(DBICreator.getDbiForDefaultDS());

    skylineCompetencyStatsModel.setUserId(atcEventObject.getUserId());
    skylineCompetencyStatsModel.setClassId(atcEventObject.getClassId());
    skylineCompetencyStatsModel.setCourseId(atcEventObject.getCourseId());
    skylineCompetencyStatsModel.setGradeId(atcEventObject.getGradeId());
    skylineCompetencyStatsModel.setSubjectCode(atcEventObject.getSubjectCode());
    // lpCompetencyStatsModel.setTotalCompetencies(totalCompetencies);
    LocalDate today = LocalDate.now();
    skylineCompetencyStatsModel.setMonth(today.getMonthValue());
    skylineCompetencyStatsModel.setYear(today.getYear());
    LocalDate localDate = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
    Date statsDate = Date.valueOf(localDate);
    skylineCompetencyStatsModel.setStatsDate(statsDate);      
    CompetencyStatsModel stats = competencyCompletionService.fetchUserSkylineCompetencyStatus(
        atcEventObject.getUserId(), atcEventObject.getSubjectCode());
    if (stats != null) {
      Integer compCount = stats.getCompletedCompetencies();
      skylineCompetencyStatsModel.setCompletedCompetencies(compCount);
      totalCompetencies = stats.getTotalCompetencies();
      skylineCompetencyStatsModel.setTotalCompetencies(totalCompetencies);
      skylineCompetencyStatsModel.setInprogressCompetencies(stats.getInprogressCompetencies());
      skylineCompetencyStatsModel.setPercentCompletion(
          totalCompetencies > 0 ? (Double.valueOf(compCount) / totalCompetencies * 100) : 0.0);
    } else {
      skylineCompetencyStatsModel.setCompletedCompetencies(0);
      skylineCompetencyStatsModel.setInprogressCompetencies(0);
      skylineCompetencyStatsModel.setPercentCompletion(0.0);
    }
  }

  private void computeSkylineBasedUserCompetencyPerformance() {
    Double userAvgScore = 0.0;
    competencyPerformanceDao = dbi.onDemand(CompetencyPerformanceDao.class);
    if (skylineCompetencyCodes != null && !skylineCompetencyCodes.isEmpty()) {
      userAvgScore =
          competencyPerformanceDao.fetchSkylineCompetencyPerformance(atcEventObject.getUserId(), 
              PGArrayUtils.convertFromListStringToSqlArrayOfString(skylineCompetencyCodes));
      if (userAvgScore != null) {
        skylineCompetencyStatsModel.setPercentScore(userAvgScore);
      }      
    }
  }
  
  private void fetchSkylineComptencies() {
    this.userSkylineDao = dbi.onDemand(SkylineDao.class);
    List<CompetencyModel> userSkylineModels = new ArrayList<>();
    userSkylineModels = userSkylineDao.fetchUserDomainCompetencyStatus(atcEventObject.getUserId(), atcEventObject.getSubjectCode());

    if (userSkylineModels.isEmpty()) {
      LOGGER.info("The User Skyline is empty");
    } else {      
      List<CompetencyModel> competencies = userSkylineModels.stream()
          .filter(skymodel -> skymodel.getStatus() >= IN_PROGRESS).collect(Collectors.toList());      
      competencies.forEach(model -> {
        String compCode = model.getCompetencyCode();
        LOGGER.debug("Skyline Competencies Code" + compCode);
        skylineCompetencyCodes.add(compCode);       
      });
    }
  }

}
