
package org.gooru.dap.jobs.group.reports.competency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.GroupConstants;
import org.gooru.dap.deps.group.dbhelpers.GroupCompetencyReportsQueueService;
import org.gooru.dap.jobs.group.reports.GroupModel;
import org.gooru.dap.jobs.group.reports.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 29-Apr-2019
 */
public class GroupCompetencyReportsProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportsProcessor.class);
  private final List<ClassCompetencyStatsModel> currentStatsModels;
  private final List<ClassCompetencyStatsModel> previousStatsModels;

  private final GroupCompetencyReportsQueueService queueService =
      new GroupCompetencyReportsQueueService(DBICreator.getDbiForDefaultDS());

  private final GroupsService groupsService = new GroupsService(DBICreator.getDbiForDefaultDS());

  private final GroupCompetencyReportsService reportService =
      new GroupCompetencyReportsService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportsProcessor(List<ClassCompetencyStatsModel> currentStatsModels,
      List<ClassCompetencyStatsModel> previousStatsModels) {
    this.currentStatsModels = currentStatsModels;
    this.previousStatsModels = previousStatsModels;
  }

  public void process() {

    List<String> uniqueClasses = new ArrayList<>(currentStatsModels.size());
    currentStatsModels.forEach(model -> {
      uniqueClasses.add(model.getClassId());
    });

    Map<String, ClassCompetencyStatsModel> previousStatsModelMap = new HashMap<>();
    previousStatsModels.forEach(model -> {
      previousStatsModelMap.put(model.getClassId(), model);
    });

    // Fetch class to school mapping
    // Here the assumption is that the classes are mapped with the schools. We are inserting only
    // those classes in the queue for the processing which are mapped to school.
    Map<String, Long> classSchoolMap = this.groupsService.fetchClassSchoolMapping(uniqueClasses);
    Set<Long> schoolIds = classSchoolMap.values().stream().collect(Collectors.toSet());
    LOGGER.debug("number of schools returned {}", schoolIds.size());

    // Fetch school to group mapping
    Map<Long, Long> schoolGroupMap = this.groupsService.fetchSchoolGroupMapping(schoolIds);
    Set<Long> groupIds = schoolGroupMap.values().stream().collect(Collectors.toSet());
    LOGGER.debug("number of groups returned {}", groupIds.size());

    // Fetch details of all groups mapped with schools above
    Map<Long, GroupModel> groupsMap = this.groupsService.fetchGroupsByIds(groupIds);

    LOGGER
        .debug("class, school and group mapping is fetched, now timespent data processing started");

    for (ClassCompetencyStatsModel model : this.currentStatsModels) {
      Long schoolId = classSchoolMap.get(model.getClassId());
      if (schoolId == null) {
        // If the school id does not present for the given class then the class is not yet
        // grouped.
        // We can skip and move ahead
        LOGGER.warn("class '{}' is not grouped under school, skipping", model.getClassId());
        
        // Even if there is no school and groups mapped with the class, at least persist the class
        // level competency data.
        ClassCompetencyDataReportsBean clsBean = createClassCompetencyDataReportsBean(model,
            previousStatsModelMap.get(model.getClassId()), null, null);
        processClassLevelCompetency(clsBean);
        continue;
      }

      Long groupId = schoolGroupMap.get(schoolId);
      if (groupId == null) {
        // If the group id is null, then the schools has not been grouped, skip and move ahead
        LOGGER.debug("school '{}' is not associated with any group, skipping", schoolId);
        continue;
      }

      GroupModel group = groupsMap.get(groupId);
      ClassCompetencyDataReportsBean clsBean = createClassCompetencyDataReportsBean(model,
          previousStatsModelMap.get(model.getClassId()), schoolId, group);

      // persist the class and group level data.
      LOGGER.debug("persisting competency at class and group level");
      processClassLevelCompetency(clsBean);
      processGroupLevelCompetency(clsBean);

      LOGGER.debug("competency data has been persisted");

      // Update the status of the queue record to complete for the queue cleanup
      updateQueueStatusToCompleted(model);
    }
  }


  private void processClassLevelCompetency(ClassCompetencyDataReportsBean clsBean) {
    this.reportService.insertOrUpdateClassCompetencyDataReport(clsBean);
  }

  private void processGroupLevelCompetency(ClassCompetencyDataReportsBean clsBean) {
    if (clsBean.getSchoolDistrictId() != null) {
      // If the group type is school district then compute the collection timespent by SD id
      computeAndPersistSchoolDistrictCompetencyData(clsBean);
    } else {
      computeAndPersistClusterCompetencyData(clsBean);
      computeAndPersistBlockCompetecyData(clsBean);
      computeAndPersistDistrictCompetencyData(clsBean);
    }
    LOGGER.debug("group level competency data has been saved");
  }

  private void computeAndPersistSchoolDistrictCompetencyData(
      ClassCompetencyDataReportsBean clsBean) {
    List<GroupCompetencyStatsModel> competencyStatsModel = computeCompetencyCompletionBySchool(
        clsBean.getSchoolDistrictId(), clsBean.getMonth(), clsBean.getYear());
    createGroupReportDataBeanAndPersist(clsBean, clsBean.getSchoolDistrictId(),
        competencyStatsModel);
  }

  private void computeAndPersistClusterCompetencyData(ClassCompetencyDataReportsBean clsBean) {
    List<GroupCompetencyStatsModel> competencyStatsModel = computeCompetencyCompletionBySchool(
        clsBean.getClusterId(), clsBean.getMonth(), clsBean.getYear());
    createGroupReportDataBeanAndPersist(clsBean, clsBean.getClusterId(), competencyStatsModel);
  }

  private void computeAndPersistBlockCompetecyData(ClassCompetencyDataReportsBean clsBean) {
    Set<Long> blockChildIds = this.groupsService.fetchGroupChilds(clsBean.getBlockId());
    List<GroupCompetencyStatsModel> competencyStatsModel = this.reportService
        .fetchCompetencyCompletionsByGroup(blockChildIds, clsBean.getMonth(), clsBean.getYear());
    createGroupReportDataBeanAndPersist(clsBean, clsBean.getBlockId(), competencyStatsModel);
  }

  private void computeAndPersistDistrictCompetencyData(ClassCompetencyDataReportsBean clsBean) {
    Set<Long> districtChildIds = this.groupsService.fetchGroupChilds(clsBean.getDistrictId());
    List<GroupCompetencyStatsModel> competencyStatsModel = this.reportService
        .fetchCompetencyCompletionsByGroup(districtChildIds, clsBean.getMonth(), clsBean.getYear());
    createGroupReportDataBeanAndPersist(clsBean, clsBean.getDistrictId(), competencyStatsModel);
  }

  private void createGroupReportDataBeanAndPersist(ClassCompetencyDataReportsBean clsBean,
      Long groupId, List<GroupCompetencyStatsModel> competencyStatsModel) {
    Long completedCount = computeCompletedCompetency(competencyStatsModel);
    Long inprogressCount = computeInprogressCompetency(competencyStatsModel);
    Long cumulativeCompletedCount = computeCumulativeCompetency(competencyStatsModel);

    GroupCompetencyDataReportsBean groupBean = createGroupCompetencyDataReportsBean(clsBean,
        groupId, completedCount, inprogressCount, cumulativeCompletedCount);
    this.reportService.insertOrUpdateGroupCompetencyDataReport(groupBean);
  }

  private List<GroupCompetencyStatsModel> computeCompetencyCompletionBySchool(Long groupId,
      Integer month, Integer year) {
    Set<Long> schoolIds = this.groupsService.fetchAllSchoolsOfGroup(groupId);
    return this.reportService.fetchCompetencyCompletionsBySchool(schoolIds, month, year);
  }

  private Long computeCompletedCompetency(List<GroupCompetencyStatsModel> dataModels) {
    return dataModels.stream().collect(Collectors.summingLong(o -> o.getCompletedCount()));
  }

  private Long computeInprogressCompetency(List<GroupCompetencyStatsModel> dataModels) {
    return dataModels.stream().collect(Collectors.summingLong(o -> o.getInprogressCount()));
  }

  private Long computeCumulativeCompetency(List<GroupCompetencyStatsModel> dataModels) {
    return dataModels.stream()
        .collect(Collectors.summingLong(o -> o.getCumulativeCompletedCount()));
  }

  private GroupCompetencyDataReportsBean createGroupCompetencyDataReportsBean(
      ClassCompetencyDataReportsBean clsBean, Long groupId, Long completedCount,
      Long inprogressCount, Long cumulativeCompletedCount) {
    GroupCompetencyDataReportsBean bean = new GroupCompetencyDataReportsBean();
    bean.setCompletedCount(completedCount);
    bean.setInprogressCount(inprogressCount);
    bean.setCumulativeCompletedCount(cumulativeCompletedCount);

    bean.setGroupId(groupId);

    bean.setSchoolId(clsBean.getSchoolId());
    bean.setStateId(clsBean.getStateId());
    bean.setCountryId(clsBean.getCountryId());
    bean.setTenant(clsBean.getTenant());

    bean.setMonth(clsBean.getMonth());
    bean.setYear(clsBean.getYear());
    return bean;
  }

  private ClassCompetencyDataReportsBean createClassCompetencyDataReportsBean(
      ClassCompetencyStatsModel currentStatsmodel, ClassCompetencyStatsModel previousStatsModel,
      Long schoolId, GroupModel group) {
    ClassCompetencyDataReportsBean bean = new ClassCompetencyDataReportsBean();
    bean.setClassId(currentStatsmodel.getClassId());

    // If the previous months stats are now available, then we will consider the current stats model
    // as actual counts for this month. Otherwise if previous months stats are available then we
    // will subtract it from current months stats and set them as current months stat
    if (previousStatsModel == null) {
      bean.setCompletedCount(currentStatsmodel.getCompletedCount());
      bean.setInprogressCount(currentStatsmodel.getInprogressCount());
    } else {
      Long completedCount =
          currentStatsmodel.getCompletedCount() - previousStatsModel.getCompletedCount();
      Long inprogressCount =
          currentStatsmodel.getInprogressCount() - previousStatsModel.getInprogressCount();
      bean.setCompletedCount(completedCount);
      bean.setInprogressCount(inprogressCount);
    }

    // As the data is cumulative, the completed count present in current months model is cumulative
    // count hence setting is directly
    bean.setCumulativeCompletedCount(currentStatsmodel.getCompletedCount());
    bean.setSchoolId(schoolId);

    // Set current month and year values
    LocalDate now = LocalDate.now();
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

  private void updateQueueStatusToCompleted(ClassCompetencyStatsModel model) {
    this.queueService.updateQueueStatusToCompleted(model.getClassId());
  }
}
