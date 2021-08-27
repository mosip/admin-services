-- -------------------------------------------------------------------------------------------------
-- Database Name: mosip_hotlist
-- Table Name 	: hotlist.hotlist
-- Purpose    	: Hotlisting Transaction : To track all Hotlisted ids in MOSIP
--           
-- Created By   : Ram Bhatt
-- Created Date	: Feb-2021
-- 
-- Modified Date        Modified By         Comments / Remarks
-- ------------------------------------------------------------------------------------------
-- 12/08/2021			Manoj SP			Updated id_value to varchar	
-- ------------------------------------------------------------------------------------------

-- object: hotlist.hotlist | type: TABLE --
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
ALTER TABLE hotlist.hotlist OWNER TO sysadmin;
-- ddl-end --


