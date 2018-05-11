CREATE TABLE userstat_resource_timespent_ts (
  activity_date date NOT NULL,
  user_id text NOT NULL,
  resource_id text NOT NULL,
  content_type text,
  time_spent bigint,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, user_id, resource_id)
);

select create_hypertable('userstat_resource_timespent_ts', 'activity_date');


CREATE TABLE userstat_resource_content_type_timespent_ts (
  activity_date date NOT NULL,
  user_id text NOT NULL,
  content_type text NOT NULL,
  time_spent bigint,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, user_id, content_type)
);

select create_hypertable('userstat_resource_content_type_timespent_ts', 'activity_date');


CREATE TABLE resource_content_type_timespent_ts (
  activity_date date NOT NULL,
  content_type text NOT NULL,
  time_spent bigint,
  view_count integer,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, content_type)
);

select create_hypertable('resource_content_type_timespent_ts', 'activity_date');

CREATE TABLE userstat_original_resource_timespent_ts (
  activity_date date NOT NULL,
  user_id text NOT NULL,
  original_resource_id text NOT NULL,
  content_type text,
  time_spent bigint,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, user_id, original_resource_id)
);

select create_hypertable('userstat_original_resource_timespent_ts', 'activity_date');

CREATE TABLE userstat_cculc_resource_timespent(
  user_id text NOT NULL,
  course_id text NOT NULL,
  unit_id text NOT NULL,
  lesson_id text NOT NULL,
  collection_id text NOT NULL,
  resource_id text NOT NULL,
  content_type  text NOT NULL,
  collection_type text NOT NULL,
  class_id text,
  time_spent bigint NOT NULL,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE(user_id,course_id,unit_id,lesson_id, collection_id, resource_id, class_id)
);

CREATE TABLE userstat_question_timespent_ts (
  activity_date date NOT NULL,
  user_id text NOT NULL,
  question_id text NOT NULL,
  question_type text,
  time_spent bigint,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, user_id, question_id)
);

select create_hypertable('userstat_question_timespent_ts', 'activity_date');


CREATE TABLE userstat_question_type_timespent_ts (
  activity_date date NOT NULL,
  user_id text NOT NULL,
  question_type text NOT NULL,
  time_spent bigint,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, user_id, question_type)
);

select create_hypertable('userstat_question_type_timespent_ts', 'activity_date');


CREATE TABLE question_type_timespent_ts (
  activity_date date NOT NULL,
  question_type text NOT NULL,
  time_spent bigint,
  view_count integer,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE (activity_date, question_type)
);

select create_hypertable('question_type_timespent_ts', 'activity_date');


CREATE TABLE userstat_cculc_question_timespent(
  user_id text NOT NULL,
  course_id text NOT NULL,
  unit_id text NOT NULL,
  lesson_id text NOT NULL,
  collection_id text NOT NULL,
  question_id text NOT NULL,
  question_type  text NOT NULL,
  collection_type text NOT NULL,
  class_id text,
  time_spent bigint NOT NULL,
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE(user_id,course_id,unit_id,lesson_id, collection_id, question_id, class_id)
);

