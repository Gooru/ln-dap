package org.gooru.dap.processors.events.question.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class QuestionTypeTimeSpentDao {

    @SqlUpdate("INSERT INTO question_type_timespent_ts(activity_date, question_type, time_spent)"
        + " VALUES (:activityDate, :questionType, :timeSpent) "
        + "ON CONFLICT (question_type, activity_date) DO UPDATE set time_spent = (:timeSpent + (select "
        + "time_spent from question_type_timespent_ts WHERE question_type = :questionType "
        + "AND activity_date = date(:activityDate)))")
    protected abstract void insertOrUpdate(@BindBean QuestionTypeTimeSpentBean questionTypeTimeSpentBean);

    @Transaction
    protected void save(QuestionTypeTimeSpentBean questionTypeTimeSpentBean) {
        this.insertOrUpdate(questionTypeTimeSpentBean);
    }

}
