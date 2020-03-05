
package org.gooru.dap.jobs.group.reports.performance;

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
import org.gooru.dap.jobs.http.response.UsageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 05-Apr-2019
 */
public class GroupPerformanceReportsProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerformanceReportsProcessor.class);

  private final List<UsageData> allUsageData;

  private final GroupsService groupsService = new GroupsService(DBICreator.getDbiForCoreDS());

  private final GroupPerformanceReportsService reportsService =
      new GroupPerformanceReportsService(DBICreator.getDbiForDefaultDS());

  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  public GroupPerformanceReportsProcessor(List<UsageData> allUsageData) {
    this.allUsageData = allUsageData;
  }

  public void process() {
    try {
      LOGGER.debug("## performance report processing started ##");
      // Find the list of distinct classes
      List<String> distinctClassIds = new ArrayList<>(allUsageData.size());
      allUsageData.forEach(usage -> {
        distinctClassIds.add(usage.getClassId());
        LOGGER.debug("class id added:'{}'", usage.getClassId());
      });
      LOGGER.debug("number of distinct classes to process {}", distinctClassIds.size());

      // Fetch class to school mapping
      // Here the assumption is that the classes are mapped with the
      // schools. We are inserting only
      // those classes in the queue for the processing which are mapped to
      // school.
      Map<String, Long> classSchoolMap =
          this.groupsService.fetchClassSchoolMapping(distinctClassIds);
      Set<Long> schoolIds = classSchoolMap.values().stream().collect(Collectors.toSet());
      LOGGER.debug("number of schools returned {}", schoolIds.size());

      // Fetch school to group mapping
      Map<Long, Long> schoolGroupMap = this.groupsService.fetchSchoolGroupMapping(schoolIds);
      Set<Long> groupIds = schoolGroupMap.values().stream().collect(Collectors.toSet());
      LOGGER.debug("number of groups returned {}", groupIds.size());

      // TODO: If not school is mapped to group then need to handle and
      // update queue status
      // accordingly

      // Fetch details of all groups mapped with schools above
      Map<Long, GroupModel> groupsMap = this.groupsService.fetchGroupsByIds(groupIds);

      // Fetch class details from core and prepare map for further use
      List<ClassModel> classDetails = this.groupsService.fetchClassDetails(distinctClassIds);
      Map<String, ClassModel> classDetailsMap = new HashMap<>();
      classDetails.forEach(model -> {
        classDetailsMap.put(model.getId(), model);
      });

      LOGGER.debug("class, school and group mapping is fetched, now usage data processing started");
      // Now iterate on usage data objects to prepare bean object and
      // persist the assessment
      // performance and time spent data of the each class. Insert or
      // update will happen for class
      // and group level tables. Inserting or updating KPI data in class
      // level table is
      // straightforward as we receive cumulative data till date from the
      // upstream class performance
      // APIs. For the group level data report updates will happen after
      // computing the average
      // performances of the child elements. We are not considering
      // assessment time spent data to be
      // aggregated for group levels.
      for (UsageData usage : allUsageData) {
        String classId = usage.getClassId();
        Long schoolId = classSchoolMap.get(classId);
        ClassModel classModel = classDetailsMap.get(classId);
        if (schoolId == null) {
          // If the school id does not present for the given class then the class is not yet
          // grouped. We can skip and move ahead
          LOGGER.debug("class '{}' is not grouped under school, persisting class level data",
              classId);

          // Even if there is no school and groups mapped with the class, at least persist the class
          // level performance.
          ClassPerformanceDataReportsBean bean = prepareClassLevelDataReportsBean(usage, null, null,
              usage.getContentSource(), classModel);
          processClassLevelAssessmentPerf(bean);
          updateQueueStatusToCompleted(usage);
          continue;
        }

        Long groupId = schoolGroupMap.get(schoolId);
        if (groupId == null) {
          // If the group id is null, then the schools has not been
          // grouped, then still persist
          // class level data and return.
          LOGGER.debug("school '{}' is not associated with any group, persisting class level data",
              schoolId);

          ClassPerformanceDataReportsBean bean = prepareClassLevelDataReportsBean(usage, schoolId,
              null, usage.getContentSource(), classModel);
          processClassLevelAssessmentPerf(bean);
          updateQueueStatusToCompleted(usage);
          continue;
        }

        GroupModel group = groupsMap.get(groupId);
        ClassPerformanceDataReportsBean bean = prepareClassLevelDataReportsBean(usage, schoolId,
            group, usage.getContentSource(), classModel);

        // persist the class and group level data.
        LOGGER.debug("persisting performances at class and group level");
        processClassLevelAssessmentPerf(bean);
        processGroupLevelAssessmentPerf(bean);

        LOGGER.debug("performance data has been persisted");

        // Update the status of the queue record to complete for the
        // queue cleanup
        updateQueueStatusToCompleted(usage);
      }
    } catch (Throwable t) {
      LOGGER.error("job execution has been intrupted by the error", t);
    }
  }

  private void updateQueueStatusToCompleted(UsageData usage) {
    this.queueService.updatePerfQueueStatusToCompleted(usage.getClassId(),
        usage.getContentSource());
  }

  private void processClassLevelAssessmentPerf(ClassPerformanceDataReportsBean bean) {
    this.reportsService.insertOrUpdateClassLevelAssessmentPerfAndTimespent(bean);
    LOGGER.debug("class level assessment performance is saved.");
  }

  private void processGroupLevelAssessmentPerf(ClassPerformanceDataReportsBean bean) {
    if (bean.getSchoolDistrictId() != null) {
      // If the group type is school district then compute the assessment
      // performance by SD id
      computeAndPersistSchoolDistrictPerfData(bean);
    } else {
      computeAndPersistClusterPerfData(bean);
      computeAndPersistBlockPerfData(bean);
      computeAndPersistDistrictPerfData(bean);
    }
    LOGGER.debug("group level assessment performances are saved");
  }

  private void computeAndPersistSchoolDistrictPerfData(ClassPerformanceDataReportsBean bean) {
    Double averagePerf = computeAssessmentPerformanceBySchool(bean.getSchoolDistrictId(),
        bean.getMonth(), bean.getYear());
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, averagePerf, bean.getSchoolDistrictId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
    LOGGER.debug("school district '{}' performance has been saved", bean.getSchoolDistrictId());
  }

  private void computeAndPersistClusterPerfData(ClassPerformanceDataReportsBean bean) {
    Double averagePerf =
        computeAssessmentPerformanceBySchool(bean.getClusterId(), bean.getMonth(), bean.getYear());
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, averagePerf, bean.getClusterId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
    LOGGER.debug("cluster '{}' performance has been saved", bean.getClusterId());
  }

  private void computeAndPersistBlockPerfData(ClassPerformanceDataReportsBean bean) {
    Set<Long> blockChildIds = this.groupsService.fetchGroupChilds(bean.getBlockId());
    List<AssessmentPerfByGroupModel> perfByBlock = this.reportsService
        .fetchAssessmentPerfByGroup(blockChildIds, bean.getMonth(), bean.getYear());
    Double blockPerf = computeAssessmentPerformance(perfByBlock);
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, blockPerf, bean.getBlockId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
    LOGGER.debug("block '{}' performance has been saved", bean.getBlockId());
  }

  private void computeAndPersistDistrictPerfData(ClassPerformanceDataReportsBean bean) {
    Set<Long> districtChildIds = this.groupsService.fetchGroupChilds(bean.getDistrictId());
    List<AssessmentPerfByGroupModel> perfByDistrict = this.reportsService
        .fetchAssessmentPerfByGroup(districtChildIds, bean.getMonth(), bean.getYear());
    Double districtPerf = computeAssessmentPerformance(perfByDistrict);
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, districtPerf, bean.getDistrictId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
    LOGGER.debug("district '{}' performance has been saved", bean.getDistrictId());
  }

  private Double computeAssessmentPerformanceBySchool(Long groupId, Integer month, Integer year) {
    Set<Long> schoolIds = this.groupsService.fetchAllSchoolsOfGroup(groupId);
    List<AssessmentPerfByGroupModel> perfDataBySchool =
        this.reportsService.fetchAssessmentPerfBySchool(schoolIds, month, year);
    return computeAssessmentPerformance(perfDataBySchool);
  }

  private Double computeAssessmentPerformance(List<AssessmentPerfByGroupModel> perfData) {
    // average performance is computed by adding up all the performances of
    // the child's and divide
    // by number of child's
    Double totalPerformance =
        perfData.stream().collect(Collectors.summingDouble(o -> o.getPerformance()));
    Double averagePerformance = totalPerformance / perfData.size();
    return averagePerformance;
  }

  private GroupPerformanceDataReportBean prepareGroupLevelDataReportBean(
      ClassPerformanceDataReportsBean clsLevelBean, Double perf, Long groupId) {
    GroupPerformanceDataReportBean bean = new GroupPerformanceDataReportBean();
    bean.setContentSource(clsLevelBean.getContentSource());
    bean.setSchoolId(clsLevelBean.getSchoolId());
    bean.setStateId(clsLevelBean.getStateId());
    bean.setCountryId(clsLevelBean.getCountryId());
    bean.setSubject(clsLevelBean.getSubject());
    bean.setFramework(clsLevelBean.getFramework());

    // As this is a generic pojo used for all type of groups, this group id
    // will be of type which is
    // passed from the caller of the method. It can be School District,
    // District and so on.
    bean.setGroupId(groupId);

    bean.setAssessmentPerformance(perf);

    // Set current month and year values
    bean.setWeek(clsLevelBean.getWeek());
    bean.setMonth(clsLevelBean.getMonth());
    bean.setYear(clsLevelBean.getYear());

    bean.setTenant(clsLevelBean.getTenant());

    return bean;
  }

  // Populate the bean from usage data, and group information
  private ClassPerformanceDataReportsBean prepareClassLevelDataReportsBean(UsageData perfData,
      Long schoolId, GroupModel group, String contentSource, ClassModel classModel) {

    ClassPerformanceDataReportsBean bean = new ClassPerformanceDataReportsBean();

    // Performance value is cumulative received from the class performance
    // API so we are persisting
    // as is
    bean.setAssessmentPerformance(perfData.getScoreInPercentage());

    // Here we are receiving the cumulative time spent from the class
    // performance API and hence we
    // are updating as is. To compute the month on month timespent, it
    // should heppen at read API of
    // the groups reports.
    bean.setAssessmentTimespent(perfData.getTimeSpent());
    bean.setClassId(perfData.getClassId());
    bean.setSchoolId(schoolId);
    bean.setContentSource(contentSource);

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

      // If the group type is school district, then set the group id as
      // school district id in bean and
      // return. Otherwise if its cluster we need to fetch the parent
      // hierarchy and set the block and
      // district id's accordingly.
      // Here we need to fetch the group hierarchy and set the appropriate
      // id's to be used in further
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

}
