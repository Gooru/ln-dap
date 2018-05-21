package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserStatsQuestionTimeSpentDaoImpl extends Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatsQuestionTimeSpentDaoImpl.class);

    private final ProcessorContext context;

    UserStatsQuestionTimeSpentDaoImpl(ProcessorContext context) {
        this.context = context;
    }

    private ContentBean contentBean;

    public ExecutionStatus validateRequest() {
        final String questionId = context.getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
        final ContentDao contentDao = getDbiForCoreDS().onDemand(ContentDao.class);
        final ContentBean contentBean = contentDao.findContentById(questionId);
        if (contentBean == null) {
            LOGGER.error("content does not exist  for this question instance {}", questionId);
            return ExecutionStatus.FAILED;
        }

        this.contentBean = contentBean;

        return ExecutionStatus.SUCCESSFUL;
    }

    public void executeRequest() {
        UserStatsQuestionTimeSpentBean userStatsQuestionTimeSpentBean =
            UserStatsQuestionTimeSpentBean.createInstance(context.getEventJsonNode(), contentBean);
        UserStatsQuestionTimeSpentDao userStatsQuestionTimeSpentDao =
            getDbiForDefaultDS().onDemand(UserStatsQuestionTimeSpentDao.class);
        userStatsQuestionTimeSpentDao.save(userStatsQuestionTimeSpentBean);

    }

}
