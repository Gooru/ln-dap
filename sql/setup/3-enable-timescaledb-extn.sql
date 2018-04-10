

-- You can run this command using the default postgres (aka superuser) user created by postgres:
-- psql -U postgres -f <path to>/3-enable-timescaledb-extn.sql nucleus

-- DROP EXTENSION "timescaledb";

CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;
