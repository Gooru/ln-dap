package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.dap.components.jdbi.PGArrayUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class GradeCompetencyCompletionService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GradeCompetencyCompletionService.class);
  private static final int IN_PROGRESS = 1;
  private static final int INFERRED = 2;
  private static final int ASSERTED = 3;
  private static final int COMPLETED = 4;
  private static final int MASTERED = 5;
  private final CompetencyCompletionDao competencyCompletionDao;
  private final SkylineDao userSkylineDao;
  private Integer completionCount;
  private CompetencyStatsModel gradeCompetencyStatsModel = new CompetencyStatsModel();

  GradeCompetencyCompletionService(DBI dbi) {
    this.competencyCompletionDao = dbi.onDemand(CompetencyCompletionDao.class);
    this.userSkylineDao = dbi.onDemand(SkylineDao.class);
  }

  CompetencyStatsModel fetchUserCompetencyStatus(String user, String subjectCode,
      List<String> competencyCodes) {

    completionCount = 0;
    List<CompetencyModel> userGradeCompetencyStatusModels = new ArrayList<>();
    List<CompetencyModel> userSkylineModels = new ArrayList<>();

    if (competencyCodes != null && !competencyCodes.isEmpty()) {
      userGradeCompetencyStatusModels = competencyCompletionDao.fetchCompetencyCompletion(user,
          subjectCode, PGArrayUtils.convertFromListStringToSqlArrayOfString(competencyCodes));
      userSkylineModels = userSkylineDao.fetchUserSkyline(user, subjectCode);
    }

    if (userGradeCompetencyStatusModels.isEmpty()) {
      LOGGER.info("The User competency Status model is empty");
      return null;
    } else {
      List<CompetencyModel> completed = userGradeCompetencyStatusModels.stream()
          .filter(model -> model.getStatus() >= COMPLETED).collect(Collectors.toList());
      completionCount = completed.size();
      LOGGER.debug("Completed/Mastered Competencies " + completionCount);

      Map<String, Map<String, CompetencyModel>> completedCompMap = new HashMap<>();
      Map<String, Map<String, CompetencyModel>> skylineCompletedCompMap = new HashMap<>();

      completed.forEach(model -> {
        String domain = model.getDomainCode();
        String compCode = model.getCompetencyCode();
        LOGGER.debug("Completed/Mastered Competencies Code" + compCode);

        if (completedCompMap.containsKey(domain)) {
          Map<String, CompetencyModel> competencies = completedCompMap.get(domain);
          competencies.put(compCode, model);
          completedCompMap.put(domain, competencies);
        } else {
          Map<String, CompetencyModel> competencies = new HashMap<>();
          competencies.put(compCode, model);
          completedCompMap.put(domain, competencies);
        }
      });

      if (!userSkylineModels.isEmpty()) {
        // We need to filter out completed/mastered specifically since
        // in the L_p_C_s_ts table
        // in-progress is also stored.
        List<CompetencyModel> skylineCompleted = userSkylineModels.stream()
            .filter(skymodel -> skymodel.getStatus() >= COMPLETED).collect(Collectors.toList());

        skylineCompleted.forEach(model -> {
          String domain = model.getDomainCode();
          String compCode = model.getCompetencyCode();
          LOGGER.debug("Skyline Completed/Mastered Competencies Code" + compCode);

          if (skylineCompletedCompMap.containsKey(domain)) {
            Map<String, CompetencyModel> skyCompetencies = skylineCompletedCompMap.get(domain);
            skyCompetencies.put(compCode, model);
            skylineCompletedCompMap.put(domain, skyCompetencies);
          } else {
            Map<String, CompetencyModel> skyCompetencies = new HashMap<>();
            skyCompetencies.put(compCode, model);
            skylineCompletedCompMap.put(domain, skyCompetencies);
          }
        });
      }

      userGradeCompetencyStatusModels.forEach(model -> {
        String domainCode = model.getDomainCode();
        int sequence = model.getCompetencySeq();
        int status = model.getStatus();

        if (completedCompMap.containsKey(domainCode)) {
          Map<String, CompetencyModel> competencies = completedCompMap.get(domainCode);
          for (Map.Entry<String, CompetencyModel> entry : competencies.entrySet()) {
            CompetencyModel compModel = entry.getValue();
            int compSeq = compModel.getCompetencySeq();

            if (sequence < compSeq && status < ASSERTED && model.getStatus() != INFERRED) {
              model.setStatus(INFERRED);
              completionCount++;
            }
          }
        }

        // Run through the skyline to check for completion
        if (skylineCompletedCompMap != null && !skylineCompletedCompMap.isEmpty()
            && skylineCompletedCompMap.containsKey(domainCode)) {
          Map<String, CompetencyModel> skyCompetencies = skylineCompletedCompMap.get(domainCode);
          for (Map.Entry<String, CompetencyModel> entry : skyCompetencies.entrySet()) {
            CompetencyModel skyCompModel = entry.getValue();
            int skyCompSeq = skyCompModel.getCompetencySeq();

            if (sequence < skyCompSeq && status < ASSERTED && model.getStatus() != INFERRED) {
              model.setStatus(INFERRED);
              completionCount++;
            }
          }
        }
      });

      gradeCompetencyStatsModel.setCompletedCompetencies(completionCount);
      LOGGER.debug("Completed/Mastered/Inferred " + completionCount);

      List<CompetencyModel> inProgress = userGradeCompetencyStatusModels.stream()
          .filter(model -> model.getStatus() == IN_PROGRESS).collect(Collectors.toList());
      gradeCompetencyStatsModel.setInprogressCompetencies(inProgress.size());
      LOGGER.debug("InProgress Competencies " + inProgress.size());

      return gradeCompetencyStatsModel;
    }
  }
}
