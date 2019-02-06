package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.skife.jdbi.v2.DBI;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceService {

  private final LearnerProfileCompetencyEvidenceDao dao;

  public LearnerProfileCompetencyEvidenceService(DBI dbi) {
    this.dao = dbi.onDemand(LearnerProfileCompetencyEvidenceDao.class);
  }

  public void insertOrUpdateLearnerProfileCompetencyEvidence(
      LearnerProfileCompetencyEvidenceBean bean) {
    this.dao.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
  }

  public void insertOrUpdateLearnerProfileMicroCompetencyEvidence(
      LearnerProfileMicroCompetencyEvidenceBean bean) {
    this.dao.insertOrUpdateLearnerProfileMicroCompetencyEvidence(bean);
  }

  public void insertOrUpdateLearnerProfileCompetencyEvidenceTS(
      LearnerProfileCompetencyEvidenceBean bean) {
    this.dao.insertOrUpdateLearnerProfileCompetencyEvidenceTS(bean);
  }

  public void insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(
      LearnerProfileMicroCompetencyEvidenceBean bean) {
    this.dao.insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(bean);
  }

  public Double getScoreForCompetency(LearnerProfileCompetencyEvidenceBean bean) {
    return this.dao.getScoreForCompetency(bean);
  }

  public Double getScoreForMicroCompetency(LearnerProfileMicroCompetencyEvidenceBean bean) {
    return this.dao.getScoreForMicroCompetency(bean);
  }

  public boolean checkIfCompetencyIsAlreadyCompletedOrMastered(
      LearnerProfileCompetencyEvidenceBean bean) {
    return this.dao.checkIfCompetencyIsCompletedOrMastered(bean);
  }

  public boolean checkIfMicroCompetencyIsAlreadyCompletedOrMastered(
      LearnerProfileMicroCompetencyEvidenceBean bean) {
    return this.dao.checkIfMicroCompetencyIsCompletedOrMastered(bean);
  }
}
