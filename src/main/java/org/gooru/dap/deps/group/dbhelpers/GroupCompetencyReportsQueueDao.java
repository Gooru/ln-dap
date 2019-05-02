
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public interface GroupCompetencyReportsQueueDao {

  @SqlUpdate("INSERT INTO competency_data_reports_queue(class_id, tenant, status) VALUES (:classId, :tenant, 'pending'")
  void insertIntoQueue(@Bind("classId") String classId, @Bind("tenant") String tenant);

  @SqlQuery("SELECT class_id FORM competency_data_reports_queue WHERE status = 'pending' ORDER BY updated_at ASC LIMIT :limit OFFSET :offset")
  List<String> fetchClassFromQueueForProcessing(@Bind("limit") Integer limit,
      @Bind("offset") Integer offset);

  @SqlUpdate("UPDATE competency_data_reports_queue SET status = 'completed' WHERE class_id = :classId")
  void updateQueueStatusToCompleted(@Bind("classId") String classId);

  @SqlUpdate("DELETE FROM competency_data_reports_queue WHERE status = 'completed'")
  void deleteFromQueue();
}
