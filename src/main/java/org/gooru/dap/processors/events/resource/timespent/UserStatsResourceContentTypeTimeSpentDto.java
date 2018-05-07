package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class UserStatsResourceContentTypeTimeSpentDto extends Repository {

    @CreateSqlObject
    abstract UserStatsResourceContentTypeTimeSpentDao getUserStatsResourceContentTypeTimespentDao();

    @Transaction
    public ExecutionStatus validateRequest() {
        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean =
            UserStatsResourceContentTypeTimeSpentBean.fromJsonNode(getContext().getEventJsonNode());
        getUserStatsResourceContentTypeTimespentDao().insertOrUpdate(userStatsResourceContentTypeTimeSpentBean);

    }

}
