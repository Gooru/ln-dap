package org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.DBI;


/**
 * @author mukul@gooru
 */
public class AtcPostProcessorService {

  private final AtcPostProcessorDao postProcessorDao;

  public AtcPostProcessorService(DBI dbi) {
    this.postProcessorDao = dbi.onDemand(AtcPostProcessorDao.class);
  }

  List<String> fetchActiveClassMembers(String classId, String courseId) {
    return postProcessorDao.fetchActiveClassMembers(classId, courseId);
  }

}
