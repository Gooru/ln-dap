package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


/**
 * @author mukul@gooru
 */
public abstract class GradeCompetencyStatsDao {
	
	@SqlUpdate("INSERT INTO user_class_competency_stats(user_id, class_id, course_id, grade_id, total, completed, in_progress, score) "
			+ "VALUES (:userId, :classId, :courseId, :gradeId, :totalCompetencies, :completedCompetencies, :inprogressCompetencies, :percentScore)")
	protected abstract void insertUserClassCompetencyStats(@BindBean GradeCompetencyStatsModel gradeCompetencyStatsModel);
	
}
