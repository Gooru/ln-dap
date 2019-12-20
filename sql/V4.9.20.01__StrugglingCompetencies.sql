CREATE TABLE struggling_competencies_details(
id bigserial PRIMARY KEY,
tx_comp_code text NOT NULL,
user_id text,
month int,
year int,
created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
UNIQUE (tx_comp_code, user_id, month, year)
);