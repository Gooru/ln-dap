package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class QuestionTypeTimeSpentProcessor extends AbstractEventProcessor {

  public QuestionTypeTimeSpentProcessor(ProcessorContext context) {
    super(context);
  }

  @Override
  protected void processEvent() {
    RepoBuilder.buildQuestionTimeSpentRepo(context).questionTypeTimeSpent();
  }

}
