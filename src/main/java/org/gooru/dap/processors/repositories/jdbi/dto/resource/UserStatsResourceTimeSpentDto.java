package org.gooru.dap.processors.repositories.jdbi.dto.resource;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.bean.resource.UserStatsResourceTimeSpentBean;
import org.gooru.dap.processors.repositories.jdbi.dao.resource.UserStatsResourceTimeSpentDao;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

public abstract class UserStatsResourceTimeSpentDto extends Repository {
    
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
