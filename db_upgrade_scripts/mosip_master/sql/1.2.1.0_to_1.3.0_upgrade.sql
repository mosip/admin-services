-- ------------------------------------------------------------------------------------------
-- Upgrade script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
-- ------------------------------------------------------------------------------------------

ALTER TABLE  master.BATCH_STEP_EXECUTION ADD CREATE_TIME TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';
ALTER TABLE  master.BATCH_STEP_EXECUTION ALTER COLUMN START_TIME DROP NOT NULL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DATE_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN LONG_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DOUBLE_VAL;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN TYPE_CD TYPE VARCHAR(100);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME TYPE_CD TO PARAMETER_TYPE;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN KEY_NAME TYPE VARCHAR(100);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME KEY_NAME TO PARAMETER_NAME;
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN STRING_VAL TYPE VARCHAR(2500);
ALTER TABLE  master.BATCH_JOB_EXECUTION_PARAMS RENAME STRING_VAL TO PARAMETER_VALUE;
ALTER TABLE  master.BATCH_JOB_EXECUTION DROP COLUMN JOB_CONFIGURATION_LOCATION;

CREATE SEQUENCE  master.BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE  master.BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE  master.BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;

INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'eng', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'spa', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'kan', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'tam', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'hin', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'fra', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-purpose', 'Authentication positive purpose', 'ara', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'eng', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'spa', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'kan', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'tam', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'hin', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'fra', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-negative-purpose', 'Authentication negative purpose', 'ara', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'eng', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'spa', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'kan', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'tam', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'hin', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'fra', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);
INSERT INTO master.template_type
(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('authentication-request-positive-summary', 'Authentication positive summary', 'ara', true, 'admin', '2024-09-13 07:14:20.069', NULL, NULL, false, NULL);

INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3515', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'تم تنفيذ طلبك للمصادقة بنجاح.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'ara', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3514', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'Votre demande d''authentification a réussi.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'fra', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3513', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'प्रमाणीकरण हेतु आपका अनुरोध सफल हुआ।', '10006', 'Resident Services', 'authentication-request-positive-summary', 'hin', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3512', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'அங்கீகாரத்திற்கான உங்கள் கோரிக்கை வெற்றிகரமாக உள்ளது.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'tam', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3511', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'ದೃಢೀಕರಣಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ಯಶಸ್ವಿಯಾಗಿದೆ.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'kan', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3510', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'Su solicitud de autenticación es exitosa.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'spa', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3509', 'Authentication positive summary', 'Authentication positive summary', 'txt', 'velocity', 'Your request to authentication is successful.', '10006', 'Resident Services', 'authentication-request-positive-summary', 'eng', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3508', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'لقد فشل طلب المصادقة الخاص بك.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'ara', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3507', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'Votre demande d''authentification a échoué.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'fra', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3506', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'प्रमाणीकरण हेतु आपका अनुरोध विफल हो गया है.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'hin', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3505', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'அங்கீகாரத்திற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'tam', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3504', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'ದೃಢೀಕರಣಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'kan', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3503', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'Su solicitud de autenticación ha fallado.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'spa', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3502', 'Authentication negative purpose', 'Authentication negative purpose', 'txt', 'velocity', 'Your request to authentication is failed.', '10006', 'Resident Services', 'authentication-request-negative-purpose', 'eng', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3501', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'تم تنفيذ طلبك للمصادقة بنجاح.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'ara', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3500', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'Votre demande d''authentification a réussi.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'fra', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3499', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'प्रमाणीकरण हेतु आपका अनुरोध सफल हुआ।', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'hin', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3498', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'அங்கீகாரத்திற்கான உங்கள் கோரிக்கை வெற்றிகரமாக உள்ளது.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'tam', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3497', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'ದೃಢೀಕರಣಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ಯಶಸ್ವಿಯಾಗಿದೆ.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'kan', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3496', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'Su solicitud de autenticación es exitosa.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'spa', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);
INSERT INTO master."template"
(id, "name", descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
VALUES('3495', 'Authentication positive purpose', 'Authentication positive purpose', 'txt', 'velocity', 'Your request to authentication is successful.', '10006', 'Resident Services', 'authentication-request-positive-purpose', 'eng', true, 'superadmin', '2024-09-13 07:15:15.394', NULL, NULL, false, NULL);