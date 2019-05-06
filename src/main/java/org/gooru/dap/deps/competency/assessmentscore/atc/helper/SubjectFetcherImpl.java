package org.gooru.dap.deps.competency.assessmentscore.atc.helper;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */
class SubjectFetcherImpl implements SubjectFetcher {

  private final DBI coredbi;
  private final DBI dsdbi;
  private SubjectFetcherFromCourseDao courseDao;
  private SubjectFetcherFromGradeDao gradeDao;

  SubjectFetcherImpl(DBI coredbi, DBI dsdbi) {
    this.coredbi = coredbi;
    this.dsdbi = dsdbi;
  }

  @Override
  public String fetchSubjectFromCourse(UUID courseId) {
    if (courseId != null) {
      String subjectCode = getSubjectFetcherFromCourseDao().fetchSubjectBucketForCourse(courseId);
      if (subjectCode != null) {
        if (isFrameworkCode(subjectCode)) {
          subjectCode = getSubjectFetcherFromCourseDao()
              .fetchGutSubjectCodeForFrameworkSubjectCode(subjectCode);
        }
        return subjectCode;
      }
    }
    return null;
  }

  public String fetchSubjectFromGrade(Integer gradeId) {
    if (gradeId != null && gradeId > 0) {
      String subjectCode = getSubjectFetcherFromGradeDao().fetchSubjectCodeFromGrade(gradeId);
      return subjectCode;
    }

    return null;
  }

  private boolean isFrameworkCode(String subjectCode) {
    long countDots = subjectCode.chars().filter(ch -> ch == '.').count();
    return countDots > 1;
  }

  private SubjectFetcherFromCourseDao getSubjectFetcherFromCourseDao() {
    if (courseDao == null) {
      courseDao = coredbi.onDemand(SubjectFetcherFromCourseDao.class);
    }
    return courseDao;
  }

  private SubjectFetcherFromGradeDao getSubjectFetcherFromGradeDao() {
    if (gradeDao == null) {
      gradeDao = dsdbi.onDemand(SubjectFetcherFromGradeDao.class);
    }
    return gradeDao;
  }
}
