
package org.gooru.dap.deps.group.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public class GroupPerfTSReortsQueueModelMapper
    implements ResultSetMapper<GroupPerfTSReortsQueueModel> {

  @Override
  public GroupPerfTSReortsQueueModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupPerfTSReortsQueueModel model = new GroupPerfTSReortsQueueModel();
    model.setClassId(r.getString("class_id"));
    model.setCourseId(r.getString("course_id"));
    model.setContentSource(r.getString("content_source"));
    model.setTenant(r.getString("tenant"));
    model.setStatus(r.getString("status"));
    return model;
  }

}
