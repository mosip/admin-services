\echo 'Upgrade Queries not required for transition from $CURRENT_VERSION to $UPGRADE_VERSION'

-- Upgrade script for master.app_detail
ALTER TABLE master.app_detail DROP CONSTRAINT IF EXISTS pk_appdtl_id CASCADE;
ALTER TABLE master.app_detail ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id);

-- Upgrade script for master.biometric_attribute
ALTER TABLE master.biometric_attribute DROP CONSTRAINT IF EXISTS pk_bmattr_code CASCADE;
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code);

-- Upgrade script for master.module_detail
ALTER TABLE master.module_detail DROP CONSTRAINT IF EXISTS pk_moddtl_code CASCADE;
ALTER TABLE master.module_detail ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id);

-- Upgrade script for master.template_file_format
ALTER TABLE master.template_file_format DROP CONSTRAINT IF EXISTS pk_tffmt_code CASCADE;
ALTER TABLE master.template_file_format ALTER COLUMN lang_code DROP NOT NULL;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code);
