package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.List;
import java.util.UUID;

import org.gooru.dap.components.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */
interface CompetencyFetcher {
    List<String> fetchCompetenciesForGrade(Integer gradeId, String subjectCode);

    static CompetencyFetcher build() {
    	
    	return new CompetencyFetcherImpl(DBICreator.getDbiForCoreDS());
        //return new CompetencyFetcherImpl(DBICreator.getDbiForDefaultDS());
    }

    static CompetencyFetcher build(DBI dbi) {
        return new CompetencyFetcherImpl(dbi);
    }
}
