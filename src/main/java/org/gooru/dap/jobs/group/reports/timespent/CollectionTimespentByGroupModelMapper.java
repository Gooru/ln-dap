
package org.gooru.dap.jobs.group.reports.timespent;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionTimespentByGroupModelMapper
    implements ResultSetMapper<CollectionTimespentByGroupModel> {

  @Override
  public CollectionTimespentByGroupModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    CollectionTimespentByGroupModel model = new CollectionTimespentByGroupModel();
    model.setGroupId(r.getLong("id"));
    model.setTimespent(r.getLong("timespent"));
    return model;
  }

}
