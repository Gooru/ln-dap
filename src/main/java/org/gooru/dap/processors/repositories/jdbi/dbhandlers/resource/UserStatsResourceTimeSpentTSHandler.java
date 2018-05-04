package org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.gooru.dap.processors.repositories.jdbi.dto.resource.UserStatsResourceTimeSpentTSDto;

class UserStatsResourceTimeSpentTSHandler implements DBHandler {

    private final ProcessorContext context;
    
    public UserStatsResourceTimeSpentTSHandler(ProcessorContext context) { 
        this.context = context;
    }
    
    @Override
    public boolean checkSanity() {
        return true;
    }

    @Override
    public Class<? extends Repository> getRepository() {
        return UserStatsResourceTimeSpentTSDto.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }
    
}
