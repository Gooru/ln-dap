package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract class ResourceContentTypeTimeSpentDao {

    @SqlUpdate("INSERT INTO resource_content_type_timespent_ts(activity_date, content_type, time_spent)"
        + " VALUES (:activityDate, :contentType, :timeSpent) "
        + "ON CONFLICT (content_type, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from resource_content_type_timespent_ts WHERE content_type = :contentType "
        + "AND activity_date = date(:activityDate)))")
    public abstract void insertOrUpdate(@BindBean ResourceContentTypeTimeSpentBean resourceContentTypeTimeSpentBean);

}
