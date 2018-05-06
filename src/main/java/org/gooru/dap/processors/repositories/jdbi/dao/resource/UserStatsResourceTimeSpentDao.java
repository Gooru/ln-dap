package org.gooru.dap.processors.repositories.jdbi.dao.resource;

import org.gooru.dap.processors.repositories.jdbi.bean.resource.UserStatsResourceTimeSpentBean;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public abstract class UserStatsResourceTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_resource_timespent_ts(activity_date, user_id, resource_id, content_type, time_spent)"
        + " VALUES (:activityDate, :userId, :resourceId, :contentType, :timeSpent) "
        + "ON CONFLICT (user_id, resource_id, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from userstat_resource_timespent_ts WHERE user_id = :userId AND resource_id = :resourceId "
        + "AND activity_date = date(:activityDate)))")
    public abstract void insertOrUpdate(@BindBean UserStatsResourceTimeSpentBean userStatsResourceTimeSpentBean);

}
