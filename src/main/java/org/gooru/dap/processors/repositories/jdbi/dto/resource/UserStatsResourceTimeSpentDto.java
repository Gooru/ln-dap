package org.gooru.dap.processors.repositories.jdbi.dto.resource;

import org.gooru.dap.processors.repositories.jdbi.dao.resource.ResourceTimespentTSDao;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

public abstract class UserStatsResourceTimeSpentTSDto extends Repository {
    
    @CreateSqlObject
    abstract ResourceTimespentTSDao getResourceTimespentTSDao();
    

    @Transaction
    public boolean validateRequest() {
        // TODO Auto-generated method stub
        return false;
    }

    @Transaction
    public void executeRequest() {
        getResourceTimespentTSDao().save();
        
    }

}
