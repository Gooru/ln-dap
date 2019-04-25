
package org.gooru.dap.jobs.group.reports.timespent;

import java.util.List;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public interface CollectionPerformanceDao {


  @Mapper(CollectionPerformanceModelMapper.class)
  @SqlQuery("SELECT class_id, content_source, sum(timespent) as timespent FROM collection_performance WHERE class_id = ANY(:classIds) AND"
      + " collection_type = 'collection' AND content_source = :contentSource GROUP BY class_id, content_source")
  List<CollectionTimespentModel> fetchTimespentByClass(@Bind("classIds") PGArray<String> classIds,
      @Bind("contentSource") String contentSource);
}
