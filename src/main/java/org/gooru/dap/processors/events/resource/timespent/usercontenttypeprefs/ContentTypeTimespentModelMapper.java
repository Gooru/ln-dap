package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish.
 */

public class ContentTypeTimespentModelMapper implements ResultSetMapper<ContentTypeTimespentModel> {

  private static final String CONTENT_TYPE = "content_type";
  private static final String AVERAGE = "average";

  @Override
  public ContentTypeTimespentModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    ContentTypeTimespentModel model = new ContentTypeTimespentModel();
    model.setContentType(r.getString(CONTENT_TYPE));
    model.setTimeSpent(r.getLong(AVERAGE));
    return model;
  }
}
