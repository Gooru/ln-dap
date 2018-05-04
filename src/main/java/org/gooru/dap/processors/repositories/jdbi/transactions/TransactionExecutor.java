package org.gooru.dap.processors.repositories.jdbi.transactions;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.gooru.dap.processors.repositories.jdbi.dbutils.DBICreator;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TransactionExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionExecutor.class);
    
    private TransactionExecutor() {
        throw new AssertionError();
    }
    
   
    public static void execute(DBHandler handler) {
        boolean doSanityCheck = handler.checkSanity();
        if (doSanityCheck) { 
            executeWithTransaction(handler);
        }
    }
    
    private static void executeWithTransaction(DBHandler handler) { 
        DBI dbi = DBICreator.getDbiForDefaultDS();
        ProcessorContext context = handler.getContext();
        Repository repository = (Repository) dbi.onDemand(handler.getRepository());
        repository.setContext(context);
        if (repository.validateRequest()) {
            repository.executeRequest();
        }
    }
    
}
