
package org.gooru.dap.jobs.group.reports.competency;

import java.util.List;
import java.util.Set;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 30-Apr-2019
 */
public class GroupCompetencyReportsService {

  private final GroupCompetencyReportsDao dao;

  public GroupCompetencyReportsService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportsDao.class);
  }

  public List<ClassCompetencyStatsModel> fetchClassCompletionsByMonthAndYear(List<String> classIds,
      Integer month, Integer year) {
    return this.dao.fetchClassCompletionsByMonthAndYear(
        CollectionUtils.convertToSqlArrayOfString(classIds), month, year);
  }

  public List<GroupCompetencyStatsModel> fetchCompetencyCompletionsBySchool(Set<Long> schoolIds,
      Integer month, Integer year) {
    return this.dao.fetchCompetencyCompletionsBySchool(
        CollectionUtils.longSetToPGArrayOfString(schoolIds), month, year);
  }

  public List<GroupCompetencyStatsModel> fetchCompetencyCompletionsByGroup(Set<Long> groupIds,
      Integer month, Integer year) {
    return this.dao.fetchCompetencyCompletionsByGroup(
        CollectionUtils.longSetToPGArrayOfString(groupIds), month, year);
  }

  public void insertOrUpdateClassCompetencyDataReport(ClassCompetencyDataReportsBean bean) {
    this.dao.insertOrUpdateClassCompetencyDataReport(bean);
  }

  public void insertOrUpdateGroupCompetencyDataReport(GroupCompetencyDataReportsBean bean) {
    this.dao.insertOrUpdateGroupCompetencyDataReport(bean);
  }
}
