package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.Repository;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserStatsCCULCResourceTimeSpentDaoImpl extends Repository {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UserStatsCCULCResourceTimeSpentDaoImpl.class);

  private final ProcessorContext context;

  UserStatsCCULCResourceTimeSpentDaoImpl(ProcessorContext context) {
    this.context = context;
  }

  private ContentBean contentBean;

  public ExecutionStatus validateRequest() {
    final ContentDao contentDao = getDbiForCoreDS().onDemand(ContentDao.class);
    final String resourceId =
        context.getEventJsonNode().get(EventMessageConstant.RESOURCE_ID).textValue();
    final ContentBean contentBean = contentDao.findContentById(resourceId);
    if (contentBean == null) {
      LOGGER.error("content does not exist  for this resource instance {}", resourceId);
      return ExecutionStatus.FAILED;
    }

    this.contentBean = contentBean;

    return ExecutionStatus.SUCCESSFUL;
  }

  public void executeRequest() {
    UserStatsCCULCResourceTimeSpentBean userStatsCCULCResourceTimeSpentBean =
        UserStatsCCULCResourceTimeSpentBean.createInstance(context.getEventJsonNode(), contentBean);
    UserStatsCCULCResourceTimeSpentDao UserStatsCCULCResourceTimeSpentDao =
        getDbiForDefaultDS().onDemand(UserStatsCCULCResourceTimeSpentDao.class);
    UserStatsCCULCResourceTimeSpentDao.save(userStatsCCULCResourceTimeSpentBean);

  }

}
