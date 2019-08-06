
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionPerformanceService {

  private final CollectionPerformanceDao dao;

  public CollectionPerformanceService(DBI dbi) {
    this.dao = dbi.onDemand(CollectionPerformanceDao.class);
  }

  public List<CollectionTimespentModel> fetchTimespentByClass(List<String> classIds,
      String contentSource) {
    return this.dao.fetchTimespentByClass(CollectionUtils.convertToSqlArrayOfString(classIds),
        contentSource);
  }
}
