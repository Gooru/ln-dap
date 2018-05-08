package org.gooru.dap.processors.repositories.jdbi.transactions;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.skife.jdbi.v2.DBI;

public final class TransactionExecutor {

    private TransactionExecutor() {
        throw new AssertionError();
    }

    public static void execute(DBHandler handler) {
        ExecutionStatus doSanityCheck = handler.checkSanity();
        if (doSanityCheck.isSuccessFul()) {
            executeWithTransaction(handler);
        }
    }

    private static void executeWithTransaction(DBHandler handler) {
        DBI dbi = DBICreator.getDbiForDefaultDS();
        ProcessorContext context = handler.getContext();
        Repository repository = dbi.onDemand(handler.getRepository());
        repository.setContext(context);
        if (repository.validateRequest().isSuccessFul()) {
            repository.executeRequest();
        }
    }

}
