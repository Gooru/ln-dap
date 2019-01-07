package org.gooru.dap.processors.events.question.timespent;

import java.util.Date;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import com.fasterxml.jackson.databind.JsonNode;

public class QuestionTypeTimeSpentBean {

  private String questionType;
  private Date activityDate;
  private long timeSpent;

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public Date getActivityDate() {
    return activityDate;
  }

  public void setActivityDate(Date activityDate) {
    this.activityDate = activityDate;
  }

  public long getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(long timeSpent) {
    this.timeSpent = timeSpent;
  }

  public static QuestionTypeTimeSpentBean createInstance(JsonNode jsonNode,
      ContentBean contentBean) {
    QuestionTypeTimeSpentBean questionTypeTimeSpentBean = new QuestionTypeTimeSpentBean();
    questionTypeTimeSpentBean
        .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
    questionTypeTimeSpentBean.setQuestionType(contentBean.getContentType());
    questionTypeTimeSpentBean
        .setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
    return questionTypeTimeSpentBean;

  }

}
