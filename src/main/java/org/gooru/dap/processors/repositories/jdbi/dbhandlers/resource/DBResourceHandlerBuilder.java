package org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;

public final class DBResourceHandlerBuilder {

    private DBResourceHandlerBuilder() {
        throw new AssertionError();
    }
    
    public static DBHandler buildUserResourceTimeSpentTS(ProcessorContext context) {
        return new UserStatsResourceTimeSpentTSHandler(context);
    }
    
}
