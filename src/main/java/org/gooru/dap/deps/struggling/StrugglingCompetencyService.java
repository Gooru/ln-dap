
package org.gooru.dap.deps.struggling;

import java.sql.Timestamp;
import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 14-Oct-2019
 */
public class StrugglingCompetencyService {

  private final StrugglingCompetencyDao dao;

  public StrugglingCompetencyService(DBI dbi) {
    this.dao = dbi.onDemand(StrugglingCompetencyDao.class);
  }

  public void insertAsStruggling(StrugglingCompetencyCommand.StrugglingCompetencyCommandBean bean) {
    this.dao.insertAsStruggling(bean);
  }

  public void removeFromStruggling(
      StrugglingCompetencyCommand.StrugglingCompetencyCommandBean bean) {
    this.dao.removeFromStruggling(bean);
  }

  public List<UserDomainCompetencyMatrixModel> fetchUserSkyline(String user, String subject,
      Timestamp toDate) {
    return this.dao.fetchUserSkyline(user, subject, toDate);
  }
}
