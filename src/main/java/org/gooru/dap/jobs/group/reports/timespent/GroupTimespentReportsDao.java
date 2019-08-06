
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public interface GroupTimespentReportsDao {

  @Mapper(CollectionTimespentByGroupModelMapper.class)
  @SqlQuery("SELECT SUM(collection_timespent) as timespent, school_id as id FROM class_performance_data_reports"
      + " WHERE school_id = ANY(:schoolIds::bigint[]) AND month = :month AND year = :year GROUP BY school_id")
  List<CollectionTimespentByGroupModel> fetchCollectionTimespentBySchool(
      @Bind("schoolIds") String schoolIds, @Bind("month") Integer month,
      @Bind("year") Integer year);

  @Mapper(CollectionTimespentByGroupModelMapper.class)
  @SqlQuery("SELECT SUM(collection_timespent) as timespent, group_id as id FROM group_performance_data_reports"
      + " WHERE group_id = ANY(:groupIds::bigint[]) AND month = :month AND year = :year GROUP BY group_id")
  List<CollectionTimespentByGroupModel> fetchGroupLevelCollectionTimespent(
      @Bind("groupIds") String groupIds, @Bind("month") Integer month, @Bind("year") Integer year);

  @SqlUpdate("INSERT INTO class_performance_data_reports(class_id, collection_timespent, school_id, state_id, country_id, month, year,"
      + " content_source, tenant) VALUES (:classId, :collectionTimespent, :schoolId, :stateId, :countryId, :month, :year, :contentSource, :tenant)"
      + " ON CONFLICT (class_id, content_source, month, year) DO UPDATE SET collection_timespent = :collectionTimespent, updated_at = now()")
  void insertOrUpdateClassLevelCollectionTimespent(@BindBean ClassTimespentDataReportBean bean);

  @SqlUpdate("INSERT INTO group_performance_data_reports(collection_timespent, group_id, school_id, state_id, country_id, month, year,"
      + " content_source, tenant) VALUES(:collectionTimespent, :groupId, :schoolId, :stateId, :countryId, :month, :year, :contentSource, :tenant)"
      + " ON CONFLICT (group_id, content_source, month, year) DO UPDATE SET collection_timespent = :collectionTimespent, updated_at = now()")
  void insertOrUpdateGroupLevelCollectionTimspent(@BindBean GroupTimespentDataReportBean bean);
}
