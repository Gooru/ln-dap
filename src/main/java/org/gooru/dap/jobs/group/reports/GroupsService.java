
package org.gooru.dap.jobs.group.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 15-Apr-2019
 */
public class GroupsService {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupsService.class);

  private final GroupsDao dao;

  public GroupsService(DBI dbi) {
    this.dao = dbi.onDemand(GroupsDao.class);
  }

  public List<GroupModel> fetchGroupHierarchy(Long parentGroupId) {
    List<GroupModel> groupModels = new ArrayList<>();

    while (true) {
      if (parentGroupId == null) {
        LOGGER.debug("no further parent found, breaking loop");
        break;
      }
      GroupModel model = fetchGroupById(parentGroupId);
      if (model != null) {
        LOGGER.debug("non null group model for id '{}'", parentGroupId);
        groupModels.add(model);
        parentGroupId = model.getParentId();
      } else {
        LOGGER.debug("null group model for id '{}'", parentGroupId);
        parentGroupId = null;
      }
    }

    return groupModels;
  }
  
  public List<ClassModel> fetchClassDetails(List<String> distinctClassIds) {
    return this.dao.fetchClassDetails(CollectionUtils.convertToSqlArrayOfUUID(distinctClassIds));
  }

  public Map<String, Long> fetchClassSchoolMapping(List<String> distinctClassIds) {
    // Fetch Class to School mapping
    List<ClassSchoolMappingModel> schools = this.dao
        .fetchClassSchoolMapping(CollectionUtils.convertToSqlArrayOfUUID(distinctClassIds));
    LOGGER.debug("# schools returned {}", schools.size());
    Map<String, Long> resultMap = new HashMap<>();
    schools.forEach(school -> {
      resultMap.put(school.getClassId(), school.getSchoolId());
    });

    LOGGER.debug("map size while returning {}", resultMap.size());
    return resultMap;
  }

  // Fetch School to Group Mapping
  public Map<Long, Long> fetchSchoolGroupMapping(Set<Long> schoolIds) {
    LOGGER.debug("fetching group mapping for schools:{}", schoolIds.size());
    Map<Long, Long> resultMap = new HashMap<>();
    List<SchoolGroupMappingModel> groups =
        this.dao.fetchSchoolGroupMapping(CollectionUtils.longSetToPGArrayOfString(schoolIds));
    groups.forEach(group -> {
      resultMap.put(group.getSchoolId(), group.getGroupId());
    });

    return resultMap;
  }

  // Fetch details of the groups
  public Map<Long, GroupModel> fetchGroupsByIds(Set<Long> groupIds) {
    Map<Long, GroupModel> resultMap = new HashMap<>();
    List<GroupModel> groupModels =
        this.dao.fetchGroupsByIds(CollectionUtils.longSetToPGArrayOfString(groupIds));
    groupModels.forEach(group -> {
      resultMap.put(group.getId(), group);
    });

    return resultMap;
  }

  public GroupModel fetchGroupById(Long groupId) {
    return this.dao.fetchGroupById(groupId);
  }

  public Set<Long> fetchAllSchoolsOfGroup(Long groupId) {
    return this.dao.fetchAllSchoolsOfGroup(groupId);
  }

  public Set<Long> fetchGroupChilds(Long groupId) {
    return this.dao.fetchGroupChilds(groupId);
  }

}
