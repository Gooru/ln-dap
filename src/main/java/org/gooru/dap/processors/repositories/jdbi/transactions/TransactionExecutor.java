package org.gooru.dap.processors.repositories.jdbi.transactions;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

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
        Repository repository = handler.getRepository();
        if (repository.validateRequest().isSuccessFul()) {
            repository.executeRequest();
        }
    }

}
