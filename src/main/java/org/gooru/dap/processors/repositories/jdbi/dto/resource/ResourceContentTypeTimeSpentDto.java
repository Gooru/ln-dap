package org.gooru.dap.processors.repositories.jdbi.dto.resource;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.bean.resource.ResourceContentTypeTimeSpentBean;
import org.gooru.dap.processors.repositories.jdbi.dao.resource.ResourceContentTypeTimeSpentDao;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

public abstract class ResourceContentTypeTimeSpentDto extends Repository {
    
    @CreateSqlObject
    abstract ResourceContentTypeTimeSpentDao getResourceContentTypeTimespentDao();
    

    @Transaction
    public ExecutionStatus validateRequest() {
        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        ResourceContentTypeTimeSpentBean resourceContentTypeTimeSpentBean = ResourceContentTypeTimeSpentBean
            .fromJsonNode(getContext().getEventJsonNode());
        getResourceContentTypeTimespentDao().insertOrUpdate(resourceContentTypeTimeSpentBean);
        
    }

}
