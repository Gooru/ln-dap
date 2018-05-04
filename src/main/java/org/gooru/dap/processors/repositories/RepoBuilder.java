package org.gooru.dap.processors.repositories;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.JdbiRepoBuilder;

public final class RepoBuilder {

    private RepoBuilder() {
        throw new AssertionError();
    }
    
    public static ResourceRepo buildTimeSpentRepo(ProcessorContext context) {
        return JdbiRepoBuilder.buildTimeSpentRepo(context);
    }
}
