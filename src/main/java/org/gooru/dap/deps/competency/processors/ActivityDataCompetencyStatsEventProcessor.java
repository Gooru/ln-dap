package org.gooru.dap.deps.competency.processors;

import java.util.HashMap;
import java.util.Map;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.StatusConstants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceBean;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceCommand;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceCommandBuilder;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceService;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusBean;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusCommand;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusCommandBuilder;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusService;
import org.gooru.dap.deps.competency.events.mapper.ActivityDataCompetencyEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityDataCompetencyStatsEventProcessor implements EventProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final ActivityDataCompetencyEventMapper activityDataEvent;
  private LearnerProfileCompetencyStatusService competencyStatusService =
      new LearnerProfileCompetencyStatusService(DBICreator.getDbiForDefaultDS());
  private LearnerProfileCompetencyEvidenceService competencyEvidenceService =
      new LearnerProfileCompetencyEvidenceService(DBICreator.getDbiForDefaultDS());

  public ActivityDataCompetencyStatsEventProcessor(ActivityDataCompetencyEventMapper activityDataEvent) {
    this.activityDataEvent = activityDataEvent;
  }

  @Override
  public void process() {
    LOGGER.debug("Activity Data Learners Competency Event :: {}", activityDataEvent.toString());
    updateLearnerCompetencyStatus();

  }

  private void updateLearnerCompetencyStatus() {
    LearnerProfileCompetencyStatusCommand competencyStatusCommand =
        LearnerProfileCompetencyStatusCommandBuilder.build(activityDataEvent);
    LearnerProfileCompetencyEvidenceCommand competencyEvidenceCommand =
        LearnerProfileCompetencyEvidenceCommandBuilder.build(activityDataEvent);

    LearnerProfileCompetencyStatusBean competencyStatusBean =
        new LearnerProfileCompetencyStatusBean(competencyStatusCommand);
    LearnerProfileCompetencyEvidenceBean competencyEvidenceBean =
        new LearnerProfileCompetencyEvidenceBean(competencyEvidenceCommand);
    competencyEvidenceBean.setGutCode(competencyEvidenceCommand.getGutCode());
    competencyEvidenceBean.setStatus(activityDataEvent.getResult().getStatus());

    UpdateLearnerProfileCompetencyStatusBuilder
        .lookupBuilder(activityDataEvent.getResult().getStatus())
        .build(competencyStatusService, competencyStatusBean);

    competencyEvidenceService
        .insertOrUpdateLearnerProfileCompetencyEvidence(competencyEvidenceBean);
    competencyEvidenceService
        .insertOrUpdateLearnerProfileCompetencyEvidenceTS(competencyEvidenceBean);


  }

  private enum UpdateLearnerProfileCompetencyStatusBuilder {


    DEFAULT(-1) {
      private final Logger LOGGER =
          LoggerFactory.getLogger(UpdateLearnerProfileCompetencyStatusBuilder.class);

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        LOGGER.error("Invalid competency type passed in, not able to handle");
      }

    },
    NOT_STARTED(StatusConstants.NOT_STARTED) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToNotStarted(bean);
      }
    },
    IN_PROGRESS(StatusConstants.IN_PROGRESS) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToInprogress(bean);
      }
    },
    INFERRED(StatusConstants.INFERRED) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToInferred(bean);
      }
    },

    ASSERTED(StatusConstants.ASSERTED) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToAsserted(bean);
      }
    },
    COMPLETED(StatusConstants.COMPLETED) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToCompleted(bean);
      }
    },
    MASTERED(StatusConstants.MASTERED) {

      @Override
      public void build(LearnerProfileCompetencyStatusService service,
          LearnerProfileCompetencyStatusBean bean) {
        service.updateLearnerProfileCompetencyStatusToMastered(bean);
      }
    };

    private Integer status;

    UpdateLearnerProfileCompetencyStatusBuilder(Integer status) {
      this.status = status;
    }

    public Integer getStatus() {
      return this.status;
    }

    private static final Map<Integer, UpdateLearnerProfileCompetencyStatusBuilder> LOOKUP =
        new HashMap<>();

    static {
      for (UpdateLearnerProfileCompetencyStatusBuilder builder : values()) {
        LOOKUP.put(builder.getStatus(), builder);
      }
    }


    public static UpdateLearnerProfileCompetencyStatusBuilder lookupBuilder(Integer status) {
      UpdateLearnerProfileCompetencyStatusBuilder builder = LOOKUP.get(status);
      if (builder == null) {
        return DEFAULT;
      }
      return builder;
    }

    public abstract void build(LearnerProfileCompetencyStatusService service,
        LearnerProfileCompetencyStatusBean bean);
  }
}
