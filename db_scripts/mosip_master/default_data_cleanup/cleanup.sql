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

drop table master.admin_param;
drop table master.appl_form_type;
drop table master.message_list;
drop table master.doc_format;
drop table master.gender;
drop table master.global_param;
drop table master.id_type;
drop table master.individual_type;
drop table master.introducer_type;
drop table master.status_list;
drop table master.status_type;









