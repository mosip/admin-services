\c mosip_master

REASSIGN OWNED BY sysadmin TO postgres;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA master FROM masteruser;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA master FROM sysadmin;

GRANT SELECT, INSERT, TRUNCATE, REFERENCES, UPDATE, DELETE ON ALL TABLES IN SCHEMA master TO masteruser;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA master TO postgres;

ALTER TABLE master.template_type ALTER COLUMN code TYPE character varying(64) ;

ALTER TABLE master.template ALTER COLUMN template_typ_code TYPE character varying(64)  ;

SELECT * INTO master.template_migr_bkp FROM master.template;

ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_moddtl CASCADE;

SELECT * INTO master.module_detail_migr_bkp FROM master.module_detail;

TRUNCATE TABLE master.module_detail cascade ;

\COPY master.module_detail (id,name,descr,lang_code,is_active,cr_by,cr_dtimes) FROM './dml/master-module_detail.csv' delimiter ',' HEADER  csv;

ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_moddtl FOREIGN KEY (module_id,lang_code)
REFERENCES master.module_detail (id,lang_code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- cleanup to map only registration-client related templates with 10002 moduleId and 
-- other reg email and sms templates mapped to 10002 is remapped to pre-reg moduleId 10001 
-- This cleanup is performed to avoid un-related templates to get synced in reg-client.
UPDATE master.template set module_id='10001' where module_id='10002' and template_typ_code not like 'reg-%';
UPDATE master.template set module_id='10002' where template_typ_code like 'reg-ack%';
UPDATE master.template set module_id='10002' where template_typ_code like 'reg-preview%';
UPDATE master.template set module_id='10002' where template_typ_code like 'reg-dashboard%';

--------------------------------------------------------------------------------------------------------------------

ALTER TABLE master.blacklisted_words DROP CONSTRAINT IF EXISTS pk_blwrd_code CASCADE;
\ir ../ddl/master-blocklisted_words.sql
ALTER TABLE master.blocklisted_words DROP CONSTRAINT IF EXISTS pk_blwrd_code CASCADE;
ALTER TABLE master.blocklisted_words ALTER COLUMN lang_code DROP NOT NULL;
INSERT into master.blocklisted_words (word,descr,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes) SELECT distinct word,descr,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes FROM master.blacklisted_words;
DELETE FROM master.blocklisted_words where lang_code!=:'primary_language_code';
ALTER TABLE master.blocklisted_words ADD CONSTRAINT pk_blwrd_code PRIMARY KEY (word);

-------------------------------------------UI SPEC TABLE ----------------------------------------------

SELECT * INTO master.identity_schema_migr_bkp FROM master.identity_schema;

\ir ../ddl/master-ui_spec.sql
TRUNCATE TABLE master.ui_spec cascade;
INSERT into master.ui_spec (id,version,domain,title,description,type,json_spec,identity_schema_id,identity_schema_version,effective_from,status_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes) SELECT id,id_version,'registration-client', title,description,'schema',id_attr_json,id,id_version,effective_from,status_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes FROM master.identity_schema;

ALTER TABLE master.ui_spec ALTER COLUMN version TYPE numeric(5,3);
ALTER TABLE master.ui_spec ALTER COLUMN identity_schema_version TYPE numeric(5,3);

ALTER TABLE IF EXISTS master.ui_spec DROP CONSTRAINT IF EXISTS unq_dmn_ttl_vrsn_ischmid;
ALTER TABLE IF EXISTS master.ui_spec DROP CONSTRAINT IF EXISTS unq_dmn_type_vrsn_ischmid;
ALTER TABLE IF EXISTS master.ui_spec ADD CONSTRAINT unq_dmn_type_vrsn_ischmid UNIQUE (domain, type, version, identity_schema_id);

ALTER TABLE master.identity_schema DROP COLUMN id_attr_json;

--------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE master.bulkupload_transaction ALTER COLUMN upload_description TYPE character varying;

-----------------------------------------------ALTER FK constraints with lang code -----------------------------------------------------------

SELECT * INTO master.biometric_type_migr_bkp FROM master.biometric_type;

ALTER TABLE master.biometric_type DROP CONSTRAINT IF EXISTS pk_bmtyp_code CASCADE;
ALTER TABLE master.biometric_type ALTER COLUMN lang_code DROP NOT NULL;
DELETE FROM master.biometric_type where lang_code!=:'primary_language_code';
ALTER TABLE master.biometric_type ADD CONSTRAINT pk_bmtyp_code PRIMARY KEY (code);

ALTER TABLE master.biometric_attribute DROP CONSTRAINT IF EXISTS fk_bmattr_bmtyp CASCADE;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT fk_bmattr_bmtyp FOREIGN KEY (bmtyp_code)
REFERENCES master.biometric_type (code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

SELECT * INTO master.reg_exceptional_holiday_migr_bkp FROM master.reg_exceptional_holiday;

ALTER TABLE master.reg_exceptional_holiday DROP CONSTRAINT IF EXISTS fk_regeh_regcntr CASCADE;
ALTER TABLE master.reg_exceptional_holiday DROP CONSTRAINT IF EXISTS pk_exceptional_hol;
DELETE FROM master.reg_exceptional_holiday where lang_code!=:'primary_language_code';
ALTER TABLE master.reg_exceptional_holiday ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.reg_exceptional_holiday ADD CONSTRAINT pk_exceptional_hol PRIMARY KEY (regcntr_id,hol_date);

-----------------------------------------------------------------------------------------------------------------------

ALTER TABLE master.user_detail_h ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.user_detail_h ALTER COLUMN status_code DROP NOT NULL;
ALTER TABLE master.user_detail ALTER COLUMN status_code DROP NOT NULL;
ALTER TABLE master.user_detail ALTER COLUMN name DROP NOT NULL;
ALTER TABLE master.user_detail ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.user_detail DROP CONSTRAINT IF EXISTS fk_usrdtl_center CASCADE;
ALTER TABLE master.zone_user ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.zone_user DROP CONSTRAINT IF EXISTS fk_zoneuser_zone CASCADE;
ALTER TABLE master.user_detail DROP COLUMN uin;
ALTER TABLE master.user_detail DROP COLUMN email;
ALTER TABLE master.user_detail DROP COLUMN mobile;

ALTER TABLE master.user_detail_h DROP COLUMN uin;
ALTER TABLE master.user_detail_h DROP COLUMN email;
ALTER TABLE master.user_detail_h DROP COLUMN mobile;


ALTER TABLE master.app_authentication_method ALTER COLUMN lang_code DROP NOT NULL;

ALTER TABLE master.app_role_priority ALTER COLUMN lang_code DROP NOT NULL;

--------------------------------------------------------------------------------------------------------

SELECT * INTO master.device_spec_migr_bkp FROM master.device_spec;
SELECT * INTO master.device_type_migr_bkp FROM master.device_type;
SELECT * INTO master.device_master_migr_bkp FROM master.device_master;

ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_zone CASCADE;
ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_dspec CASCADE;
ALTER TABLE master.device_spec DROP CONSTRAINT IF EXISTS fk_dspec_dtyp CASCADE;
ALTER TABLE master.device_type DROP CONSTRAINT IF EXISTS pk_dtyp_code;
ALTER TABLE master.device_spec DROP CONSTRAINT IF EXISTS pk_dspec_code;
ALTER TABLE master.device_master DROP CONSTRAINT IF EXISTS pk_devicem_id;
ALTER TABLE master.device_type ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.device_spec ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.device_master ALTER COLUMN lang_code DROP NOT NULL;
DELETE FROM master.device_type where lang_code!=:'primary_language_code';
DELETE FROM master.device_spec where lang_code!=:'primary_language_code';
ALTER TABLE master.device_type ADD CONSTRAINT pk_dtyp_code PRIMARY KEY (code);
ALTER TABLE master.device_spec ADD CONSTRAINT pk_dspec_code PRIMARY KEY (id);
ALTER TABLE master.device_spec ADD CONSTRAINT fk_dspec_dtyp FOREIGN KEY (dtyp_code)
REFERENCES master.device_type (code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
DELETE FROM master.device_master where lang_code!=:'primary_language_code';
ALTER TABLE master.device_master ADD CONSTRAINT pk_devicem_id PRIMARY KEY (id);
ALTER TABLE master.device_master ADD CONSTRAINT fk_devicem_dspec FOREIGN KEY (dspec_id)
REFERENCES master.device_spec (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE IF EXISTS master.device_master DROP CONSTRAINT IF EXISTS fk_devicem_center;

DELETE FROM master.device_master_h where lang_code!=:'primary_language_code';
ALTER TABLE master.device_master_h DROP CONSTRAINT IF EXISTS pk_devicem_h_id CASCADE;
ALTER TABLE master.device_master_h ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.device_master_h ADD CONSTRAINT pk_devicem_h_id PRIMARY KEY (id,eff_dtimes);

-------------------------------------------------------------------------------------------------------

SELECT * INTO master.machine_spec_migr_bkp FROM master.machine_spec;
SELECT * INTO master.machine_type_migr_bkp FROM master.machine_type;
SELECT * INTO master.machine_master_migr_bkp FROM master.machine_master;

ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_mspec CASCADE;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_zone CASCADE;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS fk_machm_center CASCADE;
ALTER TABLE master.machine_spec DROP CONSTRAINT IF EXISTS fk_mspec_mtyp CASCADE;
ALTER TABLE master.machine_type DROP CONSTRAINT IF EXISTS pk_mtyp_code;
ALTER TABLE master.machine_spec DROP CONSTRAINT IF EXISTS pk_mspec_code;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS pk_machm_id;
ALTER TABLE master.machine_type ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.machine_master ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.machine_spec ALTER COLUMN lang_code DROP NOT NULL;
DELETE FROM master.machine_type where lang_code!=:'primary_language_code';
DELETE FROM master.machine_spec where lang_code!=:'primary_language_code';
DELETE FROM master.machine_master where lang_code!=:'primary_language_code';
ALTER TABLE master.machine_type ADD CONSTRAINT pk_mtyp_code PRIMARY KEY (code);
ALTER TABLE master.machine_spec ADD CONSTRAINT pk_mspec_code PRIMARY KEY (id);
ALTER TABLE master.machine_master ADD CONSTRAINT pk_machm_id PRIMARY KEY (id);
ALTER TABLE master.machine_spec ADD CONSTRAINT fk_mspec_mtyp FOREIGN KEY (mtyp_code)
REFERENCES master.machine_type (code) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE master.machine_master ADD CONSTRAINT fk_machm_mspec FOREIGN KEY (mspec_id)
REFERENCES master.machine_spec (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

DELETE FROM master.machine_master_h where lang_code!=:'primary_language_code';
ALTER TABLE master.machine_master_h DROP CONSTRAINT IF EXISTS pk_machm_h_id CASCADE;
ALTER TABLE master.machine_master_h ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.machine_master_h ADD CONSTRAINT pk_machm_h_id PRIMARY KEY (id,eff_dtimes);


ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_name;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_key_index;
ALTER TABLE master.machine_master DROP CONSTRAINT IF EXISTS uq_machm_skey_index;

SELECT * INTO master.machine_master_migr_dupes FROM (SELECT *, count(*) OVER (PARTITION BY name) AS count FROM machine_master) machine_master_count WHERE machine_master_count.count > 1;

DELETE FROM machine_master WHERE id IN (SELECT id FROM (SELECT id, ROW_NUMBER() OVER( PARTITION BY name  ORDER BY  id ) AS row_num FROM machine_master ) t WHERE t.row_num > 1 );

ALTER TABLE master.machine_master ADD CONSTRAINT uq_machm_name UNIQUE (name);
ALTER TABLE master.machine_master ADD CONSTRAINT uq_machm_key_index UNIQUE (key_index);
ALTER TABLE master.machine_master ADD CONSTRAINT uq_machm_skey_index UNIQUE (sign_key_index);


----------------------------------------------------------------------------------------------------------------------

ALTER TABLE master.batch_job_execution_params ALTER COLUMN string_val TYPE varchar(5000) USING string_val::varchar;

----------------------------------------------CREATION OF PERMITTED LOCAL CONFIG -------------------------------------------------------------

\ir ../ddl/master-permitted_local_config.sql

-------------------------------------------------------------------------------------------------------------------------------------------
SELECT * INTO master.loc_holiday_migr_bkp FROM master.loc_holiday;

ALTER TABLE master.loc_holiday DROP CONSTRAINT IF EXISTS pk_lochol_id;

SELECT * INTO master.loc_holiday_migr_dupes FROM (SELECT *, count(*) OVER (PARTITION BY holiday_date, location_code, lang_code) AS count FROM loc_holiday) loc_holiday_count WHERE loc_holiday_count.count > 1;

DELETE FROM loc_holiday WHERE id IN (SELECT id FROM (SELECT id, ROW_NUMBER() OVER( PARTITION BY holiday_date, location_code, lang_code ORDER BY  id ) AS row_num FROM loc_holiday ) t WHERE t.row_num > 1 );

ALTER TABLE master.loc_holiday ADD CONSTRAINT pk_lochol_id PRIMARY KEY (holiday_date, location_code, lang_code);

-------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE IF EXISTS master.app_authentication_method DROP CONSTRAINT IF EXISTS fk_appauthm_authmeth;

SELECT * INTO master.authentication_method_migr_bkp FROM master.authentication_method;

DELETE FROM master.authentication_method where lang_code!=:'primary_language_code';
ALTER TABLE master.authentication_method DROP CONSTRAINT IF EXISTS pk_authm_code CASCADE;
ALTER TABLE master.authentication_method ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.authentication_method ADD CONSTRAINT pk_authm_code PRIMARY KEY (code);

CREATE INDEX IF NOT EXISTS idx_location_cr_dtimes ON master.location USING btree (cr_dtimes ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_mac_master_cr_dtimes ON master.machine_master USING btree (cr_dtimes ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_mac_master_cntr_id ON master.machine_master USING btree (regcntr_id COLLATE pg_catalog."default" ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_mac_master_regcntr_id ON master.machine_master USING btree (regcntr_id COLLATE pg_catalog."default" ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_device_master_cntr_id ON master.device_master USING btree (regcntr_id COLLATE pg_catalog."default" ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_user_detail_cntr_id ON master.user_detail USING btree (regcntr_id COLLATE pg_catalog."default" ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_reg_centr_loc_code ON master.registration_center USING btree (holiday_loc_code COLLATE pg_catalog."default" ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_app_val_doc_cr_dtimes ON master.applicant_valid_document USING btree (cr_dtimes ASC NULLS LAST) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS idx_app_val_doc_upd_dtimes ON master.applicant_valid_document USING btree (upd_dtimes ASC NULLS LAST) TABLESPACE pg_default;


ALTER TABLE IF EXISTS master.zone_user DROP CONSTRAINT IF EXISTS pk_zoneuser;
ALTER TABLE IF EXISTS master.zone_user ALTER COLUMN zone_code DROP NOT NULL;
ALTER TABLE IF EXISTS master.zone_user ADD CONSTRAINT pk_zoneuser PRIMARY KEY (usr_id);

--- applicant_valid_document is no more in use, just required for backward compatibility
ALTER TABLE IF EXISTS master.applicant_valid_document ALTER COLUMN lang_code DROP NOT NULL;

SELECT * INTO master.ca_cert_store_migr_bkp FROM master.ca_cert_store;
ALTER TABLE IF EXISTS master.ca_cert_store DROP COLUMN IF EXISTS signed_cert_data;
ALTER TABLE IF EXISTS master.ca_cert_store DROP COLUMN IF EXISTS key_usage;
ALTER TABLE IF EXISTS master.ca_cert_store DROP COLUMN IF EXISTS organization_name;

ALTER TABLE IF EXISTS master.template DROP CONSTRAINT IF EXISTS fk_tmplt_moddtl;

SELECT * INTO master.reg_working_nonworking_migr_bkp FROM master.reg_working_nonworking;
DELETE FROM master.reg_working_nonworking where lang_code!=:'primary_language_code';
ALTER TABLE master.reg_working_nonworking DROP CONSTRAINT IF EXISTS pk_working_nonworking CASCADE;
ALTER TABLE master.reg_working_nonworking ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.reg_working_nonworking ADD CONSTRAINT pk_working_nonworking PRIMARY KEY (regcntr_id,day_code);

ALTER TABLE master.reg_working_nonworking DROP CONSTRAINT IF EXISTS fk_rwn_daycode;
ALTER TABLE master.reg_working_nonworking DROP CONSTRAINT IF EXISTS fk_rwn_regcntr;

SELECT * INTO master.valid_document_migr_bkp FROM master.valid_document;
DELETE FROM master.valid_document where lang_code!=:'primary_language_code';
ALTER TABLE master.valid_document DROP CONSTRAINT IF EXISTS pk_valdoc_code CASCADE;
ALTER TABLE master.valid_document ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.valid_document ADD CONSTRAINT pk_valdoc_code PRIMARY KEY (doctyp_code,doccat_code);
