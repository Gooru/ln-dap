package org.gooru.dap.processors.repositories.jdbi.dto.resource;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.bean.resource.UserStatsResourceContentTypeTimeSpentBean;
import org.gooru.dap.processors.repositories.jdbi.dao.resource.UserStatsResourceContentTypeTimeSpentDao;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

public abstract class UserStatsResourceContentTypeTimeSpentDto extends Repository {

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
