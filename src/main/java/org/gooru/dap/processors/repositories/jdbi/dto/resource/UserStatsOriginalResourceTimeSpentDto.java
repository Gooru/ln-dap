package org.gooru.dap.processors.repositories.jdbi.dto.resource;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.bean.resource.UserStatsOriginalResourceTimeSpentBean;
import org.gooru.dap.processors.repositories.jdbi.dao.resource.OriginalResourceContentDao;
import org.gooru.dap.processors.repositories.jdbi.dao.resource.UserStatsOriginalResourceTimeSpentDao;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UserStatsOriginalResourceTimeSpentDto extends Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatsOriginalResourceTimeSpentDto.class);

    @CreateSqlObject
    abstract UserStatsOriginalResourceTimeSpentDao getOriginalResourceTimespentDao();

    @CreateSqlObject
    abstract OriginalResourceContentDao getOriginalResourceContentDao();

    private String originalContentId;

    @Transaction
    public ExecutionStatus validateRequest() {
        final String resourceId = getContext().getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
        final String originalResourceId = getOriginalResourceContentDao().getOriginalContentId(resourceId);
        if (originalResourceId == null) {
            LOGGER.error("Original resource id does not exist  for this resource instance {}", resourceId);
            return ExecutionStatus.FAILED;
        }
        this.originalContentId = originalResourceId;

        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        UserStatsOriginalResourceTimeSpentBean userStatsOriginalResourceTimeSpentBean =
            UserStatsOriginalResourceTimeSpentBean.createInstance(getContext().getEventJsonNode(), originalContentId);
        getOriginalResourceTimespentDao().insertOrUpdate(userStatsOriginalResourceTimeSpentBean);

    }

}
