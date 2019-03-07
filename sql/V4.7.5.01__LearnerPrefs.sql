CREATE TABLE learner_prefs_per_million (
  user_id text NOT NULL PRIMARY KEY,
  video_pref integer NOT NULL DEFAULT 0,
  webpage_pref integer NOT NULL DEFAULT 0,
  interactive_pref integer NOT NULL DEFAULT 0,
  image_pref integer NOT NULL DEFAULT 0,
  text_pref integer NOT NULL DEFAULT 0,
  audio_pref integer NOT NULL DEFAULT 0
);

COMMENT ON TABLE learner_prefs_per_million is 'Learner prefs value NOT in percent but in per Million. To make it one based divide the values by 1 Million. This is done to reduce comparison costs while reading and getting away from and inexact storage for decimal places. Values are derived from values present in learner_prefs_weighted_average';

CREATE TABLE learner_prefs_weighted_average (
  user_id text NOT NULL PRIMARY KEY,
  video_wa bigint NOT NULL DEFAULT 0,
  webpage_wa bigint  NOT NULL DEFAULT 0,
  interactive_wa bigint  NOT NULL DEFAULT 0,
  image_wa bigint  NOT NULL DEFAULT 0,
  text_wa bigint  NOT NULL DEFAULT 0,
  audio_wa bigint  NOT NULL DEFAULT 0
);

COMMENT ON TABLE learner_prefs_weighted_average is 'Learner prefs weighted average. Initially seeded with just SMA for timespent by user on different activities in seconds. Going forward will be modified based on exponential weighted average.';
