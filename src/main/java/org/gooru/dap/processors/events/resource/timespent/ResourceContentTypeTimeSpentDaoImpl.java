package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceContentTypeTimeSpentDaoImpl extends Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceContentTypeTimeSpentDaoImpl.class);

    private final ProcessorContext context;
    private ContentBean contentBean;

    ResourceContentTypeTimeSpentDaoImpl(ProcessorContext context) {
        this.context = context;
    }

    public ExecutionStatus validateRequest() {
        final ContentDao contentDao = getDbiForCoreDS().onDemand(ContentDao.class);
        final String resourceId = context.getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
        final ContentBean contentBean = contentDao.findContentById(resourceId);
        if (contentBean == null) {
            LOGGER.error("content does not exist  for this resource instance {}", resourceId);
            return ExecutionStatus.FAILED;
        }

        this.contentBean = contentBean;

        return ExecutionStatus.SUCCESSFUL;
    }

    public void executeRequest() {
        final ResourceContentTypeTimeSpentDao resourceContentTypeTimespentDao =
            getDbiForDefaultDS().onDemand(ResourceContentTypeTimeSpentDao.class);
        ResourceContentTypeTimeSpentBean resourceContentTypeTimeSpentBean =
            ResourceContentTypeTimeSpentBean.createInstance(context.getEventJsonNode(), contentBean);
        resourceContentTypeTimespentDao.save(resourceContentTypeTimeSpentBean);
        

    }

}
