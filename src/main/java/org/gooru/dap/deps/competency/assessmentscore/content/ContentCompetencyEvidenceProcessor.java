package org.gooru.dap.deps.competency.assessmentscore.content;

import java.util.regex.Pattern;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.Constants;
import org.gooru.dap.constants.StatusConstants;
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
public class ContentCompetencyEvidenceProcessor {

  private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");
  private static final Pattern HYPHEN_PATTERN = Pattern.compile("-");

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScore;
  private final String competencyCode;
  private final String gutCode;

  private ContentCompetencyEvidenceService service =
      new ContentCompetencyEvidenceService(DBICreator.getDbiForDefaultDS());
  private TenantSettingService tenantSettingService = 
      new TenantSettingService(DBICreator.getDbiForCoreDS());

  public ContentCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore,
      String competencyCode, String gutCode) {
    this.assessmentScore = assessmentScore;
    this.competencyCode = competencyCode;
    this.gutCode = gutCode;
  }
  
  private double getCompletionScoreSettings() {
    ContextMapper context = this.assessmentScore.getContext();
    String tenantId = context.getTenantId();
    if(tenantId != null && !tenantId.isEmpty()) {
      return tenantSettingService.fetchTenantCompletionScore(tenantId);
    } else {
      return Constants.DEFAULT_COMPLETION_SCORE;
    }
  }
  

  public void process() {

    ResultMapper result = this.assessmentScore.getResult();
    Double score = null;
    if (result != null) {
      score = this.assessmentScore.getResult().getScore();
    }

    ContentCompetencyEvidenceCommand command =
        ContentCompetencyEvidenceCommandBuilder.build(this.assessmentScore);
    ContentCompetencyEvidenceBean bean = new ContentCompetencyEvidenceBean(command);

    bean.setCompetencyCode(competencyCode);
    bean.setGutCode(gutCode);

    String frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
    bean.setFrameworkCode(frameworkCode);

    boolean isMicroCompetency = (HYPHEN_PATTERN.split(competencyCode).length >= 5);
    bean.setMicroCompetency(isMicroCompetency);

    // Calculate the status to persist in evidence ts table to uniquely
    // identify the
    // evidence by status
    int status = StatusConstants.IN_PROGRESS;
    if (score != null && score >= getCompletionScoreSettings()) {
      status = StatusConstants.COMPLETED;
    }
    LOGGER.debug("Content Competency Evidence: Competency:{} || status:{}", competencyCode, status);
    // Update - 12-July-2018:
    // Regardless of the score, persist the evidence. This will enable in
    // progress
    // evidence

    // Update - 02-Aug-2018:
    // Get the existing score for user, gut and assessment. IF there is no
    // score
    // already exists, then persist the evidence. If score exists and less
    // than 80
    // (in-progress) then update the evidence. If score exists and greater
    // than 80
    // then only update evidence if status is COMPLETED. Here, we do not
    // want to down grade the score if its already COMPLETED. However,
    // we need to persist latest evidence for them.
    Double existingScore = this.service.getCompetencyScore(bean);
    LOGGER.debug("existing score for the competency '{}' is '{}'", competencyCode, existingScore);
    if (existingScore == null || existingScore < getCompletionScoreSettings()) {
      this.service.insertOrUpdateContentCompetencyEvidence(bean);
    } else {
      if (status != StatusConstants.IN_PROGRESS) {
        this.service.insertOrUpdateContentCompetencyEvidence(bean);
      }
    }

    // set the status to persist in TS table
    bean.setStatus(status);

    // Update - 02-Aug-2018:
    // Check if the competency is already COMPLETED
    // If current status of the competency is INPROGRESS and its not already
    // COMPLETED then Insert new or update the score
    // If current status of the competency is COMPLETED then straight
    // forward INSERT, In case of conflict update score
    boolean isCompleted = this.service.checkIfCompetencyIsCompleted(bean);
    LOGGER.debug("competency '{}' || isCompletedOrMastered:{}", competencyCode, isCompleted);
    if (status == StatusConstants.IN_PROGRESS) {
      if (!isCompleted) {
        this.service.insertOrUpdateContentCompetencyEvidenceTS(bean);
      }
    } else {
      this.service.insertOrUpdateContentCompetencyEvidenceTS(bean);
    }
  }
}
