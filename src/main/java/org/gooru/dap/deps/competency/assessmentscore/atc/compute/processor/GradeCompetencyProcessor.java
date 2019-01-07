package org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor;

import java.util.List;
import org.gooru.dap.components.jdbi.DBICreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class GradeCompetencyProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(GradeCompetencyProcessor.class);
  // We need to get the Competency Map right from the earthline, and hence the pseudo userId
  private static final String userUUIDStr = "00000000-0000-0000-0000-000000000000";
  private GradeCompetencyService gradeCompetencyService =
      new GradeCompetencyService(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForCoreDS());

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<String> getGradeCompetency(Integer studGradeId, String subjectCode) {

    GradeCompetencyCommand command =
        GradeCompetencyCommand.builder(userUUIDStr, studGradeId, subjectCode);
    List competencyList = gradeCompetencyService.calculateGradeCompetencies(command);
    return (competencyList != null) ? competencyList : null;
  }
}
