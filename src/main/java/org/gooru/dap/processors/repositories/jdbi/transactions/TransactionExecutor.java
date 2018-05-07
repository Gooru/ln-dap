package org.gooru.dap.processors.repositories.jdbi.transactions;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.ProcessorBuilder;
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
        DBI dbi = new DBI("jdbc:postgresql://localhost:5432/dsdb",
            "dsuser", "dspass");
        ProcessorContext context = handler.getContext();
        Repository repository = (Repository) dbi.onDemand(handler.getRepository());
        repository.setContext(context);
        if (repository.validateRequest().isSuccessFul()) {
            repository.executeRequest();
        }
    }
    
    public static void main(String a[]) { 
     String message = "{ \"userId\": \"mUkU41b-40c0-4279-9411-4d0e22c12bbf\", \"resourceId\": \"ROqS0u-8f8c-44ab-a1cf-229d5a8a2373\", \"contentType\": \"video\", \"eventId\": \"0750d14a-9099-4844-9b53-13334dc1f4c2\", \"eventName\": \"resource.timespent\", \"activityTime\": 1525440559096, \"version\": \"1.0\", \"context\": { \"collectionId\": \"5e34f405-cdc5-47b4-b29f-efb0189a7c07\", \"resourceType\": \"resource\", \"collectionType\": \"assessment\", \"courseId\": \"febdfb18-be0a-4665-9b6e-3f86b4eb9fc2\", \"classId\": \"34c60960-479c-4309-a352-946ec8defa32\", \"unitId\": \"b86835fd-9a1e-4a52-ae74-ee96685bfb3f\", \"lessonId\": \"37c9f1c6-e381-4c3f-8d38-20a8e5dcf7f5\", \"pathId\": 0 }, \"metrics\": { \"score\": 0, \"timeSpent\": 8000, \"maxScore\": 0 } }";   
     ProcessorBuilder.buildResourceEventProcessor(message).process();
    }
   
}
