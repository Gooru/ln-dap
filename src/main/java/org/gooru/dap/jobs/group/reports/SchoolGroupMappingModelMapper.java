
package org.gooru.dap.jobs.group.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public class SchoolGroupMappingModelMapper implements ResultSetMapper<SchoolGroupMappingModel> {

  @Override
  public SchoolGroupMappingModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    SchoolGroupMappingModel model = new SchoolGroupMappingModel();
    model.setGroupId(r.getLong("group_id"));
    model.setSchoolId(r.getLong("school_id"));
    return model;
  }

}
