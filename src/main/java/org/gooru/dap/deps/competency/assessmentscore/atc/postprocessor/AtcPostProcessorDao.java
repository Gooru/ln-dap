package org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;


/**
 * @author mukul@gooru
 */
interface AtcPostProcessorDao {

  @SqlQuery("select distinct(user_id) from user_class_competency_stats where class_id = :classId and course_id = :courseId")
  List<String> fetchActiveClassMembers(@Bind("classId") String classId,
      @Bind("courseId") String courseId);

}
