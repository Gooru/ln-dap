package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;


/**
 * @author mukul@gooru
 */
interface CompetencyFetcherDao {

  @SqlQuery("select highline_tx_comp_code from grade_competency_bound where grade_id = :gradeId AND tx_subject_code = :subjectCode")
  List<String> findCompetenciesForGrade(@Bind("gradeId") Integer gradeId, @Bind("subjectCode") String subjectCode);

}
