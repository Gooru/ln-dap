package org.gooru.dap.deps.competency.status;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 04-May-2018
 */
public abstract class CompetencyStatusDao {

	/*
	 * competency status - "in_progress" and "completed" If user_id scores >80% in
	 * ANY assessment associated with a competency_code then the status for that
	 * competency for that user will be "complete" else it will be "in-progress"
	 */

	@SqlUpdate("INSERT INTO competency_status(user_id, competency_code, framework_code, status, created_at, updated_at) VALUES(:userId,"
			+ " :competencyCode, :frameworkCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, competency_code) DO UPDATE SET"
			+ " status = :status, updated_at = :updatedAt WHERE competency_status.status <> 'completed'")
	protected abstract void insertOrUpdateCompetencyStatus(@BindBean CompetencyStatusBean bean);
}
