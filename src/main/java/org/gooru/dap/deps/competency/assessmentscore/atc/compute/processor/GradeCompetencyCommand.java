package org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor;

import java.util.UUID;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.GradeCompetencyCalculatorModel;

/**
 * @author mukul@gooru
 */
public class GradeCompetencyCommand {

  private Integer gradeId;
  private UUID userId;
  private String subjectCode;

  public Integer getGradeId() {
    return gradeId;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public static GradeCompetencyCommand builder(String userId, Integer gradeId, String subjectCode) {
    GradeCompetencyCommand command = new GradeCompetencyCommand();

    command.userId = UUID.fromString(userId);
    command.gradeId = gradeId;
    command.subjectCode = subjectCode;

    return command;
  }

  GradeCompetencyCalculatorModel gradeCompetencyCalculatorModel() {
    return new GradeCompetencyCalculatorModel(userId, gradeId, subjectCode);
  }

  public static final class CommandAttributes {

    static final String GRADE_ID = "gradeId";
    static final String USER_ID = "userId";
    static final String SUBJECT_CODE = "subjectCode";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }
}
