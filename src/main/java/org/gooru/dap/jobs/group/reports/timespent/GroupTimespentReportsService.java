
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import java.util.Set;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class GroupTimespentReportsService {

  private final GroupTimespentReportsDao dao;

  public GroupTimespentReportsService(DBI dbi) {
    this.dao = dbi.onDemand(GroupTimespentReportsDao.class);
  }

  public List<CollectionTimespentByGroupModel> fetchCollectionTimespentBySchool(Set<Long> schoolIds,
      Integer month, Integer year) {
    return this.dao.fetchCollectionTimespentBySchool(
        CollectionUtils.longSetToPGArrayOfString(schoolIds), month, year);
  }

  public List<CollectionTimespentByGroupModel> fetchGroupLevelCollectionTimespent(
      Set<Long> groupIds, Integer month, Integer year) {
    return this.dao.fetchGroupLevelCollectionTimespent(
        CollectionUtils.longSetToPGArrayOfString(groupIds), month, year);
  }

  public void insertOrUpdateClassLevelCollectionTimespent(ClassTimespentDataReportBean bean) {
    this.dao.insertOrUpdateClassLevelCollectionTimespent(bean);
  }

  public void insertOrUpdateGroupLevelCollectionTimspent(GroupTimespentDataReportBean bean) {
    this.dao.insertOrUpdateGroupLevelCollectionTimspent(bean);
  }
}
