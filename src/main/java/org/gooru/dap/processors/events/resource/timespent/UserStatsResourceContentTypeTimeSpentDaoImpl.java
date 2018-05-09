package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentDao;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class UserStatsResourceContentTypeTimeSpentDaoImpl extends Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatsResourceContentTypeTimeSpentDaoImpl.class);

    @CreateSqlObject
    abstract UserStatsResourceContentTypeTimeSpentDao getUserStatsResourceContentTypeTimespentDao();

    @CreateSqlObject
    abstract ContentDao getContentDao();

    private ContentBean contentBean;

    @Transaction
    public ExecutionStatus validateRequest() {
        final String resourceId = getContext().getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
        final ContentBean contentBean = getContentDao().findOriginalContentById(resourceId);
        if (contentBean == null) {
            LOGGER.error("content does not exist  for this resource instance {}", resourceId);
            return ExecutionStatus.FAILED;
        }

        this.contentBean = contentBean;

        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean =
            UserStatsResourceContentTypeTimeSpentBean.createInstance(getContext().getEventJsonNode(), contentBean);
        getUserStatsResourceContentTypeTimespentDao().insertOrUpdate(userStatsResourceContentTypeTimeSpentBean);

    }

}
