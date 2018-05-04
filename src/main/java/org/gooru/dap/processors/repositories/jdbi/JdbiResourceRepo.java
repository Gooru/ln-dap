package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.ResourceRepo;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource.DBResourceHandlerBuilder;
import org.gooru.dap.processors.repositories.jdbi.transactions.TransactionExecutor;

public class JdbiResourceRepo implements ResourceRepo {

    private final ProcessorContext context;

    public JdbiResourceRepo(ProcessorContext context) {
        this.context = context;
    }
    
    @Override
    public void userStatsResourceTimeSpent() {
        TransactionExecutor.execute(DBResourceHandlerBuilder.buildUserResourceTimeSpentTS(context));
    }

}
