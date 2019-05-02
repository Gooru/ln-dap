package org.gooru.dap.deps.competency.assessmentscore.atc.compute;


import java.sql.Date;
/**
 * @author mukul@gooru
 * 
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userId", "classId", "courseId", "gradeId", "classGradeId", "subjectCode",
    "totalCompetencies", "classCompetencies", "completedCompetencies", "inprogressCompetencies",
    "percentCompletion", "percentScore", "statsDate", "month", "year"})
public class CompetencyStatsModel {

  @JsonProperty("userId")
  private String userId;
  @JsonProperty("classId")
  private String classId;
  @JsonProperty("courseId")
  private String courseId;
  @JsonProperty("gradeId")
  private Integer gradeId;
  @JsonProperty("classGradeId")
  private Integer classGradeId;
  @JsonProperty("subjectCode")
  private String subjectCode;
  @JsonProperty("totalCompetencies")
  private Integer totalCompetencies;
  @JsonProperty("classCompetencies")
  private Integer classCompetencies;
  @JsonProperty("completedCompetencies")
  private Integer completedCompetencies;
  @JsonProperty("inprogressCompetencies")
  private Integer inprogressCompetencies;
  @JsonProperty("percentCompletion")
  private Double percentCompletion;
  @JsonProperty("percentScore")
  private Double percentScore;
  @JsonProperty("statsDate")
  private Date statsDate;
  @JsonProperty("month")
  private Integer month;
  @JsonProperty("year")
  private Integer year;

  /**
   * No args constructor for use in serialization
   *
   */
  public CompetencyStatsModel() {}

  /**
   *
   * @param inprogressCompetencies
   * @param gradeId
   * @param completedCompetencies
   * @param classId
   * @param percentCompletion
   * @param userId
   * @param totalCompetencies
   * @param courseId
   */
  public CompetencyStatsModel(String userId, String classId, String courseId, Integer gradeId,
      Integer classGradeId, String subjectCode, Integer totalCompetencies,
      Integer classCompetencies, Integer completedCompetencies, Integer inprogressCompetencies,
      Double percentCompletion, Double percentScore, Date statsDate, Integer month, Integer year) {
    super();
    this.userId = userId;
    this.classId = classId;
    this.courseId = courseId;
    this.gradeId = gradeId;
    this.classGradeId = classGradeId;
    this.subjectCode = subjectCode;
    this.totalCompetencies = totalCompetencies;
    this.classCompetencies = classCompetencies;
    this.completedCompetencies = completedCompetencies;
    this.inprogressCompetencies = inprogressCompetencies;
    this.percentCompletion = percentCompletion;
    this.percentScore = percentScore;
    this.statsDate = statsDate;
    this.month = month;
    this.year = year;
  }

  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }

  @JsonProperty("userId")
  public void setUserId(String userId) {
    this.userId = userId;
  }

  public CompetencyStatsModel withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  @JsonProperty("classId")
  public String getClassId() {
    return classId;
  }

  @JsonProperty("classId")
  public void setClassId(String classId) {
    this.classId = classId;
  }

  public CompetencyStatsModel withClassId(String classId) {
    this.classId = classId;
    return this;
  }

  @JsonProperty("courseId")
  public String getCourseId() {
    return courseId;
  }

  @JsonProperty("courseId")
  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public CompetencyStatsModel withCourseId(String courseId) {
    this.courseId = courseId;
    return this;
  }

  @JsonProperty("gradeId")
  public Integer getGradeId() {
    return gradeId;
  }

  @JsonProperty("gradeId")
  public void setGradeId(Integer gradeId) {
    this.gradeId = gradeId;
  }

  public CompetencyStatsModel withGradeId(Integer gradeId) {
    this.gradeId = gradeId;
    return this;
  }

  @JsonProperty("classGradeId")
  public Integer getClassGradeId() {
    return classGradeId;
  }

  @JsonProperty("classGradeId")
  public void setClassGradeId(Integer classGradeId) {
    this.classGradeId = classGradeId;
  }

  public CompetencyStatsModel withClassGradeId(Integer classGradeId) {
    this.classGradeId = classGradeId;
    return this;
  }

  @JsonProperty("subjectCode")
  public String getSubjectCode() {
    return subjectCode;
  }

  @JsonProperty("subjectCode")
  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public CompetencyStatsModel withGradeId(String subjectCode) {
    this.subjectCode = subjectCode;
    return this;
  }

  @JsonProperty("totalCompetencies")
  public Integer getTotalCompetencies() {
    return totalCompetencies;
  }

  @JsonProperty("totalCompetencies")
  public void setTotalCompetencies(Integer totalCompetencies) {
    this.totalCompetencies = totalCompetencies;
  }

  public CompetencyStatsModel withTotalCompetencies(Integer totalCompetencies) {
    this.totalCompetencies = totalCompetencies;
    return this;
  }

  @JsonProperty("classCompetencies")
  public Integer getClassCompetencies() {
    return classCompetencies;
  }

  @JsonProperty("classCompetencies")
  public void setClassCompetencies(Integer classCompetencies) {
    this.classCompetencies = classCompetencies;
  }

  public CompetencyStatsModel withClassCompetencies(Integer classCompetencies) {
    this.classCompetencies = classCompetencies;
    return this;
  }

  @JsonProperty("completedCompetencies")
  public Integer getCompletedCompetencies() {
    return completedCompetencies;
  }

  @JsonProperty("completedCompetencies")
  public void setCompletedCompetencies(Integer completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

  public CompetencyStatsModel withCompletedCompetencies(Integer completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
    return this;
  }

  @JsonProperty("inprogressCompetencies")
  public Integer getInprogressCompetencies() {
    return inprogressCompetencies;
  }

  @JsonProperty("inprogressCompetencies")
  public void setInprogressCompetencies(Integer inprogressCompetencies) {
    this.inprogressCompetencies = inprogressCompetencies;
  }

  public CompetencyStatsModel withInprogressCompetencies(Integer inprogressCompetencies) {
    this.inprogressCompetencies = inprogressCompetencies;
    return this;
  }

  @JsonProperty("percentCompletion")
  public Double getPercentCompletion() {
    return percentCompletion;
  }

  @JsonProperty("percentCompletion")
  public void setPercentCompletion(Double percentCompletion) {
    this.percentCompletion = percentCompletion;
  }

  public CompetencyStatsModel withPercentCompletion(Double percentCompletion) {
    this.percentCompletion = percentCompletion;
    return this;
  }

  @JsonProperty("percentScore")
  public Double getPercentScore() {
    return percentScore;
  }

  @JsonProperty("percentScore")
  public void setPercentScore(Double percentScore) {
    this.percentScore = percentScore;
  }

  public CompetencyStatsModel withPercentScore(Double percentScore) {
    this.percentScore = percentScore;
    return this;
  }

  @JsonProperty("statsDate")
  public Date getStatsDate() {
    return statsDate;
  }

  @JsonProperty("statsDate")
  public void setStatsDate(Date statsDate) {
    this.statsDate = statsDate;
  }

  public CompetencyStatsModel withStatsDate(Date statsDate) {
    this.statsDate = statsDate;
    return this;
  }

  @JsonProperty("month")
  public Integer getMonth() {
    return month;
  }

  @JsonProperty("month")
  public void setMonth(Integer month) {
    this.month = month;
  }

  public CompetencyStatsModel withMonth(Integer month) {
    this.month = month;
    return this;
  }

  @JsonProperty("year")
  public Integer getYear() {
    return year;
  }

  @JsonProperty("year")
  public void setYear(Integer year) {
    this.year = year;
  }

  public CompetencyStatsModel withYear(Integer year) {
    this.year = year;
    return this;
  }
}
