CREATE SCHEMA IF NOT EXISTS master;
CREATE SCHEMA IF NOT EXISTS kernel;

CREATE MEMORY TABLE IF NOT EXISTS master.appl_form_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	
);

CREATE MEMORY TABLE if not exists master.location(
	code character varying(36) NOT NULL,
	name character varying(128) NOT NULL,
	hierarchy_level smallint NOT NULL,
	hierarchy_level_name character varying(64) NOT NULL,
	parent_loc_code character varying(36),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp

	
);
CREATE MEMORY TABLE if not exists master.language(
	code character varying(3) NOT NULL,
	name character varying(64) NOT NULL,
	family character varying(64),
	native_name character varying(64),
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE if not exists master.loc_hierarchy_list(
	hierarchy_level smallint NOT NULL,
	hierarchy_level_name character varying(64) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);


CREATE MEMORY TABLE if not exists master.biometric_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp	
	

);   
--ALTER TABLE master.biometric_type ADD CONSTRAINT [master.pk_bmtyp_code] PRIMARY KEY(CODE, LANG_CODE);

CREATE MEMORY TABLE if not exists master.biometric_attribute(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	bmtyp_code character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);


CREATE MEMORY TABLE if not exists master.device_master(
	id 			character varying(36) NOT NULL,
	name 		character varying(64) NOT NULL,
	mac_address character varying(64) NOT NULL,
	serial_num 	character varying(64) NOT NULL,
	ip_address 	character varying(17),
	validity_end_dtimes timestamp,
	dspec_id 	character varying(36) NOT NULL,
	zone_code 	character varying(36) NOT NULL,
	regcntr_id  character varying(10),
	lang_code 	character varying(3) NOT NULL,
	is_active 	boolean NOT NULL,
	cr_by 		character varying(256) NOT NULL,
	cr_dtimes 	timestamp NOT NULL,
	upd_by 		character varying(256),
	upd_dtimes 	timestamp,
	is_deleted 	boolean,
	del_dtimes 	timestamp
	

);


