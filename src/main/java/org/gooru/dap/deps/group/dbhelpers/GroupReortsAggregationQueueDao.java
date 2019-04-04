
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public interface GroupReortsAggregationQueueDao {

  @SqlUpdate("INSERT INTO group_level_data_aggregation_queue(class_id, tenant) VALUES (:classId, :tenant) ON CONFLICT (class_id) DO NOTHING")
  void insertIntoQueue(@Bind("classId") String classId, @Bind("tenant") String tenant);

  @Mapper(GroupReortsAggregationQueueModelMapper.class)
  @SqlQuery("SELECT class_id, tenant FROM group_level_data_aggregation_queue")
  List<GroupReortsAggregationQueueModel> fetchClassesForProcessing();

  @SqlUpdate("DELETE FROM group_level_data_aggregation_queue WHERE class_id = :classId AND tenant = :tenant")
  void removeFromQueue(@Bind("classId") String classId, @Bind("tenant") String tenant);
}
