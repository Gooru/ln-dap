package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import java.util.List;
import org.gooru.dap.processors.events.resource.timespent.UserStatsResourceContentTypeTimeSpentBean;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Here is the algo implemented:
 * <ul>
 * <li>Fetch the record from the learner_prefs_weighted_average table
 * <ul>
 * <li>If NOT found
 * <ul>
 * <li>Fetch all records from userstat_resource_content_type_timespent_ts
 * <li>Create base record to be inserted into learner_prefs_weighted_average table
 * <li>Update learner_prefs_weighted_average with new record
 * </ul>
 * </ul>
 * <li>Calculate weighted average for the specific type of resource
 * <li>Update the value in learner_prefs_weighted_average table
 * <li>Update the learner_prefs_per_million table
 * <li>Done
 * </ul>
 *
 * @author ashish.
 */

class UserPrefsContentTypeServiceImpl implements UserPrefsContentTypeService {

  private static final double MILLISEC_TO_SEC_CONVERSION_FACTOR = 1000;
  private final DBI dbi;
  private UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean;
  private final UserPrefsContentTypeDao userPrefsContentTypeDao;
  private LearnerPrefsWeightedAverageModel learnerPrefsWeightedAverageModel;
  private static final Logger LOGGER = LoggerFactory.getLogger(UserPrefsContentTypeService.class);

  UserPrefsContentTypeServiceImpl(DBI dbi) {

    this.dbi = dbi;
    this.userPrefsContentTypeDao = this.dbi.onDemand(UserPrefsContentTypeDao.class);
  }


  @Override
  public void updateUserPrefsForContentTypeBasedOnTimespent(
      UserStatsResourceContentTypeTimeSpentBean userStatsResourceContentTypeTimeSpentBean) {
    this.userStatsResourceContentTypeTimeSpentBean = userStatsResourceContentTypeTimeSpentBean;
    if (!learnerPrefsForSpecifiedUserExists()) {
      createAndInitializeUserRecordInLearnerPrefs();
    }
    calculateAndUpdateWeightedAverageForUserAndSpecifiedResourceType();
    calculateAndUpdateLearnerPrefsForUserAndSpecifiedResourceType();
  }

  private void calculateAndUpdateLearnerPrefsForUserAndSpecifiedResourceType() {
    /*
     * Update the learner_prefs_per_million table
     */
    userPrefsContentTypeDao.updateLearnerPrefsNormalized(
        learnerPrefsWeightedAverageModel.asLearnerPrefWeightedAverageNormalizedBean());

  }

  private void calculateAndUpdateWeightedAverageForUserAndSpecifiedResourceType() {
    /*
     * - Calculate weighted average for the specific type of resource, - the base reading is
     * existing entry, incoming entry is converted to min and scaled to 1M - Update the value in
     * learner_prefs_weighted_average table
     */
    learnerPrefsWeightedAverageModel = userPrefsContentTypeDao
        .fetchLearnerPrefsForSpecifiedUser(userStatsResourceContentTypeTimeSpentBean.getUserId());

    Long oldValue = fetchOldValueByContentType();
    Double newValue = userStatsResourceContentTypeTimeSpentBean.getTimeSpent()
        / MILLISEC_TO_SEC_CONVERSION_FACTOR;
    Double newEma = new EMACalculator().calculateNewEma(oldValue.doubleValue(), newValue);
    updateNewValueByContentType(newEma);
    userPrefsContentTypeDao
        .updateLearnerPrefsWeightedAverageRecord(learnerPrefsWeightedAverageModel);
  }

  private void updateNewValueByContentType(Double newValue) {
    switch (userStatsResourceContentTypeTimeSpentBean.getContentType()) {
      case "webpage_resource":
        learnerPrefsWeightedAverageModel.setWebpage(newValue.longValue());
        break;
      case "interactive_resource":
        learnerPrefsWeightedAverageModel.setInteractive(newValue.longValue());
        break;
      case "audio_resource":
        learnerPrefsWeightedAverageModel.setAudio(newValue.longValue());
        break;
      case "video_resource":
        learnerPrefsWeightedAverageModel.setVideo(newValue.longValue());
        break;
      case "image_resource":
        learnerPrefsWeightedAverageModel.setImage(newValue.longValue());
        break;
      case "text_resource":
        learnerPrefsWeightedAverageModel.setText(newValue.longValue());
        break;
      default:
        LOGGER.warn("Invalid or incorrect content-type found: '{}'",
            userStatsResourceContentTypeTimeSpentBean.getContentType());

    }

  }

  private Long fetchOldValueByContentType() {
    Long oldValue = null;
    switch (userStatsResourceContentTypeTimeSpentBean.getContentType()) {
      case "webpage_resource":
        oldValue = learnerPrefsWeightedAverageModel.getWebpage();
        break;
      case "interactive_resource":
        oldValue = learnerPrefsWeightedAverageModel.getInteractive();
        break;
      case "audio_resource":
        oldValue = learnerPrefsWeightedAverageModel.getAudio();
        break;
      case "video_resource":
        oldValue = learnerPrefsWeightedAverageModel.getVideo();
        break;
      case "image_resource":
        oldValue = learnerPrefsWeightedAverageModel.getImage();
        break;
      case "text_resource":
        oldValue = learnerPrefsWeightedAverageModel.getText();
        break;
      default:
        LOGGER.warn("Invalid or incorrect content-type found: '{}'",
            userStatsResourceContentTypeTimeSpentBean.getContentType());

    }
    return oldValue;
  }

  private void createAndInitializeUserRecordInLearnerPrefs() {
    /*
     * - Fetch all records from userstat_resource_content_type_timespent_ts - Create base record to
     * be inserted into learner_prefs_weighted_average table - Update learner_prefs_weighted_average
     * with new record
     */
    List<ContentTypeTimespentModel> contentTypeTimespentModels =
        userPrefsContentTypeDao.fetchCategorizedAverageTimespentForUser(
            userStatsResourceContentTypeTimeSpentBean.getUserId());
    LearnerPrefsWeightedAverageModel model =
        LearnerPrefsWeightedAverageModel.fromContentTypeTimespentList(
            userStatsResourceContentTypeTimeSpentBean.getUserId(), contentTypeTimespentModels);
    userPrefsContentTypeDao.createLearnerPrefsWeightedAverageRecord(model);

  }

  private boolean learnerPrefsForSpecifiedUserExists() {
    return userPrefsContentTypeDao
        .learnerPrefsForSpecifiedUserExists(userStatsResourceContentTypeTimeSpentBean.getUserId());
  }
}
