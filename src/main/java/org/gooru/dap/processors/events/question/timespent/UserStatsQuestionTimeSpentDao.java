package org.gooru.dap.processors.events.question.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract class UserStatsQuestionTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_question_timespent_ts(activity_date, user_id, question_id, question_type, time_spent)"
        + " VALUES (:activityDate, :userId, :questionId, :questionType, :timeSpent) "
        + "ON CONFLICT (user_id, question_id, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from userstat_question_timespent_ts WHERE user_id = :userId AND question_id = :questionId "
        + "AND activity_date = date(:activityDate)))")
    public abstract void insertOrUpdate(@BindBean UserStatsQuestionTimeSpentBean userStatsQuestionTimeSpentBean);

}
