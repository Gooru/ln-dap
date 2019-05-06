package org.gooru.dap.deps.competency.assessmentscore.atc.preprocessor;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class AtcPreProcessorService {

  private final AtcPreProcessorDao atcPreprocessorDao;
  private static final String PREMIUM = "premium";


  AtcPreProcessorService(DBI coreDbi) {
    this.atcPreprocessorDao = coreDbi.onDemand(AtcPreProcessorDao.class);
  }

  String fetchCourseFromClass(String classId) {
    String courseId = atcPreprocessorDao.fetchCoursefromClass(UUID.fromString(classId));
    return courseId;
  }

  boolean CheckifClassPremium(String classId) {
    String isPremium = atcPreprocessorDao.isClassPremium(UUID.fromString(classId));
    if (isPremium != null && !isPremium.isEmpty()) {
      return isPremium.equalsIgnoreCase(PREMIUM) ? true : false;
    } else {
      return false;
    }
  }
}
