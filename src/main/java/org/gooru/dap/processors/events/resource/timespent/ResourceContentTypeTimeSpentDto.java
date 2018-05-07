package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

abstract class ResourceContentTypeTimeSpentDto extends Repository {
    
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
