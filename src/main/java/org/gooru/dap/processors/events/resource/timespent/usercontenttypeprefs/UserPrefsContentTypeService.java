package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import org.gooru.dap.processors.events.resource.timespent.UserStatsResourceContentTypeTimeSpentBean;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface UserPrefsContentTypeService {

  void updateUserPrefsForContentTypeBasedOnTimespent(
      UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean);

  static UserPrefsContentTypeService build(DBI dbi) {
    return new UserPrefsContentTypeServiceImpl(dbi);
  }

}
