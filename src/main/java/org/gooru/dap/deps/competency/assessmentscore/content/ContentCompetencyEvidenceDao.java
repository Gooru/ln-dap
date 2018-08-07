package org.gooru.dap.deps.competency.assessmentscore.content;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 14-May-2018
 */
public abstract class ContentCompetencyEvidenceDao {

	@SqlUpdate("INSERT INTO content_competency_evidence(user_id, competency_code, framework_code, is_micro_competency, gut_code, class_id, course_id,"
			+ " unit_id, lesson_id, latest_session_id, collection_id, collection_path_id, collection_score, collection_type, created_at, updated_at)"
			+ " VALUES (:userId, :competencyCode, :frameworkCode, :microCompetency, :gutCode, :classId, :courseId, :unitId, :lessonId,"
			+ " :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :createdAt, :updatedAt) ON CONFLICT (user_id,"
			+ " competency_code, collection_id) DO UPDATE SET latest_session_id = :latestSessionId, class_id = :classId, course_id = :courseId,"
			+ " unit_id = :unitId, lesson_id = :lessonId, collection_path_id = :collectionPathId, collection_score = :collectionScore, updated_at"
			+ " = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileCompetencyEvidence(
			@BindBean ContentCompetencyEvidenceBean bean);

	// Get the score if there is already evidence
	@SqlQuery("SELECT collection_score FROM content_competency_evidence WHERE user_id = :userId AND competency_code = :competencyCode")
	protected abstract Double getCompetencyScore(@BindBean ContentCompetencyEvidenceBean bean);

	@SqlUpdate("INSERT INTO content_competency_evidence_ts(user_id, competency_code, framework_code, is_micro_competency, gut_code, class_id, course_id,"
			+ " unit_id, lesson_id, latest_session_id, collection_id, collection_path_id, collection_score, collection_type, status, created_at, updated_at)"
			+ " VALUES (:userId, :competencyCode, :frameworkCode, :microCompetency, :gutCode, :classId, :courseId, :unitId, :lessonId,"
			+ " :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :status, :createdAt, :updatedAt) ON CONFLICT (user_id,"
			+ " competency_code, collection_id, status) DO UPDATE SET latest_session_id = :latestSessionId, collection_path_id = :collectionPathId,"
			+ " collection_score = :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileCompetencyEvidenceTS(
			@BindBean ContentCompetencyEvidenceBean bean);

	// Query to check of competency is already COMPLETED
	@SqlQuery("SELECT EXISTS (SELECT 1 FROM content_competency_evidence_ts WHERE user_id = :userId AND competency_code = :competencyCode AND status = 4)")
	protected abstract boolean checkIfCompetencyIsCompleted(@BindBean ContentCompetencyEvidenceBean bean);
}
