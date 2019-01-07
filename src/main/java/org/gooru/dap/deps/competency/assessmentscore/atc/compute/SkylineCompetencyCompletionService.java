package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkylineCompetencyCompletionService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SkylineCompetencyCompletionService.class);
  private static final int IN_PROGRESS = 1;
  private static final int INFERRED = 2;
  private static final int ASSERTED = 3;
  private static final int COMPLETED = 4;
  private static final int MASTERED = 5;
  private final SkylineDao userSkylineDao;
  private Integer completionCount;
  private CompetencyStatsModel skylineCompetencyStatsModel = new CompetencyStatsModel();

  SkylineCompetencyCompletionService(DBI dbi) {
    this.userSkylineDao = dbi.onDemand(SkylineDao.class);
  }

  CompetencyStatsModel fetchUserSkylineCompetencyStatus(String user, String subjectCode) {

    completionCount = 0;
    Integer totalCompetencies = 0;

    List<CompetencyModel> userSkylineModels = new ArrayList<>();
    userSkylineModels = userSkylineDao.fetchUserDomainCompetencyStatus(user, subjectCode);

    if (userSkylineModels.isEmpty()) {
      LOGGER.info("The User Skyline is empty");
      return null;
    } else {
      Map<String, Map<String, CompetencyModel>> completedCompMap = new HashMap<>();
      Map<String, Map<String, CompetencyModel>> skylineCompletedCompMap = new HashMap<>();
      totalCompetencies = userSkylineModels.size();
      skylineCompetencyStatsModel.setTotalCompetencies(totalCompetencies);

      // We need to filter out completed/mastered specifically since in the L_p_C_s_ts table
      // in-progress is also stored.
      List<CompetencyModel> skylineCompleted = userSkylineModels.stream()
          .filter(skymodel -> skymodel.getStatus() >= COMPLETED).collect(Collectors.toList());
      completionCount = skylineCompleted.size();
      LOGGER.debug("Completed/Mastered Competencies " + completionCount);

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

      userSkylineModels.forEach(model -> {
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
      });

      skylineCompetencyStatsModel.setCompletedCompetencies(completionCount);
      LOGGER.debug("Completed/Mastered/Inferred " + completionCount);

      List<CompetencyModel> inProgress = userSkylineModels.stream()
          .filter(model -> model.getStatus() == IN_PROGRESS).collect(Collectors.toList());
      skylineCompetencyStatsModel.setInprogressCompetencies(inProgress.size());
      LOGGER.debug("InProgress Competencies " + inProgress.size());

      return skylineCompetencyStatsModel;
    }
  }
}
