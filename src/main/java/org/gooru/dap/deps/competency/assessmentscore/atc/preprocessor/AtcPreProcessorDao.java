package org.gooru.dap.deps.competency.assessmentscore.atc.preprocessor;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;


/**
 * @author mukul@gooru
 */
interface AtcPreProcessorDao {

  @SqlQuery("select version from course where id IN "
      + "(select course_id from class where id = :classId and is_deleted = false and is_archived = false)")
  String isClassPremium(@Bind("classId") UUID classId);

  @SqlQuery("select course_id from class where id = :classId")
  String fetchCoursefromClass(@Bind("classId") UUID classId);

}
