
package org.gooru.dap.jobs.group.reports.performance;

import java.util.List;
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
      + " WHERE school_id = ANY(:schoolIds::bigint[]) AND month = :month AND year = :year GROUP BY school_id")
  List<AssessmentPerfByGroupModel> fetchAssessmentPerfBySchool(@Bind("schoolIds") String schoolIds,
      @Bind("month") Integer month, @Bind("year") Integer year);

  @Mapper(AssessmentPerfByGroupModelMapper.class)
  @SqlQuery("SELECT AVG(assessment_performance) as performance, group_id as id FROM group_performance_data_reports"
      + " WHERE group_id = ANY(:groupIds::bigint[]) AND month = :month AND year = :year GROUP BY group_id")
  List<AssessmentPerfByGroupModel> fetchGroupLevelAssessmentPerf(@Bind("groupIds") String groupIds,
      @Bind("month") Integer month, @Bind("year") Integer year);

  @SqlUpdate("INSERT INTO class_performance_data_reports(class_id, assessment_timespent, assessment_performance, school_id, state_id, country_id,"
      + " week, month, year, content_source, tenant, subject, framework) VALUES (:classId, :assessmentTimespent, :assessmentPerformance, :schoolId,"
      + " :stateId, :countryId, :week, :month, :year, :contentSource, :tenant, :subject, :framework) ON CONFLICT (class_id, content_source, week,"
      + " month, year) DO UPDATE SET assessment_timespent = :assessmentTimespent, assessment_performance = :assessmentPerformance, updated_at = now()")
  void insertOrUpdateClassLevelAssessmentPerfAndTimespent(
      @BindBean ClassPerformanceDataReportsBean bean);

  @SqlUpdate("INSERT INTO group_performance_data_reports(assessment_performance, group_id, school_id, state_id, country_id, week, month, year,"
      + " content_source, tenant, subject, framework) VALUES(:assessmentPerformance, :groupId, :schoolId, :stateId, :countryId, :week, :month, :year,"
      + " :contentSource, :tenant, :subject, :framework) ON CONFLICT (group_id, content_source, week, month, year) DO UPDATE SET"
      + " assessment_performance = :assessmentPerformance, updated_at = now()")
  void insertOrUpdateGroupLevelAssessmentPerformance(@BindBean GroupPerformanceDataReportBean bean);

}
