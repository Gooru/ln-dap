package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract class UserStatsResourceContentTypeTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_resource_content_type_timespent_ts(activity_date, user_id, content_type, time_spent)"
        + " VALUES (:activityDate, :userId,  :contentType, :timeSpent) "
        + "ON CONFLICT (user_id, content_type, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from userstat_resource_content_type_timespent_ts WHERE user_id = :userId AND content_type = :contentType "
        + "AND activity_date = date(:activityDate)))")
    public abstract void insertOrUpdate(@BindBean UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean);

}
