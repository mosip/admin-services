-- --------------------------------------------------------------------------------------------------------
-- Database Name: mosip_master
-- Release Version 	: 1.2.0
-- Purpose    		: Revoking Database Alter deployement done for release in Master DB.       
-- Create By   		: Ram Bhatt
-- Created Date		: Jan-2021
-- 
-- Modified Date        Modified By         Comments / Remarks
-- -----------------------------------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------------------------------------
\c mosip_master sysadmin

DROP TABLE master.ui_spec;

DROP TABLE IF EXISTS master.identity_schema;
CREATE TABLE identity_schema AS (SELECT * FROM master.identity_schema_migr_bkp);

--------------------------------------------------------------------------------------------------------------
ALTER TABLE master.bulkupload_transaction ALTER COLUMN upload_description character varying(256);


-----------------------------------------------------------------------------------------------------------------

ALTER TABLE master.user_detail ADD COLUMN uin character varying(28);
ALTER TABLE master.user_detail ADD COLUMN email character varying(256);
ALTER TABLE master.user_detail ADD COLUMN mobile character varying(16);

ALTER TABLE master.user_detail_h ADD COLUMN uin character varying(28);
ALTER TABLE master.user_detail_h ADD COLUMN email character varying(256);
ALTER TABLE master.user_detail_h ADD COLUMN mobile character varying(16);
