ALTER TABLE class_performance_data_reports ADD COLUMN week integer;
ALTER TABLE class_performance_data_reports ADD COLUMN subject text;
ALTER TABLE class_performance_data_reports ADD COLUMN framework text;
UPDATE class_performance_data_reports SET week = extract('week' FROM created_at);
ALTER TABLE class_performance_data_reports DROP CONSTRAINT class_performance_data_report_class_id_content_source_month_key, ADD CONSTRAINT cpdr_unique UNIQUE (class_id, content_source, week, month, year);

ALTER TABLE class_competency_data_reports ADD COLUMN week integer;
ALTER TABLE class_competency_data_reports ADD COLUMN subject text;
ALTER TABLE class_competency_data_reports ADD COLUMN framework text;
UPDATE class_competency_data_reports SET week = extract('week' FROM created_at);
ALTER TABLE class_competency_data_reports DROP CONSTRAINT class_competency_data_reports_class_id_month_year_key, ADD CONSTRAINT ccdr_unique UNIQUE (class_id, week, month, year);

ALTER TABLE group_performance_data_reports ADD COLUMN week integer;
ALTER TABLE group_performance_data_reports ADD COLUMN subject text;
ALTER TABLE group_performance_data_reports ADD COLUMN framework text;
UPDATE group_performance_data_reports SET week = extract('week' FROM created_at);
ALTER TABLE group_performance_data_reports DROP CONSTRAINT group_performance_data_report_group_id_content_source_month_key, ADD CONSTRAINT gpdr_unique UNIQUE  (group_id, content_source, week, month, year);

ALTER TABLE group_competency_data_reports ADD COLUMN week integer;
ALTER TABLE group_competency_data_reports ADD COLUMN subject text;
ALTER TABLE group_competency_data_reports ADD COLUMN framework text;
UPDATE group_competency_data_reports SET week = extract('week' FROM created_at);
ALTER TABLE group_competency_data_reports DROP CONSTRAINT group_competency_data_reports_group_id_month_year_key, ADD CONSTRAINT gcdr_unique UNIQUE (group_id, week, month, year);