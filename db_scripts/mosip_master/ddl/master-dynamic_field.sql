
-- object: master.dynamic_field | type: TABLE --
-- DROP TABLE IF EXISTS master.dynamic_field CASCADE;
CREATE TABLE master.dynamic_field(
	id character varying(36) NOT NULL,
	name character varying(36) NOT NULL,
	description character varying(256),
	data_type character varying(16),
	value_json character varying,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean DEFAULT FALSE,
	del_dtimes timestamp,
	CONSTRAINT pk_dynamic_id PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON TABLE master.dynamic_field IS 'Schema Dynamic Fields: Table to store the fields which are used dynamically in MOSIP applications. These fields are also part of identity schema.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.id IS 'ID: Unigue ID generated by MOSIP systema assigned to dynamic fields.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.name IS 'Name: Name of the dynamic field. example location hierarchy name like regin, destrict, zone..etc';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.description IS 'Description: Description of the dynamic field.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.data_type IS 'Data Type: Data type of the dynamic field. ';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.value_json IS 'Value JSON: Value of the dynamic field listed in this column.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.lang_code IS 'Language Code : For multilanguage implementation this attribute Refers master.language.code. The value of some of the attributes in current record is stored in this respective language.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.is_active IS 'IS_Active : Flag to mark whether the record/device is Active or In-active';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.is_deleted IS 'IS_Deleted : Flag to mark whether the record is Soft deleted.';
-- ddl-end --
COMMENT ON COLUMN master.dynamic_field.del_dtimes IS 'Deleted DateTimestamp : Date and Timestamp when the record is soft deleted with is_deleted=TRUE';
-- ddl-end --
