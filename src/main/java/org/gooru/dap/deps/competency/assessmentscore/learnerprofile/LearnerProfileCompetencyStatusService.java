package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.gooru.dap.constants.StatusConstants;
import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusService {

  private final LearnerProfileCompetencyStatusDao dao;

  public LearnerProfileCompetencyStatusService(DBI dbi) {
    this.dao = dbi.onDemand(LearnerProfileCompetencyStatusDao.class);
  }

  public void updateLearnerProfileCompetencyStatusToNotStarted(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.NOT_STARTED);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  public void updateLearnerProfileCompetencyStatusToInprogress(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.IN_PROGRESS);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  public void updateLearnerProfileCompetencyStatusToCompleted(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.COMPLETED);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  public void updateLearnerProfileCompetencyStatusToAsserted(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.ASSERTED);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  public void updateLearnerProfileCompetencyStatusToInferred(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.INFERRED);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  public void updateLearnerProfileCompetencyStatusToMastered(
      LearnerProfileCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.MASTERED);
    insertOrUpdateLearnerProfileCompetencyStatus(bean);
  }

  private void insertOrUpdateLearnerProfileCompetencyStatus(
      LearnerProfileCompetencyStatusBean bean) {
    dao.insertOrUpdateLearnerProfileCompetencyStatus(bean);
    dao.insertOrUpdateLearnerProfileCompetencyStatusTS(bean);
  }
}
