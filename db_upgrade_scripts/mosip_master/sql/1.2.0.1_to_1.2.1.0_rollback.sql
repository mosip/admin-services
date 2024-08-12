\echo 'Upgrade Queries not required for transition from $CURRENT_VERSION to $UPGRADE_VERSION'

-- Rollback script for master.app_detail
ALTER TABLE master.app_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.app_detail DROP CONSTRAINT pk_appdtl_id;

-- Rollback script for master.biometric_attribute
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.biometric_attribute DROP CONSTRAINT pk_bmattr_code;

-- Rollback script for master.module_detail
ALTER TABLE master.module_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.module_detail DROP CONSTRAINT pk_moddtl_code;

-- Rollback script for master.template_file_format
ALTER TABLE master.template_file_format ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.template_file_format DROP CONSTRAINT pk_tffmt_code;
