package org.gooru.dap.deps.competency.assessmentscore.atc.compute.processor;

import java.util.List;

import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.CompetencyExtractor;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.GradeCompetencyCalculator;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.GradeCompetencyModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author mukul@gooru
 */
public class GradeCompetencyService {

    private final DBI dbiForDefaultDS;
    private final DBI dbiForCoreDS;    

    GradeCompetencyService (DBI dbiForCoreDS, DBI dbiForDefaultDS) {

        this.dbiForDefaultDS = dbiForDefaultDS;
        this.dbiForCoreDS = dbiForCoreDS;
    }

    List<String> calculateGradeCompetencies(GradeCompetencyCommand command) {
        GradeCompetencyCalculator gradeCompetencyCalculator = GradeCompetencyCalculator.build();
        GradeCompetencyModel gradeCompetencyModel =
        		gradeCompetencyCalculator.calculateGradeCompetency(command.gradeCompetencyCalculatorModel());
        if (gradeCompetencyModel != null) {
            List<String> competencyList = createCompetencyList(gradeCompetencyModel);        
            return competencyList;        	
        } else {
        	return null;
        }
    }

    private List<String> createCompetencyList(GradeCompetencyModel gradeCompetencyModel) {    	
    	List<String> competencyList = new CompetencyExtractor().getComptencyList(gradeCompetencyModel);    			
        return competencyList;
    }
}
