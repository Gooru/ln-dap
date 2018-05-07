package org.gooru.dap.processors.events.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class UserStatsOriginalResourceTimeSpentProcessor extends AbstractEventProcessor {

    public UserStatsOriginalResourceTimeSpentProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void processEvent() {
       RepoBuilder.buildResourceRepo(context).userStatsOriginalResourceTimeSpent();
    }

}
