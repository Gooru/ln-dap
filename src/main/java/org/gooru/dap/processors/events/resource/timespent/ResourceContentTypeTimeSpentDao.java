package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class ResourceContentTypeTimeSpentDao {

    @SqlUpdate("INSERT INTO resource_content_type_timespent_ts(activity_date, content_type, time_spent, view_count)"
        + " VALUES (:activityDate, :contentType, :timeSpent, 1) "
        + "ON CONFLICT (content_type, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from resource_content_type_timespent_ts WHERE content_type = :contentType "
        + "AND activity_date = date(:activityDate))), view_count = (1 + (select view_count from resource_content_type_timespent_ts "
        + "WHERE content_type = :contentType AND activity_date = date(:activityDate)))")
    protected abstract void insertOrUpdate(@BindBean ResourceContentTypeTimeSpentBean resourceContentTypeTimeSpentBean);

    @Transaction
    protected void save(ResourceContentTypeTimeSpentBean resourceContentTypeTimeSpentBean) {
        this.insertOrUpdate(resourceContentTypeTimeSpentBean);
    }

}
