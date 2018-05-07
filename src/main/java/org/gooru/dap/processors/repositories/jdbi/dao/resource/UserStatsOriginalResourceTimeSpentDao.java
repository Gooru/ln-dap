package org.gooru.dap.processors.repositories.jdbi.dao.resource;

import org.gooru.dap.processors.repositories.jdbi.bean.resource.UserStatsOriginalResourceTimeSpentBean;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public abstract class UserStatsOriginalResourceTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_original_resource_timespent_ts(activity_date, user_id, original_resource_id, content_type, time_spent)"
        + " VALUES (:activityDate, :userId, :originalResourceId, :contentType, :timeSpent) "
        + "ON CONFLICT (user_id, original_resource_id, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from userstat_original_resource_timespent_ts WHERE user_id = :userId AND original_resource_id = :originalResourceId "
        + "AND activity_date = date(:activityDate)))")
    public abstract void insertOrUpdate(@BindBean UserStatsOriginalResourceTimeSpentBean userStatsOriginalResourceTimeSpentBean);

}
