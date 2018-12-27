package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class UserStatsQuestionTypeTimeSpentProcessor extends AbstractEventProcessor {

  public UserStatsQuestionTypeTimeSpentProcessor(ProcessorContext context) {
    super(context);
  }

  @Override
  protected void processEvent() {
    RepoBuilder.buildQuestionTimeSpentRepo(context).userStatsQuestionTypeTimeSpent();
  }

}
