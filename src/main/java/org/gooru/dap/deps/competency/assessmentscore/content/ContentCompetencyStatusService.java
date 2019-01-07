package org.gooru.dap.deps.competency.assessmentscore.content;

import org.gooru.dap.constants.StatusConstants;
import org.skife.jdbi.v2.DBI;

public class ContentCompetencyStatusService {

  private final ContentCompetencyStatusDao dao;

  public ContentCompetencyStatusService(DBI dbi) {
    this.dao = dbi.onDemand(ContentCompetencyStatusDao.class);
  }

  public void insertContentCompetencyStatusToInProgress(ContentCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.IN_PROGRESS);
    insertOrUpdateContentComptencyStatus(bean);
  }

  public void insertOrUpdateContentCompetencyStatusToInferred(ContentCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.INFERRED);
    insertOrUpdateContentComptencyStatus(bean);
  }

  public void insertOrUpdateContentCompetencyStatusToCompleted(ContentCompetencyStatusBean bean) {
    bean.setStatus(StatusConstants.COMPLETED);
    insertOrUpdateContentComptencyStatus(bean);
  }

  private void insertOrUpdateContentComptencyStatus(ContentCompetencyStatusBean bean) {
    this.dao.insertOrUpdateContentComptencyStatus(bean);
    this.dao.insertOrUpdateContentComptencyStatusTS(bean);
  }
}
