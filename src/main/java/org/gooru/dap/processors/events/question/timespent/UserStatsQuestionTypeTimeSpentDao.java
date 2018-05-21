package org.gooru.dap.processors.events.question.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class UserStatsQuestionTypeTimeSpentDao {

    @SqlUpdate("INSERT INTO userstat_question_type_timespent_ts(activity_date, user_id, question_type, time_spent)"
        + " VALUES (:activityDate, :userId,  :questionType, :timeSpent) "
        + "ON CONFLICT (user_id, question_type, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from userstat_question_type_timespent_ts WHERE user_id = :userId AND question_type = :questionType "
        + "AND activity_date = date(:activityDate)))")
    protected abstract void insertOrUpdate(@BindBean UserStatsQuestionTypeTimeSpentBean userStatsQuestionTypeTimeSpentBean);

    @Transaction
    protected void save(UserStatsQuestionTypeTimeSpentBean userStatsQuestionTypeTimeSpentBean) {
        this.insertOrUpdate(userStatsQuestionTypeTimeSpentBean);
    } 
}
