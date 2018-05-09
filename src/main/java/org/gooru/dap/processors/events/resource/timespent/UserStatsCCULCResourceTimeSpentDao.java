package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class UserStatsCCULCResourceTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_cculc_resource_timespent(user_id, course_id, unit_id, lesson_id, collection_id, resource_id, "
        + "class_id, content_type, collection_type, time_spent) VALUES (:userId, :courseId, :unitId, :lessonId, :collectionId, "
        + ":resourceId, :classId, :contentType, :collectionType, :timeSpent) ON CONFLICT (user_id, course_id, unit_id, lesson_id, "
        + "collection_id, resource_id, class_id) DO UPDATE set time_spent = (:timeSpent + (select time_spent from "
        + "userstat_cculc_resource_timespent WHERE user_id = :userId AND course_id = :courseId AND unit_id = :unitId AND lesson_id = "
        + ":lessonId AND collection_id = :collectionId AND resource_id = :resourceId AND class_id = :classId)) ")
    protected abstract void
        insertOrUpdate(@BindBean UserStatsCCULCResourceTimeSpentBean userStatsCCULCResourceTimeSpentBean);

    @Transaction
    protected void save(UserStatsCCULCResourceTimeSpentBean userStatsCCULCResourceTimeSpentBean) {
        this.insertOrUpdate(userStatsCCULCResourceTimeSpentBean);
    }

}
