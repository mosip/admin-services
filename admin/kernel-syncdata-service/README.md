## Sync Data Service 

[Background & Design](../../docs/design/kernel/kernel-syncservices.md)

[Api Documentation](https://github.com/mosip/mosip/wiki/Kernel-APIs#sync-data)


Default Port and Context Path

```
server.port=8089
server.servlet.path=/syncdata

```

localhost:8089/syncdata/swagger-ui.html


**Application Properties**

[kernel-syncdata-service-dev.properties](../../config/kernel-syncdata-service-dev.properties)

```
mosip.kernel.syncdata.global-config-file=global-config.json
mosip.kernel.syncdata.registration-center-config-file=registration-center-config.json

javax.persistence.jdbc.driver=org.postgresql.Driver
javax.persistence.jdbc.url=jdbc:postgresql://localhost:8888/mosip_master
javax.persistence.jdbc.user=dbuser
javax.persistence.jdbc.password=dbpwd


```


** 1. Global config **

* This API takes care of the overall configuration of the application.   
* **Admin** can only make these configuration changes.

```
endpoint: /syncdata/v1.0/globalconfigs 
```

** Sample Usage: **

Request:

```
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder().url("http://localhost:8086/syncdata/v1.0/globalconfigs").get().build();

Response response = client.newCall(request).execute();

```

Response:

Status: 200 OK

```
{
		"archivalPolicy":"arc_policy_2",
		"otpTimeOutInMinutes":2,
		"numberOfWrongAttemptsForOtp":5,
		"accountFreezeTimeoutInHours":10, 
		"uinLength":24,
		"vidLength":32,
		"pridLength":32,
		"tokenIdLength":23,
		"tspIdLength":24,
		"registrationCenterId":"KDUE83CJ3",
		"machineId":"MCBD3UI3",
		"mobilenumberlength":10,
		"restrictedNumbers":"8732,321,65",
		"languagesSupported":"eng,ara,fra"
}

```


** 2. Registration Center Config **

*  This API takes care of the configuration that is specific to Registration center.
*  **Admin** can only make these configuration changes.

```
endpoint: /syncdata/v1.0/registrationcenterconfig/{reg_center_id}
```

** Sample Usage: **

Request:

```
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder().url("http://localhost:8086/syncdata/v1.0/registrationcenterconfig/1").get().build();

Response response = client.newCall(request).execute();

```

Response:

Status: 200 OK

```
{
		"loginMode":"bootable_dongle",
		"fingerprintQualityThreshold":120,
		"fingerprintRetryAttempts":234,
		"irisQualityThreshold":25,
		"irisRetryAttempts":10,
		"faceQualityThreshold":25,
		"faceRetry":12,
		"supervisorVerificationRequiredForExceptions":true,
		"operatorRegSubmissionMode":"fingerprint",
		"supervisorAuthMode":"IRIS",
		"passwordExpiryDurationInDays":3,
		"keyValidityPeriodRegPack":3,
		"keyValidityPeriodPreRegPack":3,
		"retentionPeriodAudit":3,
		"automaticSyncFreqServerToClient":25,
		"blockRegistrationIfNotSynced":10,
		"maxDurationRegPermittedWithoutMasterdataSyncInDays":10,
		"maxDurationWithoutMasterdataSyncInDays":7,
		"modeOfNotifyingIndividual":"mobile",
		"noOfFingerprintAuthToOnboardUser":10,
		"noOfIrisAuthToOnboardUser":10,
		"multifactorauthentication":true,
		"gpsDistanceRadiusInMeters":3,
		"officerAuthType":"password",
		"supervisorAuthType":"password",
		"defaultDOB":"1-Jan",
		"maxDocSizeInMB":150
}

```

** 3. sync master data ** 

* If the input parameter in the 'Last Updated Time Stamp' is null, respond with all the data in the Master data tables. (For initial setup).
* Machine related master data should be sent only for the specific machine from which request is received
* Registration Center related data should be sent only for the specific Registration Center mapped to the machine from which request is received
* Device related data should be sent only for devices mapped to the machine from which request is received

```
endpoint:  /syncdata/v1.0/masterdata/{machineId}?lastUpdated=?

```
Sample Usage:

Request:

```

Request request = new Request.Builder().url("http://localhost:8086/syncdata/v1.0/masterdata/1001").get().build();

Response response = client.newCall(request).execute();
```
Response:

Status: 200 OK

```
{
  "registrationCenter": [
    {
      "id": "1",
      "name": "BangaloreMain",
      "centerTypeCode": "REG01",
      "addressLine1": "Global village",
      "addressLine2": null,
      "addressLine3": null,
      "latitude": "12.9180022",
      "longitude": "77.5028892",
      "locationCode": "LOC01",
      "holidayLocationCode": "LOC01",
      "contactPhone": "9348548",
      "numberOfStations": null,
      "workingHours": null,
      "languageCode": "ENG",
      "numberOfKiosks": 4,
      "perKioskProcessTime": "00:13:00",
      "centerStartTime": "09:00:00",
      "centerEndTime": "17:00:00",
      "timeZone": null,
      "contactPerson": null,
      "lunchStartTime": "13:00:00",
      "lunchEndTime": "14:00:00",
      "isActive": true
    }
  ],
  "registrationCenterTypes": [
    {
      "code": "REG01",
      "langCode": "ENG",
      "name": "Center One",
      "descr": "Registration Center One",
      "isActive": true
    }
  ],
  "machineDetails": [
    {
      "id": "HP",
      "name": "HP",
      "serialNum": "12345",
      "macAddress": null,
      "ipAddress": "127.01.01.01",
      "machineSpecId": "HP_ID",
      "langCode": "ENG",
      "isActive": true,
      "validityDateTime": "2022-11-15T22:55:42"
    }
  ],
  "machineSpecification": [
    {
      "id": "HP_ID",
      "name": "HP",
      "brand": "HP",
      "model": "Intel",
      "machineTypeCode": "1001",
      "minDriverversion": "0.05",
      "description": "HP laptop",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "machineType": [
    {
      "code": "1001",
      "langCode": "ENG",
      "name": "HP",
      "description": "HP laptop",
      "isActive": true
    }
  ],
  "devices": [
    {
      "id": "5454",
      "name": "HP",
      "serialNum": "678",
      "deviceSpecId": "1010",
      "macAddress": "129.0.0.0",
      "ipAddress": "129.0.0.0",
      "langCode": "ENG",
      "active": false
    }
  ],
  "deviceTypes": [
    {
      "code": "scanner",
      "langCode": "ENG",
      "name": "scanner",
      "description": "scanner descr",
      "isActive": true
    }
  ],
  "deviceSpecifications": [
    {
      "id": "1010",
      "name": "Scanner",
      "brand": "Scanner",
      "model": "Model 10",
      "deviceTypeCode": "scanner",
      "minDriverversion": "Min drive",
      "description": "scanner dis",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "holidays": [
    {
      "holidayId": "56",
      "holidayDate": "1994-12-12",
      "holidayDay": "1",
      "holidayMonth": "12",
      "holidayYear": "1994",
      "holidayName": "string",
      "languageCode": "ENG",
      "locationCode": null,
      "isActive": true
    }
  ],
  "documentCategories": [
    {
      "code": "DC021",
      "name": "string",
      "description": "string",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "documentTypes": [
    {
      "code": "string",
      "name": "string",
      "description": "string",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "validDocumentMapping": [
    {
      "docTypeCode": "DT007",
      "docCategoryCode": "DC007",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "templates": [
    {
      "id": "1",
      "name": "Sms template",
      "description": null,
      "fileFormatCode": "xml",
      "model": null,
      "fileText": null,
      "moduleId": "registation",
      "moduleName": null,
      "templateTypeCode": "SMS",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "templateFileFormat": [
    {
      "code": "html",
      "description": "html format",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "reasonCategory": [
    {
      "code": "RC1",
      "name": "reason_category",
      "description": "reason categroy",
      "langCode": "ENG",
      "isActive": true,
      "isDeleted": false
    }
  ],
  "blackListedWords": [
    {
      "word": "xyz",
      "description": "xyz",
      "langCode": "HIN",
      "isActive": true
    }
  ],
  "locationHierarchy": [
    {
      "code": "LOC01",
      "name": "LocationOne",
      "hierarchyLevel": 1,
      "hierarchyName": "Country",
      "parentLocCode": null,
      "languageCode": "ENG",
      "isActive": true,
      "createdBy": "mosip",
      "updatedBy": null
    }
    {
      "code": "IND",
      "name": "INDIA",
      "hierarchyLevel": 0,
      "hierarchyName": "COUNTRY",
      "parentLocCode": null,
      "languageCode": "KAN",
      "isActive": true,
      "createdBy": "defaultadmin@mosip.io",
      "updatedBy": null
    }
  ],
  "biometricattributes": [
    {
      "code": "1",
      "name": "sample data",
      "description": "sample data desc",
      "biometricTypeCode": "4",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "biometricTypes": [
    {
      "code": "1",
      "name": "Data Matcjing",
      "description": null,
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "applications": [
    {
      "code": "101",
      "name": "pre-registeration",
      "description": "Pre-registration Application Form",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "idTypes": [
    {
      "code": "T001",
      "descr": "Proof Of Identity",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "genders": [
    {
      "code": "string          ",
      "genderName": "string",
      "langCode": "ENG",
      "isActive": true
    }
  ],
  "languages": [
    {
      "code": "ENG",
      "name": "english",
      "family": "english",
      "nativeName": "english",
      "isActive": true
    }
  ]
}

```

