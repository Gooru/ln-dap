package org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.components.jdbi.PGArrayUtils;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcDao;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor.GradeCompetencyProcessor;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class AtcPostProcessorCoreService {

  private final ClassDao classDao;

  public AtcPostProcessorCoreService(DBI coreDbi) {
    this.classDao = coreDbi.onDemand(ClassDao.class);
  }

  public String fetchCoursefromClass(@Bind("classId") String classId) {
    return classDao.fetchCoursefromClass(UUID.fromString(classId));
  }

  Integer fetchGradefromClassMembers(String userId, String classId) {
    return classDao.fetcheGradefromClassMembers(UUID.fromString(userId), UUID.fromString(classId));
  }

  Integer fetchGradefromClass(String classId, String courseId) {
    return classDao.fetcheGradefromClass(UUID.fromString(classId), UUID.fromString(courseId));
  }

  List<String> fetchClassMembers(String classId) {
    return classDao.fetchClassMembers(UUID.fromString(classId));
  }
}
