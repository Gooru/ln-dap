package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.resource.timespent.DBResourceTimeSpentHandlerBuilder;
import org.gooru.dap.processors.repositories.ResourceTimeSpentRepo;
import org.gooru.dap.processors.repositories.jdbi.transactions.TransactionExecutor;

public class JdbiResourceTimeSpentRepo implements ResourceTimeSpentRepo {

    private final ProcessorContext context;

    public JdbiResourceTimeSpentRepo(ProcessorContext context) {
        this.context = context;
    }
    
    @Override
    public void userStatsResourceTimeSpent() {
        TransactionExecutor.execute(DBResourceTimeSpentHandlerBuilder.buildUserStatsResourceTimeSpent(context));
    }

    @Override
    public void userStatsResourceContentTypeTimeSpent() {
        TransactionExecutor.execute(DBResourceTimeSpentHandlerBuilder.buildUserStatsResourceContentTypeTimeSpent(context)); 
    }

    @Override
    public void resourceContentTypeTimeSpent() {
        TransactionExecutor.execute(DBResourceTimeSpentHandlerBuilder.buildResourceContentTypeTimeSpent(context));
    }

    @Override
    public void userStatsOriginalResourceTimeSpent() {
        TransactionExecutor.execute(DBResourceTimeSpentHandlerBuilder.buildUserStatsOriginalResourceTimeSpent(context));
    }

}
