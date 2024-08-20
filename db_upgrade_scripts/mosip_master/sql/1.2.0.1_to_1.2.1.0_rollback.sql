-- Rollback script for master.app_detail
ALTER TABLE master.app_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.app_detail DROP CONSTRAINT pk_appdtl_id;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id, lang_code);

-- Rollback script for master.biometric_attribute
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.biometric_attribute DROP CONSTRAINT pk_bmattr_code;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.module_detail
ALTER TABLE master.module_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.module_detail DROP CONSTRAINT pk_moddtl_code;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id, lang_code);

-- Rollback script for master.template_file_format
ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_tffmt CASCADE;
ALTER TABLE master.template_file_format ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.template_file_format DROP CONSTRAINT pk_tffmt_code;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.template
ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_tffmt FOREIGN KEY (file_format_code, lang_code) REFERENCES master.template_file_format (code, lang_code);


-------------------------------------------------------------------------------------------------------------------------------------------


-- ------------------------------------------------------------------------------------------
-- Revoke script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
-- ------------------------------------------------------------------------------------------
ALTER TABLE master.BATCH_STEP_EXECUTION DROP COLUMN CREATE_TIME;
ALTER TABLE master.BATCH_STEP_EXECUTION ALTER COLUMN START_TIME DROP NOT NULL;
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
