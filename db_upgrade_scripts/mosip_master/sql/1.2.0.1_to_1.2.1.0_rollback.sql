-- Rollback script for master.app_detail
ALTER TABLE master.app_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.app_detail DROP CONSTRAINT pk_appdtl_id;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id, lang_code);

-- Rollback script for master.biometric_attribute
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.biometric_attribute DROP CONSTRAINT pk_bmattr_code;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.module_detail
ALTER TABLE master.module_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.module_detail DROP CONSTRAINT pk_moddtl_code;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id, lang_code);

-- Rollback script for master.template_file_format
ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_tffmt CASCADE;
ALTER TABLE master.template_file_format ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.template_file_format DROP CONSTRAINT pk_tffmt_code;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.template
ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_tffmt FOREIGN KEY (file_format_code, lang_code) REFERENCES master.template_file_format (code, lang_code);