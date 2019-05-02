
package org.gooru.dap.jobs.group.reports.competency;

import java.util.List;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public interface GroupCompetencyReportsDao {

  @Mapper(ClassCompetencyStatsModelMapper.class)
  @SqlQuery("SELECT SUM(completed) AS completed_count, SUM(in_progress) AS inprogress_count, class_id FROM user_class_competency_stats WHERE"
      + " class_id = ANY(:classIds) AND month = :month AND year = :year GROUP BY class_id")
  List<ClassCompetencyStatsModel> fetchClassCompletionsByMonthAndYear(
      @Bind("classIds") PGArray<String> classIds, @Bind("month") Integer month,
      @Bind("year") Integer year);

  @Mapper(GroupCompetencyStatsModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_count, SUM(inprogress_count) AS inprogress_count, SUM(cumulative_completed_count) AS"
      + " cumulative_completed_count, school_id as id FROM class_competency_data_reports WHERE school_id = ANY(:schoolIds::bigint[]) AND"
      + " month = :month AND year = :year GROUP BY school_id")
  List<GroupCompetencyStatsModel> fetchCompetencyCompletionsBySchool(
      @Bind("schoolIds") String schoolIds, @Bind("month") Integer month,
      @Bind("year") Integer year);

  @SqlQuery("SELECT SUM(completed_count) AS completed_count, SUM(inprogress_count) AS inprogress_count, SUM(cumulative_completed_count) AS"
      + " cumulative_completed_count, group_id as id FROM group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND month = :month"
      + " AND year = :year GROUP BY group_id")
  List<GroupCompetencyStatsModel> fetchCompetencyCompletionsByGroup(
      @Bind("groupIds") String groupIds, @Bind("month") Integer month, @Bind("year") Integer year);

  @SqlUpdate("INSERT INTO class_competency_data_reports(class_id, completed_count, inprogress_count, cumulative_completed_count, school_id, state_id,"
      + " country_id, month, year, tenant) VALUES (:class_id, :completedCount, :inprogressCount, :cumulativeCompletedCount, :schoolId, :stateId,"
      + " :countryId, :month, :year, :tenant) ON CONFLICT (class_id, month, year) DO UPDATE class_id = :classId, completed_count = :completedCount,"
      + " inprogress_count = :inprogressCount, cumulative_completed_count = :cumulativeCompletedCount, updated_at = now()")
  void insertOrUpdateClassCompetencyDataReport(@BindBean ClassCompetencyDataReportsBean bean);

  @SqlUpdate("INSERT INTO group_competency_data_reports(group_id, completed_count, inprogress_count, cumulative_completed_count, school_id, state_id,"
      + " country_id, month, year, tenant) VALUES (:groupId, :completedCount, :inprogressCount, :cumulativeCompletedCount, :schoolId, :stateId,"
      + " :countryId, :month, :year, :tenant) ON CONFLICT (group_id, month, year) DO UPDATE group_id = :groupId, completed_count = :completedCount,"
      + " inprogress_count = :inprogressCount, cumulative_completed_count = :cumulativeCompletedCount, updated_at = now()")
  void insertOrUpdateGroupCompetencyDataReport(@BindBean GroupCompetencyDataReportsBean bean);
}
