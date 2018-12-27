package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.UUID;

/**
 * @author mukul@gooru
 */
public class GradeCompetencyCalculatorModel {
  private UUID userId;
  private Integer gradeId;
  // private UUID classId;
  private String subjectCode;

  public UUID getUserId() {
    return userId;
  }

  public Integer getGradeId() {
    return gradeId;
  }

  // public UUID getClassId() {
  // return classId;
  // }

  public String getSubjectCode() {
    return subjectCode;
  }

  public GradeCompetencyCalculatorModel(UUID userId, Integer gradeId, String subjectCode) {
    this.userId = userId;
    this.gradeId = gradeId;
    // this.classId = classId;
    // @Mukul - Hardcoded
    // this.subjectCode = "K12.MA";
    this.subjectCode = subjectCode;
  }

}
