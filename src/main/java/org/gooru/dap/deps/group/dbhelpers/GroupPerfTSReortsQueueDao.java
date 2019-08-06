
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
public interface GroupPerfTSReortsQueueDao {

  // For Performance processing

  @SqlUpdate("INSERT INTO perf_ts_data_reports_queue (class_id, course_id, kpi, content_source, tenant, status) VALUES (:classId, :courseId,"
      + " 'performance', :contentSource, :tenantId, 'pending') ON CONFLICT (class_id, kpi, content_source) DO UPDATE SET course_id = :courseId")
  void insertIntoPerfQueue(@BindBean ContextObject context);

  @Mapper(GroupPerfTSReortsQueueModelMapper.class)
  @SqlQuery("SELECT class_id, course_id, content_source, tenant, status FROM perf_ts_data_reports_queue WHERE kpi = 'performance' AND status ="
      + " 'pending' ORDER BY updated_at asc limit :limit offset :offset")
  List<GroupPerfTSReortsQueueModel> fetchClassesForPerfProcessing(@Bind("limit") Integer limit,
      @Bind("offset") Integer offset);

  @SqlUpdate("DELETE FROM perf_ts_data_reports_queue WHERE status = 'completed' AND kpi = 'performance'")
  void deleteCompletedFromPerfQueue();

  @SqlUpdate("UPDATE perf_ts_data_reports_queue SET status = 'completed' WHERE class_id = :classId AND content_source = :contentSource AND"
      + " kpi = 'performance'")
  void updatePerfQueueStatusToCompleted(@Bind("classId") String classId,
      @Bind("contentSource") String contentSource);

  // For Timespent processing

  @SqlUpdate("INSERT INTO perf_ts_data_reports_queue (class_id, course_id, kpi, content_source, tenant, status) VALUES (:classId, :courseId,"
      + " 'timespent', :contentSource, :tenantId, 'pending') ON CONFLICT (class_id, kpi, content_source) DO UPDATE SET course_id = :courseId")
  void insertIntoTimespentQueue(@BindBean ContextObject context);

  @Mapper(GroupPerfTSReortsQueueModelMapper.class)
  @SqlQuery("SELECT class_id, course_id, content_source, tenant, status FROM perf_ts_data_reports_queue WHERE kpi = 'timespent' AND status ="
      + " 'pending' ORDER BY updated_at asc limit :limit offset :offset")
  List<GroupPerfTSReortsQueueModel> fetchClassesForTimespentProcessing(@Bind("limit") Integer limit,
      @Bind("offset") Integer offset);

  @SqlUpdate("DELETE FROM perf_ts_data_reports_queue WHERE status = 'completed' AND kpi = 'timespent'")
  void deleteCompletedFromTimespentQueue();

  @SqlUpdate("UPDATE perf_ts_data_reports_queue SET status = 'completed' WHERE class_id = :classId AND content_source = :contentSource AND"
      + " kpi = 'timespent'")
  void updateTimespentQueueStatusToCompleted(@Bind("classId") String classId,
      @Bind("contentSource") String contentSource);
}
