package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import java.util.regex.Pattern;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.Constants;
import org.gooru.dap.constants.StatusConstants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ContextMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceProcessor {

  // TODO: Move this to the config
  
  private final static Pattern HYPHEN_PATTERN = Pattern.compile("-");

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScore;
  private final String gutCode;
  private final boolean isSignature;

  private LearnerProfileCompetencyEvidenceService service =
      new LearnerProfileCompetencyEvidenceService(DBICreator.getDbiForDefaultDS());
  private TenantSettingService tenantSettingService = new TenantSettingService(DBICreator.getDbiForCoreDS());
  
  public LearnerProfileCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore,
      String gutCode, boolean isSignature) {
    this.assessmentScore = assessmentScore;
    this.gutCode = gutCode;
    this.isSignature = isSignature;
  }
  
  private double getCompletionScoreThreshold() {
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

    // If the score is greater than the mastry/completion score then only
    // persist
    // the evidence. Below mastry/completion score we are not treating the
    // competency as mastered or completed hence no need to persist the
    // evidence.

    // Update: 12-July-2018: Requirement is to persist the evidence for in
    // progress
    // status in non TS tables

    boolean isMicroCompetency = (HYPHEN_PATTERN.split(gutCode).length >= 5);
    LearnerProfileCompetencyEvidenceCommand command =
        LearnerProfileCompetencyEvidenceCommandBuilder.build(this.assessmentScore);

    // Calculate the status to persist in evidence ts table to uniquely
    // identify the
    // evidence by status
    int status = StatusConstants.IN_PROGRESS;
    if(score != null) {
      if (isSignature && score >= Constants.MASTERY_SCORE) {
        status = StatusConstants.MASTERED;
      } else if (score >= getCompletionScoreThreshold()) {
        status = StatusConstants.COMPLETED;
      }
    }

    // Regardless of score always persist the evidence. This is basically
    // NOT to overwrite the evidence in TS table when competency is
    // transitioned to
    // completed/mastered from in progress. Which also enables to persist
    // evidence
    // for in progress status. However in NON TS table evidence will be
    // overwritten
    // with latest score if its same content, else will be inserted
    LOGGER.debug("LP Evidence: Competency:{} || status:{}", gutCode, status);
    if (isMicroCompetency) {
      processMicroCompetency(command, status);
      processMicroCompetencyForTS(command, status);
    } else {
      processCompetency(command, status);
      processCompetencyForTS(command, status);
    }
  }

  private void processCompetency(LearnerProfileCompetencyEvidenceCommand command, int status) {
    LearnerProfileCompetencyEvidenceBean bean = new LearnerProfileCompetencyEvidenceBean(command);
    bean.setGutCode(gutCode);

    // Get the existing score for user, gut and assessment. IF there is no
    // score
    // already exists, then persist the evidence. If score exists and less
    // than 80
    // (in-progress) then update the evidence. If score exists and greater
    // than 80
    // then only update evidence if status is COMPLETED or MASTERED. Here,
    // we do not
    // want to down grade the score if its already COMPLETED or MASTERED.
    // However,
    // we need to persist latest evidence for them.
    Double score = this.service.getScoreForCompetency(bean);
    LOGGER.debug("existing score for the competency '{}' is '{}'", gutCode, score);
    if (score == null || score < Constants.MASTERY_SCORE) {
      this.service.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
    } else {
      if (status != StatusConstants.IN_PROGRESS) {
        this.service.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
      }
    }
  }

  private void processMicroCompetency(LearnerProfileCompetencyEvidenceCommand command, int status) {
    LearnerProfileMicroCompetencyEvidenceBean microCompetencyBean =
        new LearnerProfileMicroCompetencyEvidenceBean(command);
    microCompetencyBean.setMicroCompetencyCode(gutCode);

    // Refer to comment in processCompetency() for logic
    Double score = this.service.getScoreForMicroCompetency(microCompetencyBean);
    LOGGER.debug("existing score for the micro competency '{}' is '{}'", gutCode, score);
    if (score == null || score < Constants.MASTERY_SCORE) {
      this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidence(microCompetencyBean);
    } else {
      if (status != StatusConstants.IN_PROGRESS) {
        this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidence(microCompetencyBean);
      }
    }
  }

  /*
   * For TS tables even if we are persisting multiple evidence for the same competency, however, if
   * the competency is already COMPLETED or MASTERED then there is no point in persisting evidence
   * for INPROGRESS again. In this case we should avoid persisting the evidence.
   */
  private void processCompetencyForTS(LearnerProfileCompetencyEvidenceCommand command, int status) {
    LearnerProfileCompetencyEvidenceBean bean = new LearnerProfileCompetencyEvidenceBean(command);
    bean.setGutCode(gutCode);
    bean.setStatus(status);

    // Check if the competency is already COMPLETED or MASTERED
    // If current status of the competency is INPROGRESS and its not already
    // COMPLETED or MASTERED then Insert new or update the score
    // If current status of the competency is COMPLETED / MASTERED then
    // straight
    // forward INSERT, In case of conflict update score
    boolean isCompletedOrMastered =
        this.service.checkIfCompetencyIsAlreadyCompletedOrMastered(bean);
    LOGGER.debug("competency '{}' ||  isCompletedOrMastered:{}", gutCode, isCompletedOrMastered);
    if (status == StatusConstants.IN_PROGRESS) {
      if (!isCompletedOrMastered) {
        this.service.insertOrUpdateLearnerProfileCompetencyEvidenceTS(bean);
      }
    } else {
      this.service.insertOrUpdateLearnerProfileCompetencyEvidenceTS(bean);
    }

  }

  private void processMicroCompetencyForTS(LearnerProfileCompetencyEvidenceCommand command,
      int status) {
    LOGGER.debug("persisting micro competency evidence for TS");
    LearnerProfileMicroCompetencyEvidenceBean microCompetencyBean =
        new LearnerProfileMicroCompetencyEvidenceBean(command);
    microCompetencyBean.setMicroCompetencyCode(gutCode);
    microCompetencyBean.setStatus(status);

    // Check if the micro competency is already COMPLETED or MASTERED
    // If current status of the micro competency is INPROGRESS and its not
    // already
    // COMPLETED or MASTERED then Insert new or update the score
    // If current status of the micro competency is COMPLETED / MASTERED
    // then
    // straight forward INSERT, In case of conflict update score
    boolean isCompletedOrMastered =
        this.service.checkIfMicroCompetencyIsAlreadyCompletedOrMastered(microCompetencyBean);
    LOGGER.debug("micro competency '{}' ||  isCompletedOrMastered:{}", gutCode,
        isCompletedOrMastered);
    if (status == StatusConstants.IN_PROGRESS) {
      if (!isCompletedOrMastered) {
        this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(microCompetencyBean);
      }
    } else {
      this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(microCompetencyBean);
    }
  }

}
