
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionPerformanceService {

  private final CollectionPerformanceDao dao;

  public CollectionPerformanceService(DBI dbi) {
    this.dao = dbi.onDemand(CollectionPerformanceDao.class);
  }

  public List<CollectionPerformanceModel> fetchTimespentByClass(PGArray<String> classIds,
      String contentSource) {
    return this.dao.fetchTimespentByClass(classIds, contentSource);
  }
}
