package org.gooru.dap.processors.events.question.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract class UserStatsCCULCQuestionTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_cculc_question_timespent(user_id, course_id, unit_id, lesson_id, collection_id, question_id, "
        + "class_id, question_type, collection_type, time_spent) VALUES (:userId, :courseId, :unitId, :lessonId, :collectionId, "
        + ":questionId, :classId, :questionType, :collectionType, :timeSpent) ON CONFLICT (user_id, course_id, unit_id, lesson_id, "
        + "collection_id, question_id, class_id) DO UPDATE set time_spent = (:timeSpent + (select time_spent from "
        + "userstat_cculc_question_timespent WHERE user_id = :userId AND course_id = :courseId AND unit_id = :unitId AND lesson_id = "
        + ":lessonId AND collection_id = :collectionId AND question_id = :questionId AND class_id = :classId)) ")
    public abstract void insertOrUpdate(
        @BindBean UserStatsCCULCQuestionTimeSpentBean userStatsCCULCQuestionTimeSpentBean);

}
