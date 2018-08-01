package org.gooru.dap.deps.competency.common;

import java.util.List;

import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetencyMapper;
import org.gooru.dap.deps.competency.db.mapper.GutCode;
import org.gooru.dap.deps.competency.db.mapper.GutCodeMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author gooru on 08-May-2018
 */
public abstract class CompetencyAssessmentDao {

	@Mapper(AssessmentCompetencyMapper.class)
	@SqlQuery("SELECT taxonomy, gut_codes FROM collection WHERE id = :assessmentId::uuid AND is_deleted = false")
	protected abstract AssessmentCompetency getAssessmentCompetency(@Bind("assessmentId") String assessmentId);

	@Mapper(GutCodeMapper.class)
	@SqlQuery("SELECT source_taxonomy_code_id, target_taxonomy_code_id FROM taxonomy_code_mapping WHERE target_taxonomy_code_id"
			+ " = ANY(:taxonomyIds::text[])")
	protected abstract List<GutCode> fetchGutCodes(@Bind("taxonomyIds") String taxonomyIds);
	
	@SqlQuery("SELECT competency_gut_code FROM signature_items WHERE item_id = :assessmentId")
	protected abstract List<String> fetchSignatureAssessment(@Bind("assessmentId") String assessmentId);
}
