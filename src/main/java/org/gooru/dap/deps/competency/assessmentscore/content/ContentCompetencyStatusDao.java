package org.gooru.dap.deps.competency.assessmentscore.content;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 14-May-2018
 */
public abstract class ContentCompetencyStatusDao {

  @SqlUpdate("INSERT INTO content_competency_status(user_id, competency_code, framework_code, status, created_at, updated_at) VALUES(:userId,"
      + " :competencyCode, :frameworkCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, competency_code) DO UPDATE SET status ="
      + " :status, updated_at = :updatedAt WHERE content_competency_status.status <> 4")
  protected abstract void insertOrUpdateContentComptencyStatus(
      @BindBean ContentCompetencyStatusBean bean);

  @SqlUpdate("INSERT INTO content_competency_status_ts(user_id, competency_code, framework_code, status, created_at, updated_at) VALUES(:userId,"
      + " :competencyCode, :frameworkCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, competency_code, status) DO NOTHING")
  protected abstract void insertOrUpdateContentComptencyStatusTS(
      @BindBean ContentCompetencyStatusBean bean);
}
