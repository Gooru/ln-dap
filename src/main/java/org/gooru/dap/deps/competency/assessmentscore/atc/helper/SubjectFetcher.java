package org.gooru.dap.deps.competency.assessmentscore.atc.helper;

import java.util.UUID;
import org.gooru.dap.components.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */
public interface SubjectFetcher {

  String fetchSubjectFromCourse(UUID courseId);
  
  String fetchSubjectFromGrade(Integer gradeId);

  static SubjectFetcher build() {
    return new SubjectFetcherImpl(DBICreator.getDbiForCoreDS(), DBICreator.getDbiForDefaultDS());
  }

}
