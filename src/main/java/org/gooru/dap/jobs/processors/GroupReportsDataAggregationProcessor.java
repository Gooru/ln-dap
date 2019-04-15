
package org.gooru.dap.jobs.processors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.GroupConstants;
import org.gooru.dap.jobs.group.reports.GroupModel;
import org.gooru.dap.jobs.group.reports.GroupsService;
import org.gooru.dap.jobs.group.reports.performance.AssessmentPerfByGroupModel;
import org.gooru.dap.jobs.group.reports.performance.ClassPerformanceDataReportsBean;
import org.gooru.dap.jobs.group.reports.performance.GroupPerformanceDataReportBean;
import org.gooru.dap.jobs.group.reports.performance.GroupPerformanceReportsService;
import org.gooru.dap.jobs.http.response.UsageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 05-Apr-2019
 */
public class GroupReportsDataAggregationProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupReportsDataAggregationProcessor.class);

  private final List<UsageData> allUsageData;

  private final GroupsService groupsService = new GroupsService(DBICreator.getDbiForDefaultDS());

  private final GroupPerformanceReportsService reportsService =
      new GroupPerformanceReportsService(DBICreator.getDbiForDefaultDS());

  public GroupReportsDataAggregationProcessor(List<UsageData> allUsageData) {
    this.allUsageData = allUsageData;
  }

  public void process() {
    // Find the list of distinct classes
    List<String> distinctClassIds = new ArrayList<>(allUsageData.size());

    allUsageData.forEach(usage -> {
      distinctClassIds.add(usage.getClassId());
    });

    // Fetch class to school mapping
    Map<String, Long> classSchoolMap = this.groupsService.fetchClassSchoolMapping(distinctClassIds);
    Set<Long> schoolIds = new HashSet<>();
    classSchoolMap.entrySet().stream().filter(entry -> schoolIds.add(entry.getValue()));

    // Fetch school to group mapping
    Map<Long, Long> schoolGroupMap = this.groupsService.fetchSchoolGroupMapping(schoolIds);
    Set<Long> groupIds = new HashSet<>();
    schoolGroupMap.entrySet().stream().filter(entry -> groupIds.add(entry.getValue()));

    // Fetch details of all groups mapped with schools above
    Map<Long, GroupModel> groupsMap = this.groupsService.fetchGroupsByIds(groupIds);

    // Now iterate on usage data objects to prepare bean object and persist the assessment
    // performance and time spent data of the each class. Insert or update will happen for class and
    // group level tables. Inserting or updating KPI data in class level table is straightforward as
    // we receive cumulative data till date from the upstream class performance APIs. For the group
    // level data report updates will happen after computing the average performances of the child
    // elements. We are not considering assessment time spent data to be aggregated for group
    // levels.
    for (UsageData usage : allUsageData) {
      String classId = usage.getClassId();
      Long schoolId = classSchoolMap.get(classId);
      if (schoolId == null) {
        // If the school id does not present for the given class then the class is not yet grouped.
        // We can skip and move ahead
        continue;
      }

      Long groupId = schoolGroupMap.get(schoolId);
      if (groupId == null) {
        // If the group id is null, then the schools has not been grouped, skip and move ahead
        continue;
      }

      GroupModel group = groupsMap.get(groupId);
      ClassPerformanceDataReportsBean bean =
          prepareClassLevelDataReportsBean(usage, schoolId, group, usage.getContentSource());

      processClassLevelAssessmentPerf(bean);
      processGroupLevelAssessmentPerf(bean);
      usage.setProcessed(true);
    }
  }

  private void processClassLevelAssessmentPerf(ClassPerformanceDataReportsBean bean) {
    this.reportsService.insertOrUpdateClassLevelAssessmentPerfAndTimespent(bean);
  }

  private void processGroupLevelAssessmentPerf(ClassPerformanceDataReportsBean bean) {
    if (bean.getSchoolDistrictId() != null) {
      // If the group type is school district then compute the assessment performance by SD id
      computeAndPersistSchoolDistrictPerfData(bean);
    } else {
      computeAndPersistClusterPerfData(bean);
      computeAndPersistBlockPerfData(bean);
      computeAndPersistDistrictPerfData(bean);
    }
  }

  private void computeAndPersistSchoolDistrictPerfData(ClassPerformanceDataReportsBean bean) {
    Double averagePerf = computeAssessmentPerformanceBySchool(bean.getSchoolDistrictId());
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, averagePerf, bean.getSchoolDistrictId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
  }

  private void computeAndPersistClusterPerfData(ClassPerformanceDataReportsBean bean) {
    Double averagePerf = computeAssessmentPerformanceBySchool(bean.getClusterId());
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, averagePerf, bean.getClusterId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
  }

  private void computeAndPersistBlockPerfData(ClassPerformanceDataReportsBean bean) {
    Set<Long> blockChildIds = this.reportsService.fetchGroupChilds(bean.getBlockId());
    List<AssessmentPerfByGroupModel> perfByBlock =
        this.reportsService.fetchAssessmentPerfByGroup(blockChildIds);
    Double blockPerf = computeAssessmentPerformance(perfByBlock);
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, blockPerf, bean.getBlockId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
  }

  private void computeAndPersistDistrictPerfData(ClassPerformanceDataReportsBean bean) {
    Set<Long> districtChildIds = this.reportsService.fetchGroupChilds(bean.getDistrictId());
    List<AssessmentPerfByGroupModel> perfByDistrict =
        this.reportsService.fetchAssessmentPerfByGroup(districtChildIds);
    Double districtPerf = computeAssessmentPerformance(perfByDistrict);
    GroupPerformanceDataReportBean groupBean =
        prepareGroupLevelDataReportBean(bean, districtPerf, bean.getDistrictId());
    this.reportsService.insertOrUpdateGroupLevelAssessmentPerf(groupBean);
  }

  private Double computeAssessmentPerformanceBySchool(Long groupId) {
    Set<Long> schoolIds = this.groupsService.fetchAllSchoolsOfGroup(groupId);
    List<AssessmentPerfByGroupModel> perfDataBySchool =
        this.reportsService.fetchAssessmentPerfBySchool(schoolIds);
    return computeAssessmentPerformance(perfDataBySchool);
  }

  private Double computeAssessmentPerformance(List<AssessmentPerfByGroupModel> perfData) {
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

    bean.setGroupId(groupId);

    bean.setAssessmentPerformance(perf);

    // Set current month and year values
    LocalDate now = LocalDate.now();
    bean.setMonth(now.getMonthValue());
    bean.setYear(now.getYear());

    bean.setTenant(clsLevelBean.getTenant());

    return bean;
  }

  private ClassPerformanceDataReportsBean prepareClassLevelDataReportsBean(UsageData perfData,
      Long schoolId, GroupModel group, String contentSource) {

    // Populate the bean from usage data, and group information
    ClassPerformanceDataReportsBean bean = new ClassPerformanceDataReportsBean();
    bean.setAssessmentPerformance(perfData.getScoreInPercentage());
    bean.setAssessmentTimespent(perfData.getTimeSpent());
    bean.setClassId(perfData.getClassId());
    bean.setSchoolId(schoolId);
    bean.setStateId(group.getStateId());
    bean.setCountryId(group.getCountryId());
    bean.setContentSource(contentSource);
    bean.setTenant(group.getTenant());

    // Set current month and year values
    LocalDate now = LocalDate.now();
    bean.setMonth(now.getMonthValue());
    bean.setYear(now.getYear());

    // If the group type is school district, then set the group id as school district id in bean and
    // return. Otherwise if its cluster we need to fetch the parent hierarchy and set the block and
    // district ids accordingly
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

    return bean;
  }

}
