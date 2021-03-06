
package org.gooru.dap.jobs.group.reports.timespent;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.GroupConstants;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueService;
import org.gooru.dap.jobs.group.reports.ClassModel;
import org.gooru.dap.jobs.group.reports.GroupModel;
import org.gooru.dap.jobs.group.reports.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class GroupTimespentReportsProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupTimespentReportsProcessor.class);
  private final List<CollectionTimespentModel> timespentData;

  private final GroupsService groupsService = new GroupsService(DBICreator.getDbiForCoreDS());
  private final GroupTimespentReportsService reportService =
      new GroupTimespentReportsService(DBICreator.getDbiForDefaultDS());
  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  public GroupTimespentReportsProcessor(List<CollectionTimespentModel> allModels) {
    this.timespentData = allModels;
  }

  public void process() {
    try {
      LOGGER.debug("## timespent report processing started ##");
      // Find the list of distinct classes
      List<String> distinctClassIds = new ArrayList<>(timespentData.size());
      this.timespentData.forEach(usage -> {
        distinctClassIds.add(usage.getClassId());
        LOGGER.debug("class id added:'{}'", usage.getClassId());
      });
      LOGGER.debug("number of distinct classes to process {}", distinctClassIds.size());

      // Fetch class to school mapping
      // Here the assumption is that the classes are mapped with the schools. We are inserting only
      // those classes in the queue for the processing which are mapped to school.
      Map<String, Long> classSchoolMap =
          this.groupsService.fetchClassSchoolMapping(distinctClassIds);
      Set<Long> schoolIds = classSchoolMap.values().stream().collect(Collectors.toSet());
      LOGGER.debug("number of schools returned {}", schoolIds.size());

      // Fetch school to group mapping
      Map<Long, Long> schoolGroupMap = this.groupsService.fetchSchoolGroupMapping(schoolIds);
      Set<Long> groupIds = schoolGroupMap.values().stream().collect(Collectors.toSet());
      LOGGER.debug("number of groups returned {}", groupIds.size());

      // Fetch details of all groups mapped with schools above
      Map<Long, GroupModel> groupsMap = this.groupsService.fetchGroupsByIds(groupIds);
      
      // Fetch class details from core and prepare map for further use
      List<ClassModel> classDetails = this.groupsService.fetchClassDetails(distinctClassIds);
      Map<String, ClassModel> classDetailsMap = new HashMap<>();
      classDetails.forEach(model -> {
        classDetailsMap.put(model.getId(), model);
      });
      
      LOGGER.debug(
          "class, school and group mapping is fetched, now timespent data processing started");
      for (CollectionTimespentModel model : timespentData) {
        String classId = model.getClassId();
        Long schoolId = classSchoolMap.get(classId);
        ClassModel classModel = classDetailsMap.get(classId);
        if (schoolId == null) {
          // If the school id does not present for the given class then the class is not yet
          // grouped.
          // We can skip and move ahead
          LOGGER.warn("class '{}' is not grouped under school, persisting class level data", classId);
          
          // Even if there is no school and groups mapped with the class, at least persist the class
          // level time spent data.
          ClassTimespentDataReportBean clsbean =
              createClassTSDataReportBean(model, null, null, classModel);
          processClassLevelCollectionTS(clsbean);
          updateQueueStatusToCompleted(model);
          continue;
        }

        Long groupId = schoolGroupMap.get(schoolId);
        if (groupId == null) {
          // If the group id is null, then the schools has not been grouped, then still persist the
          // class level data and return
          LOGGER.debug("school '{}' is not associated with any group, persisting class level data", schoolId);
          ClassTimespentDataReportBean clsbean =
              createClassTSDataReportBean(model, schoolId, null, classModel);
          processClassLevelCollectionTS(clsbean);
          updateQueueStatusToCompleted(model);
          continue;
        }

        GroupModel group = groupsMap.get(groupId);
        ClassTimespentDataReportBean clsbean =
            createClassTSDataReportBean(model, schoolId, group, classModel);

        // persist the class and group level data.
        LOGGER.debug("persisting timespent at class and group level");
        processClassLevelCollectionTS(clsbean);
        processGroupLevelCollectionTS(clsbean);

        LOGGER.debug("timespent data has been persisted");

        // Update the status of the queue record to complete for the queue cleanup
        updateQueueStatusToCompleted(model);
      }

    } catch (Throwable t) {
      LOGGER.error("job execution has been intrupted by the error", t);
    }
  }

  private ClassTimespentDataReportBean createClassTSDataReportBean(CollectionTimespentModel tsData,
      Long schoolId, GroupModel group, ClassModel classModel) {
    ClassTimespentDataReportBean bean = new ClassTimespentDataReportBean();
    bean.setClassId(tsData.getClassId());
    bean.setCollectionTimespent(tsData.getTimespent());

    bean.setSchoolId(schoolId);
    bean.setContentSource(tsData.getContentSource());

    bean.setSubject(classModel.getSubject());
    bean.setFramework(classModel.getFramework());
    
    // Set current month and year values
    LocalDate now = LocalDate.now();
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    bean.setWeek(now.get(weekFields.weekOfWeekBasedYear()));
    bean.setMonth(now.getMonthValue());
    bean.setYear(now.getYear());

    if (group != null) {
      bean.setStateId(group.getStateId());
      bean.setCountryId(group.getCountryId());
      bean.setTenant(group.getTenant());
      
      // If the group type is school district, then set the group id as school district id in bean and
      // return. Otherwise if its cluster we need to fetch the parent hierarchy and set the block and
      // district id's accordingly.
      // Here we need to fetch the group hierarchy and set the appropriate id's to be used in further
      // processing of the report generation
      if (group.getSubType().equalsIgnoreCase(GroupConstants.GROUP_SUBTYPE_SCHOOL_DISTRICT)) {
        bean.setSchoolDistrictId(group.getId());
      } else if (group.getSubType().equalsIgnoreCase(GroupConstants.GROUP_SUBTYPE_CLUSTER)) {
        bean.setClusterId(group.getId());
        List<GroupModel> groupModels = this.groupsService.fetchGroupHierarchy(group.getParentId());
        groupModels.forEach(groupModel -> {
          if (groupModel.getSubType().equalsIgnoreCase(GroupConstants.GROUP_SUBTYPE_BLOCK)) {
            bean.setBlockId(groupModel.getId());
          } else if (groupModel.getSubType()
              .equalsIgnoreCase(GroupConstants.GROUP_SUBTYPE_DISTRICT)) {
            bean.setDistrictId(groupModel.getId());
          }
        });
      }
    }
    return bean;
  }

  private void processClassLevelCollectionTS(ClassTimespentDataReportBean clsbean) {
    this.reportService.insertOrUpdateClassLevelCollectionTimespent(clsbean);
  }

  private void processGroupLevelCollectionTS(ClassTimespentDataReportBean bean) {
    if (bean.getSchoolDistrictId() != null) {
      // If the group type is school district then compute the collection timespent by SD id
      computeAndPersistSchoolDistrictTSData(bean);
    } else {
      computeAndPersistClusterTSData(bean);
      computeAndPersistBlockTSData(bean);
      computeAndPersistDistrictTSData(bean);
    }
    LOGGER.debug("group level collection timespent are saved");
  }

  private void computeAndPersistDistrictTSData(ClassTimespentDataReportBean bean) {
    Set<Long> districtChildIds = this.groupsService.fetchGroupChilds(bean.getDistrictId());
    List<CollectionTimespentByGroupModel> tsByBlock = this.reportService
        .fetchGroupLevelCollectionTimespent(districtChildIds, bean.getMonth(), bean.getYear());
    Long ts = computeCollectionTimespent(tsByBlock);
    GroupTimespentDataReportBean groupBean =
        createGroupTSDataReportBean(bean, bean.getDistrictId(), ts);
    this.reportService.insertOrUpdateGroupLevelCollectionTimspent(groupBean);
    LOGGER.debug("district '{}' timespent has been saved", bean.getDistrictId());
  }

  private void computeAndPersistBlockTSData(ClassTimespentDataReportBean bean) {
    Set<Long> blockChildIds = this.groupsService.fetchGroupChilds(bean.getBlockId());
    List<CollectionTimespentByGroupModel> tsByBlock = this.reportService
        .fetchGroupLevelCollectionTimespent(blockChildIds, bean.getMonth(), bean.getYear());
    Long ts = computeCollectionTimespent(tsByBlock);
    GroupTimespentDataReportBean groupBean =
        createGroupTSDataReportBean(bean, bean.getBlockId(), ts);
    this.reportService.insertOrUpdateGroupLevelCollectionTimspent(groupBean);
    LOGGER.debug("block '{}' timespent has been saved", bean.getBlockId());
  }

  private void computeAndPersistClusterTSData(ClassTimespentDataReportBean bean) {
    Long ts =
        computeCollectionTimespentBySchool(bean.getClusterId(), bean.getMonth(), bean.getYear());
    GroupTimespentDataReportBean groupBean =
        createGroupTSDataReportBean(bean, bean.getClusterId(), ts);
    this.reportService.insertOrUpdateGroupLevelCollectionTimspent(groupBean);
    LOGGER.debug("cluster '{}' timespent has been saved", bean.getClusterId());
  }

  private void computeAndPersistSchoolDistrictTSData(ClassTimespentDataReportBean bean) {
    Long ts = computeCollectionTimespentBySchool(bean.getSchoolDistrictId(), bean.getMonth(),
        bean.getYear());
    GroupTimespentDataReportBean groupBean =
        createGroupTSDataReportBean(bean, bean.getSchoolDistrictId(), ts);
    this.reportService.insertOrUpdateGroupLevelCollectionTimspent(groupBean);
    LOGGER.debug("school district '{}' timespent has been saved", bean.getSchoolDistrictId());
  }

  private GroupTimespentDataReportBean createGroupTSDataReportBean(
      ClassTimespentDataReportBean clsLevelBean, Long groupId, Long collectionTimespent) {
    GroupTimespentDataReportBean bean = new GroupTimespentDataReportBean();

    bean.setContentSource(clsLevelBean.getContentSource());
    bean.setSchoolId(clsLevelBean.getSchoolId());
    bean.setStateId(clsLevelBean.getStateId());
    bean.setCountryId(clsLevelBean.getCountryId());
    bean.setSubject(clsLevelBean.getSubject());
    bean.setFramework(clsLevelBean.getFramework());
    
    // As this is a generic pojo used for all type of groups, this group id will be of type which is
    // passed from the caller of the method. It can be School District, District and so on.
    bean.setGroupId(groupId);

    bean.setCollectionTimespent(collectionTimespent);

    // Set current month and year values
    bean.setWeek(clsLevelBean.getWeek());
    bean.setMonth(clsLevelBean.getMonth());
    bean.setYear(clsLevelBean.getYear());

    bean.setTenant(clsLevelBean.getTenant());

    return bean;
  }

  private Long computeCollectionTimespentBySchool(Long groupId, Integer month, Integer year) {
    Set<Long> schoolIds = this.groupsService.fetchAllSchoolsOfGroup(groupId);
    List<CollectionTimespentByGroupModel> tsDataBySchool =
        this.reportService.fetchCollectionTimespentBySchool(schoolIds, month, year);
    return computeCollectionTimespent(tsDataBySchool);
  }

  private Long computeCollectionTimespent(List<CollectionTimespentByGroupModel> tsDataBySchool) {
    return tsDataBySchool.stream().collect(Collectors.summingLong(o -> o.getTimespent()));
  }

  private void updateQueueStatusToCompleted(CollectionTimespentModel tsData) {
    this.queueService.updateTimespentQueueStatusToCompleted(tsData.getClassId(),
        tsData.getContentSource());
  }

}
