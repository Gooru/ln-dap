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
	 * competency for that user will be "complete" else it will be "in-progress" GUT
	 * Codes will be stored in the competency_code (updated req)
	 */

	@SqlUpdate("INSERT INTO competency_status(user_id, competency_code, competency_display_code, framework_code, status) VALUES(:userId,"
			+ " :competencyCode, :competencyDisplayCode, :frameworkCode, :status)")
	protected abstract void insertCompetencyStatus(@BindBean CompetencyStatusCommand.CompetencyStatusBean bean);
}
