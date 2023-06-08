\c mosip_master sysadmin

TRUNCATE TABLE master.template cascade ;

TRUNCATE TABLE master.module_detail cascade ;

INSERT INTO master.module_detail SELECT * FROM master.module_detail_migr_bkp;

INSERT INTO master.template SELECT * FROM master.template_migr_bkp;

DROP TABLE IF EXISTS master.module_detail_migr_bkp;

DROP TABLE IF EXISTS master.template_migr_bkp;

DROP TABLE IF EXISTS master.blocklisted_words;

ALTER TABLE master.blacklisted_words DROP CONSTRAINT IF EXISTS pk_blwrd_code CASCADE;

ALTER TABLE master.blacklisted_words ADD CONSTRAINT pk_blwrd_code PRIMARY KEY (word, lang_code);

DROP TABLE IF EXISTS master.ui_spec;

TRUNCATE TABLE master.identity_schema;

ALTER TABLE master.identity_schema ADD COLUMN IF NOT EXISTS id_attr_json character varying(20480);

INSERT INTO master.identity_schema(id,id_version,title,description,id_attr_json,schema_json,status_code,add_props,effective_from,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes) SELECT id,id_version,title,description,id_attr_json,schema_json,status_code,add_props,effective_from,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes FROM master.identity_schema_migr_bkp;

DROP TABLE IF EXISTS master.identity_schema_migr_bkp;

ALTER TABLE master.bulkupload_transaction ALTER COLUMN upload_description TYPE character varying(256);

TRUNCATE TABLE master.biometric_type cascade;

ALTER TABLE master.biometric_type DROP CONSTRAINT IF EXISTS pk_bmtyp_code CASCADE;

ALTER TABLE master.biometric_type ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.biometric_type ADD CONSTRAINT pk_bmtyp_code PRIMARY KEY (code, lang_code);

INSERT INTO master.biometric_type SELECT * FROM master.biometric_type_migr_bkp;

DROP TABLE IF EXISTS master.biometric_type_migr_bkp;

ALTER TABLE master.biometric_attribute DROP CONSTRAINT IF EXISTS fk_bmattr_bmtyp CASCADE;

