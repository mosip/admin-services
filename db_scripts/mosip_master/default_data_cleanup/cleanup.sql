DELETE FROM master.template_file_format
WHERE lang_code !='eng';

DELETE FROM master.authentication_method
WHERE lang_code !='eng';

DELETE FROM master.app_detail
WHERE lang_code !='eng';

DELETE FROM master.biometric_type
WHERE lang_code !='eng';

DELETE FROM master.biometric_attribute
WHERE lang_code !='eng';

DELETE FROM master.module_detail
WHERE lang_code !='eng';

ALTER TABLE master.admin_param RENAME TO admin_param_archive;
ALTER TABLE master.appl_form_type RENAME TO appl_form_type_archive;
ALTER TABLE master.message_list RENAME TO message_list_archive;
ALTER TABLE master.doc_format RENAME TO doc_format_archive;
ALTER TABLE master.gender RENAME TO gender_archive;
ALTER TABLE master.global_param RENAME TO global_param_archive;
ALTER TABLE master.id_type RENAME TO id_type_archive;
ALTER TABLE master.individual_type RENAME TO individual_type_archive;
ALTER TABLE master.introducer_type RENAME TO introducer_type_archive;
ALTER TABLE master.status_list RENAME TO status_list_archive;
ALTER TABLE master.status_type RENAME TO status_type_archive;
