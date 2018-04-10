
-- You can run this command using the default postgres (aka superuser) user created by postgres:
-- psql -U postgres -f <path to>/2-create-db.sql

-- DROP DATABASE dsdb;

CREATE DATABASE dsdb
  WITH OWNER = dsuser
       ENCODING = 'UTF8'
       LC_COLLATE = 'C'
       LC_CTYPE = 'C'
       TEMPLATE template0;

