package org.gooru.dap.processors.repositories;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.JdbiRepoBuilder;

public final class RepoBuilder {

  private RepoBuilder() {
    throw new AssertionError();
  }

  public static ResourceTimeSpentRepo buildResourceTimeSpentRepo(ProcessorContext context) {
    return JdbiRepoBuilder.buildResourceTimeSpentRepo(context);
  }

  public static QuestionTimeSpentRepo buildQuestionTimeSpentRepo(ProcessorContext context) {
    return JdbiRepoBuilder.buildQuestionTimeSpentRepo(context);
  }

}