CREATE MEMORY TABLE if not exists master.device_spec(
	id character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	brand character varying(32) NOT NULL,
	model character varying(16) NOT NULL,
	dtyp_code character varying(36) NOT NULL,
	min_driver_ver character varying(16) NOT NULL,
	descr character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE if not exists master.device_master_h(
	id 			character varying(36) NOT NULL,
	name 		character varying(64) NOT NULL,
	mac_address character varying(64) NOT NULL,
	serial_num 	character varying(64) NOT NULL,
	ip_address 	character varying(17),
	validity_end_dtimes timestamp,
	dspec_id 	character varying(36) NOT NULL,
	zone_code 	character varying(36) NOT NULL,
	regcntr_id  character varying(10),
	lang_code 	character varying(3) NOT NULL,
	is_active 	boolean NOT NULL,
	cr_by 		character varying(256) NOT NULL,
	cr_dtimes 	timestamp NOT NULL,
	upd_by 		character varying(256),
	upd_dtimes 	timestamp,
	is_deleted 	boolean,
	del_dtimes 	timestamp,
	eff_dtimes 	timestamp NOT NULL
	

);
CREATE MEMORY TABLE if not exists master.device_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
CREATE MEMORY TABLE if not exists master.doc_category(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
CREATE MEMORY TABLE if not exists master.doc_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE if not exists master.valid_document(
	doctyp_code character varying(36) NOT NULL,
	doccat_code character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	

);

CREATE MEMORY TABLE if not exists master.dynamic_field(
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
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE if not exists master.loc_holiday(
	id integer NOT NULL,
	location_code character varying(36) NOT NULL,
	holiday_date date,
	holiday_name character varying(64),
	holiday_desc character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);


CREATE MEMORY TABLE if not exists master.reg_exceptional_holiday(
	regcntr_id character varying(10) NOT NULL,
	hol_date date NOT NULL,
	hol_name character varying(128),
	hol_reason character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);   

CREATE MEMORY TABLE if not exists master.reg_center_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp

);


CREATE MEMORY TABLE if not exists master.registration_center(
	id character varying(10) NOT NULL,
	name character varying(128) NOT NULL,
	cntrtyp_code character varying(36),
	addr_line1 character varying(256),
	addr_line2 character varying(256),
	addr_line3 character varying(256),
	latitude character varying(32),
	longitude character varying(32),
	location_code character varying(36) NOT NULL,
	contact_phone character varying(16),
	contact_person character varying(128),
	number_of_kiosks smallint,
	working_hours character varying(32),
	per_kiosk_process_time time,
	center_start_time time,
	center_end_time time,
	lunch_start_time time,
	lunch_end_time time,
	time_zone character varying(64),
	holiday_loc_code character varying(36),
	zone_code character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE if not exists master.blacklisted_words(
	word character varying(128) NOT NULL,
	descr character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);



create MEMORY table IF NOT EXISTS master.applicant_valid_document(
	apptyp_code character varying(36) not null,
	doccat_code character varying(36) not null,
	doctyp_code character varying(36) not null,
	lang_code character varying(3) not null,
	is_active boolean not null,
	cr_by character varying(256) not null,
	cr_dtimes timestamp not null,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
CREATE MEMORY TABLE if not exists master.id_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

CREATE MEMORY TABLE IF NOT EXISTS MASTER.ZONE(
    CODE CHARACTER VARYING(36) NOT NULL,
    NAME CHARACTER VARYING(128) NOT NULL,
    HIERARCHY_LEVEL SMALLINT NOT NULL,
    HIERARCHY_LEVEL_NAME CHARACTER VARYING(64) NOT NULL,
    HIERARCHY_PATH CHARACTER VARYING(1024),
    PARENT_ZONE_CODE CHARACTER VARYING(36),
    LANG_CODE CHARACTER VARYING(3) NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL,
    CR_BY CHARACTER VARYING(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    UPD_BY CHARACTER VARYING(256),
    UPD_DTIMES TIMESTAMP,
    IS_DELETED BOOLEAN,
    DEL_DTIMES TIMESTAMP

);

CREATE MEMORY TABLE  IF NOT EXISTS MASTER.daysofweek_list(
    code character varying(3) NOT NULL,
    name character varying(36)  NOT NULL,
    day_seq smallint NOT NULL,
    is_global_working boolean NOT NULL,
    lang_code character varying(3) NOT NULL,
    is_active boolean NOT NULL,
    cr_by character varying(256) NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
    
);

CREATE MEMORY TABLE IF NOT EXISTS MASTER.reg_working_nonworking(
    regcntr_id character varying(10)  NOT NULL,
    day_code character varying(3)  NOT NULL,
    lang_code character varying(3) ,
    is_working boolean NOT NULL,
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
	
);


CREATE MEMORY TABLE IF NOT EXISTS MASTER.user_detail_h(
    id character varying(256) NOT NULL,
    name character varying(64) NOT NULL,
    status_code character varying(36) ,
    regcntr_id character varying(10) ,
    lang_code character varying(3) ,
    last_login_dtimes timestamp without time zone,
    last_login_method character varying(64) ,
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone,
    eff_dtimes timestamp without time zone NOT NULL
    
);

CREATE MEMORY TABLE IF NOT EXISTS MASTER.machine_master_h(
    id character varying(10) NOT NULL,
    name character varying(64) NOT NULL,
    mac_address character varying(64) ,
    serial_num character varying(64),
    ip_address character varying(17),
    validity_end_dtimes timestamp without time zone,
    mspec_id character varying(36),
    public_key character varying(1024),
    key_index character varying(128),
    sign_public_key character varying(1024),
    sign_key_index character varying(128),
    zone_code character varying(36) NOT NULL,
    regcntr_id character varying(10),
    lang_code character varying(3),
    is_active boolean NOT NULL,
    cr_by character varying(256) NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256),
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone,
    eff_dtimes timestamp without time zone NOT NULL
    
)
