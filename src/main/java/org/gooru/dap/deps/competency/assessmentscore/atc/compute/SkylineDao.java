package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author mukul@gooru
 */
interface SkylineDao {

  @Mapper(CompetencyModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status from domain_competency_matrix dcm "
      + " INNER JOIN learner_profile_competency_status lps ON dcm.tx_comp_code=lps.gut_code and "
      + "lps.user_id = :user where dcm.tx_subject_code = :subjectCode order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyModel> fetchUserSkyline(@Bind("user") String user,
      @Bind("subjectCode") String subjectCode);

  @Mapper(CompetencyModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status from domain_competency_matrix dcm "
      + " LEFT JOIN learner_profile_competency_status lps ON dcm.tx_subject_code = lps.tx_subject_code and dcm.tx_comp_code=lps.gut_code and  "
      + "lps.user_id = :user where dcm.tx_subject_code = :subjectCode order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyModel> fetchUserDomainCompetencyStatus(@Bind("user") String user,
      @Bind("subjectCode") String subjectCode);

}
