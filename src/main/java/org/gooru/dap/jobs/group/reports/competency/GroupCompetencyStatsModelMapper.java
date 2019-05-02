
package org.gooru.dap.jobs.group.reports.competency;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 02-May-2019
 */
public class GroupCompetencyStatsModelMapper implements ResultSetMapper<GroupCompetencyStatsModel> {

  @Override
  public GroupCompetencyStatsModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupCompetencyStatsModel model = new GroupCompetencyStatsModel();
    model.setGroupId(r.getLong("id"));
    model.setCompletedCount(r.getLong("completed_count"));
    model.setInprogressCount(r.getLong("inprogress_count"));
    model.setCumulativeCompletedCount(r.getLong("cumulative_completed_count"));
    return model;
  }

}
