package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class UserStatsResourceContentTypeTimespentService {

  private final DBI dbi;

  UserStatsResourceContentTypeTimespentService(DBI dbi) {
    this.dbi = dbi;
  }

  void updateUserStatResourceContentTypeTimespent(
      UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean) {

    UserStatsResourceContentTypeTimeSpentDao userStatsResourceContentTypeTimeSpentDao =
        dbi.onDemand(UserStatsResourceContentTypeTimeSpentDao.class);
    userStatsResourceContentTypeTimeSpentDao.save(userStatsResourceContentTypeTimeSpentBean);
  }

}
