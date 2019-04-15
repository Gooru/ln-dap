
package org.gooru.dap.jobs.group.reports.performance;

import java.util.List;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public interface GroupPerformanceReportsDao {

  // Collection Timespent is Month on Month
  // Performance is cumulative

  @Mapper(AssessmentPerfByGroupModelMapper.class)
  @SqlQuery("SELECT AVG(assessment_performance) as performance, school_id as id FROM class_performance_data_reports"
      + " WHERE school_id = ANY(:schoolIds) GROUP BY school_id")
  List<AssessmentPerfByGroupModel> fetchAssessmentPerfBySchool(
      @Bind("schoolIds") PGArray<Long> schoolIds);

  @Mapper(AssessmentPerfByGroupModelMapper.class)
  @SqlQuery("SELECT AVG(assessment_performance) as performance, group_id as id FROM group_performance_data_reports WHERE group_id = ANY(:groupIds)"
      + " GROUP BY group_id")
  List<AssessmentPerfByGroupModel> fetchGroupLevelAssessmentPerf(
      @Bind("groupIds") PGArray<Long> groupIds);

  @SqlUpdate("INSERT INTO class_performance_data_reports(class_id, assessment_timespent, assessment_performance, school_id, state_id, country_id,"
      + " month, year, content_source, tenant) VALUES (:classId, :assessmentTimespent, :assessmentPerformance, :schoolId, :stateId, :countryId,"
      + " :month, :year, :contentSource, :tenant) ON CONFLICT (class_id, content_source, month, year) DO UPDATE SET assessment_timespent ="
      + " assessmentTimespent, assessment_performance = :assessmentPerformnace, updated_at = now()")
  void insertOrUpdateClassLevelAssessmentPerfAndTimespent(
      @BindBean ClassPerformanceDataReportsBean bean);


  // INSERT INTO userstat_resource_content_type_timespent_ts(activity_date, user_id, content_type,
  // time_spent) VALUES (:a, :b, :c :d) ON CONFLICT (user_id, content_type, activity_date) DO UPDATE
  // set time_spent = userstat_resource_content_type_timespent_ts.time_spent + excluded.time_spent;

  void insertOrUpdateClassLevelCollectionTimespent(@BindBean ClassPerformanceDataReportsBean bean);


  @SqlUpdate("INSERT INTO group_performance_data_reports(assessment_performance, group_id, school_id, state_id, country_id, month, year,"
      + " content_source, tenant) VALUES(:assessmentPerformance, :groupId, :schoolId, :stateId, :countryId, :month, :year, :contentSource, :tenant)"
      + " ON CONFLICT (group_id, content_source, month, year) DO UPDATE SET assessment_performance = :assessmentPerformance, updated_at = now()")
  void insertOrUpdateGroupLevelAssessmentPerformance(@BindBean GroupPerformanceDataReportBean bean);

  void insertOrUpdateGroupLevelCollectionTimspent();

}
