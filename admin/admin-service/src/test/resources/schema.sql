CREATE SCHEMA IF NOT EXISTS master;
CREATE SCHEMA IF NOT EXISTS kernel;
CREATE SCHEMA IF NOT EXISTS admin;

CREATE MEMORY TABLE IF NOT EXISTS master.bulkupload_transaction
(
    id character varying(36)  NOT NULL,
    entity_name character varying(64) NOT NULL,
    upload_operation character varying(64)  NOT NULL,
    status_code character varying(36)  NOT NULL,
    record_count integer,
    uploaded_by character varying(256)  NOT NULL,
    upload_category character varying(36) ,
    uploaded_dtimes timestamp without time zone NOT NULL,
    upload_description character varying ,
    lang_code character varying(3)  NOT NULL,
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
   
);
