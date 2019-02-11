package org.gooru.dap.deps.competency.assessmentscore.atc.helper;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface SubjectFetcherFromGradeDao {

  @SqlQuery("select tx_subject_code from grade_master where id = :gradeId")
  String fetchSubjectCodeFromGrade(@Bind("gradeId") Integer gradeId);

}
