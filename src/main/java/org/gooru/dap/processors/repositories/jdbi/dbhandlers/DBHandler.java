package org.gooru.dap.processors.repositories.jdbi.dbhandlers;

import org.gooru.dap.processors.ProcessorContext;

public interface DBHandler {

    boolean checkSanity();
    
    Class<? extends Repository> getRepository();
    
    ProcessorContext getContext();
}
