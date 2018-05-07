package org.gooru.dap.processors.events.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class UserStatsResourceContentTypeTimeSpentProcessor extends AbstractEventProcessor {

    public UserStatsResourceContentTypeTimeSpentProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void processEvent() {
       RepoBuilder.buildResourceRepo(context).userStatsResourceContentTypeTimeSpent();
    }

}
