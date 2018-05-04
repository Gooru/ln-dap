package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.ResourceRepo;

public final class JdbiRepoBuilder {

    private JdbiRepoBuilder() {
        throw new AssertionError();
    }

    public static ResourceRepo buildTimeSpentRepo(ProcessorContext context) {
        return new JdbiResourceRepo(context);
    }
    
}
