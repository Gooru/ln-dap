package org.gooru.dap.processors.repositories.jdbi.dbhandlers;

import org.gooru.dap.processors.ProcessorContext;

public abstract class Repository {

    private ProcessorContext context;
    
    public abstract boolean validateRequest();

    public abstract void executeRequest();

    public ProcessorContext getContext() {
        return context;
    }

    public void setContext(ProcessorContext context) {
        this.context = context;
    }
}
