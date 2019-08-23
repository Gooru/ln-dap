
package org.gooru.dap.jobs.group.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public class ClassSchoolMappingModelMapper implements ResultSetMapper<ClassSchoolMappingModel> {

  @Override
  public ClassSchoolMappingModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    ClassSchoolMappingModel model = new ClassSchoolMappingModel();
    model.setClassId(r.getString("id"));
    model.setSchoolId(r.getLong("school_id"));
    return model;
  }

}
