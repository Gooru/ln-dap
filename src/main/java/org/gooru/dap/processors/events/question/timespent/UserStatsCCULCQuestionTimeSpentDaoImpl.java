package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentDao;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class UserStatsCCULCQuestionTimeSpentDaoImpl extends Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatsCCULCQuestionTimeSpentDaoImpl.class);

    @CreateSqlObject
    abstract UserStatsCCULCQuestionTimeSpentDao getUserStatsCCULCQuestionTimespentDao();

    @CreateSqlObject
    abstract ContentDao getContentDao();

    private ContentBean contentBean;

    @Transaction
    public ExecutionStatus validateRequest() {
        final String questionId = getContext().getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
        final ContentBean contentBean = getContentDao().findOriginalContentById(questionId);
        if (contentBean == null) {
            LOGGER.error("content does not exist  for this question instance {}", questionId);
            return ExecutionStatus.FAILED;
        }

        this.contentBean = contentBean;

        return ExecutionStatus.SUCCESSFUL;
    }

    @Transaction
    public void executeRequest() {
        UserStatsCCULCQuestionTimeSpentBean userStatsCCULCQuestionTimeSpentBean =
            UserStatsCCULCQuestionTimeSpentBean.createInstance(getContext().getEventJsonNode(), contentBean);
        getUserStatsCCULCQuestionTimespentDao().insertOrUpdate(userStatsCCULCQuestionTimeSpentBean);

    }

}
