
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class GroupTimespentReportsService {

  private final GroupTimespentReportsDao dao;

  public GroupTimespentReportsService(DBI dbi) {
    this.dao = dbi.onDemand(GroupTimespentReportsDao.class);
  }

  public List<CollectionTimespentByGroupModel> fetchCollectionTimespentBySchool(String schoolIds) {
    return this.dao.fetchCollectionTimespentBySchool(schoolIds);
  }

  public List<CollectionTimespentByGroupModel> fetchGroupLevelCollectionTimespent(String groupIds) {
    return this.dao.fetchGroupLevelCollectionTimespent(groupIds);
  }

  public void insertOrUpdateClassLevelCollectionTimespent(ClassTimespentDataReportBean bean) {
    this.dao.insertOrUpdateClassLevelCollectionTimespent(bean);
  }

  public void insertOrUpdateGroupLevelCollectionTimspent(GroupTimespentDataReportBean bean) {
    this.dao.insertOrUpdateGroupLevelCollectionTimspent(bean);
  }
}
