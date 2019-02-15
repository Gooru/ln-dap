ALTER TABLE content_competency_evidence ADD COLUMN content_source varchar(128);
ALTER TABLE content_competency_evidence_ts ADD COLUMN content_source varchar(128);
ALTER TABLE learner_profile_competency_evidence ADD COLUMN content_source varchar(128);
ALTER TABLE learner_profile_competency_evidence_ts ADD COLUMN content_source varchar(128);
ALTER TABLE learner_profile_micro_competency_evidence ADD COLUMN content_source varchar(128);
ALTER TABLE learner_profile_micro_competency_evidence_ts ADD COLUMN content_source varchar(128);

UPDATE content_competency_evidence SET content_source = 'coursemap';
UPDATE content_competency_evidence_ts SET content_source = 'coursemap';
UPDATE learner_profile_competency_evidence SET content_source = 'coursemap';
UPDATE learner_profile_competency_evidence_ts SET content_source = 'coursemap';
UPDATE learner_profile_micro_competency_evidence SET content_source = 'coursemap';
UPDATE learner_profile_micro_competency_evidence_ts SET content_source = 'coursemap';
