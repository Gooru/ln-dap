package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.QuestionTimeSpentRepo;
import org.gooru.dap.processors.repositories.ResourceTimeSpentRepo;

public final class JdbiRepoBuilder {

    private JdbiRepoBuilder() {
        throw new AssertionError();
    }

    public static ResourceTimeSpentRepo buildResourceTimeSpentRepo(ProcessorContext context) {
        return new JdbiResourceTimeSpentRepo(context);
    }
    
    public static QuestionTimeSpentRepo buildQuestionTimeSpentRepo(ProcessorContext context) {
        return new JdbiQuestionTimeSpentRepo(context);
    }
    
}
