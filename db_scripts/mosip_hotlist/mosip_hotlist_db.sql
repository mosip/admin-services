DROP DATABASE IF EXISTS mosip_hotlist;
CREATE DATABASE mosip_hotlist
	ENCODING = 'UTF8'
	LC_COLLATE = 'en_US.UTF-8'
	LC_CTYPE = 'en_US.UTF-8'
	TABLESPACE = pg_default
	OWNER = sysadmin
	TEMPLATE  = template0;
-- ddl-end --
COMMENT ON DATABASE mosip_hotlist IS 'Hotlisting related requests, transactions and mapping related data like virtual ids, tokens, etc. will be stored in this database';
-- ddl-end --

\c mosip_ida sysadmin

-- object: ida | type: SCHEMA --
DROP SCHEMA IF EXISTS hotlist CASCADE;
CREATE SCHEMA hotlist;
-- ddl-end --
ALTER SCHEMA ida OWNER TO sysadmin;
-- ddl-end --

ALTER DATABASE mosip_hotlist SET search_path TO hotlist,pg_catalog,public;
-- ddl-end --

-- REVOKECONNECT ON DATABASE mosip_hotlist FROM PUBLIC;
-- REVOKEALL ON SCHEMA hotlist FROM PUBLIC;
-- REVOKEALL ON ALL TABLES IN SCHEMA hotlist FROM PUBLIC ;
