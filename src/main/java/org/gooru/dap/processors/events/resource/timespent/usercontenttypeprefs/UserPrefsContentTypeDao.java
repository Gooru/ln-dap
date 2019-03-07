package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import java.util.List;
import org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs.LearnerPrefsWeightedAverageModel.LearnerPrefWeightedAverageNormalizedBean;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

public interface UserPrefsContentTypeDao {

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM learner_prefs_weighted_average WHERE user_id = :userId)")
  boolean learnerPrefsForSpecifiedUserExists(@Bind("userId") String userId);

  @Mapper(LearnerPrefsWeightedAverageModelMapper.class)
  @SqlQuery(
      "SELECT user_id, video_wa, webpage_wa, interactive_wa, image_wa, text_wa, audio_wa FROM learner_prefs_weighted_average "
          + " where user_id = :userId")
  LearnerPrefsWeightedAverageModel fetchLearnerPrefsForSpecifiedUser(@Bind("userId") String userId);

  @SqlUpdate("insert into learner_prefs_weighted_average (user_id, video_wa, webpage_wa, interactive_wa, image_wa, text_wa, audio_wa) "
      + " values (:userId, :video, :webpage, :interactive, :image, :text, :audio) "
      + " ON CONFLICT (user_id) DO NOTHING")
  void createLearnerPrefsWeightedAverageRecord(@BindBean LearnerPrefsWeightedAverageModel model);

  @SqlUpdate("insert into learner_prefs_per_million (user_id, video_pref, webpage_pref, interactive_pref, image_pref, text_pref, audio_pref) "
      + " values (:userId, :video, :webpage, :interactive, :image, :text, :audio) "
      + " ON CONFLICT (user_id) DO UPDATE set video_pref = :video, webpage_pref = :webpage, interactive_pref = :interactive, "
      + " image_pref = :image, text_pref = :text, audio_pref = :audio")
  void updateLearnerPrefsNormalized(@BindBean LearnerPrefWeightedAverageNormalizedBean bean);


  // While selecting we are converting milliseconds to seconds and casting it to bigint/long
  @Mapper(ContentTypeTimespentModelMapper.class)
  @SqlQuery(
      "select content_type, (avg(time_spent)/1000)::bigint average from userstat_resource_content_type_timespent_ts "
          + " where user_id = :userId group by content_type")
  List<ContentTypeTimespentModel> fetchCategorizedAverageTimespentForUser(
      @Bind("userId") String userId);

  @SqlUpdate("update learner_prefs_weighted_average set video_wa = :video, webpage_wa = :webpage, interactive_wa = :interactive, "
      + " image_wa = :image, text_wa = :text, audio_wa = :audio where user_id = :userId ")
  void updateLearnerPrefsWeightedAverageRecord(@BindBean LearnerPrefsWeightedAverageModel model);
}
