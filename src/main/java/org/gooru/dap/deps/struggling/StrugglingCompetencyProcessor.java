
package org.gooru.dap.deps.struggling;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.StatusConstants;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 11-Oct-2019
 */
public class StrugglingCompetencyProcessor {

  private final double MASTERY_SCORE = 80.00;

  private final static Logger LOGGER = LoggerFactory.getLogger(StrugglingCompetencyProcessor.class);

  private final AssessmentScoreEventMapper assessmentScore;
  private final List<String> gutCodes;

  private List<String> completedCompetencies;
  private List<String> inferredCompetencies;

  private static final Pattern HYPEN_PATTERN = Pattern.compile("-");

  private final static StrugglingCompetencyService SERVICE =
      new StrugglingCompetencyService(DBICreator.getDbiForDefaultDS());

  public StrugglingCompetencyProcessor(AssessmentScoreEventMapper assessmentScore,
      List<String> gutCodes) {
    this.assessmentScore = assessmentScore;
    this.gutCodes = gutCodes;
  }

  public void process() {

    ResultMapper result = this.assessmentScore.getResult();
    Double score = null;
    if (result != null) {
      score = this.assessmentScore.getResult().getScore();
    }

    // We are inferring the subject code from the first competency assuming
    // that that competencies
    // tagged to assessment are inline with class subject and all
    // competencies are of same subject
    String subjectCode = HYPEN_PATTERN.split(gutCodes.get(0))[0];
    fetchUserSkyline(assessmentScore.getUserId(), subjectCode);

    for (String gut : gutCodes) {
      StrugglingCompetencyCommand command =
          StrugglingCompetencyCommand.build(gut, this.assessmentScore.getUserId());
      StrugglingCompetencyCommand.StrugglingCompetencyCommandBean bean = command.asBean();

      // if the assessment is completed then all the competencies tagged
      // to assessment need to be
      // removed from the struggling competencies if they are present for
      // this month and year. We
      // need to keep previous months records as is to report the
      // competencies are struggling in
      // those months.
      if (score != null && score >= MASTERY_SCORE) {
        SERVICE.removeFromStruggling(bean);
      }

      // Check if the competency is already been completed or inferred
      // from the user skyline data
      if (checkIfCompletedOrInferred(gut)) {
        LOGGER.debug("competecy '{}' of the user '{}' has inferred mastery, skipping", gut,
            bean.getUserId());
        continue;
      }

      // After all checks looks like the competency is really struggling
      // as its not been already
      // completed or inferred. Hence we will insert in as struggling
      // competencies
      SERVICE.insertAsStruggling(bean);
    }
  }

  private boolean checkIfCompletedOrInferred(String gutCode) {
    return (completedCompetencies.contains(gutCode) || inferredCompetencies.contains(gutCode))
        ? true
        : false;
  }

  // Fetch the user skyline and populate the completed and inferred competency
  // lists for the further
  // computation
  private void fetchUserSkyline(String userId, String subject) {
    final List<UserDomainCompetencyMatrixModel> userCompetencyMatrixModels =
        SERVICE.fetchUserSkyline(userId, subject, Timestamp.valueOf(LocalDateTime.now()));

    completedCompetencies = new ArrayList<>();
    inferredCompetencies = new ArrayList<>();

    if (!userCompetencyMatrixModels.isEmpty()) {
      List<UserDomainCompetencyMatrixModel> completed = userCompetencyMatrixModels.stream()
          .filter(model -> model.getStatus() >= StatusConstants.COMPLETED)
          .collect(Collectors.toList());

      Map<String, Map<String, UserDomainCompetencyMatrixModel>> completedCompMatrixMap =
          new HashMap<>();
      completed.forEach(model -> {
        String domain = model.getDomainCode();
        String compCode = model.getCompetencyCode();
        completedCompetencies.add(compCode);
        if (completedCompMatrixMap.containsKey(domain)) {
          Map<String, UserDomainCompetencyMatrixModel> competencies =
              completedCompMatrixMap.get(domain);
          competencies.put(compCode, model);
          completedCompMatrixMap.put(domain, competencies);
        } else {
          Map<String, UserDomainCompetencyMatrixModel> competencies = new HashMap<>();
          competencies.put(compCode, model);
          completedCompMatrixMap.put(domain, competencies);
        }
      });

      userCompetencyMatrixModels.forEach(model -> {
        String domainCode = model.getDomainCode();
        int sequence = model.getCompetencySeq();
        int status = model.getStatus();

        if (completedCompMatrixMap.containsKey(domainCode)) {
          Map<String, UserDomainCompetencyMatrixModel> competencies =
              completedCompMatrixMap.get(domainCode);
          for (Map.Entry<String, UserDomainCompetencyMatrixModel> entry : competencies.entrySet()) {
            UserDomainCompetencyMatrixModel compModel = entry.getValue();
            int compSeq = compModel.getCompetencySeq();

            if (sequence < compSeq && status < StatusConstants.ASSERTED) {
              model.setStatus(StatusConstants.INFERRED);
              inferredCompetencies.add(model.getCompetencyCode());
            }
          }
        }
      });
    }
  }
}
