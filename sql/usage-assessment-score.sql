CREATE TABLE user_competency_matrix (
    id bigserial PRIMARY KEY,
    tx_subject_code text NOT NULL,
    user_id text NOT NULL,
    gut_code text NOT NULL,
    status smallint NOT NULL DEFAULT 0 CHECK(status::smallint = ANY(ARRAY[0, 1, 2, 3, 4, 5])),
    created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    UNIQUE(user_id, gut_code) 
);

CREATE TABLE competency_status ( 
    id BIGSERIAL PRIMARY KEY,
    user_id text,
    competency_code text,
    framework_code text,
    status text NOT NULL CHECK(status::text = ANY(ARRAY['in_progress', 'completed'])),
    created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    UNIQUE(user_id, competency_code)
);

CREATE TABLE competency_assessment_score (
    id BIGSERIAL PRIMARY KEY,
    user_id text NOT NULL,
    competency_code text,
	competency_display_code text,
	competency_title text,
    framework_code text,
    gut_code text,
    class_id text,
    course_id text NOT NULL,
    unit_id text NOT NULL,
    lesson_id text NOT NULL,
    latest_session_id text,
    collection_id text NOT NULL,
    collection_path_id bigint,
    collection_score numeric(5,2),
    collection_type text,
    created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    UNIQUE(user_id, gut_code, course_id, unit_id, lesson_id, collection_id) 
);
