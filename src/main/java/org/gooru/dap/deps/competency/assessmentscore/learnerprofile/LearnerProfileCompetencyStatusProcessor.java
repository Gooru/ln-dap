package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.Constants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusProcessor {


  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScore;
  private final String gutCode;
  private final boolean isSignature;
  private LearnerProfileCompetencyStatusService service =
      new LearnerProfileCompetencyStatusService(DBICreator.getDbiForDefaultDS());
  private GetTenantSettingService tenant = new GetTenantSettingService(DBICreator.getDbiForCoreDS());

  public LearnerProfileCompetencyStatusProcessor(AssessmentScoreEventMapper assessmentScore,
      String gutCode, boolean isSignature) {
    this.assessmentScore = assessmentScore;
    this.gutCode = gutCode;
    this.isSignature = isSignature;
  }
  private int getCompletedSettingsScore() {
    String tenantId = this.assessmentScore.context.getTenantId();
    if(tenantId != null && !tenantId.isEmpty()) {
      String settingData = tenant.getTenantSettings(tenantId);
      return Integer.parseInt(settingData);
    } else {
      return Constants.DEFAULT_COMPLETED_SCORE;
    }
  }
  public void process() {
    LearnerProfileCompetencyStatusCommand command =
        LearnerProfileCompetencyStatusCommandBuilder.build(assessmentScore, gutCode);
    LearnerProfileCompetencyStatusBean bean = new LearnerProfileCompetencyStatusBean(command);

    /*
     * if score > 80 and signature assessment update to mastered. If score > 80 and regular
     * assessment update to completed. If score < 80 and regular assessment update to in_progress
     */

    ResultMapper result = this.assessmentScore.getResult();
    Double score = null;
    if (result != null) {
      score = this.assessmentScore.getResult().getScore();
    }

    if (score != null) {
      if (this.isSignature && score >= Constants.MASTERY_SCORE) {
        LOGGER.info("LP Status: score = {} || isSignature = {} || gutCode = {} || status=Masterd",
            score, this.isSignature, gutCode);
        service.updateLearnerProfileCompetencyStatusToMastered(bean);
      } 
      if(score >= getCompletedSettingsScore()) {
        LOGGER.info("LP Status: score = {} || isSignature = {} || gutCode = {} || status=Completed",
            score, this.isSignature, gutCode);
        service.updateLearnerProfileCompetencyStatusToCompleted(bean);
      }
    } else {
      if (!this.isSignature) {
        LOGGER.info(
            "LP Status: score = {} || isSignature = {} || gutCode = {} || status=InProgress", score,
            this.isSignature, gutCode);
        service.updateLearnerProfileCompetencyStatusToInprogress(bean);
      }
    }
  }
}
