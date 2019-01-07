package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public interface AtcCompute {

    CompetencyStatsModel compute(AtcEvent scoreObject);
    
    public static final Logger LOGGER = LoggerFactory.getLogger(AtcCompute.class);
    
    static AtcCompute createInstance(AtcEvent scoreObject) {
    	if (scoreObject.getGradeId() != null && scoreObject.getGradeId() > 0) {
    		LOGGER.info("Computing Grade based Competency Stats for grade " + scoreObject.getGradeId());
    		return new AtcComputeGradeBased(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForCoreDS());    		
    	} else {
    		LOGGER.info("Computing Competency Stats based on User's Learner Profile");
    		return new AtcComputeSkylineBased(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForCoreDS());
    	}        
    }
}
