package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.sql.Timestamp;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


/**
 * @author mukul@gooru
 */
public abstract class CompetencyStatsDao {

  @SqlUpdate("INSERT INTO user_class_competency_stats(user_id, class_id, course_id, grade_id, class_grade_id, total, "
      + "class_total, completed, in_progress, score, subject_code, percent_completed, stats_date, month, year) "
      + "VALUES (:userId, :classId, :courseId, :gradeId, :classGradeId, :totalCompetencies, :classCompetencies, :completedCompetencies, "
      + ":inprogressCompetencies, :percentScore, :subjectCode, :percentCompletion, :statsDate, :month, :year) ON CONFLICT  "
      + "(user_id, class_id, course_id, month, year) DO UPDATE SET grade_id = :gradeId, class_grade_id = :classGradeId, total =:totalCompetencies, "
      + " class_total = :classCompetencies, completed = :completedCompetencies, in_progress = :inprogressCompetencies, score = :percentScore, "
      + " percent_completed = :percentCompletion, updated_at = :currentTime")
  protected abstract void insertUserClassCompetencyStats(
      @BindBean CompetencyStatsModel gradeCompetencyStatsModel,
      @Bind("currentTime") Timestamp currentTime);

}
