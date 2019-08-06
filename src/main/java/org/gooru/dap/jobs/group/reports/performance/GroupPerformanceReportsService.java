
package org.gooru.dap.jobs.group.reports.performance;

import java.util.List;
import java.util.Set;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public class GroupPerformanceReportsService {

  private final GroupPerformanceReportsDao dao;

  public GroupPerformanceReportsService(DBI dsdbi) {
    this.dao = dsdbi.onDemand(GroupPerformanceReportsDao.class);
  }

  public void insertOrUpdateClassLevelAssessmentPerfAndTimespent(
      ClassPerformanceDataReportsBean bean) {
    this.dao.insertOrUpdateClassLevelAssessmentPerfAndTimespent(bean);
  }

  public void insertOrUpdateGroupLevelAssessmentPerf(GroupPerformanceDataReportBean bean) {
    this.dao.insertOrUpdateGroupLevelAssessmentPerformance(bean);
  }

  public List<AssessmentPerfByGroupModel> fetchAssessmentPerfBySchool(Set<Long> schoolIds,
      Integer month, Integer year) {
    return this.dao.fetchAssessmentPerfBySchool(CollectionUtils.longSetToPGArrayOfString(schoolIds),
        month, year);
  }

  public List<AssessmentPerfByGroupModel> fetchAssessmentPerfByGroup(Set<Long> groupIds,
      Integer month, Integer year) {
    return this.dao.fetchGroupLevelAssessmentPerf(
        CollectionUtils.longSetToPGArrayOfString(groupIds), month, year);
  }
}
