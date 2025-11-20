-- Upgrade script for master.app_detail
SELECT * INTO master.app_detail_bkp FROM master.app_detail;
DELETE FROM master.app_detail WHERE lang_code !='eng';
ALTER TABLE master.app_detail DROP CONSTRAINT IF EXISTS pk_appdtl_id CASCADE;
ALTER TABLE master.app_detail ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id);

-- Upgrade script for master.biometric_attribute
SELECT * INTO master.biometric_attribute_bkp FROM master.biometric_attribute;
DELETE FROM master.biometric_attribute WHERE lang_code !='eng';
ALTER TABLE master.biometric_attribute DROP CONSTRAINT IF EXISTS pk_bmattr_code CASCADE;
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code);

-- Upgrade script for master.module_detail
SELECT * INTO master.module_detail_bkp FROM master.module_detail;
DELETE FROM master.module_detail WHERE lang_code !='eng';
ALTER TABLE master.module_detail DROP CONSTRAINT IF EXISTS pk_moddtl_code CASCADE;
ALTER TABLE master.module_detail ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id);

-- Upgrade script for master.template_file_format
SELECT * INTO master.template_file_format_bkp FROM master.template_file_format;
ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_tffmt CASCADE;
ALTER TABLE master.template_file_format DROP CONSTRAINT IF EXISTS pk_tffmt_code CASCADE;
DELETE FROM master.template_file_format WHERE lang_code !='eng';
ALTER TABLE master.template_file_format ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code);

-- Upgrade script for master.template
ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_tffmt FOREIGN KEY (file_format_code) REFERENCES master.template_file_format (code);

-- ------------------------------------------------------------------------------------------
-- Upgrade script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
-- ------------------------------------------------------------------------------------------

ALTER TABLE  master.BATCH_STEP_EXECUTION ADD CREATE_TIME TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';
ALTER TABLE  master.BATCH_STEP_EXECUTION ALTER COLUMN START_TIME DROP NOT NULL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DATE_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN LONG_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DOUBLE_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN TYPE_CD TYPE VARCHAR(100);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME TYPE_CD TO PARAMETER_TYPE;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN KEY_NAME TYPE VARCHAR(100);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME KEY_NAME TO PARAMETER_NAME;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN STRING_VAL TYPE VARCHAR(2500);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME STRING_VAL TO PARAMETER_VALUE;
ALTER TABLE  master.BATCH_JOB_EXECUTION DROP COLUMN JOB_CONFIGURATION_LOCATION;

CREATE SEQUENCE  master.BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE  master.BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE  master.BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;


--------ca_cert_store-upgrade-db script------------
ALTER TABLE IF EXISTS master.ca_cert_store ADD COLUMN ca_cert_type character varying(25);

-- UPGRADE FOR PERFORMANCE OPTIMIZATION INDEXES

CREATE INDEX idx_ca_cert_store_cr_dtimes ON master.ca_cert_store (cr_dtimes);
CREATE INDEX idx_ca_cert_store_upd_dtimes ON master.ca_cert_store (upd_dtimes);
CREATE INDEX idx_ca_cert_store_del_dtimes ON master.ca_cert_store (del_dtimes);
CREATE INDEX idx_ca_cert_times ON ca_cert_store (cr_dtimes, upd_dtimes, del_dtimes);

CREATE INDEX CONCURRENTLY idx_machine_keyindex_not_deleted ON master.machine_master (LOWER(key_index)) WHERE is_deleted = false OR is_deleted IS NULL;
CREATE INDEX idx_machine_keyindex_lower ON master.machine_master (LOWER(key_index));
CREATE INDEX IF NOT EXISTS idx_mac_master_regcntr_id_id ON master.machine_master (regcntr_id, id);
CREATE INDEX IF NOT EXISTS idx_mac_master_cr_dtimes ON master.machine_master (cr_dtimes);
CREATE INDEX IF NOT EXISTS idx_mac_master_upd_dtimes ON master.machine_master (upd_dtimes);
CREATE INDEX IF NOT EXISTS idx_mac_master_del_dtimes ON master.machine_master (del_dtimes);
CREATE UNIQUE INDEX IF NOT EXISTS uq_mac_master_name_lower ON master.machine_master (LOWER(name)) WHERE is_deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uq_mac_master_key_index_lower ON master.machine_master (LOWER(key_index)) WHERE is_deleted = false;

CREATE INDEX IF NOT EXISTS idx_syncjob_cr_dtimes ON master.sync_job_def (cr_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_upd_dtimes ON master.sync_job_def (upd_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_del_dtimes ON master.sync_job_def (del_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_active_upd ON master.sync_job_def (upd_dtimes) WHERE is_deleted = false;

CREATE MATERIALIZED VIEW mv_syncjob_max_times AS SELECT MAX(cr_dtimes) AS max_cr, MAX(upd_dtimes) AS max_upd, MAX(del_dtimes) AS max_del FROM master.sync_job_def;
REFRESH MATERIALIZED VIEW mv_syncjob_max_times;

CREATE INDEX CONCURRENTLY idx_userdetails_regcenter_active ON user_detail (regcntr_id) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_id_active ON master.user_detail (regcntr_id) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_sync_window ON master.user_detail (regcntr_id, cr_dtimes, upd_dtimes, del_dtimes);
CREATE INDEX IF NOT EXISTS idx_user_detail_timestamps ON master.user_detail (cr_dtimes, upd_dtimes, del_dtimes);
CREATE INDEX IF NOT EXISTS idx_user_detail_active_id ON master.user_detail (LOWER(id)) WHERE is_active = true AND is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr ON master.user_detail(regcntr_id);
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_flags ON master.user_detail(regcntr_id, is_deleted, is_active);
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_change ON master.user_detail(regcntr_id, cr_dtimes, upd_dtimes, del_dtimes);

---END UPGRADE FOR PERFORMANCE OPTIMIZATION INDEXES--

-- Below script required to upgrade from 1.3.0-B1 to 1.3.0-B2 --
-- ca_cert_type column is added to the ca_cert_store table --
ALTER TABLE IF EXISTS master.ca_cert_store ADD COLUMN ca_cert_type character varying(25);
