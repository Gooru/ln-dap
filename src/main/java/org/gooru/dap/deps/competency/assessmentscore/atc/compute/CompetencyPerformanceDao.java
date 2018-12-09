package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;


/**
 * @author mukul@gooru
 */
interface CompetencyPerformanceDao {
	   
	@SqlQuery("select AVG(compPerf.scoreInPercentage) AS scoreInPercentage FROM (SELECT DISTINCT ON (gut_code) FIRST_VALUE(collection_score) "
			+ "OVER (PARTITION BY gut_code ORDER BY updated_at desc) AS scoreInPercentage, collection_id, gut_code "
			+ "FROM learner_profile_competency_evidence WHERE gut_code = ANY(:competencyCodes) "
			+ "AND user_id = :user AND collection_type = 'assessment' ORDER BY gut_code, updated_at DESC) AS compPerf")
	Double fetchGradeCompetencyPerformance (@Bind("user") String user, 
			@Bind("competencyCodes") PGArray<String> competencyCodes);

}
