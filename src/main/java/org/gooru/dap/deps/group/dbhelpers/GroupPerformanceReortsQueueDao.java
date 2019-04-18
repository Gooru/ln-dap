
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.gooru.dap.deps.group.processors.ContextObject;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public interface GroupPerformanceReortsQueueDao {

  @SqlUpdate("INSERT INTO performance_data_reports_queue (class_id, course_id, content_source, tenant, status) VALUES (:classId, :courseId,"
      + " :contentSource, :tenantId, 'pending') ON CONFLICT (class_id, content_source) DO UPDATE SET course_id = :courseId")
  void insertIntoQueue(@BindBean ContextObject context);

  @Mapper(GroupReortsAggregationQueueModelMapper.class)
  @SqlQuery("SELECT class_id, course_id, content_source, tenant, status FROM performance_data_reports_queue ORDER BY updated_at asc limit :limit"
      + " offset :offset")
  List<GroupReortsAggregationQueueModel> fetchClassesForProcessing(@Bind("limit") Integer limit,
      @Bind("offset") Integer offset);

  @SqlUpdate("DELETE FROM performance_data_reports_queue WHERE status = 'completed'")
  void deleteCompletedFromQueue();

  @SqlUpdate("UPDATE performance_data_reports_queue SET status = 'completed' WHERE class_id = :classId AND content_source = :contentSource")
  void updateQueueStatusToCompleted(@Bind("classId") String classId, @Bind("contentSource") String contentSource);
}
