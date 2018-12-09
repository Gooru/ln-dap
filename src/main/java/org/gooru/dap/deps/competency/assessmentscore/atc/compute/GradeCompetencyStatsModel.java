package org.gooru.dap.deps.competency.assessmentscore.atc.compute;


/**
 * @author mukul@gooru
 * 
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import org.apache.commons.lang.builder.EqualsBuilder;
//import org.apache.commons.lang.builder.HashCodeBuilder;
//import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"userId",
"classId",
"courseId",
"gradeId",
"totalCompetencies",
"completedCompetencies",
"inprogressCompetencies",
"percentCompletion",
"percentScore"
})
public class GradeCompetencyStatsModel {

@JsonProperty("userId")
private String userId;
@JsonProperty("classId")
private String classId;
@JsonProperty("courseId")
private String courseId;
@JsonProperty("gradeId")
private Integer gradeId;
@JsonProperty("totalCompetencies")
private Double totalCompetencies;
@JsonProperty("completedCompetencies")
private Double completedCompetencies;
@JsonProperty("inprogressCompetencies")
private Double inprogressCompetencies;
@JsonProperty("percentCompletion")
private Double percentCompletion;
@JsonProperty("percentScore")
private Double percentScore;

/**
* No args constructor for use in serialization
*
*/
public GradeCompetencyStatsModel() {
}

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
public GradeCompetencyStatsModel(String userId, String classId, String courseId, Integer gradeId, Double totalCompetencies, 
		Double completedCompetencies, Double inprogressCompetencies, Double percentCompletion, Double percentScore) {
super();
this.userId = userId;
this.classId = classId;
this.courseId = courseId;
this.gradeId = gradeId;
this.totalCompetencies = totalCompetencies;
this.completedCompetencies = completedCompetencies;
this.inprogressCompetencies = inprogressCompetencies;
this.percentCompletion = percentCompletion;
this.percentScore = percentScore;
}

@JsonProperty("userId")
public String getUserId() {
return userId;
}

@JsonProperty("userId")
public void setUserId(String userId) {
this.userId = userId;
}

public GradeCompetencyStatsModel withUserId(String userId) {
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

public GradeCompetencyStatsModel withClassId(String classId) {
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

public GradeCompetencyStatsModel withCourseId(String courseId) {
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

public GradeCompetencyStatsModel withGradeId(Integer gradeId) {
this.gradeId = gradeId;
return this;
}

@JsonProperty("totalCompetencies")
public Double getTotalCompetencies() {
return totalCompetencies;
}

@JsonProperty("totalCompetencies")
public void setTotalCompetencies(Double totalCompetencies) {
this.totalCompetencies = totalCompetencies;
}

public GradeCompetencyStatsModel withTotalCompetencies(Double totalCompetencies) {
this.totalCompetencies = totalCompetencies;
return this;
}

@JsonProperty("completedCompetencies")
public Double getCompletedCompetencies() {
return completedCompetencies;
}

@JsonProperty("completedCompetencies")
public void setCompletedCompetencies(Double completedCompetencies) {
this.completedCompetencies = completedCompetencies;
}

public GradeCompetencyStatsModel withCompletedCompetencies(Double completedCompetencies) {
this.completedCompetencies = completedCompetencies;
return this;
}

@JsonProperty("inprogressCompetencies")
public Double getInprogressCompetencies() {
return inprogressCompetencies;
}

@JsonProperty("inprogressCompetencies")
public void setInprogressCompetencies(Double inprogressCompetencies) {
this.inprogressCompetencies = inprogressCompetencies;
}

public GradeCompetencyStatsModel withInprogressCompetencies(Double inprogressCompetencies) {
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

public GradeCompetencyStatsModel withPercentCompletion(Double percentCompletion) {
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

public GradeCompetencyStatsModel withPercentScore(Double percentScore) {
this.percentScore = percentScore;
return this;
}

//@Override
//public String toString() {
//return new ToStringBuilder(this).append("userId", userId).append("classId", classId).append("courseId", courseId).append("gradeId", gradeId).append("totalCompetencies", totalCompetencies).append("completedCompetencies", completedCompetencies).append("inprogressCompetencies", inprogressCompetencies).append("percentCompletion", percentCompletion).toString();
//}

//@Override
//public int hashCode() {
//return new HashCodeBuilder().append(inprogressCompetencies).append(gradeId).append(completedCompetencies).append(classId).append(percentCompletion).append(userId).append(totalCompetencies).append(courseId).toHashCode();
//}

//@Override
//public boolean equals(Object other) {
//if (other == this) {
//return true;
//}
//if ((other instanceof GradeCompetencyStatsModel) == false) {
//return false;
//}

//GradeCompetencyStatsModel rhs = ((GradeCompetencyStatsModel) other);
//return new EqualsBuilder().append(inprogressCompetencies, rhs.inprogressCompetencies).append(gradeId, rhs.gradeId).append(completedCompetencies, rhs.completedCompetencies).append(classId, rhs.classId).append(percentCompletion, rhs.percentCompletion).append(userId, rhs.userId).append(totalCompetencies, rhs.totalCompetencies).append(courseId, rhs.courseId).isEquals();
//}
}
