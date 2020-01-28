package org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author mukul@gooru
 */
public interface ClassDao {

  @SqlQuery("select course_id from class where id = :classId")
  String fetchCoursefromClass(@Bind("classId") UUID classId);

  @SqlQuery("select grade_upper_bound from class_member where user_id = :userId and class_id = :classId")
  Integer fetcheGradefromClassMembers(@Bind("userId") UUID userId, @Bind("classId") UUID classId);

  @SqlQuery("select grade_upper_bound from class where id = :classId and course_id = :courseId")
  Integer fetcheGradefromClass(@Bind("classId") UUID classId, @Bind("courseId") UUID courseId);

  @SqlQuery("select grade_current from class where id = :classId and course_id = :courseId")
  Integer fetchClassCurrentGrade(@Bind("classId") UUID classId, @Bind("courseId") UUID courseId);

  @SqlQuery("select user_id from class_member where class_id = :classId and class_member_status = 'joined'")
  List<String> fetchClassMembers(@Bind("classId") UUID classId);

}
