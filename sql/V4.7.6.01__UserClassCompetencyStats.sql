--
-- Name: user_class_competency_stats; Type: TABLE; Schema: public; Owner: dsuser
--

CREATE TABLE public.user_class_competency_stats (
    user_id character varying NOT NULL,
    class_id character varying NOT NULL,
    course_id character varying NOT NULL,
    grade_id bigint,
    total bigint,
    completed bigint,
    in_progress bigint,
    score numeric(5,2),
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    subject_code character varying,
    percent_completed numeric(5,2),
    month smallint,
    year smallint,
    stats_date date,
    class_grade_id bigint,
    class_total bigint
);


ALTER TABLE ONLY public.user_class_competency_stats
    ADD CONSTRAINT user_class_course_month_year_unique UNIQUE (user_id, class_id, course_id, month, year);

