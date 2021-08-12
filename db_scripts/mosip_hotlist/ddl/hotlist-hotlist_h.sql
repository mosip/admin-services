-- -------------------------------------------------------------------------------------------------
-- Database Name: mosip_hotlist
-- Table Name 	: hotlist.hotlist_h
-- Purpose    	: Hotlisting Transaction : To track history of Hotlisted ids in MOSIP
--           
-- Created By   : Ram Bhatt
-- Created Date	: Feb-2021
-- 
-- Modified Date        Modified By         Comments / Remarks
-- ------------------------------------------------------------------------------------------
-- 12/08/2021			Manoj SP			Updated id_value to varchar	
-- ------------------------------------------------------------------------------------------

-- object: hotlist.hotlist_h | type: TABLE --
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
ALTER TABLE hotlist.hotlist_h OWNER TO sysadmin;
-- ddl-end --


