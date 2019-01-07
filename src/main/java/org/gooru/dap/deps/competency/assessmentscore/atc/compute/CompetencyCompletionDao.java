package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.util.List;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author mukul@gooru
 */
interface CompetencyCompletionDao {

  @Mapper(CompetencyModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status from domain_competency_matrix cm "
      + "left join learner_profile_competency_status ucm on  cm.tx_subject_code = ucm.tx_subject_code and "
      + "cm.tx_comp_code = ucm.gut_code and ucm.user_id = :user where "
      + "cm.tx_subject_code = :subjectCode and cm.tx_comp_code = ANY(:competencyCodes) order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyModel> fetchCompetencyCompletion(@Bind("user") String user,
      @Bind("subjectCode") String subjectCode,
      @Bind("competencyCodes") PGArray<String> competencyCodes);

}
