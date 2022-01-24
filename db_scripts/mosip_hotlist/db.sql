CREATE DATABASE mosip_hotlist
	ENCODING = 'UTF8' 
	LC_COLLATE = 'en_US.UTF-8' 
	LC_CTYPE = 'en_US.UTF-8' 
	TABLESPACE = pg_default 
	OWNER = postgres
	TEMPLATE  = template0;

COMMENT ON DATABASE mosip_hotlist IS 'Hotlist data related logs and the data is stored in this database';

\c mosip_hotlist postgres

DROP SCHEMA IF EXISTS hotlist CASCADE;
CREATE SCHEMA hotlist;
ALTER SCHEMA hotlist OWNER TO postgres;
ALTER DATABASE mosip_hotlist SET search_path TO hotlist,pg_catalog,public;

