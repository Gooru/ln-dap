package org.gooru.dap.deps.competency.preprocessors;

import org.skife.jdbi.v2.DBI;


/**
 * @author mukul@gooru
 */
public class DCAContentService {

  private final DCAContentDao dcaContentDao;

  public DCAContentService(DBI coredbi) {
    dcaContentDao = coredbi.onDemand(DCAContentDao.class);
  }

  public DCAContentModel getDCAContentId(Integer id) {
    return dcaContentDao.fetchDCAContentId(id);
  }

}
