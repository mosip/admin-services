-- ---------------------------------------------------------------------------------------------------------
-- Database Name: mosip_master
-- Release Version 	: 1.2.1
-- Purpose    		: Database Alter scripts for the release for Master DB.       
-- Create By   		: Balaji A
-- Created Date		: April 2023
-- 
-- Modified Date        Modified By         Comments / Remarks
-- -----------------------------------------------------------------------------------------------------------

\c mosip_master sysadmin

DROP TABLE IF EXISTS master.blacklisted_words;