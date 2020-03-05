package org.gooru.dap.deps.competency.preprocessors;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author mukul@gooru
 */
interface DCAContentDao {

  @Mapper(DCAContentModelMapper.class)
  @SqlQuery("SELECT class_id, content_id, content_type, allow_mastery_accrual FROM class_contents WHERE id = :id")
  DCAContentModel fetchDCAContentId(@Bind("id") Integer id);

}
