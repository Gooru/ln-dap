-- CONVENTION
-- Tables have different suffixes
--      _data: Implies this table is used as a view to API. The data in this table is populated using events with some transformation
--      _data_ml: Implies this table is used as a view to API. The data is populated using some processing job probably using ML
--      _tx: taxonomy related static tables
--      _ts: This can follow other suffixes and if present should be last. This will imply that table is hypertable (ts implies it stores time series data)
--      _master: Kind of metadata tables. The SSE may happen to modify this table



-- Sample queries (assuming duration of 3 days)

-- Total active users irrespective of subject for given days
--  select count(distinct(user_id)) from user_activity_data_ts where activity_date <= current_date and activity_date >= current_date - 3;
-- Total active users for a subject for given days
--  select count(distinct(user_id)) from user_activity_data_ts where activity_date < current_date and activity_date >= current_date - 3 and subject_code = 'sub1';
-- Total active users for a zoom code for given days
--  select count(distinct(a.user_id)) from user_activity_data_ts inner join user_profile p on a.user_id = p.user_id where a.activity_date < current_date and a.activity_date >= current_date - 3 and p.zoom_code = 'zoom1';
-- Total active users for a zoom code and subject for given days
--  select count(distinct(a.user_id)) from user_activity_data_ts inner join user_profile p  on a.user_id = p.user_id where a.activity_date < current_date and a.activity_date >= current_date - 3 and p.zoom_code = 'zoom1' and a.subject_code = 'sub1';
-- Other queries can be run are to find total users for a subject without passing in duration


CREATE TABLE user_activity_data_ts (
    activity_date date NOT NULL,
    user_id text NOT NULL,
    activity_count bigint,
    subject_code character varying(512) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE (activity_date, user_id, subject_code)
);

COMMENT on TABLE user_activity_data_ts is 'Table used to serve data to API for queries like active users for duration, active users for specified subject for specified duration. To query on active users for specified subject, specified zoom code and specified zoom, this table needs to be joined with table which is storing user profile.';

select create_hypertable('user_activity_data_ts', 'activity_date');  

