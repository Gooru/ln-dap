package org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;

public final class DBResourceHandlerBuilder {

    private DBResourceHandlerBuilder() {
        throw new AssertionError();
    }
    
    public static DBHandler buildUserStatsResourceTimeSpent(ProcessorContext context) {
        return new UserStatsResourceTimeSpentHandler(context);
    }
    
    public static DBHandler buildUserStatsResourceContentTypeTimeSpent(ProcessorContext context) {
        return new UserStatsResourceContentTypeTimeSpentHandler(context);
    }
    
    public static DBHandler buildResourceContentTypeTimeSpent(ProcessorContext context) {
        return new ResourceContentTypeTimeSpentHandler(context);
    }
    
}
