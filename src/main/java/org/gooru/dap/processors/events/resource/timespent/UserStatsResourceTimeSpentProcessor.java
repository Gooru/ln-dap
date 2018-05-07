package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class UserStatsResourceTimeSpentProcessor extends AbstractEventProcessor {

    public UserStatsResourceTimeSpentProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void processEvent() {
       RepoBuilder.buildResourceTimeSpentRepo(context).userStatsResourceTimeSpent();
    }

}
