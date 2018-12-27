package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import org.gooru.dap.components.jdbi.DBICreator;

public interface GradeCompetencyCalculator {

    GradeCompetencyModel calculateGradeCompetency(GradeCompetencyCalculatorModel model);
    
    static GradeCompetencyCalculator build() {
        return new GradeCompetencyCalculatorService(DBICreator.getDbiForCoreDS(), DBICreator.getDbiForDefaultDS());
    }
}
