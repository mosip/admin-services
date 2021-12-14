CREATE SCHEMA IF NOT EXISTS master;

CREATE SCHEMA IF NOT EXISTS kernel;

CREATE MEMORY TABLE IF NOT EXISTS master.machine_master(
    id character varying(10)  NOT NULL,
    name character varying(64)  NOT NULL,
    mac_address character varying(64) ,
    serial_num character varying(64) ,
    ip_address character varying(17) ,
    validity_end_dtimes timestamp without time zone,
    mspec_id character varying(36)  NOT NULL,
    public_key character varying(1024) ,
    key_index character varying(128) ,
    sign_public_key character varying(1024) ,
    sign_key_index character varying(128) ,
    zone_code character varying(36)  NOT NULL,
    regcntr_id character varying(10) ,
    lang_code character varying(3) ,
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
    
);

CREATE MEMORY TABLE IF NOT EXISTS master.machine_spec
(
    id character varying(36) NOT NULL,
    name character varying(64) NOT NULL,
    brand character varying(32) NOT NULL,
    model character varying(16) NOT NULL,
    mtyp_code character varying(36) NOT NULL,
    min_driver_ver character varying(16) NOT NULL,
    descr character varying(256) ,
    lang_code character varying(3) ,
    is_active boolean NOT NULL,
    cr_by character varying(256) NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone

);

CREATE MEMORY TABLE IF NOT EXISTS master.user_detail(
    id character varying(256)  NOT NULL,
    name character varying(64) ,
    status_code character varying(36) ,
    regcntr_id character varying(10) ,
    lang_code character varying(3),
    last_login_dtimes timestamp without time zone,
    last_login_method character varying(64),
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
    
);

CREATE  MEMORY TABLE IF NOT EXISTS master.ca_cert_store
(
    cert_id character varying(36) NOT NULL,
    cert_subject character varying(500) NOT NULL,
    cert_issuer character varying(500) NOT NULL,
    issuer_id character varying(36) NOT NULL,
    cert_not_before timestamp without time zone,
    cert_not_after timestamp without time zone,
    crl_uri character varying(120) ,
    cert_data character varying ,
    cert_thumbprint character varying(100) ,
    cert_serial_no character varying(50) ,
    partner_domain character varying(36) ,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean,
    del_dtimes timestamp without time zone
   
);
