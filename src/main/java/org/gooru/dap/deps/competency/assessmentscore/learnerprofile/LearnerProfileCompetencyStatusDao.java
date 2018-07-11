package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 14-May-2018
 */
public abstract class LearnerProfileCompetencyStatusDao {

	/*
	 * Status values ranges from 0 to 5.
	 * 
	 * 0 = Not started
	 * 
	 * 1 = in_progress (If there is any collection/assessment that are started and
	 * NO SINGLE assessment for that competency is completed as yet)
	 * 
	 * 2 = inferred (This is based on the progression data. A competency is inferred
	 * (as mastered) if the competency above it is Mastered by the user)
	 * 
	 * 3 = NA cannot be derived for now.
	 * 
	 * 4 = completed (If there is any collection/assessment that are started and ANY
	 * SINGLE assessment(s) for that competency is completed (i.e score is
	 * available, received collection.play (.stop) event)
	 * 
	 * 5 = mastered (If there is any collection/assessment that are started and ANY
	 * SINGLE assessment(s) for that competency is completed and the SCORE is >80%)
	 */

	/*
	 * Uniqueness is based on user and gut code. Compentecy status may transition
	 * from 0/1 to 4/5. In short for one competency and user there will always be
	 * one status present in this table.
	 */
	@SqlUpdate("INSERT INTO learner_profile_competency_status(tx_subject_code, user_id, gut_code, status, created_at, updated_at) "
			+ "VALUES(:txSubjectCode, :userId, :gutCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, gut_code) "
			+ "DO UPDATE SET status = :status, updated_at = :updatedAt WHERE learner_profile_competency_status.status <= EXCLUDED.status")
	protected abstract void insertOrUpdateLearnerProfileCompetencyStatus(
			@BindBean LearnerProfileCompetencyStatusBean bean);
	
	
	/*
	 * We do not need to update anything in TS tables if the conflict found based on
	 * user, gut code and status. We always serve cumulative data up to month/week.
	 */
	@SqlUpdate("INSERT INTO learner_profile_competency_status_ts(tx_subject_code, user_id, gut_code, status, created_at, updated_at) "
			+ "VALUES(:txSubjectCode, :userId, :gutCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, gut_code, status) DO NOTHING")
	protected abstract void insertOrUpdateLearnerProfileCompetencyStatusTS(
			@BindBean LearnerProfileCompetencyStatusBean bean);
}
