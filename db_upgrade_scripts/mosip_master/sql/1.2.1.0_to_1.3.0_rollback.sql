-- ------------------------------------------------------------------------------------------
-- Rollback script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
-- ------------------------------------------------------------------------------------------

ALTER TABLE master.BATCH_STEP_EXECUTION DROP COLUMN CREATE_TIME;
ALTER TABLE master.BATCH_STEP_EXECUTION ALTER COLUMN START_TIME SET NOT NULL;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ADD COLUMN DATE_VAL DATE;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ADD COLUMN LONG_VAL BIGINT;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ADD COLUMN DOUBLE_VAL DOUBLE PRECISION;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_TYPE TO TYPE_CD;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN TYPE_CD TYPE VARCHAR(6);
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_NAME TO KEY_NAME;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN KEY_NAME TYPE VARCHAR(100);
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_VALUE TO STRING_VAL;
ALTER TABLE master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN STRING_VAL TYPE VARCHAR(250);
ALTER TABLE master.BATCH_JOB_EXECUTION ADD COLUMN JOB_CONFIGURATION_LOCATION VARCHAR(2500);

DROP SEQUENCE master.BATCH_STEP_EXECUTION_SEQ;
DROP SEQUENCE master.BATCH_JOB_EXECUTION_SEQ;
DROP SEQUENCE master.BATCH_JOB_SEQ;

DELETE FROM master."template"
WHERE id='3515' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3514' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3513' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3512' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3511' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3510' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3509' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='3508' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3507' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3506' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3505' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3504' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3503' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3502' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='3501' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3500' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3499' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3498' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3497' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3496' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3495' AND lang_code='eng';

DELETE FROM master."template"
WHERE id='1394' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='1394' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='tam';

DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='ara';