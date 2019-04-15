package org.gooru.dap.constants;

import java.util.Arrays;
import java.util.List;

public final class EventMessageConstant {

  public static final String EVENT_NAME = "eventName";
  public final static String ACTIVITY_TIME = "activityTime";
  public final static String RESOURCE_ID = "resourceId";
  public final static String USER_ID = "userId";
  public final static String METRICS_TIMESPENT = "/result/timeSpent";
  public final static String CONTENT_TYPE = "contentType";
  public final static String CTX_COLLECTION_ID = "/context/collectionId";
  public final static String CTX_LESSON_ID = "/context/lessonId";
  public final static String CTX_UNIT_ID = "/context/unitId";
  public final static String CTX_COURSE_ID = "/context/courseId";
  public final static String CTX_CLASS_ID = "/context/classId";
  public final static String CTX_COLLECTION_TYPE = "/context/collectionType";

  public static final String CONTENT_SOURCE_CM = "coursemap";
  public static final String CONTENT_SOURCE_CA = "dailyclassactivity";

  public static final List<String> VALID_CONTENT_SOURCE_FOR_GROUP_REPORTS =
      Arrays.asList(CONTENT_SOURCE_CA, CONTENT_SOURCE_CM);
}
