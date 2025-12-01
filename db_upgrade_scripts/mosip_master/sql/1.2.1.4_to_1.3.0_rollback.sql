-- Below script is required to rollback from 1.3.0 to 1.2.1.4 --

\c mosip_master

-- Rollback script for master.app_detail
ALTER TABLE master.app_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.app_detail DROP CONSTRAINT pk_appdtl_id;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id, lang_code);
INSERT INTO master.app_detail SELECT * FROM master.app_detail_bkp WHERE lang_code !='eng';
DROP TABLE IF EXISTS master.app_detail_bkp;


-- Rollback script for master.biometric_attribute
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.biometric_attribute DROP CONSTRAINT pk_bmattr_code;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code, lang_code);
INSERT INTO master.biometric_attribute SELECT * FROM master.biometric_attribute_bkp WHERE lang_code !='eng';
DROP TABLE IF EXISTS master.biometric_attribute_bkp;


-- Rollback script for master.module_detail
ALTER TABLE master.module_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.module_detail DROP CONSTRAINT pk_moddtl_code;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id, lang_code);
INSERT INTO master.module_detail SELECT * FROM master.module_detail_bkp WHERE lang_code !='eng';
DROP TABLE IF EXISTS master.module_detail;


-- Rollback script for master.template_file_format
ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_tffmt CASCADE;
ALTER TABLE master.template_file_format ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.template_file_format DROP CONSTRAINT pk_tffmt_code;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code, lang_code);
INSERT INTO master.template_file_format SELECT * FROM master.template_file_format_bkp WHERE lang_code !='eng';
DROP TABLE IF EXISTS master.template_file_format;

-- Rollback script for master.template
ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_tffmt FOREIGN KEY (file_format_code, lang_code) REFERENCES master.template_file_format (code, lang_code);

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

DROP SEQUENCE IF EXISTS master.BATCH_STEP_EXECUTION_SEQ;
DROP SEQUENCE IF EXISTS master.BATCH_JOB_EXECUTION_SEQ;
DROP SEQUENCE IF EXISTS master.BATCH_JOB_SEQ;


----------ca_cert_store-rollback- db script-------------
ALTER TABLE IF EXISTS master.ca_cert_store DROP COLUMN IF EXISTS ca_cert_type;

-- Below scripts are required to rollback from 1.3.0 to 1.3.0-beta.2 --

-- ROLLBACK FOR PERFORMANCE OPTIMIZATION INDEXES

DROP INDEX IF EXISTS master.idx_ca_cert_store_cr_dtimes;
DROP INDEX IF EXISTS master.idx_ca_cert_store_upd_dtimes;
DROP INDEX IF EXISTS master.idx_ca_cert_store_del_dtimes;

DROP INDEX IF EXISTS idx_ca_cert_times;

DROP INDEX CONCURRENTLY IF EXISTS master.idx_machine_keyindex_not_deleted;
DROP INDEX IF EXISTS master.idx_machine_keyindex_lower;
DROP INDEX IF EXISTS master.idx_mac_master_regcntr_id_id;
DROP INDEX IF EXISTS master.idx_mac_master_cr_dtimes;
DROP INDEX IF EXISTS master.idx_mac_master_upd_dtimes;
DROP INDEX IF EXISTS master.idx_mac_master_del_dtimes;

DROP INDEX IF EXISTS master.uq_mac_master_name_lower;
DROP INDEX IF EXISTS master.uq_mac_master_key_index_lower;

DROP INDEX IF EXISTS master.idx_syncjob_cr_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_upd_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_del_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_active_upd;

DROP MATERIALIZED VIEW IF EXISTS mv_syncjob_max_times;

DROP INDEX CONCURRENTLY IF EXISTS idx_userdetails_regcenter_active;

DROP INDEX IF EXISTS master.idx_user_detail_regcntr_id_active;
DROP INDEX IF EXISTS master.idx_user_detail_sync_window;
DROP INDEX IF EXISTS master.idx_user_detail_timestamps;
DROP INDEX IF EXISTS master.idx_user_detail_active_id;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr_flags;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr_change;

-- END ROLLBACK FOR PERFORMANCE OPTIMIZATION INDEXES
