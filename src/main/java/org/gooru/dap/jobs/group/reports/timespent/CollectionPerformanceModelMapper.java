
package org.gooru.dap.jobs.group.reports.timespent;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionPerformanceModelMapper
    implements ResultSetMapper<CollectionTimespentModel> {

  @Override
  public CollectionTimespentModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    CollectionTimespentModel model = new CollectionTimespentModel();
    model.setClassId(r.getString("class_id"));
    model.setTimespent(r.getLong("timespent"));
    model.setContentSource(r.getString("content_source"));
    return model;
  }

}
