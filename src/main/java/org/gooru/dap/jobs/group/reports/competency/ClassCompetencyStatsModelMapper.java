
package org.gooru.dap.jobs.group.reports.competency;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class ClassCompetencyStatsModelMapper implements ResultSetMapper<ClassCompetencyStatsModel> {

  @Override
  public ClassCompetencyStatsModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    ClassCompetencyStatsModel model = new ClassCompetencyStatsModel();
    model.setClassId(r.getString("class_id"));
    model.setCompletedCount(r.getLong("completed_count"));
    model.setInprogressCount(r.getLong("inprogress_count"));
    return model;
  }

}