ALTER TABLE master.biometric_attribute ADD CONSTRAINT fk_bmattr_bmtyp FOREIGN KEY (bmtyp_code, lang_code)
REFERENCES master.biometric_type (code, lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE master.user_detail_h ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.user_detail_h ALTER COLUMN status_code set  NOT NULL;

ALTER TABLE master.user_detail ALTER COLUMN status_code set NOT NULL;

ALTER TABLE master.user_detail ALTER COLUMN name set NOT NULL;

ALTER TABLE master.user_detail ALTER COLUMN lang_code set  NOT NULL;

ALTER TABLE master.zone_user ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.user_detail ADD COLUMN IF NOT EXISTS uin character varying(28);

ALTER TABLE master.user_detail ADD COLUMN IF NOT EXISTS email character varying(256);

ALTER TABLE master.user_detail ADD COLUMN IF NOT EXISTS mobile character varying(16);

ALTER TABLE master.user_detail_h ADD COLUMN IF NOT EXISTS uin character varying(28);

ALTER TABLE master.user_detail_h ADD COLUMN IF NOT EXISTS email character varying(256);

ALTER TABLE master.user_detail_h ADD COLUMN IF NOT EXISTS mobile character varying(16);

ALTER TABLE master.app_authentication_method ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.app_role_priority ALTER COLUMN lang_code set NOT NULL;


TRUNCATE TABLE master.reg_exceptional_holiday;

ALTER TABLE master.reg_exceptional_holiday ALTER COLUMN lang_code set NOT NULL;

INSERT INTO master.reg_exceptional_holiday SELECT * FROM master.reg_exceptional_holiday_migr_bkp;

DROP TABLE IF EXISTS master.reg_exceptional_holiday_migr_bkp;

DROP TABLE IF EXISTS master.permitted_local_config;


------------------------------------------------

ALTER TABLE master.device_type ALTER COLUMN lang_code set NOT NULL;
ALTER TABLE master.device_spec ALTER COLUMN lang_code set NOT NULL;
ALTER TABLE master.device_master ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_dspec CASCADE;
ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_zone CASCADE;
ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_center CASCADE;
ALTER TABLE master.device_spec DROP CONSTRAINT IF EXISTS fk_dspec_dtyp CASCADE;

ALTER TABLE master.device_type DROP CONSTRAINT IF EXISTS pk_dtyp_code;
ALTER TABLE master.device_spec DROP CONSTRAINT IF EXISTS pk_dspec_code;
ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS pk_devicem_id;

ALTER TABLE master.device_type ADD CONSTRAINT pk_dtyp_code PRIMARY KEY (code, lang_code);
ALTER TABLE master.device_spec ADD CONSTRAINT pk_dspec_code PRIMARY KEY (id, lang_code);
ALTER TABLE master.device_master ADD CONSTRAINT pk_devicem_id PRIMARY KEY (id,lang_code);

ALTER TABLE master.device_spec ADD CONSTRAINT fk_dspec_dtyp FOREIGN KEY (dtyp_code,lang_code)
REFERENCES master.device_type (code,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.device_master ADD CONSTRAINT fk_devicem_dspec FOREIGN KEY (dspec_id,lang_code)
REFERENCES master.device_spec (id,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.device_master ADD CONSTRAINT fk_devicem_zone FOREIGN KEY (zone_code,lang_code)
REFERENCES master.zone (code,lang_code) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.device_master ADD CONSTRAINT fk_devicem_center FOREIGN KEY (regcntr_id,lang_code)
REFERENCES master.registration_center (id,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

truncate table master.device_master cascade;
truncate table master.device_spec cascade;
truncate table master.device_type cascade;

INSERT INTO master.device_type SELECT * FROM master.device_type_migr_bkp;
INSERT INTO master.device_spec SELECT * FROM master.device_spec_migr_bkp;
INSERT INTO master.device_master SELECT * FROM master.device_master_migr_bkp;

DROP TABLE IF EXISTS master.device_type_migr_bkp;
DROP TABLE IF EXISTS master.device_spec_migr_bkp;
DROP TABLE IF EXISTS master.device_master_migr_bkp;

----------------------------------------------------------

ALTER TABLE master.machine_type ALTER COLUMN lang_code set NOT NULL;
ALTER TABLE master.machine_spec ALTER COLUMN lang_code set NOT NULL;
ALTER TABLE master.machine_master ALTER COLUMN lang_code set NOT NULL;

ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_mspec CASCADE;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_zone CASCADE;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_center CASCADE;
ALTER TABLE master.machine_spec DROP CONSTRAINT IF EXISTS fk_mspec_mtyp CASCADE;

ALTER TABLE master.machine_type DROP CONSTRAINT IF EXISTS pk_mtyp_code;
ALTER TABLE master.machine_spec DROP CONSTRAINT IF EXISTS pk_mspec_code;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS pk_machm_id;

ALTER TABLE master.machine_type ADD CONSTRAINT pk_mtyp_code PRIMARY KEY (code, lang_code);
ALTER TABLE master.machine_spec ADD CONSTRAINT pk_mspec_code PRIMARY KEY (id, lang_code);
ALTER TABLE master.machine_master ADD CONSTRAINT pk_machm_id PRIMARY KEY (id, lang_code);

ALTER TABLE master.machine_spec ADD CONSTRAINT fk_mspec_mtyp FOREIGN KEY (mtyp_code,lang_code)
REFERENCES master.machine_type (code,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.machine_master ADD CONSTRAINT fk_machm_mspec FOREIGN KEY (mspec_id,lang_code)
REFERENCES master.machine_spec (id,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.machine_master ADD CONSTRAINT fk_machm_zone FOREIGN KEY (zone_code,lang_code)
REFERENCES master.zone (code,lang_code) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE master.machine_master ADD CONSTRAINT fk_machm_center FOREIGN KEY (regcntr_id,lang_code)
REFERENCES master.registration_center (id,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

truncate table master.machine_master cascade;
truncate table master.machine_spec cascade;
truncate table master.machine_type cascade;

ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_name;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_key_index;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_skey_index;

INSERT INTO master.machine_type SELECT * FROM master.machine_type_migr_bkp;
INSERT INTO master.machine_spec SELECT * FROM master.machine_spec_migr_bkp;
INSERT INTO master.machine_master SELECT * FROM master.machine_master_migr_bkp;

DROP TABLE IF EXISTS master.machine_type_migr_bkp;
DROP TABLE IF EXISTS master.machine_spec_migr_bkp;
DROP TABLE IF EXISTS master.machine_master_migr_bkp;
DROP TABLE IF EXISTS master.machine_master_migr_dupes;

--------------------------------------------------------------------------------------------

truncate table master.loc_holiday cascade;

ALTER TABLE master.loc_holiday DROP CONSTRAINT IF EXISTS pk_lochol_id;

ALTER TABLE master.loc_holiday ADD CONSTRAINT pk_lochol_id PRIMARY KEY (id,location_code,lang_code);

INSERT INTO master.loc_holiday SELECT * FROM master.loc_holiday_migr_bkp;

DROP TABLE IF EXISTS master.loc_holiday_migr_dupes;

DROP TABLE IF EXISTS master.loc_holiday_migr_bkp;

-----------------------------------------------------------------------------------




