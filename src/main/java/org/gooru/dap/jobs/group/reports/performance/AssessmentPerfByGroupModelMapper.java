
package org.gooru.dap.jobs.group.reports.performance;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 10-Apr-2019
 */
public class AssessmentPerfByGroupModelMapper
    implements ResultSetMapper<AssessmentPerfByGroupModel> {

  @Override
  public AssessmentPerfByGroupModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    AssessmentPerfByGroupModel model = new AssessmentPerfByGroupModel();
    model.setPerformance(r.getDouble("performance"));
    model.setGroupId(r.getLong("id"));
    return model;
  }

}
