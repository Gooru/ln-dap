package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class UserStatsResourceTimeSpentDto extends Repository {
    
    @CreateSqlObject
    abstract UserStatsResourceTimeSpentDao getResourceTimespentDao();
    

    @Transaction
    public ExecutionStatus validateRequest() {
        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        UserStatsResourceTimeSpentBean userStatsResourceTimeSpentBean = UserStatsResourceTimeSpentBean
            .fromJsonNode(getContext().getEventJsonNode());
        getResourceTimespentDao().insertOrUpdate(userStatsResourceTimeSpentBean);
        
    }

}
