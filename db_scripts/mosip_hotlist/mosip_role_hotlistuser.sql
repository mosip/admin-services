-- object: hotlistuser | type: ROLE --
-- DROP ROLE IF EXISTS hotlistuser;
CREATE ROLE hotlistuser WITH 
	INHERIT
	LOGIN
	PASSWORD :dbuserpwd;
-- ddl-end --

