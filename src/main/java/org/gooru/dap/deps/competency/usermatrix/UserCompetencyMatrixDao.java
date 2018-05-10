package org.gooru.dap.deps.competency.usermatrix;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 04-May-2018
 */
public abstract class UserCompetencyMatrixDao {

	// Use GUT codes to store

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

	@SqlUpdate("INSERT INTO user_competency_matrix(tx_subject_code, user_id, gut_code, status, created_at, updated_at) VALUES(:txSubjectCode,"
			+ " :userId, :gutCode, :status, :createdAt, :updatedAt) ON CONFLICT (user_id, gut_code) DO UPDATE SET status = :status,"
			+ " updated_at = :updatedAt WHERE user_competency_matrix.status <> 5")
	protected abstract void updateUserCompetencyMatrix(@BindBean UserCompetencyMatrixBean bean);
}
