package org.gooru.dap.deps.competency.perf;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 04-May-2018
 */
public abstract class CompetencyCollectionPerformanceDao {

	@SqlUpdate("INSERT INTO competency_collection_performance(user_id, competency_code, competency_display_code, competency_title,"
			+ " framework_code, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id, collection_id, collection_path_id,"
			+ " collection_time_spent, collection_score, collection_type) VALUES (:userId, :competencyCode, :competencyDisplayCode,"
			+ " :competencyTitle, :frameworkCode, :gutCode, :classId, :courseId, :unitId, :lessonId, :latestSessionId, :collectionId,"
			+ " :collectionPathId, :collectionTimeSpent, :collectionScore, :collectionType)")
	protected abstract void insertCollectionPerformance(@BindBean CompetencyCollectionPerformanceCommand.CompetencyCollectionPerformanceBean bean);

}