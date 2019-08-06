package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

public class LearnerPrefsWeightedAverageModel {

  private static final Long NORMALIZED_WEIGHT_BY_MILLION = 1_000_000L;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(LearnerPrefsWeightedAverageModel.class);

  private String userId;
  private Long video = 0L;
  private Long webpage = 0L;
  private Long interactive = 0L;
  private Long image = 0L;
  private Long text = 0L;
  private Long audio = 0L;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getVideo() {
    return video;
  }

  public void setVideo(Long video) {
    this.video = video;
  }

  public Long getWebpage() {
    return webpage;
  }

  public void setWebpage(Long webpage) {
    this.webpage = webpage;
  }

  public Long getInteractive() {
    return interactive;
  }

  public void setInteractive(Long interactive) {
    this.interactive = interactive;
  }

  public Long getImage() {
    return image;
  }

  public void setImage(Long image) {
    this.image = image;
  }

  public Long getText() {
    return text;
  }

  public void setText(Long text) {
    this.text = text;
  }

  public Long getAudio() {
    return audio;
  }

  public void setAudio(Long audio) {
    this.audio = audio;
  }

  public static LearnerPrefsWeightedAverageModel fromContentTypeTimespentList(String userId,
      List<ContentTypeTimespentModel> models) {
    LearnerPrefsWeightedAverageModel result = new LearnerPrefsWeightedAverageModel();
    result.setUserId(userId);

    if (models == null || models.isEmpty()) {
      return result;
    }

    for (ContentTypeTimespentModel model : models) {
      switch (model.getContentType()) {
        case "webpage_resource":
          result.setWebpage(model.getTimeSpent());
          break;
        case "interactive_resource":
          result.setInteractive(model.getTimeSpent());
          break;
        case "audio_resource":
          result.setAudio(model.getTimeSpent());
          break;
        case "video_resource":
          result.setVideo(model.getTimeSpent());
          break;
        case "image_resource":
          result.setImage(model.getTimeSpent());
          break;
        case "text_resource":
          result.setText(model.getTimeSpent());
          break;
        default:
          LOGGER.warn("Invalid or incorrect content-type found: '{}'", model.getContentType());
      }
    }
    return result;
  }

  LearnerPrefWeightedAverageNormalizedBean asLearnerPrefWeightedAverageNormalizedBean() {
    LearnerPrefWeightedAverageNormalizedBean result =
        new LearnerPrefWeightedAverageNormalizedBean();
    result.setUserId(userId);
    Long sum = audio + image + interactive + text + webpage + video;

    if (sum == 0) {
      return result;
    }

    Long interim = calculateWeightedPrefsPerMillion(sum, audio);
    result.audio = interim.intValue();

    interim = (calculateWeightedPrefsPerMillion(sum, text));
    result.text = interim.intValue();

    interim = (calculateWeightedPrefsPerMillion(sum, image));
    result.image = interim.intValue();

    interim = (calculateWeightedPrefsPerMillion(sum, interactive));
    result.interactive = interim.intValue();

    interim = (calculateWeightedPrefsPerMillion(sum, webpage));
    result.webpage = interim.intValue();

    interim = (calculateWeightedPrefsPerMillion(sum, video));
    result.video = interim.intValue();

    adjustForOffset(result);

    return result;
  }

  private void adjustForOffset(LearnerPrefWeightedAverageNormalizedBean result) {
    long offset = NORMALIZED_WEIGHT_BY_MILLION - result.audio - result.video - result.webpage
        - result.text - result.interactive - result.image;

    result.webpage += offset;
  }

  private Long calculateWeightedPrefsPerMillion(Long sum, Long datum) {
    double v = (datum / sum.doubleValue()) * NORMALIZED_WEIGHT_BY_MILLION;
    return (long) v;
  }

  public static class LearnerPrefWeightedAverageNormalizedBean {
    private String userId;
    private int video;
    private int webpage;
    private int interactive;
    private int image;
    private int text;
    private int audio;

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public int getVideo() {
      return video;
    }

    public void setVideo(int video) {
      this.video = video;
    }

    public int getWebpage() {
      return webpage;
    }

    public void setWebpage(int webpage) {
      this.webpage = webpage;
    }

    public int getInteractive() {
      return interactive;
    }

    public void setInteractive(int interactive) {
      this.interactive = interactive;
    }

    public int getImage() {
      return image;
    }

    public void setImage(int image) {
      this.image = image;
    }

    public int getText() {
      return text;
    }

    public void setText(int text) {
      this.text = text;
    }

    public int getAudio() {
      return audio;
    }

    public void setAudio(int audio) {
      this.audio = audio;
    }
  }
}
