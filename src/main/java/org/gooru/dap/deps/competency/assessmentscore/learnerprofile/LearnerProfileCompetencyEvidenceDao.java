package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 14-May-2018
 */
public abstract class LearnerProfileCompetencyEvidenceDao {
	
	@SqlUpdate("INSERT INTO learner_profile_competency_evidence(user_id, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
			+ " collection_id, collection_path_id, collection_score, collection_type, created_at, updated_at) VALUES (:userId, :gutCode, :classId,"
			+ " :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :createdAt,"
			+ " :updatedAt) ON CONFLICT (user_id, gut_code, collection_id) DO UPDATE SET latest_session_id = :latestSessionId, class_id = :classId,"
			+ " course_id = :courseId, unit_id = :unitId, lesson_id = :lessonId, collection_path_id = :collectionPathId, collection_score ="
			+ " :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileCompetencyEvidence(@BindBean LearnerProfileCompetencyEvidenceBean bean);
	
	@SqlUpdate("INSERT INTO learner_profile_micro_competency_evidence(user_id, micro_competency_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
			+ " collection_id, collection_path_id, collection_score, collection_type, created_at, updated_at) VALUES (:userId, :microCompetencyCode, :classId,"
			+ " :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :createdAt,"
			+ " :updatedAt) ON CONFLICT (user_id, micro_competency_code, collection_id) DO UPDATE SET latest_session_id = :latestSessionId, class_id = :classId,"
			+ " course_id = :courseId, unit_id = :unitId, lesson_id = :lessonId, collection_path_id = :collectionPathId, collection_score ="
			+ " :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileMicroCompetencyEvidence(@BindBean LearnerProfileMicroCompetencyEvidenceBean bean);
}
