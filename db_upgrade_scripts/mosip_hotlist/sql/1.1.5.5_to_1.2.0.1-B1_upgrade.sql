CREATE DATABASE mosip_hotlist
	ENCODING = 'UTF8'
	LC_COLLATE = 'en_US.UTF-8'
	LC_CTYPE = 'en_US.UTF-8'
	TABLESPACE = pg_default
	OWNER = postgres
	TEMPLATE  = template0;

COMMENT ON DATABASE mosip_hotlist IS 'Hotlist data related logs and the data is stored in this database';

\c mosip_hotlist

REASSIGN OWNED BY sysadmin TO postgres;

DROP SCHEMA IF EXISTS hotlist CASCADE;
CREATE SCHEMA hotlist;
ALTER SCHEMA hotlist OWNER TO postgres;
ALTER DATABASE mosip_hotlist SET search_path TO hotlist,pg_catalog,public;

CREATE ROLE hotlistuser WITH
	INHERIT
	LOGIN
	PASSWORD :dbuserpwd;

GRANT CONNECT
   ON DATABASE mosip_hotlist
   TO hotlistuser;

GRANT USAGE
   ON SCHEMA hotlist
   TO hotlistuser;

-- DROP TABLE IF EXISTS hotlist.hotlist CASCADE;
CREATE TABLE hotlist.hotlist (
	id_hash character varying(128) NOT NULL,
	id_value character varying NOT NULL,
	id_type character varying(128) NOT NULL,
	status character varying(64),
	start_timestamp timestamp,
	expiry_timestamp timestamp,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT "pk_idHashidType" PRIMARY KEY (id_hash,id_type)

);
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.id_hash IS E'idHash : Hashed value of ID, This can be UIN or VID or any other id which needs to be hotlisted. Hash value is stored.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.id_value IS E'idValue: This can be UIN or VID or any other id which needs to be hotlisted.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.id_type IS E'idType: Type of ID such as UIN, VID, DeviceID etc';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.status IS E'status: status to denote whether the respective ID is blocked or unblocked.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.start_timestamp IS E'startTimestamp: Timestamp when respective ID is hotlisted.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist.expiry_timestamp IS E'expiryTimestamp: Timestamp when respective hotlisted ID will expire.';
-- ddl-end --


-- DROP TABLE IF EXISTS hotlist.hotlist_h CASCADE;
CREATE TABLE hotlist.hotlist_h (
	id_hash character varying(128) NOT NULL,
	id_value character varying NOT NULL,
	id_type character varying(128) NOT NULL,
	status character varying(64),
	start_timestamp timestamp NOT NULL,
	expiry_timestamp timestamp,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT "pk_idHashidTypestarttimestamp" PRIMARY KEY (id_hash,id_type,start_timestamp)

);
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist_h.id_hash IS E'idHash : Hashed value of ID, This can be UIN or VID or any other id which needs to be hotlisted. Hash value is stored.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist_h.id_value IS E'idValue: This can be UIN or VID or any other id which needs to be hotlisted.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist_h.id_type IS E'idType: Type of ID such as UIN, VID, DeviceID etc';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist_h.status IS E'status: status to denote whether the respective ID is blocked or unblocked.';
-- ddl-end --
COMMENT ON COLUMN hotlist.hotlist_h.start_timestamp IS E'startTimestamp: Timestamp when respective ID is hotlisted.';
-- ddl-end --

GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE,REFERENCES
   ON ALL TABLES IN SCHEMA hotlist
   TO hotlistuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA hotlist
	GRANT SELECT,INSERT,UPDATE,DELETE,REFERENCES ON TABLES TO hotlistuser;