
package org.gooru.dap.jobs.group.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public class GroupModelMapper implements ResultSetMapper<GroupModel> {

  @Override
  public GroupModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GroupModel model = new GroupModel();
    model.setId(r.getLong("id"));
    model.setType(r.getString("type"));
    model.setSubType(r.getString("sub_type"));
    model.setParentId(r.getLong("parent_id"));
    model.setStateId(r.getLong("state_id"));
    model.setCountryId(r.getLong("country_id"));
    model.setTenant(r.getString("tenant"));
    return model;
  }

}
