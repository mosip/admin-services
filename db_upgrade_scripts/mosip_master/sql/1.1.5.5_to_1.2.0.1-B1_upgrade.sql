\c mosip_master

REASSIGN OWNED BY sysadmin TO postgres;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA master FROM masteruser;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA master FROM sysadmin;

GRANT SELECT, INSERT, TRUNCATE, REFERENCES, UPDATE, DELETE ON ALL TABLES IN SCHEMA master TO masteruser;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA master TO postgres;

ALTER TABLE master.template_type ALTER COLUMN code TYPE character varying(64) ;

ALTER TABLE master.template ALTER COLUMN template_typ_code TYPE character varying(64)  ;

SELECT * INTO master.template_migr_bkp FROM master.template;

-- cleanup to map only registration-client related templates with 10002 moduleId and 
-- other reg email and sms templates mapped to 10002 is remapped to pre-reg moduleId 10001 
-- This cleanup is performed to avoid un-related templates to get synced in reg-client.
UPDATE master.template set module_id=(select distinct id from module_detail where name='Pre-Registration'),module_name='Pre-Registration',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where module_id=(select distinct id from module_detail where name='Registration Client');
UPDATE master.template set module_id=(select distinct id from module_detail where name='Registration Client'),module_name='Registration Client',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where template_typ_code in ('reg-dashboard-template', 'reg-consent-template');
UPDATE master.template set module_id=(select distinct id from module_detail where name='Registration Client'),module_name='Registration Client',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where template_typ_code like 'reg-preview-template-part%';
UPDATE master.template set module_id=(select distinct id from module_detail where name='Registration Client'),module_name='Registration Client',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where template_typ_code like 'reg-ack-template-part%';
UPDATE master.template set module_id=(select distinct id from module_detail where name='Registration Client'),module_name='Registration Client',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where template_typ_code like 'reg-android-ack-template-part%';
UPDATE master.template set module_id=(select distinct id from module_detail where name='Registration Client'),module_name='Registration Client',upd_by='superadmin',upd_dtimes=(now() at time zone('utc')) where template_typ_code like 'reg-android-preview-template-part%';

--------------------------------------------------------------------------------------------------------------------

ALTER TABLE master.blacklisted_words DROP CONSTRAINT IF EXISTS pk_blwrd_code CASCADE;
CREATE TABLE master.blocklisted_words(
	word character varying(128) NOT NULL,
	descr character varying(256),
	lang_code character varying(3),
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean DEFAULT FALSE,
	del_dtimes timestamp,
	CONSTRAINT pk_blwrd_code PRIMARY KEY (word)

);
-- ddl-end --
COMMENT ON TABLE master.blocklisted_words IS 'Black Listed Words : List of words that are black listed.';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.word IS 'Word: Word that is blocklisted by the system';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.descr IS 'Description : Description of word blocklisted';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.lang_code IS 'Language Code : For multilanguage implementation this attribute Refers master.language.code. The value of some of the attributes in current record is stored in this respective language. ';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.is_active IS 'IS_Active : Flag to mark whether the record is Active or In-active';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.is_deleted IS 'IS_Deleted : Flag to mark whether the record is Soft deleted.';
-- ddl-end --
COMMENT ON COLUMN master.blocklisted_words.del_dtimes IS 'Deleted DateTimestamp : Date and Timestamp when the record is soft deleted with is_deleted=TRUE';
-- ddl-end --
GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE,REFERENCES ON master.blocklisted_words TO masteruser;

ALTER TABLE master.blocklisted_words DROP CONSTRAINT IF EXISTS pk_blwrd_code CASCADE;
ALTER TABLE master.blocklisted_words ALTER COLUMN lang_code DROP NOT NULL;
INSERT into master.blocklisted_words (word,descr,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes) SELECT distinct word,descr,lang_code,is_active,cr_by,cr_dtimes,upd_by,upd_dtimes,is_deleted,del_dtimes FROM master.blacklisted_words;
DELETE FROM master.blocklisted_words where lang_code!=:'primary_language_code';
ALTER TABLE master.blocklisted_words ADD CONSTRAINT pk_blwrd_code PRIMARY KEY (word);

-------------------------------------------UI SPEC TABLE ----------------------------------------------

SELECT * INTO master.identity_schema_migr_bkp FROM master.identity_schema;

CREATE TABLE master.ui_spec (
	id character varying(36) NOT NULL,
	version numeric(5,3) NOT NULL,
	domain character varying(36) NOT NULL,
	title character varying(64) NOT NULL,
	description character varying(256) NOT NULL,
	type character varying(36) NOT NULL,
	json_spec character varying NOT NULL,
	identity_schema_id character varying(36) NOT NULL,
	identity_schema_version numeric(5,3) NOT NULL,
	effective_from timestamp,
	status_code character varying(36) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT unq_dmn_type_vrsn_ischmid UNIQUE (domain,type,version,identity_schema_id),
	CONSTRAINT ui_spec_pk PRIMARY KEY (id)

);
-- ddl-end --
COMMENT ON TABLE master.ui_spec IS E'UI Specifications :  Stores UI Specifications with values used in application modules.';
-- ddl-end --
COMMENT ON CONSTRAINT unq_dmn_type_vrsn_ischmid ON master.ui_spec  IS E'Unique Constraint on domain,title,version,identity_schema_id';
-- ddl-end --
GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE,REFERENCES ON master.ui_spec TO masteruser;

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

CREATE TABLE master.permitted_local_config (
	code character varying(128) NOT NULL,
	name character varying(128) NOT NULL,
	config_type character varying(128) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(32) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(32),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT permitted_local_config_pk PRIMARY KEY (code)
);
GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE,REFERENCES ON master.permitted_local_config TO masteruser;

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
