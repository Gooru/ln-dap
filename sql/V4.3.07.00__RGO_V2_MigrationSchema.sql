CREATE TABLE learner_profile_competency_status_ts (
  id bigserial PRIMARY KEY,
  tx_subject_code text NOT NULL,
  user_id text NOT NULL,
  gut_code text NOT NULL,
  status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
  created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE(user_id, gut_code, status)
);

CREATE TABLE learner_profile_competency_evidence_ts (
  id BIGSERIAL PRIMARY KEY,
  user_id text NOT NULL,
  gut_code text NOT NULL,
  class_id text,
  course_id text,
  unit_id text,
  lesson_id text,
  latest_session_id text,
  collection_id text,
  collection_path_id bigint,
  collection_score numeric(5,2),
  collection_type text,
  status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
  created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE(user_id, gut_code, collection_id, status)
);

CREATE TABLE learner_profile_micro_competency_evidence_ts (
  id BIGSERIAL PRIMARY KEY,
  user_id text NOT NULL,
  micro_competency_code text NOT NULL,
  class_id text,
  course_id text,
  unit_id text,
  lesson_id text,
  latest_session_id text,
  collection_id text,
  collection_path_id bigint,
  collection_score numeric(5,2),
  collection_type text,
  status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
  created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  UNIQUE(user_id, micro_competency_code, collection_id, status)
);

CREATE TABLE users_profile_master (
  id bigserial PRIMARY KEY,
  user_id text NOT NULL UNIQUE,
  username text,
  reference_id text,
  email text,
  login_type text,
  first_name text,
  last_name text,
  parent_user_id text,
  user_category text,
  roles text,
  birth_date date,
  grade text,
  course text,
  thumbnail text,
  gender text,
  about text,
  school_id text,
  school text,
  school_district_id text,
  school_district text,
  country_id text,
  country text,
  state_id text,
  state text,
  metadata text,
  roster_id text,
  roster_global_userid text,
  tenant_root text,
  tenant_id text,
  partner_id text,
  display_name text,
  is_deleted boolean,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

CREATE TABLE user_grade_map (
  id bigserial PRIMARY KEY,
  user_id text NOT NULL,
  subject_code text NOT NULL,
  subject_name text NOT NULL,
  grade text,
  updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
  UNIQUE (user_id, subject_code)
);

-- NOTE : the vector values are stored as integer and not real or decimal because of they being inexact
-- We need to pick up a integer spread and then scale it down when it is returned by any API
-- We shall take default spread as 1000 that is to say, table will store values from 0 to 1000
CREATE TABLE user_vectors (
  id bigserial PRIMARY KEY,
  user_id text NOT NULL UNIQUE,
  authority integer,
  citizenship integer,
  reputation integer,
  updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

CREATE TABLE course_performance (
  id SERIAL PRIMARY KEY,
  user_id text,
  class_id text,
  class_title text,
  class_code text,
  course_id text,
  course_title text,
  course_asmt_time_spent bigint,
  course_asmt_score numeric(5,2),
  course_coll_time_spent bigint,
  course_coll_score numeric(5,2),
  unit_id text,
  unit_title text,
  unit_sequence_id smallint,
  unit_asmt_time_spent bigint,
  unit_asmt_score numeric(5,2),
  unit_coll_time_spent bigint,
  unit_coll_score numeric(5,2),
  lesson_id text,
  lesson_title text,
  lesson_sequence_id smallint,
  lesson_asmt_time_spent bigint,
  lesson_asmt_score numeric(5,2),
  lesson_coll_time_spent bigint,
  lesson_coll_score numeric(5,2),
  course_assessments_complete smallint,
  total_assessments smallint,
  created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);


--CREATE TABLE rep_monthly_user_competency_stats (
--  id BIGSERIAL PRIMARY KEY,
--  user_id text,
--  in_progress int,
--  completed int,
--  mastered int,
--  month int NOT NULL CHECK(month::int = ANY(ARRAY[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])),
--  year int,
--  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
-- UNIQUE(user_id, month, year)
--);

--CREATE TABLE rep_monthly_user_competency (
--  id BIGSERIAL PRIMARY KEY,
--  user_id text,
--  gut_code text,
--  framework_code text,
--  status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
--  month int NOT NULL CHECK(month::int = ANY(ARRAY[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])),
--  year int,
--  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
--  UNIQUE(user_id, gut_code, month, year)
--);

--CREATE TABLE rep_monthly_user_timespent_stats (
--  id BIGSERIAL PRIMARY KEY,
--  user_id text,
--  content_type text,
--  time_spent bigint,
--  month int NOT NULL CHECK(month::int = ANY(ARRAY[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])),
--  year int,
--  updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
--  UNIQUE(user_id, content_type, month, year)
--);
