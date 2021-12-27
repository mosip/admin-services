--DELETE FROM MASTER.appl_form_type;
INSERT INTO master.appl_form_type(code, name,descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES
('105','form','form desc','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('106','form1','form desc1','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.location;
INSERT INTO MASTER.location(code, name, hierarchy_level, hierarchy_level_name, parent_loc_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, IS_DELETED, del_dtimes) VALUES
('MOR','MyCountry',0,'Country','Country','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RSK','Rabat Sale Kenitra',1,'Region','MOR','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KTA','Kenitra',2,'Province','RSK','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KNT','Kenitra',3,'City','KTA','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BNMR','Ben Mansour',4,'Zone','KNT','ara',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('14022','14022',5,'Postal Code','BNMR','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MOGR','Mograne',4,'Zone','KNT','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL);

       
-- 3 +/- SELECT COUNT(*) FROM MASTER.LANGUAGE;
DELETE FROM MASTER.LANGUAGE;
INSERT INTO MASTER.LANGUAGE(CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, FAMILY, NAME, NATIVE_NAME) VALUES
('eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Indo-European', 'English', 'English'),
('ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Afro-Asiatic', 'Arabic', STRINGDECODE('\u0627\u0644\u0639\u064e\u0631\u064e\u0628\u0650\u064a\u064e\u0651\u0629\u200e'));
--('fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Indo-European', 'French', STRINGDECODE('fran\u00e7ais'));
DELETE FROM MASTER.loc_hierarchy_list;
INSERT INTO master.loc_hierarchy_list(hierarchy_level, hierarchy_level_name, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, IS_DELETED, del_dtimes) VALUES
('0','Country','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
('0','بلد','ara',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
--('0','Pays','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
('1','Region','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL);

DELETE FROM MASTER.BIOMETRIC_ATTRIBUTE;
DELETE FROM master.biometric_type;
INSERT INTO master.biometric_type(CODE, LANG_CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, DESCR, NAME) VALUES
('FNR', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Finger prints of the applicant', 'Fingerprint'),
('IRS', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Iris of the applicant', 'Iris'),
('PHT', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Photo of the face of the applicant', 'Photo');
--('FNR', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0628\u0635\u0645\u0627\u062a \u0627\u0644\u0623\u0635\u0627\u0628\u0639 \u0644\u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0628\u0635\u0645\u0629 \u0627\u0644\u0625\u0635\u0628\u0639')),
--('IRS', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0622\u064a\u0631\u064a\u0633 \u0644\u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u0639\u064a\u0646')),
--('PHT', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0635\u0648\u0631\u0629 \u0644\u0648\u062c\u0647 \u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0635\u0648\u0631')),
--('FNR', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Empreintes digitales du demandeur', 'Empreintes digitales'),
--('IRS', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Iris du demandeur', 'Iris'),
--('PHT', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Photo du visage du demandeur', 'Photo');

  
  
INSERT INTO MASTER.BIOMETRIC_ATTRIBUTE(CODE, LANG_CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, BMTYP_CODE, DESCR, NAME) VALUES
('TM', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Left and Right Thumbs', 'Thumbs'),
('RH', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Right Slab', 'Right Slab'),
('LH', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Left Slab', 'Left Slab'),
('LI', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Print of Left Iris', 'Left Iris'),
('RI', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Print of Right Iris', 'Right Iris');
--('TM', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0625\u0628\u0647\u0627\u0645 \u0627\u0644\u0623\u064a\u0633\u0631 \u0648\u0627\u0644\u0623\u064a\u0645\u0646'), STRINGDECODE('\u0627\u0644\u0627\u0628\u0647\u0627\u0645')),
--('RH', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0645\u0646 \u0627\u0644\u064a\u0645\u064a\u0646 \u0644\u0648\u062d'), STRINGDECODE('\u0644\u0648\u062d \u0627\u0644\u062d\u0642')),
--('LH', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0628\u0644\u0627\u0637\u0629 \u0627\u0644\u064a\u0633\u0627\u0631'), STRINGDECODE('\u0644\u0648\u062d \u0627\u0644\u064a\u0633\u0627\u0631')),
--('LI', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u064a\u0633\u0631\u0649'), STRINGDECODE('\u063a\u0627\u062f\u0631 \u0627\u0644\u0642\u0632\u062d\u064a\u0629')),
--('RI', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u064a\u0645\u0646\u0649'), STRINGDECODE('\u0627\u0644\u062d\u0642 \u0627\u064a\u0631\u064a\u0633')),
--('TM', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Empreinte des pouces gauche et droit', 'Les pouces'),
--('RH', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Impression de la dalle droite', 'Dalle droite'),
--('LH', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Impression de la dalle gauche', 'Dalle gauche'),
--('LI', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Gravure de Iris Gauche', 'Iris gauche'),
--('RI', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Empreinte de l''iris droit', 'Iris droit');
    
DELETE FROM MASTER.device_master;
DELETE FROM MASTER.device_spec;
DELETE FROM MASTER.device_type;

INSERT INTO master.device_type(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES
('FRS','Finger Print Scanner','For scanning fingerprints','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('CMR','Camera','For capturing photo','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('PRT','Printer','For printing Documents','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('SCN','Document Scanner','For scanning documents','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

INSERT INTO master.device_spec(id, name, brand, model, dtyp_code, min_driver_ver, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
--('165','Fingerprint Scanner','Safran Morpho','1300 E2','FRS','1.12','To scan fingerprint','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('165','Fingerprint Scanner','Safran Morpho','1300 E2','FRS','1.12','To scan fingerprint','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('1','High Speed Dual Iris Scanner','Cogent','3M','CMR','2.34','To scan iris','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('327','High Speed Dual Iris Scanner','Cogent','3M','CMR','2.34','To scan iris','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
   




INSERT INTO master.device_master(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('3000038','Dummy Finger Print Scanner 18','2D-F1-90-89-64-AC','DJ2032361963',NULL,NULL,'165','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000039','Dummy Finger Print Scanner 19','91-16-2F-35-1F-9F','RU3582050357',NULL,NULL,'165','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000040','Dummy Finger Print Scanner 20','CB-B1-03-83-80-1D','RB2496727160',NULL,NULL,'165','NTH','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000058','Dummy IRIS Scanner 18','AA-D8-DC-3A-0A-A8','AQ8028890156',NULL,NULL,'327','NTH',NULL,'eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000059','Dummy IRIS Scanner 19','0D-1B-B0-5A-D9-98','KM7656759005',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000060','Dummy IRIS Scanner 20','FA-60-AC-D0-54-1C','OZ8730264911',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000077','Dummy Web Camera 17','0F-ED-8F-0F-94-88','H706H5247430756',NULL,NULL,'327','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000078','Dummy Web Camera 18','F2-D9-F8-92-F6-FB','U178N6742928759',NULL,NULL,'327','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000079','Dummy Web Camera 19','76-24-19-E2-8E-30','Z405J2389264415',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000080','Dummy Web Camera 20','D1-2C-D9-BA-5F-FE','J499B8422627284',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);




DELETE FROM MASTER.device_master_h;
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes, eff_dtimes)	VALUES 
('3000021','Dummy Finger Print Scanner 1','85-BB-97-4B-14-05','SZ5912878988',NULL,NULL,'165','NTH','10001','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000022','Dummy Finger Print Scanner 2','CD-27-9D-D6-F4-7B','HI5265090311',NULL,NULL,'165','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000023','Dummy Finger Print Scanner 3','6B-D5-10-4B-3A-9E','AT8075685650',NULL,NULL,'165','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes, eff_dtimes)	VALUES 
('3000042','Dummy IRIS Scanner 2','6D-A3-60-2E-B8-2F','RI8899475915',NULL,NULL,'327','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000043','Dummy IRIS Scanner 3','08-4B-68-23-1A-E6','VK7923826383',NULL,NULL,'327','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000044','Dummy IRIS Scanner 4','DE-BE-02-60-64-4D','BB4283888901',NULL,NULL,'327','RSK','10004','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes, eff_dtimes)	VALUES 
('3000075','Dummy Web Camera 15','BA-DE-90-AF-C4-6E','R169V3235864050',NULL,NULL,'736','MRS','10013','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000076','Dummy Web Camera 16','8D-28-4B-A7-F6-98','M262X1515179346',NULL,NULL,'736','MRS','10013','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000077','Dummy Web Camera 17','0F-ED-8F-0F-94-88','H706H5247430756',NULL,NULL,'736','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');


DELETE FROM MASTER.valid_document;
DELETE FROM MASTER.doc_category;
INSERT INTO master.doc_category(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('POI','Proof of Identity','Identity Proof','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POI','Proof of Identity1','Identity Proof','ara',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POR','Proof of Relationship','Proof Relationship of the person','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POA','Proof of Biometric Exception','Proof of Biometric Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POA2','Proof of Biometric Exception2','Proof of Biometric Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('P1','Proof of Biometric Exception1','Proof of Biometric Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.doc_type;
INSERT INTO master.doc_type(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('CIN','Certification of Exception','Certificate of Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('DOC002','PAN card','PAN card','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('C1','Certificate of residence1','Proof of Resident','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR2','Certificate of residence1','Proof of Resident','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR','Certificate of residence','Proof of Resident','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);



INSERT INTO master.valid_document(doctyp_code, doccat_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('CIN','POI','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR','POA','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('CIN','POI','ara',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('DOC002','POI','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR2','POA2','eng',False,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.dynamic_field;
INSERT INTO master.dynamic_field(id, name, description, data_type,value_json,lang_code,is_active,cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('10001','bloodType1','Blood Type11','string','{\"code\":\"code\",\"value\":\"value\"}','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10002','bloodType2','Blood Type12','string','{\"code\":\"code\",\"value\":\"value\"}','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10003','bloodType2','Blood Type12','string','{\"code\":\"code\",\"value\":\"value\"}','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);


DELETE FROM MASTER.loc_holiday;
INSERT INTO master.loc_holiday(id, location_code, holiday_date,holiday_name,holiday_desc, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('2000001','KTA',TO_DATE('10-12-2019','dd-MM-yyyy'),'New Year Day', 'National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000002','KTA',TO_DATE('12-12-2019','dd-MM-yyyy'),'Anniversary of the Independence Manifesto',' National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000004','KTA',TO_DATE('14-12-2019','dd-MM-yyyy'),'Eid','National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000005','RBT',TO_DATE('20-12-2019','dd-MM-yyyy'),'Feast of the Throne',' National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.reg_exceptional_holiday;
INSERT INTO master.reg_exceptional_holiday(regcntr_id, hol_date, hol_name,hol_reason, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('10001',TO_DATE('01-12-2019','dd-MM-yyyy'),'Emergency Holiday','Emergency Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10002',TO_DATE('01-12-2019','dd-MM-yyyy'),'Emergency Holiday','Emergency Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);


DELETE FROM MASTER.ui_spec;
INSERT INTO master.ui_spec(id, version, domain, title, description, type, json_spec, identity_schema_id, identity_schema_version, effective_from, status_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('1','0.100','REG-CLIENT','BIOMETRIC','biometric','screens','[{"order":1,"name":"ConsentDetails","label":{"ara":"موافقة","fra":"Consentement","eng":"Consent"},"caption":{"ara":"موافقة","fra":"Consentement","eng":"Consent"},"fields":["consentText","consent"],"layoutTemplate":null,"preRegFetchRequired":false,"active":false},{"order":2,"name":"DemographicDetails","label":{"ara":"التفاصيل الديموغرافية","fra":"Détails démographiques","eng":"Demographic Details"},"caption":{"ara":"التفاصيل الديموغرافية","fra":"Détails démographiques","eng":"Demographic Details"},"fields":["fullName","dateOfBirth","gender","residenceStatus","addressLine1","addressLine2","addressLine3","referenceIdentityNumber","region","province","city","zone","postalCode","phone","email","introducerName","introducerRID","introducerUIN"],"layoutTemplate":null,"preRegFetchRequired":true,"active":false},{"order":3,"name":"DocumentsDetails","label":{"ara":"تحميل الوثيقة","fra":"Des documents","eng":"Document Upload"},"caption":{"ara":"وثائق","fra":"Des documents","eng":"Documents"},"fields":["proofOfAddress","proofOfIdentity","proofOfRelationship","proofOfDateOfBirth","proofOfException","proofOfException-1"],"layoutTemplate":null,"preRegFetchRequired":false,"active":false},{"order":4,"name":"BiometricDetails","label":{"ara":"التفاصيل البيومترية","fra":"Détails biométriques","eng":"Biometric Details"},"caption":{"ara":"التفاصيل البيومترية","fra":"Détails biométriques","eng":"Biometric Details"},"fields":["individualBiometrics","individualAuthBiometrics","introducerBiometrics"],"layoutTemplate":null,"preRegFetchRequired":false,"active":false}]','1001','0.001', TIMESTAMP '2021-12-10 11:42:52.994', 'PUBLISHED', true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.blacklisted_words;
INSERT INTO master.blacklisted_words(word, descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES
('shit','Blacklisted Word','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('damm','Blacklisted Word','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('damit','Blacklisted Word','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);



DELETE FROM MASTER.applicant_valid_document;
Insert INTO master.applicant_valid_document(apptyp_code, doccat_code, doctyp_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('001','POI','CIN','eng',true,'superadmin',Timestamp '2018-12-10 11:42:52.994',null,null,null,null),
('001','POI','RNCC','ara',true,'superadmin',Timestamp '2018-12-10 11:42:52.994',null,null,null,null),
('002','POA','COR','eng',true,'superadmin',Timestamp '2018-12-10 11:42:52.994',null,null,null,null);
--('002','POB','COB','eng',true,'superadmin',Timestamp '2018-12-10 11:42:52.994',null,null,null,null);

DELETE FROM MASTER.id_type;
INSERT INTO master.id_type(code, name, descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('UIN','Unique Identification Number','National ID given to the applicant','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('PRID','Pre-registration ID','ID assigned after Pre-registration','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('RID','Registration ID','ID assigned after registration','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('VID','Virtual ID','ID used in replacement of UIN','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);



DELETE FROM MASTER.ZONE;
INSERT INTO MASTER.ZONE(CODE, NAME, HIERARCHY_LEVEL, HIERARCHY_LEVEL_NAME, HIERARCHY_PATH, PARENT_ZONE_CODE, LANG_CODE, IS_ACTIVE, CR_BY, CR_DTIMES, UPD_BY, UPD_DTIMES, IS_DELETED, DEL_DTIMES) VALUES
('MOR', 'Morocco', 0, 'Country', 'MOR', '', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
--('MOR', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2019\u00d9\u20ac\u00d9\u2026\u00d9\u017d\u00d8\u00ba\u00d9\u2019\u00d8\u00b1\u00d9\ufffd\u00d8\u00a8\u00d9\ufffd'), 0, STRINGDECODE('\u00d8\u00a8\u00d9\u201e\u00d8\u00af'), 'MOR', NULL, 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
--('MOR', 'Maroc', 0, 'Pays', 'MOR', '', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NTH', 'North', 1, 'Direction', 'MOR/NTH', 'MOR', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NTH', STRINGDECODE('\u00d8\u00b4\u00d9\u2026\u00d8\u00a7\u00d9\u201e'), 1, STRINGDECODE('\u00d8\u00a7\u00d8\u00aa\u00d8\u00ac\u00d8\u00a7\u00d9\u2021'), 'MOR/NTH', 'MOR', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NTH', 'Nord', 1, 'Direction', 'MOR/NTH', 'MOR', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RSK', 'RabatSaleKenitra', 2, 'Region', 'MOR/NTH/RSK', 'NTH', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RSK', STRINGDECODE('\u00d8\u00ac\u00d9\u2021\u00d8\u00a9\u00d8\u00a7\u00d9\u201e\u00d8\u00b1\u00d8\u00a8\u00d8\u00a7\u00d8\u00b7\u00d8\u00b3\u00d9\u201e\u00d8\u00a7\u00d8\u00a7\u00d9\u201e\u00d9\u201a\u00d9\u2020\u00d9\u0160\u00d8\u00b7\u00d8\u00b1\u00d8\u00a9'), 2, STRINGDECODE('\u00d9\u2026\u00d9\u2020\u00d8\u00b7\u00d9\u201a\u00d8\u00a9'), 'MOR/NTH/RSK', 'NTH', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RSK', STRINGDECODE('Rabat-Sal\u00c3\u00a9-K\u00c3\u00a9nitra'), 2, STRINGDECODE('R\u00c3\u00a9gion'), 'MOR/NTH/RSK', 'NTH', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RBT', 'Rabat', 3, 'Province', 'MOR/NTH/RSK/RBT', 'RSK', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RBT', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d8\u00b1\u00d8\u00a8\u00d8\u00a7\u00d8\u00b7'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/RSK/RBT', 'RSK', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RBT', 'Rabat', 3, 'Province', 'MOR/NTH/RSK/RBT', 'RSK', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KTA', 'Kenitra', 3, 'Province', 'MOR/NTH/RSK/KTA', 'RSK', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KTA', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u201a\u00d9\u2020\u00d9\u0160\u00d8\u00b7\u00d8\u00b1\u00d8\u00a9'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/RSK/KTA', 'RSK', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KTA', STRINGDECODE('K\u00c3\u00a9nitra'), 3, 'Province', 'MOR/NTH/RSK/KTA', 'RSK', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAL', STRINGDECODE('Sal\u00c3\u00a9\u00c2\u00a0'), 3, 'Province', 'MOR/NTH/RSK/SAL', 'RSK', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAL', STRINGDECODE('\u00d8\u00b3\u00d9\u201e\u00d8\u00a7'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/RSK/SAL', 'RSK', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAL', 'Sala', 3, 'Province', 'MOR/NTH/RSK/SAL', 'RSK', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('CST', 'Casablanca-Settat', 2, 'Region', 'MOR/NTH/CST', 'NTH', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL);               


DELETE FROM MASTER.registration_center;
DELETE FROM MASTER.reg_center_type;
INSERT INTO master.reg_center_type(code,name,descr,lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('REG','Regular','Regular,Regular Registration Center','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);


INSERT INTO master.registration_center(id,name,cntrtyp_code,addr_line1,addr_line2,addr_line3,latitude,longitude,location_code,contact_phone,contact_person,number_of_kiosks,working_hours,per_kiosk_process_time,center_start_time,center_end_time,lunch_start_time,lunch_end_time,time_zone,holiday_loc_code,zone_code,lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_DELETEd, del_dtimes) VALUES 
('10001','Center A Ben Mansour','REG','P4238','Ben Mansour','MyCountry','23.3454','34.5434','14022','779517433','John Doe',3,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10002','Center A Ben','REG','P4239','Ben Mansour','MyCountry','34.52117','-6.453275','14022','779517433','John Doee',2,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10004','Center A Ben1','REG','P4239','Ben Mansour1','MyCountry1','34.52117','-6.453275','14022','779517433','John Doee',2,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10077','Center A','REG','P4239','Ben Mansour1','MyCountry1','34.52117','-6.453275','14022','779517433','John Doee',2,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','kan',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10078','Center B','REG','P4239','Ben Mansour1','MyCountry1','34.52117','-6.453275','14022','779517433','John Doee',2,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','kan',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10003','Center Q','REG','P4239','Q Mansour','MyCountry','34.52117','-6.453275','14022','779517433','John Doee',2,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','CST','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);


DELETE FROM MASTER.daysofweek_list;
INSERT INTO master.daysofweek_list(code, name, day_seq, is_global_working, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('101', 'SUN',0, true,'eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('102', 'MON',1, FALSE,'eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL);

DELETE FROM MASTER.reg_working_nonworking;
INSERT INTO MASTER.reg_working_nonworking(regcntr_id, day_code, lang_code, is_working, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('10001','101','eng',TRUE,TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('10002','102','eng',FALSE,TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL);

DELETE FROM MASTER.machine_master_h;
INSERT INTO MASTER.machine_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, mspec_id, public_key, key_index, sign_public_key, sign_key_index, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes) VALUES
('10001','alm1009', 'E8-A9-64-1F-27-E6','NM19837379','192.168.0.120',NULL,'1001','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB','B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48', NULL,NULL,'NTH','10001','eng',true, 'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2023-12-10 11:42:52.994');

DELETE FROM MASTER.user_detail_h;
INSERT INTO MASTER.user_detail_h(id, name, status_code, regcntr_id, lang_code, last_login_dtimes, last_login_method, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes) VALUES
('1','abc','ACT','10001','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL, TIMESTAMP '2023-12-10 11:42:52.994'),
('100','abc','ACT','10003','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL, TIMESTAMP '2023-12-10 11:42:52.994');

DELETE FROM MASTER.zone_user;
INSERT INTO MASTER.zone_user(zone_code, usr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) values
('NTH', 'global-admin', 'eng', TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('RSK', 'zonal-admin', 'eng', TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('RSK', '4', 'eng', TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('RBT','7','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL),
('NTN','42','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL),
('CST','3','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL);


DELETE FROM MASTER.machine_master;
INSERT INTO MASTER.machine_master(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, mspec_id, public_key, key_index, sign_public_key, sign_key_index, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('10','alm1009', 'E8-A9-64-1F-27-E6','NM19837379','192.168.0.120',NULL,'1001','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB','B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48', NULL,NULL,'NTH','10001','eng',true, 'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('40','alm1409', 'E8-A9-84-1F-27-E6','NM19887379','192.168.0.128',NULL,'1001','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB','B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48', NULL,NULL,'NTH',NULL,'eng',true, 'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('20','alm2009', 'E8-A9-64-1F-34-E6','NM11037379','192.168.0.121',NULL,'2222','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB','B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48', NULL,NULL,'NTH','10002','eng',true, 'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('30','alm3009', 'E8-A9-64-1F-56-E6','NM19107379','192.168.0.122',NULL,'1001','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB','B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48', NULL,NULL,'NTH','10001','eng',true, 'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.zone_user_h;
INSERT INTO master.zone_user_h(zone_code, usr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes) VALUES
('JRD','1','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('BRK','5','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');

DELETE FROM MASTER.machine_type;
INSERT INTO master.machine_type(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('DKS','Desktop','desktop computer','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL),
('VDKS','Desktop','desktop computer','eng',false,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL),
('Vostro','Desktop','desktop computer','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994', NULL,NULL,NULL,NULL);

DELETE FROM MASTER.user_detail;
INSERT INTO MASTER.user_detail(id, name, status_code, regcntr_id, lang_code, last_login_dtimes, last_login_method, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('2','abcd','ACT','10002','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('3','test','ACT','10001','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, false,NULL),
('4','dummy','ACT','10002','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('42','dummy42','ACT','10002','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('5','dummy1','ACT','10002','eng',NULL,'PWD', true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL);

DELETE FROM MASTER.machine_spec;
INSERT INTO master.machine_spec(id, name, brand, model, mtyp_code, min_driver_ver, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('1001','HP','HP','1234','DKS','3.2','HP brand','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('11','HPTest','HPTest','1234','DKS','3.2','HP brand','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2222','HP1','HP1','1243','DKS','3.2','HP brand','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

DELETE FROM MASTER.template;
INSERT INTO MASTER.template(id, name, descr, file_format_code, model, file_txt, module_id, module_name, template_typ_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('1','test-template','test template desc','txt','velocity','Hi this is file','10003','testing','EMAIL','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('2','test-template1','test template desc','txt','velocity','Hi this is file1','10004','testing','EMAIL','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('3','test-template2','test template desc','txt','velocity','Hi this is file2','10005','testing','SMS','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('4','test-template3','test template desc','txt','velocity','Hi this is file3','10006','testing','EMAIL','eng',true,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL);

DELETE FROM MASTER.identity_schema;
INSERT INTO MASTER.identity_schema(id, id_version, title, description, schema_json, status_code, add_props, effective_from, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('1','0.1','mp1','mosip description','{"schema":"schema"}','DRAFT',false, null, 'eng',true, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('2','0.1','mp2','mosip description','{"schema":"schema"}','DRAFT',false, null, 'eng',true, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL),
('3','1.1','mp3','mosip description','{"schema":"schema"}','PUBLISHED',false, null, 'eng',true, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994',NULL, NULL, NULL, NULL);

DELETE FROM MASTER.template_file_format;
INSERT INTO master.template_file_format(code, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('json','json file','eng',true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('txt','text file','eng',true,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL);

DELETE FROM MASTER.registration_center_h;
INSERT INTO master.registration_center_h(id, name, cntrtyp_code, addr_line1, addr_line2, addr_line3, latitude, longitude, location_code, contact_phone, contact_person, number_of_kiosks, working_hours, per_kiosk_process_time, center_start_time, center_end_time, lunch_start_time, lunch_end_time, time_zone, holiday_loc_code, zone_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes) VALUES
('10001','Center A Ben Mansour','REG','P4238','Ben Mansour','MyCountry','23.3454','34.5434','14022','779517433','John Doe',3,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');

