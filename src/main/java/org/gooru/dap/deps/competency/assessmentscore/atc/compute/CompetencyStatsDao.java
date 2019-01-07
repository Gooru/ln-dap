package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.sql.Timestamp;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


/**
 * @author mukul@gooru
 */
public abstract class CompetencyStatsDao {

  @SqlUpdate("INSERT INTO user_class_competency_stats(user_id, class_id, course_id, grade_id, total, "
      + "completed, in_progress, score, subject_code, percent_completed, month, year) "
      + "VALUES (:userId, :classId, :courseId, :gradeId, :totalCompetencies, :completedCompetencies, "
      + ":inprogressCompetencies, :percentScore, :subjectCode, :percentCompletion, :month, :year) ON CONFLICT  "
      + "(user_id, class_id, course_id, month, year) DO UPDATE SET grade_id = :gradeId, total =:totalCompetencies, "
      + " completed = :completedCompetencies, in_progress = :inprogressCompetencies, score = :percentScore, "
      + " percent_completed = :percentCompletion, updated_at = :currentTime")
  protected abstract void insertUserClassCompetencyStats(
      @BindBean CompetencyStatsModel gradeCompetencyStatsModel,
      @Bind("currentTime") Timestamp currentTime);

}
