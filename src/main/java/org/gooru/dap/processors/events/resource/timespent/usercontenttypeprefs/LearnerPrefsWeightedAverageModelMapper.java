package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish.
 */

public class LearnerPrefsWeightedAverageModelMapper implements
    ResultSetMapper<LearnerPrefsWeightedAverageModel> {

  private static final String USER_ID = "user_id";
  private static final String VIDEO_WA = "video_wa";
  private static final String WEBPAGE_WA = "webpage_wa";
  private static final String INTERACTIVE_WA = "interactive_wa";
  private static final String IMAGE_WA = "image_wa";
  private static final String TEXT_WA = "text_wa";
  private static final String AUDIO_WA = "audio_wa";

  @Override
  public LearnerPrefsWeightedAverageModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    LearnerPrefsWeightedAverageModel model = new LearnerPrefsWeightedAverageModel();
    model.setUserId(r.getString(USER_ID));
    model.setAudio(r.getLong(AUDIO_WA));
    model.setImage(r.getLong(IMAGE_WA));
    model.setInteractive(r.getLong(INTERACTIVE_WA));
    model.setText(r.getLong(TEXT_WA));
    model.setVideo(r.getLong(VIDEO_WA));
    model.setWebpage(r.getLong(WEBPAGE_WA));

    return model;
  }
}
