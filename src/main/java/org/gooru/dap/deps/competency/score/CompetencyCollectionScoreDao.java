package org.gooru.dap.deps.competency.score;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 04-May-2018
 */
public abstract class CompetencyCollectionScoreDao {

	@SqlUpdate("INSERT INTO competency_assessment_score(user_id, competency_code, competency_display_code, competency_title,"
			+ " framework_code, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id, collection_id, collection_path_id,"
			+ " collection_score, collection_type, created_at, updated_at) VALUES (:userId, :competencyCode, :competencyDisplayCode, :competencyTitle, :frameworkCode,"
			+ " :gutCode, :classId, :courseId, :unitId, :lessonId, :latestSessionId, :collectionId, :collectionPathId, :collectionScore,"
			+ " :collectionType, :createdAt, :updatedAt) ON CONFLICT (user_id, gut_code, course_id, unit_id, lesson_id, collection_id) DO UPDATE SET competency_code ="
			+ " :competencyCode, competency_display_code = :competencyDisplayCode, competency_title = :competencyTitle, framework_code ="
			+ " :frameworkCode, collection_path_id = :collectionPathId, collection_score = :collectionScore, updated_at = :updatedAt")
	protected abstract void insertOrUpdate(@BindBean CompetencyCollectionScoreBean bean);

}