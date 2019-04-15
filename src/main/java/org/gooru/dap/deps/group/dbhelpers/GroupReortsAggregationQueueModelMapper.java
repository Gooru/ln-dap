
package org.gooru.dap.deps.group.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public class GroupReortsAggregationQueueModelMapper
    implements ResultSetMapper<GroupReortsAggregationQueueModel> {

  @Override
  public GroupReortsAggregationQueueModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupReortsAggregationQueueModel model = new GroupReortsAggregationQueueModel();
    model.setClassId(r.getString("class_id"));
    model.setCourseId(r.getString("course_id"));
    model.setContentSource(r.getString("content_source"));
    model.setTenant(r.getString("tenant"));
    model.setStatus(r.getString("status"));
    return model;
  }

}
