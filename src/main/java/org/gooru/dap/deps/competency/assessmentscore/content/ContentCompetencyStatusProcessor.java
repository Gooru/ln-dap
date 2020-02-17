package org.gooru.dap.deps.competency.assessmentscore.content;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.Constants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.TenantSettingService;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ContextMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyStatusProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScore;
  private final String competencyCode;

  private ContentCompetencyStatusService service =
      new ContentCompetencyStatusService(DBICreator.getDbiForDefaultDS());
  private TenantSettingService tenantSettingService = 
      new TenantSettingService(DBICreator.getDbiForCoreDS());

  public ContentCompetencyStatusProcessor(AssessmentScoreEventMapper assessmentScore,
      String competencyCode) {
    this.assessmentScore = assessmentScore;
    this.competencyCode = competencyCode;
  }

  private double getCompletedScoreSettings() {
    ContextMapper context = this.assessmentScore.getContext();
    String tenantId = context.getTenantId();
    if(tenantId != null && !tenantId.isEmpty()) {
      String completedScore = tenantSettingService.fetchTenantSettings(tenantId);
      return Double.parseDouble(completedScore);
    } else {
      return Constants.DEFAULT_COMPLETED_SCORE;
    }
  }
  
  public void process() {
    ContentCompetencyStatusCommand command =
        ContentCompetencyStatusCommandBuilder.build(this.assessmentScore, this.competencyCode);
    ContentCompetencyStatusBean bean = new ContentCompetencyStatusBean(command);

    ResultMapper result = this.assessmentScore.getResult();
    Double score = null;
    if (result != null) {
      score = this.assessmentScore.getResult().getScore();
    }

    // If user obtained score is greater than completion score then update
    // the
    // status to completed. Status should not be updated to completed if its
    // already
    // completed. This should be handled in the query at DAO layer.
    if (score != null && score >= getCompletedScoreSettings()) {
      LOGGER.debug("Content Competency Status: competency:{} || status=completed",
          this.competencyCode);
      this.service.insertOrUpdateContentCompetencyStatusToCompleted(bean);
      return;
    }

    // If user obtained score is less that completion score then update the
    // status
    // to in_progress. Status should not be updated to in_progress if its
    // already
    // completed. This should be handled in the query at DAO layer.
    LOGGER.debug("Content Competency Status: competency:{} || status=inprogress",
        this.competencyCode);
    this.service.insertContentCompetencyStatusToInProgress(bean);
  }
}
