package org.gooru.dap.deps.competency.timespent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author gooru on 04-May-2018
 */
public abstract class CompetencyCollectionTimeSpentDao {

	@SqlUpdate("INSERT INTO competency_collection_timespent(user_id, competency_code, competency_display_code, competency_title, framework_code,"
			+ " gut_code, class_id, course_id, unit_id, lesson_id, collection_id, collection_path_id, collection_time_spent, collection_type)"
			+ " VALUES(:userId, :competencyCode, :competencyDisplayCode, :competencyTitle, :frameworkCode, :gutCode, :classId, :courseId, :unitId,"
			+ " :lessonId, :collectionId, :collectionPathId, :collectionTimeSpent, :collectionType)")
	protected abstract void insertCollectionTimeSpent(
			@BindBean CompetencyCollectionTimeSpentCommand.CompetencyCollectionTimeSpentBean bean);

	@SqlUpdate("INSERT INTO competency_collection_timespent(user_id, competency_code, competency_display_code, competency_title, framework_code,"
			+ " gut_code, class_id, course_id, unit_id, lesson_id, collection_id, collection_path_id, collection_time_spent, collection_type)"
			+ " VALUES(:userId, :competencyCode, :competencyDisplayCode, :competencyTitle, :frameworkCode, :gutCode, :classId, :courseId, :unitId,"
			+ " :lessonId, :collectionId, :collectionPathId, :collectionTimeSpent, :collectionType) ON CONFLICT (user_id, gut_code, course_id,"
			+ " unit_id, lesson_id, collection_id) DO UPDATE SET competency_code = :competencyCode, competency_display_code = :competencyDisplayCode,"
			+ " competency_title = :competencyTitle, framework_code = :frameworkCode, collection_path_id = :collectionPathId, collection_time_spent"
			+ " = :collectionTimeSpent")
	protected abstract void insertOrUpdate(
			@BindBean CompetencyCollectionTimeSpentCommand.CompetencyCollectionTimeSpentBean bean);

}