CREATE TABLE user_profile_master (
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
    zoom_code1 text,
    zoom_code2 text,
    zoom_code3 text,
    zoom_code4 text,
    zoom_code5 text,
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

COMMENT on table user_profile_master is 'This table stores the data from core db for user profile. It also stores the zoom code associated with the users in this table. Note that there are all five zoom values stored even though they may overlap with other cols like country, state. This is to avoid confusion about country code or id should be used on zoom value. This table can also be queried for total users.';


CREATE TABLE user_grade_map_data_ml (
    id bigserial PRIMARY KEY,
    user_id text NOT NULL,
    subject_code text NOT NULL,
    grade text,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,    
    UNIQUE (user_id, subject_code)
);

COMMENT on table user_grade_map_data_ml is 'This table stores the relevant grade for user per subject. This needs to be inferred and populated as this information is not readily available in user profile';


CREATE TABLE user_vectors_data_ml (
    id bigserial PRIMARY KEY,
    user_id text NOT NULL UNIQUE,
    authority integer,
    citizenship integer,
    reputation integer,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

COMMENT on table user_vectors_data_ml is 'This table stores the relevant user vectors per user. This should be populated using sciences based on other activites in system. The vector values are stored as integer and not real or decimal because of they being inexact. We need to pick up a integer spread and then scale it down when it is returned by any API. We shall take default spread as 1000 that is to say, table will store values from 0 to 1000';



CREATE TABLE user_prefs_content_data_ml (
    id bigserial PRIMARY KEY,
    user_id text NOT NULL UNIQUE,
    audio integer,
    interactive integer,
    webpage integer,
    text integer,
    video integer,
    image integer,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

COMMENT on table user_prefs_content_data_ml is 'Note that prefs values are stored as INT which are scaled to 1000. API should scale it down between 0 and 1 before returning it';


CREATE TABLE user_prefs_curator_ml (
    id bigserial PRIMARY KEY,
    user_id text NOT NULL,
    curator_id bigint NOT NULL,
    pref integer,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE(user_id, curator_id)
);

COMMENT on table user_prefs_curator_ml is 'Note that prefs values are stored as INT which are scaled to 1000. API should scale it down between 0 and 1 before returning it';


CREATE TABLE user_prefs_provider_ml (
    id bigserial PRIMARY KEY,
    user_id text NOT NULL,
    provider_id bigint NOT NULL,
    pref integer,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE(user_id, provider_id)
);

COMMENT on table user_prefs_provider_ml is 'Note that prefs values are stored as INT which are scaled to 1000. API should scale it down between 0 and 1 before returning it';

-- This section is about the master tables related to taxonomy and competency matrix

CREATE TABLE courses_tx (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    tx_course_code text NOT NULL,
    tx_course_name text NOT NULL,
    tx_course_seq int NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE(tx_subject_code, tx_course_code)
);

CREATE TABLE domains_tx (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    tx_domain_code text NOT NULL,
    tx_domain_name text NOT NULL,
    tx_domain_seq int NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE(tx_subject_code, tx_domain_code)
);


CREATE TABLE domain_competency_matrix_tx (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    tx_domain_code text NOT NULL,
    tx_comp_code text NOT NULL,
    tx_comp_name text NOT NULL,
    tx_comp_desc text,
    tx_comp_student_desc text,
    tx_comp_seq smallint NOT NULL DEFAULT 0,
    UNIQUE(tx_subject_code, tx_domain_code, tx_comp_code)
);

CREATE TABLE course_competency_matrix_tx (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    tx_course_code text NOT NULL,
    tx_comp_code text NOT NULL,
    tx_comp_name text NOT NULL,
    tx_comp_desc text,
    tx_comp_student_desc text,
    tx_comp_seq smallint NOT NULL DEFAULT 0,
    UNIQUE(tx_subject_code, tx_course_code, tx_comp_code)
);

CREATE TABLE competency_matrix_tx (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    tx_course_code text NOT NULL,
    tx_domain_code text NOT NULL,
    tx_comp_code text NOT NULL,
    tx_comp_name text NOT NULL,
    tx_comp_desc text,
    tx_comp_student_desc text,
    tx_comp_seq int NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    UNIQUE(tx_subject_code, tx_course_code, tx_domain_code, tx_comp_code)
);

-- Curators and Providers master data

CREATE TABLE curators_master (
    id bigserial PRIMARY KEY,
    name text NOT NULL UNIQUE,
    thumbnail text,
    short_name text
);

CREATE TABLE providers_master (
    id bigserial PRIMARY KEY,
    name text NOT NULL UNIQUE,
    thumbnail text
);

CREATE TABLE course_completion_ts (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
course_id text NOT NULL,
items_completed bigint,
activity_date date NOT NULL,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
UNIQUE (activity_date, user_id, course_id));

select create_hypertable('course_completion_ts', 'activity_date');

CREATE TABLE course_score_ts (
id SERIAL PRIMARY KEY,
user_id text NOT NULL,
course_id text NOT NULL,
score numeric(5,2),
activity_date date NOT NULL,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
UNIQUE (activity_date, user_id, course_id));

select create_hypertable('course_completion_ts', 'activity_date');

CREATE TABLE course_timespent_ts (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
course_id text NOT NULL,
time_spent bigint,
activity_date date NOT NULL,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

select create_hypertable('course_timespent_ts', 'activity_date');

CREATE TABLE course_timespent (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
course_id text NOT NULL,
time_spent bigint,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
UNIQUE (activity_date, user_id, course_id));

CREATE TABLE ul_score (
id SERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text NOT NULL,
unit_id text NOT NULL,
unit_sequence_id smallint,
unit_score numeric(5,2),
lesson_id text NOT NULL,
lesson_sequence_id smallint,
lesson_score numeric(5,2),
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE ul_timespent (
id SERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text NOT NULL,
unit_id text NOT NULL,
unit_sequence_id smallint,
unit_time_spent bigint,
lesson_id text NOT NULL,
lesson_sequence_id smallint,
lesson_time_spent bigint,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE course_assessment_score (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text,
unit_id text,
lesson_id text,
latest_session_id text,
assessment_id text NOT NULL,
path_id bigint,
score numeric(5,2),
num_attempts smallint,
sequence_id text,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE course_assessment_timespent (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text NOT NULL,
unit_id text,
lesson_id text,
assessment_id text NOT NULL,
path_id bigint,
time_spent bigint,
sequence_id text,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE course_collection_score (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text NOT NULL,
unit_id text,
lesson_id text,
collection_id text NOT NULL,
path_id bigint,
score numeric(5,2),
sequence_id text,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE course_collection_timespent (
id BIGSERIAL PRIMARY KEY,
user_id text NOT NULL,
class_id text,
course_id text NOT NULL,
unit_id text,
lesson_id text,
collection_id text NOT NULL,
path_id bigint,
time_spent bigint,
sequence_id text,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'));

CREATE TABLE userstat_resource_timespent_ts (
activity_date date NOT NULL,
user_id text NOT NULL,
resource_id text NOT NULL,
content_type text,
time_spent bigint,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE (activity_date, user_id, resource_id));

select create_hypertable('userstat_resource_timespent_ts', 'activity_date');


CREATE TABLE userstat_resource_content_type_timespent_ts (
activity_date date NOT NULL,
user_id text NOT NULL,
content_type text NOT NULL,
time_spent bigint,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE (activity_date, user_id, content_type));

select create_hypertable('userstat_resource_content_type_timespent_ts', 'activity_date');


CREATE TABLE resource_content_type_timespent_ts (
activity_date date NOT NULL,
content_type text NOT NULL,
time_spent bigint,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE (activity_date, content_type));

select create_hypertable('resource_content_type_timespent_ts', 'activity_date');

CREATE TABLE userstat_original_resource_timespent_ts (
activity_date date NOT NULL,
user_id text NOT NULL,
original_resource_id text NOT NULL,
content_type text,
time_spent bigint,
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE (activity_date, user_id, original_resource_id));

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
UNIQUE(user_id,course_id,unit_id,lesson_id, collection_id, resource_id, class_id));




