package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
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
	protected abstract void insertOrUpdateLearnerProfileCompetencyEvidence(
			@BindBean LearnerProfileCompetencyEvidenceBean bean);

	@SqlUpdate("INSERT INTO learner_profile_micro_competency_evidence(user_id, micro_competency_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
			+ " collection_id, collection_path_id, collection_score, collection_type, created_at, updated_at) VALUES (:userId, :microCompetencyCode, :classId,"
			+ " :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :createdAt,"
			+ " :updatedAt) ON CONFLICT (user_id, micro_competency_code, collection_id) DO UPDATE SET latest_session_id = :latestSessionId, class_id = :classId,"
			+ " course_id = :courseId, unit_id = :unitId, lesson_id = :lessonId, collection_path_id = :collectionPathId, collection_score ="
			+ " :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileMicroCompetencyEvidence(
			@BindBean LearnerProfileMicroCompetencyEvidenceBean bean);

	/*
	 * Need to maintain the evidence for in progress as well
	 */
	@SqlUpdate("INSERT INTO learner_profile_competency_evidence_ts(user_id, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
			+ " collection_id, collection_path_id, collection_score, collection_type, status, created_at, updated_at) VALUES (:userId, :gutCode, :classId,"
			+ " :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :status, :createdAt,"
			+ " :updatedAt) ON CONFLICT (user_id, gut_code, collection_id, status) DO UPDATE SET latest_session_id = :latestSessionId,"
			+ " collection_path_id = :collectionPathId, collection_score = :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileCompetencyEvidenceTS(
			@BindBean LearnerProfileCompetencyEvidenceBean bean);

	@SqlUpdate("INSERT INTO learner_profile_micro_competency_evidence_ts(user_id, micro_competency_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
			+ " collection_id, collection_path_id, collection_score, collection_type, status, created_at, updated_at) VALUES (:userId, :microCompetencyCode, :classId,"
			+ " :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore, :collectionType, :status, :createdAt,"
			+ " :updatedAt) ON CONFLICT (user_id, micro_competency_code, collection_id, status) DO UPDATE SET latest_session_id = :latestSessionId,"
			+ " collection_path_id = :collectionPathId, collection_score = :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(
			@BindBean LearnerProfileMicroCompetencyEvidenceBean bean);

	// Get the score if there is already evidence
	@SqlQuery("SELECT collection_score FROM learner_profile_competency_evidence WHERE user_id = :userId AND gut_code = :gutCode")
	protected abstract Double getScoreForCompetency(@BindBean LearnerProfileCompetencyEvidenceBean bean);

	@SqlQuery("SELECT collection_score FROM learner_profile_micro_competency_evidence WHERE user_id = :userId AND micro_competency_code = :microCompetencyCode")
	protected abstract Double getScoreForMicroCompetency(@BindBean LearnerProfileMicroCompetencyEvidenceBean bean);

	// Query to check of competency is already COMPLETED or MASTERED
	@SqlQuery("SELECT EXISTS (SELECT 1 FROM learner_profile_competency_evidence_ts WHERE user_id = :userId AND gut_code = :gutCode AND status IN (4,5))")
	protected abstract boolean checkIfCompetencyIsCompletedOrMastered(
			@BindBean LearnerProfileCompetencyEvidenceBean bean);

	@SqlQuery("SELECT EXISTS (SELECT 1 FROM learner_profile_micro_competency_evidence_ts WHERE user_id = :userId AND micro_competency_code = :microCompetencyCode AND status IN (4,5))")
	protected abstract boolean checkIfMicroCompetencyIsCompletedOrMastered(
			@BindBean LearnerProfileMicroCompetencyEvidenceBean bean);
}
