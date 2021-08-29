
CREATE SCHEMA IF NOT EXISTS master;
CREATE SCHEMA IF NOT EXISTS kernel;

CREATE MEMORY TABLE IF NOT EXISTS MASTER.ZONE(
    CODE CHARACTER VARYING(36) NOT NULL,
    NAME CHARACTER VARYING(128) NOT NULL,
    HIERARCHY_LEVEL SMALLINT NOT NULL,
    HIERARCHY_LEVEL_NAME CHARACTER VARYING(64) NOT NULL,
    HIERARCHY_PATH CHARACTER VARYING(1024),
    PARENT_ZONE_CODE CHARACTER VARYING(36),
    LANG_CODE CHARACTER VARYING(3) NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL,
    CR_BY CHARACTER VARYING(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    UPD_BY CHARACTER VARYING(256),
    UPD_DTIMES TIMESTAMP,
    IS_DELETED BOOLEAN,
    DEL_DTIMES TIMESTAMP
);         
ALTER TABLE MASTER.ZONE ADD CONSTRAINT IF NOT EXISTS MASTER.PK_ZONE_CODE PRIMARY KEY(CODE, LANG_CODE);      
-- 63 +/- SELECT COUNT(*) FROM MASTER.ZONE;   
INSERT INTO MASTER.ZONE(CODE, NAME, HIERARCHY_LEVEL, HIERARCHY_LEVEL_NAME, HIERARCHY_PATH, PARENT_ZONE_CODE, LANG_CODE, IS_ACTIVE, CR_BY, CR_DTIMES, UPD_BY, UPD_DTIMES, IS_DELETED, DEL_DTIMES) VALUES
('MOR', 'Morocco', 0, 'Country', 'MOR', NULL, 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MOR', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2019\u00d9\u20ac\u00d9\u2026\u00d9\u017d\u00d8\u00ba\u00d9\u2019\u00d8\u00b1\u00d9\ufffd\u00d8\u00a8\u00d9\ufffd'), 0, STRINGDECODE('\u00d8\u00a8\u00d9\u201e\u00d8\u00af'), 'MOR', NULL, 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MOR', 'Maroc', 0, 'Pays', 'MOR', NULL, 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
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
INSERT INTO MASTER.ZONE(CODE, NAME, HIERARCHY_LEVEL, HIERARCHY_LEVEL_NAME, HIERARCHY_PATH, PARENT_ZONE_CODE, LANG_CODE, IS_ACTIVE, CR_BY, CR_DTIMES, UPD_BY, UPD_DTIMES, IS_DELETED, DEL_DTIMES) VALUES
('CST', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d8\u00af\u00d8\u00a7\u00d8\u00b1\u00d8\u00a7\u00d9\u201e\u00d8\u00a8\u00d9\u0160\u00d8\u00b6\u00d8\u00a7\u00d8\u00a1-\u00d8\u00b3\u00d8\u00b7\u00d8\u00a7\u00d8\u00aa'), 2, STRINGDECODE('\u00d9\u2026\u00d9\u2020\u00d8\u00b7\u00d9\u201a\u00d8\u00a9'), 'MOR/NTH/CST', 'NTH', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('CST', 'Casablanca-Settat', 2, STRINGDECODE('R\u00c3\u00a9gion'), 'MOR/NTH/CST', 'NTH', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BSN', 'Benslimane', 3, 'Province', 'MOR/NTH/CST/BSN', 'CST', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BSN', STRINGDECODE('\u00d8\u00a8\u00d9\u2020\u00d8\u00b3\u00d9\u201e\u00d9\u0160\u00d9\u2026\u00d8\u00a7\u00d9\u2020'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/CST/BSN', 'CST', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BSN', 'Benslimane', 3, 'Province', 'MOR/NTH/CST/BSN', 'CST', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('CSB', 'Casablanca', 3, 'Province', 'MOR/NTH/CST/CSB', 'CST', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('CSB', STRINGDECODE('\u00d9\u00b1\u00d9\u201e\u00d8\u00af\u00d9\u017d\u00d9\u2018\u00d8\u00a7\u00d8\u00b1\u00d9\u00b1\u00d9\u201e\u00d9\u2019\u00d8\u00a8\u00d9\u017d\u00d9\u0160\u00d9\u2019\u00d8\u00b6\u00d9\u017d\u00d8\u00a7\u00d8\u00a1\u00e2\u20ac\u017d'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/CST/CSB', 'CST', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('CSB', 'Casablanca', 3, 'Province', 'MOR/NTH/CST/CSB', 'CST', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STT', 'Settat', 3, 'Province', 'MOR/NTH/CST/STT', 'CST', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STT', STRINGDECODE('\u00d8\u00b3\u00d8\u00b7\u00d8\u00a7\u00d8\u00aa\u00e2\u20ac\u017d'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/CST/STT', 'CST', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STT', 'Settat', 3, 'Province', 'MOR/NTH/CST/STT', 'CST', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('ORT', 'Oriental', 2, 'Region', 'MOR/NTH/ORT', 'NTH', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('ORT', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d8\u00b4\u00d8\u00b1\u00d9\u201a'), 2, STRINGDECODE('\u00d9\u2026\u00d9\u2020\u00d8\u00b7\u00d9\u201a\u00d8\u00a9'), 'MOR/NTH/ORT', 'NTH', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('ORT', 'Oriental', 2, STRINGDECODE('R\u00c3\u00a9gion'), 'MOR/NTH/ORT', 'NTH', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NDR', 'Nador', 3, 'Province', 'MOR/NTH/ORT/NDR', 'ORT', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NDR', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2020\u00d8\u00a7\u00d8\u00b8\u00d9\u02c6\u00d8\u00b1'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/ORT/NDR', 'ORT', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('NDR', 'Nador', 3, 'Province', 'MOR/NTH/ORT/NDR', 'ORT', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BRK', 'Berkane', 3, 'Province', 'MOR/NTH/ORT/BRK', 'ORT', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL);              
INSERT INTO MASTER.ZONE(CODE, NAME, HIERARCHY_LEVEL, HIERARCHY_LEVEL_NAME, HIERARCHY_PATH, PARENT_ZONE_CODE, LANG_CODE, IS_ACTIVE, CR_BY, CR_DTIMES, UPD_BY, UPD_DTIMES, IS_DELETED, DEL_DTIMES) VALUES
('BRK', STRINGDECODE('\u00d8\u00a8\u00d9\u017d\u00d8\u00b1\u00d9\u0192\u00d8\u00a7\u00d9\u2020'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/ORT/BRK', 'ORT', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BRK', 'Berkane', 3, 'Province', 'MOR/NTH/ORT/BRK', 'ORT', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('JRD', STRINGDECODE('Jerada\u00c2\u00a0'), 3, 'Province', 'MOR/NTH/ORT/JRD', 'ORT', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('JRD', STRINGDECODE('\u00d8\u00ac\u00d8\u00b1\u00d8\u00a7\u00d8\u00af\u00d8\u00a9'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/NTH/ORT/JRD', 'ORT', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('JRD', STRINGDECODE('Jerada\u00c2\u00a0'), 3, 'Province', 'MOR/NTH/ORT/JRD', 'ORT', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STH', 'South', 1, 'Direction', 'MOR/STH', 'MOR', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STH', STRINGDECODE('\u00d8\u00ac\u00d9\u2020\u00d9\u02c6\u00d8\u00a8'), 1, STRINGDECODE('\u00d8\u00a7\u00d8\u00aa\u00d8\u00ac\u00d8\u00a7\u00d9\u2021'), 'MOR/STH', 'MOR', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('STH', 'Sud', 1, 'Direction', 'MOR/STH', 'MOR', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MRS', 'Marrakesh-Safi', 2, 'Region', 'MOR/STH/MRS', 'STH', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MRS', STRINGDECODE('\u00d9\u2026\u00d8\u00b1\u00d8\u00a7\u00d9\u0192\u00d8\u00b4\u00d8\u00a2\u00d8\u00b3\u00d9\ufffd\u00d9\u0160\u00e2\u20ac\u017d'), 2, STRINGDECODE('\u00d9\u2026\u00d9\u2020\u00d8\u00b7\u00d9\u201a\u00d8\u00a9'), 'MOR/STH/MRS', 'STH', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MRS', 'Marrakesh-Safi', 2, STRINGDECODE('R\u00c3\u00a9gion'), 'MOR/STH/MRS', 'STH', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAF', STRINGDECODE('Safi\u00c2\u00a0'), 3, 'Province', 'MOR/STH/MRS/SAF', 'MRS', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAF', STRINGDECODE('\u00d8\u00a2\u00d8\u00b3\u00d9\ufffd\u00d9\u0160'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/STH/MRS/SAF', 'MRS', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SAF', STRINGDECODE('Safi\u00c2\u00a0'), 3, 'Province', 'MOR/STH/MRS/SAF', 'MRS', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('YSF', STRINGDECODE('Youssoufia\u00c2\u00a0'), 3, 'Province', 'MOR/STH/MRS/YSF', 'MRS', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('YSF', STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u0160\u00d9\u02c6\u00d8\u00b3\u00d9\ufffd\u00d9\u0160\u00d8\u00a9'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/STH/MRS/YSF', 'MRS', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('YSF', STRINGDECODE('Youssoufia\u00c2\u00a0'), 3, 'Province', 'MOR/STH/MRS/YSF', 'MRS', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SOS', 'Souss-Massa', 2, 'Region', 'MOR/STH/SOS', 'STH', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('SOS', STRINGDECODE('\u00d8\u00b3\u00d9\u02c6\u00d8\u00b3\u00d9\u2026\u00d8\u00a7\u00d8\u00b3\u00d8\u00a9'), 2, STRINGDECODE('\u00d9\u2026\u00d9\u2020\u00d8\u00b7\u00d9\u201a\u00d8\u00a9'), 'MOR/STH/SOS', 'STH', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL); 
INSERT INTO MASTER.ZONE(CODE, NAME, HIERARCHY_LEVEL, HIERARCHY_LEVEL_NAME, HIERARCHY_PATH, PARENT_ZONE_CODE, LANG_CODE, IS_ACTIVE, CR_BY, CR_DTIMES, UPD_BY, UPD_DTIMES, IS_DELETED, DEL_DTIMES) VALUES
('SOS', 'Sus-Massa', 2, STRINGDECODE('R\u00c3\u00a9gion'), 'MOR/STH/SOS', 'STH', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TTA', STRINGDECODE('Tata\u00c2\u00a0'), 3, 'Province', 'MOR/STH/SOS/TTA', 'SOS', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TTA', STRINGDECODE('\u00d8\u00b7\u00d8\u00a7\u00d8\u00b7\u00d8\u00a7'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/STH/SOS/TTA', 'SOS', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TTA', STRINGDECODE('Tata\u00c2\u00a0'), 3, 'Province', 'MOR/STH/SOS/TTA', 'SOS', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TZT', 'Tiznit', 3, 'Province', 'MOR/STH/SOS/TZT', 'SOS', 'eng', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TZT', STRINGDECODE('\u00d8\u00aa\u00d8\u00b2\u00d9\u2020\u00d9\u0160\u00d8\u00aa\u00e2\u20ac\u017d'), 3, STRINGDECODE('\u00d8\u00a7\u00d9\u201e\u00d9\u2026\u00d8\u00ad\u00d8\u00a7\u00d9\ufffd\u00d8\u00b8\u00d8\u00a9'), 'MOR/STH/SOS/TZT', 'SOS', 'ara', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('TZT', 'Tiznit', 3, 'Province', 'MOR/STH/SOS/TZT', 'SOS', 'fra', TRUE, 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL); 

CREATE MEMORY TABLE if not exists master.location(
	code character varying(36) NOT NULL,
	name character varying(128) NOT NULL,
	hierarchy_level smallint NOT NULL,
	hierarchy_level_name character varying(64) NOT NULL,
	parent_loc_code character varying(36),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_loc_code PRIMARY KEY (code,lang_code)

);
INSERT INTO MASTER.location(code, name, hierarchy_level, hierarchy_level_name, parent_loc_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('MOR','MyCountry',0,'Country',NULL,'eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('RSK','Rabat Sale Kenitra',1,'Region','MOR','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KTA','Kenitra',2,'Province','RSK','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('KNT','Kenitra',3,'City','KTA','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('BNMR','Ben Mansour',4,'Zone','KNT','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('14022','14022',5,'Postal Code','BNMR','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL),
('MOGR','Mograne',4,'Zone','KNT','eng',TRUE,'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, NULL, NULL, NULL);

CREATE MEMORY TABLE if not exists MASTER.LANGUAGE(
    CODE VARCHAR(3) NOT NULL,
    CR_BY VARCHAR(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    DEL_DTIMES TIMESTAMP,
    IS_ACTIVE BOOLEAN NOT NULL,
    IS_DELETED BOOLEAN,
    UPD_BY VARCHAR(256),
    UPD_DTIMES TIMESTAMP,
    FAMILY VARCHAR(64),
    NAME VARCHAR(64) NOT NULL,
    NATIVE_NAME VARCHAR(64)
);  
ALTER TABLE MASTER.LANGUAGE ADD CONSTRAINT MASTER.CONSTRAINT_C PRIMARY KEY(CODE);             
-- 3 +/- SELECT COUNT(*) FROM MASTER.LANGUAGE;
INSERT INTO MASTER.LANGUAGE(CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, FAMILY, NAME, NATIVE_NAME) VALUES
('eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Indo-European', 'English', 'English'),
('ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Afro-Asiatic', 'Arabic', STRINGDECODE('\u0627\u0644\u0639\u064e\u0631\u064e\u0628\u0650\u064a\u064e\u0651\u0629\u200e')),
('fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Indo-European', 'French', STRINGDECODE('fran\u00e7ais'));
CREATE MEMORY TABLE  IF NOT EXISTS MASTER.LOC_HIERARCHY_LIST(
    hierarchy_level smallint NOT NULL,
    hierarchy_level_name character varying(64) NOT NULL,
    lang_code character varying(3) NOT NULL,
    is_active boolean NOT NULL,
    cr_by character varying(256) NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256),
    upd_dtimes timestamp,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp
);   
ALTER TABLE MASTER.LOC_HIERARCHY_LIST ADD CONSTRAINT pk_loc_hierlst PRIMARY KEY (hierarchy_level, hierarchy_level_name, lang_code); 


INSERT INTO master.loc_hierarchy_list(
	hierarchy_level, hierarchy_level_name, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes)
	VALUES ('0','Country','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
('0','بلد','ara',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
('0','Pays','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL),
('1','Region','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL, NULL, NULL);
CREATE MEMORY TABLE if not exists MASTER.REG_EXCEPTIONAL_HOLIDAY(
    REGCNTR_ID CHARACTER VARYING(10) NOT NULL,
    HOL_DATE DATE NOT NULL,
    HOL_NAME CHARACTER VARYING(128),
    HOL_REASON CHARACTER VARYING(256),
    LANG_CODE CHARACTER VARYING(3) NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL,
    CR_BY CHARACTER VARYING(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    UPD_BY CHARACTER VARYING(256),
    UPD_DTIMES TIMESTAMP,
    IS_DELETED BOOLEAN,
    DEL_DTIMES TIMESTAMP
);            
ALTER TABLE MASTER.REG_EXCEPTIONAL_HOLIDAY ADD CONSTRAINT MASTER.PK_EXCEPTIONAL_HOL PRIMARY KEY(REGCNTR_ID, HOL_DATE);        
-- 0 +/- SELECT COUNT(*) FROM MASTER.REG_EXCEPTIONAL_HOLIDAY;
CREATE MEMORY TABLE MASTER.BIOMETRIC_TYPE(
    CODE VARCHAR(255) NOT NULL,
    LANG_CODE VARCHAR(3) NOT NULL,
    CR_BY VARCHAR(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    DEL_DTIMES TIMESTAMP,
    IS_ACTIVE BOOLEAN NOT NULL,
    IS_DELETED BOOLEAN,
    UPD_BY VARCHAR(256),
    UPD_DTIMES TIMESTAMP,
    DESCR VARCHAR(255),
    NAME VARCHAR(255) NOT NULL
);   
ALTER TABLE MASTER.BIOMETRIC_TYPE ADD CONSTRAINT MASTER.CONSTRAINT_B PRIMARY KEY(CODE, LANG_CODE);            
-- 9 +/- SELECT COUNT(*) FROM MASTER.BIOMETRIC_TYPE;          
INSERT INTO MASTER.BIOMETRIC_TYPE(CODE, LANG_CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, DESCR, NAME) VALUES
('FNR', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Finger prints of the applicant', 'Fingerprint'),
('IRS', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Iris of the applicant', 'Iris'),
('PHT', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Photo of the face of the applicant', 'Photo'),
('FNR', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0628\u0635\u0645\u0627\u062a \u0627\u0644\u0623\u0635\u0627\u0628\u0639 \u0644\u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0628\u0635\u0645\u0629 \u0627\u0644\u0625\u0635\u0628\u0639')),
('IRS', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0622\u064a\u0631\u064a\u0633 \u0644\u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u0639\u064a\u0646')),
('PHT', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, STRINGDECODE('\u0635\u0648\u0631\u0629 \u0644\u0648\u062c\u0647 \u0645\u0642\u062f\u0645 \u0627\u0644\u0637\u0644\u0628'), STRINGDECODE('\u0635\u0648\u0631')),
('FNR', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Empreintes digitales du demandeur', 'Empreintes digitales'),
('IRS', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Iris du demandeur', 'Iris'),
('PHT', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'Photo du visage du demandeur', 'Photo');
CREATE MEMORY TABLE if not exists MASTER.BIOMETRIC_ATTRIBUTE(
    CODE VARCHAR(255) NOT NULL,
    LANG_CODE VARCHAR(3) NOT NULL,
    CR_BY VARCHAR(256) NOT NULL,
    CR_DTIMES TIMESTAMP NOT NULL,
    DEL_DTIMES TIMESTAMP,
    IS_ACTIVE BOOLEAN NOT NULL,
    IS_DELETED BOOLEAN,
    UPD_BY VARCHAR(256),
    UPD_DTIMES TIMESTAMP,
    BMTYP_CODE VARCHAR(36) NOT NULL,
    DESCR VARCHAR(128),
    NAME VARCHAR(255) NOT NULL
);        
ALTER TABLE MASTER.BIOMETRIC_ATTRIBUTE ADD CONSTRAINT MASTER.CONSTRAINT_35 PRIMARY KEY(CODE, LANG_CODE);      
-- 15 +/- SELECT COUNT(*) FROM MASTER.BIOMETRIC_ATTRIBUTE;    
INSERT INTO MASTER.BIOMETRIC_ATTRIBUTE(CODE, LANG_CODE, CR_BY, CR_DTIMES, DEL_DTIMES, IS_ACTIVE, IS_DELETED, UPD_BY, UPD_DTIMES, BMTYP_CODE, DESCR, NAME) VALUES
('TM', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Left and Right Thumbs', 'Thumbs'),
('RH', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Right Slab', 'Right Slab'),
('LH', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Print of Left Slab', 'Left Slab'),
('LI', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Print of Left Iris', 'Left Iris'),
('RI', 'eng', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Print of Right Iris', 'Right Iris'),
('TM', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0625\u0628\u0647\u0627\u0645 \u0627\u0644\u0623\u064a\u0633\u0631 \u0648\u0627\u0644\u0623\u064a\u0645\u0646'), STRINGDECODE('\u0627\u0644\u0627\u0628\u0647\u0627\u0645')),
('RH', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0645\u0646 \u0627\u0644\u064a\u0645\u064a\u0646 \u0644\u0648\u062d'), STRINGDECODE('\u0644\u0648\u062d \u0627\u0644\u062d\u0642')),
('LH', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0628\u0644\u0627\u0637\u0629 \u0627\u0644\u064a\u0633\u0627\u0631'), STRINGDECODE('\u0644\u0648\u062d \u0627\u0644\u064a\u0633\u0627\u0631')),
('LI', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u064a\u0633\u0631\u0649'), STRINGDECODE('\u063a\u0627\u062f\u0631 \u0627\u0644\u0642\u0632\u062d\u064a\u0629')),
('RI', 'ara', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', STRINGDECODE('\u0637\u0628\u0627\u0639\u0629 \u0627\u0644\u0642\u0632\u062d\u064a\u0629 \u0627\u0644\u064a\u0645\u0646\u0649'), STRINGDECODE('\u0627\u0644\u062d\u0642 \u0627\u064a\u0631\u064a\u0633')),
('TM', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Empreinte des pouces gauche et droit', 'Les pouces'),
('RH', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Impression de la dalle droite', 'Dalle droite'),
('LH', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'FNR', 'Impression de la dalle gauche', 'Dalle gauche'),
('LI', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Gravure de Iris Gauche', 'Iris gauche'),
('RI', 'fra', 'superadmin', TIMESTAMP '2018-12-10 11:42:52.994', NULL, TRUE, NULL, NULL, NULL, 'IRS', 'Empreinte de l''iris droit', 'Iris droit');
CREATE MEMORY TABLE if not exists master.device_master(
	id 			character varying(36) NOT NULL,
	name 		character varying(64) NOT NULL,
	mac_address character varying(64) NOT NULL,
	serial_num 	character varying(64) NOT NULL,
	ip_address 	character varying(17),
	validity_end_dtimes timestamp,
	dspec_id 	character varying(36) NOT NULL,
	zone_code 	character varying(36) NOT NULL,
	regcntr_id  character varying(10),
	lang_code 	character varying(3) NOT NULL,
	is_active 	boolean NOT NULL,
	cr_by 		character varying(256) NOT NULL,
	cr_dtimes 	timestamp NOT NULL,
	upd_by 		character varying(256),
	upd_dtimes 	timestamp,
	is_deleted 	boolean,
	del_dtimes 	timestamp
	

);
ALTER TABLE MASTER.device_master ADD CONSTRAINT pk_devicem_id PRIMARY KEY (id,lang_code);
-- 15 +/- SELECT COUNT(*) FROM master.device_master;
INSERT INTO master.device_master(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('3000038','Dummy Finger Print Scanner 18','2D-F1-90-89-64-AC','DJ2032361963',NULL,NULL,'165','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000039','Dummy Finger Print Scanner 19','91-16-2F-35-1F-9F','RU3582050357',NULL,NULL,'165','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000040','Dummy Finger Print Scanner 20','CB-B1-03-83-80-1D','RB2496727160',NULL,NULL,'165','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000058','Dummy IRIS Scanner 18','AA-D8-DC-3A-0A-A8','AQ8028890156',NULL,NULL,'327','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000059','Dummy IRIS Scanner 19','0D-1B-B0-5A-D9-98','KM7656759005',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000060','Dummy IRIS Scanner 20','FA-60-AC-D0-54-1C','OZ8730264911',NULL,NULL,'327','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000077','Dummy Web Camera 17','0F-ED-8F-0F-94-88','H706H5247430756',NULL,NULL,'736','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000078','Dummy Web Camera 18','F2-D9-F8-92-F6-FB','U178N6742928759',NULL,NULL,'736','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000079','Dummy Web Camera 19','76-24-19-E2-8E-30','Z405J2389264415',NULL,NULL,'736','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000080','Dummy Web Camera 20','D1-2C-D9-BA-5F-FE','J499B8422627284',NULL,NULL,'736','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000117','Dummy Printer 17','95-E6-66-FB-BC-CB','NO281K8273556',NULL,NULL,'920','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000118','Dummy Printer 18','91-18-5A-B4-C1-CB','VL347T8888447',NULL,NULL,'920','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000119','Dummy Printer 19','FD-68-06-74-5B-1B','TN262J8370270',NULL,NULL,'920','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('3000120','Dummy Printer 20','4C-D6-9C-DB-B4-CC','IX248Z5303484',NULL,NULL,'920','SOS','10015','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE master.device_spec(
	id character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	brand character varying(32) NOT NULL,
	model character varying(16) NOT NULL,
	dtyp_code character varying(36) NOT NULL,
	min_driver_ver character varying(16) NOT NULL,
	descr character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
ALTER TABLE master.device_spec ADD CONSTRAINT pk_dspec_code PRIMARY KEY (id,lang_code);
-- 15 +/- SELECT COUNT(*) FROM master.device_spec;
INSERT INTO master.device_spec(id, name, brand, model, dtyp_code, min_driver_ver, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('165','Fingerprint Scanner','Safran Morpho','1300 E2','FRS','1.12','To scan fingerprint','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('165','Fingerprint Scanner','Safran Morpho','1300 E2','FRS','1.12','To scan fingerprint','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('327','High Speed Dual Iris Scanner','Cogent','3M','IRS','2.34','To scan iris','fra',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('327','High Speed Dual Iris Scanner','Cogent','3M','IRS','2.34','To scan iris','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

CREATE MEMORY TABLE if not exists master.device_master_h(
	id 			character varying(36) NOT NULL,
	name 		character varying(64) NOT NULL,
	mac_address character varying(64) NOT NULL,
	serial_num 	character varying(64) NOT NULL,
	ip_address 	character varying(17),
	validity_end_dtimes timestamp,
	dspec_id 	character varying(36) NOT NULL,
	zone_code 	character varying(36) NOT NULL,
	regcntr_id  character varying(10),
	lang_code 	character varying(3) NOT NULL,
	is_active 	boolean NOT NULL,
	cr_by 		character varying(256) NOT NULL,
	cr_dtimes 	timestamp NOT NULL,
	upd_by 		character varying(256),
	upd_dtimes 	timestamp,
	is_deleted 	boolean,
	del_dtimes 	timestamp,
	eff_dtimes 	timestamp NOT NULL
	

);
ALTER TABLE master.device_master_h ADD CONSTRAINT pk_devicem_h_id PRIMARY KEY (id,lang_code,eff_dtimes);
-- 15 +/- SELECT COUNT(*) FROM master.device_master_h;
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes)	VALUES 
('3000021','Dummy Finger Print Scanner 1','85-BB-97-4B-14-05','SZ5912878988',NULL,NULL,'165','NTH','10001','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000022','Dummy Finger Print Scanner 2','CD-27-9D-D6-F4-7B','HI5265090311',NULL,NULL,'165','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000023','Dummy Finger Print Scanner 3','6B-D5-10-4B-3A-9E','AT8075685650',NULL,NULL,'165','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes)	VALUES 
('3000042','Dummy IRIS Scanner 2','6D-A3-60-2E-B8-2F','RI8899475915',NULL,NULL,'327','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000043','Dummy IRIS Scanner 3','08-4B-68-23-1A-E6','VK7923826383',NULL,NULL,'327','NTH','10003','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000044','Dummy IRIS Scanner 4','DE-BE-02-60-64-4D','BB4283888901',NULL,NULL,'327','RSK','10004','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');
INSERT INTO master.device_master_h(id, name, mac_address, serial_num, ip_address, validity_end_dtimes, dspec_id, zone_code, regcntr_id, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes, eff_dtimes)	VALUES 
('3000075','Dummy Web Camera 15','BA-DE-90-AF-C4-6E','R169V3235864050',NULL,NULL,'736','MRS','10013','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000076','Dummy Web Camera 16','8D-28-4B-A7-F6-98','M262X1515179346',NULL,NULL,'736','MRS','10013','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994'),
('3000077','Dummy Web Camera 17','0F-ED-8F-0F-94-88','H706H5247430756',NULL,NULL,'736','SOS','10014','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL,TIMESTAMP '2018-12-10 11:42:52.994');
CREATE MEMORY TABLE if not exists  master.device_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	
);
ALTER TABLE master.device_type ADD CONSTRAINT pk_dtyp_code PRIMARY KEY (code,lang_code);
-- 15 +/- SELECT COUNT(*) FROM master.device_type;
INSERT INTO master.device_type(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('FRS','Finger Print Scanner','For scanning fingerprints','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('CMR','Camera','For capturing photo','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('PRT','Printer','For printing Documents','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('SCN','Document Scanner','For scanning documents','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE  if not exists master.doc_category(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
ALTER TABLE master.doc_category ADD CONSTRAINT pk_doccat_code PRIMARY KEY (code,lang_code);
-- 15 +/- SELECT COUNT(*) FROM master.device_type;
INSERT INTO master.doc_category(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('POI','Proof of Identity','Identity Proof','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POR','Proof of Relationship','Proof Relationship of the person','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('POE','Proof of Biometric Exception','Proof of Biometric Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE if not exists  master.doc_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
ALTER TABLE master.doc_type ADD CONSTRAINT pk_doctyp_code PRIMARY KEY (code,lang_code);
-- 15 +/- SELECT COUNT(*) FROM master.device_type;
INSERT INTO master.doc_type(code, name, descr, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('COE','Certification of Exception','Certificate of Exception','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('DOC002','PAN card','PAN card','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR','Certificate of residence','Proof of Resident','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE if not exists  master.valid_document(
	doctyp_code character varying(36) NOT NULL,
	doccat_code character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);
ALTER TABLE master.valid_document ADD CONSTRAINT pk_valdoc_code PRIMARY KEY (doctyp_code,doccat_code);
-- 15 +/- SELECT COUNT(*) FROM master.valid_document;
INSERT INTO master.valid_document(doctyp_code, doccat_code, lang_code, is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('CIN','POI','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('COR','POA','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('DOC001','POI','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE if not exists master.dynamic_field(
	id character varying(36) NOT NULL,
	name character varying(36) NOT NULL,
	description character varying(256),
	data_type character varying(16),
	value_json character varying,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_schfld_id PRIMARY KEY (id),
	CONSTRAINT uk_schfld_name UNIQUE (name,lang_code)

);
INSERT INTO master.dynamic_field(id, name, description, data_type,value_json,lang_code,is_active,cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('10001','bloodType1','Blood Type11','string',Null,'eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10002','bloodType2','Blood Type12','string',Null,'eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE MEMORY TABLE if not exists master.id_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp
	

);

ALTER TABLE MASTER.id_type ADD CONSTRAINT pk_idtyp_code PRIMARY KEY (code,lang_code);
INSERT INTO master.id_type(code, name, descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('UIN','Unique Identification Number','National ID given to the applicant','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('PRID','Pre-registration ID','ID assigned after Pre-registration','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('RID','Registration ID','ID assigned after registration','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('VID','Virtual ID','ID used in replacement of UIN','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

CREATE MEMORY TABLE if not exists master.loc_holiday(
	id integer NOT NULL,
	location_code character varying(36) NOT NULL,
	holiday_date date,
	holiday_name character varying(64),
	holiday_desc character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp	

);
ALTER TABLE master.loc_holiday ADD CONSTRAINT pk_lochol_id PRIMARY KEY (id,location_code,lang_code);
--CONSTRAINT uk_lochol_name UNIQUE (holiday_name,holiday_date,location_code,lang_code),CONSTRAINT fk_lochol_loc FOREIGN KEY (lang_code, location_code);
INSERT INTO master.loc_holiday(id, location_code, holiday_date,holiday_name,holiday_desc, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('2000001','KTA',TO_DATE('10-12-2019','dd-MM-yyyy'),'New Year Day', 'National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000002','KTA',TO_DATE('12-12-2019','dd-MM-yyyy'),'Anniversary of the Independence Manifesto',' National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000004','RBT',TO_DATE('14-12-2019','dd-MM-yyyy'),'Eid al-Fitr','National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('2000005','RBT',TO_DATE('20-12-2019','dd-MM-yyyy'),'Feast of the Throne',' National Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);

CREATE MEMORY TABLE if not exists master.reg_exceptional_holiday(
	regcntr_id character varying(10) NOT NULL,
	hol_date date NOT NULL,
	hol_name character varying(128),
	hol_reason character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_exceptional_hol PRIMARY KEY (regcntr_id,hol_date)

);
INSERT INTO master.reg_exceptional_holiday(regcntr_id, hol_date, hol_name,hol_reason, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('10001',TO_DATE('01-12-2019','dd-MM-yyyy'),'Emergency Holiday','Emergency Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('10002',TO_DATE('01-12-2019','dd-MM-yyyy'),'Emergency Holiday','Emergency Holiday','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE TABLE master.registration_center(
	id character varying(10) NOT NULL,
	name character varying(128) NOT NULL,
	cntrtyp_code character varying(36),
	addr_line1 character varying(256),
	addr_line2 character varying(256),
	addr_line3 character varying(256),
	latitude character varying(32),
	longitude character varying(32),
	location_code character varying(36) NOT NULL,
	contact_phone character varying(16),
	contact_person character varying(128),
	number_of_kiosks smallint,
	working_hours character varying(32),
	per_kiosk_process_time time,
	center_start_time time,
	center_end_time time,
	lunch_start_time time,
	lunch_end_time time,
	time_zone character varying(64),
	holiday_loc_code character varying(36),
	zone_code character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_regcntr_code PRIMARY KEY (id,lang_code)

);
INSERT INTO master.registration_center(id,name,cntrtyp_code,addr_line1,addr_line2,addr_line3,latitude,longitude,location_code,contact_phone,contact_person,number_of_kiosks,working_hours,per_kiosk_process_time,center_start_time,center_end_time,lunch_start_time,lunch_end_time,time_zone,holiday_loc_code,zone_code,lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES 
('10001','Center A Ben Mansour','REG','P4238','Ben Mansour','MyCountry','34.52117','-6.453275','14022','779517433','John Doe',3,'8:00:00',Time '0:15:00', Time '9:00:00',Time '17:00:00',Time '13:00:00',Time '14:00:00','(GTM+01:00) CENTRAL EUROPEAN TIME','KTA','RBT','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE TABLE master.blacklisted_words(
	word character varying(128) NOT NULL,
	descr character varying(256),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_blwrd_code PRIMARY KEY (word,lang_code)

);
INSERT INTO master.blacklisted_words(word, descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('shit','Blacklisted Word','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);
CREATE TABLE master.appl_form_type(
	code character varying(36) NOT NULL,
	name character varying(64) NOT NULL,
	descr character varying(128),
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	CONSTRAINT pk_applftyp_code PRIMARY KEY (code,lang_code)

);
INSERT INTO master.appl_form_type(code, name,descr, lang_code,is_active, cr_by, cr_dtimes, upd_by, upd_dtimes, is_deleted, del_dtimes) VALUES
('105','form','form desc','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL),
('106','form1','form desc1','eng',TRUE,'superadmin',TIMESTAMP '2018-12-10 11:42:52.994',NULL,NULL,NULL,NULL);