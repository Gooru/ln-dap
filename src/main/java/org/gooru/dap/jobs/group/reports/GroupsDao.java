
package org.gooru.dap.jobs.group.reports;

import java.util.List;
import java.util.Set;
import org.gooru.dap.components.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 15-Apr-2019
 */
public interface GroupsDao {
  @Mapper(ClassSchoolMappingModelMapper.class)
  @SqlQuery("SELECT school_id, class_id FROM school_class_mapping WHERE class_id = ANY(:classIds)")
  List<ClassSchoolMappingModel> fetchClassSchoolMapping(@Bind("classIds") PGArray<String> classIds);

  @Mapper(SchoolGroupMappingModelMapper.class)
  @SqlQuery("SELECT group_id, school_id FROM group_school_mapping WHERE school_id = ANY(:schoolIds::bigint[])")
  List<SchoolGroupMappingModel> fetchSchoolGroupMapping(@Bind("schoolIds") String schoolIds);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, type, sub_type, parent_id, state_id, country_id, tenant FROM groups WHERE id = ANY(:groupIds::bigint[])")
  List<GroupModel> fetchGroupsByIds(@Bind("groupIds") String groupIds);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, type, sub_type, parent_id, state_id, country_id, tenant FROM groups WHERE id = :groupId")
  GroupModel fetchGroupById(@Bind("groupId") Long groupId);

  @SqlQuery("SELECT school_id FROM group_school_mapping WHERE group_id = :groupId")
  Set<Long> fetchAllSchoolsOfGroup(@Bind("groupId") Long groupId);

  @SqlQuery("SELECT id FROM groups WHERE parent_id = :groupId")
  Set<Long> fetchGroupChilds(@Bind("groupId") Long groupId);
}
