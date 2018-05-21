CREATE TABLE learner_profile_competency_status (
	id bigserial PRIMARY KEY,
	tx_subject_code text NOT NULL,
	user_id text NOT NULL,
	gut_code text NOT NULL,
	status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(user_id, gut_code)
);

CREATE TABLE learner_profile_competency_evidence (
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
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(user_id, gut_code, collection_id)
);

CREATE TABLE learner_profile_micro_competency_evidence (
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
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE(user_id, micro_competency_code, collection_id)
);

CREATE TABLE content_competency_status (
	id BIGSERIAL PRIMARY KEY,
	user_id text,
	competency_code text,
	framework_code text,
	status smallint NOT NULL CHECK(status::smallint = ANY(ARRAY[1, 2, 4])),
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(user_id, competency_code)
);

CREATE TABLE content_competency_evidence (
	id BIGSERIAL PRIMARY KEY,
	user_id text NOT NULL,
	competency_code text NOT NULL,
	framework_code text,
	is_micro_competency boolean NOT NULL default false,
	gut_code text,
	class_id text,
	course_id text,
	unit_id text,
	lesson_id text,
	latest_session_id text,
	collection_id text,
	collection_path_id bigint,
	collection_score numeric(5,2),
	collection_type text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(user_id, competency_code, collection_id)
);



