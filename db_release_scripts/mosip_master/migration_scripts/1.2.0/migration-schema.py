#!/usr/bin/python3


## This script should be executed after DB upgrade and 1.2.0.* masterdata-service deployment

from datetime import datetime, timezone, timedelta
import requests
import json


authURL='https://qa3.mosip.net/v1/authmanager/authenticate/useridPwd'
schemaURL='https://qa3.mosip.net/v1/syncdata/latestidschema?schemaVersion=0'
uispecURL='https://qa3.mosip.net/v1/masterdata/uispec'
uispecPublishURL='https://qa3.mosip.net/v1/masterdata/uispec/publish'
primaryLang='eng'
secondaryLang='eng'
username='110006'
password='mosip'

## Constant
domain = 'registration-client'


# Get request auth token
auth_req_data = {
  'id': 'string',
  'metadata': {},
  'request': {
    'appId': 'admin',
    'password': password,
    'userName': username
  },
  'requesttime': '2022-05-20T06:12:52.994Z',
  'version': 'string'
}
authresponse = requests.post(authURL, json= auth_req_data)
access_token = authresponse.headers["authorization"]

# invoke syncdata service with authtoken in headers
req_headers = {'Cookie' : 'Authorization='+access_token}
schema_resp = requests.get(schemaURL, headers=req_headers)

schema_resp_1 = schema_resp.json()
schema_resp_2 = schema_resp_1['response']

identity_schema_id = schema_resp_2['id']
cur_schema = schema_resp_2['schema']


#publish ui-spec of type 'schema' for backward compatibility
publish_spec(domain, 'schema', cur_schema)


demographics=[]
documents=[]
biometrics=[]
# read response json and create UI-specs
for field in cur_schema:
	if(field['inputRequired']):
		#Add labels		
		labels = field['label']
		labels[primaryLang] = labels['primary']
		labels[secondaryLang] = labels['secondary']
		field['label'] = labels

		if field['type'] == 'documentType':
			documents.append(field)
		elif field['type'] == 'biometricsType':
			biometrics.append(field)
		else
			demographics.append(field)


new_spec = {
					   "id": "NEW",
					   "order": 1,
					   "flow": "NEW"
					   "label": {
					        "eng": "New Registration",
		              "ara": "تسجيل جديد",
		              "fra": "Nouvelle inscription"
					   },
					   "screens": [{
					           "order": 1,
					           "name": "DemographicsDetails",
					           "label": {
					              "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "caption": {
					               "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "fields": demographics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": true,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 2,
					           "name": "DocumentDetails",
					           "label": {
					               "ara": "تحميل الوثيقة",
			                  "fra": "Des documents",
			                  "eng": "Document Upload"
					           },
					           "caption": {
					               "ara": "وثائق",
			                  "fra": "Des documents",
			                  "eng": "Documents"
					           },
					           "fields": documents,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 2,
					           "name": "BiometricDetails",
					           "label": {
					              "ara": "التفاصيل البيومترية",
			                  "fra": "Détails biométriques",
			                  "eng": "Biometric Details"
					           },
					           "caption": {
					               "ara": "التفاصيل البيومترية",
				                  "fra": "Détails biométriques",
				                  "eng": "Biometric Details"
					           },
					           "fields": biometrics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       }
					   ],
					   "caption": {
					       "eng": "New Registration",
		              "ara": "تسجيل جديد",
		              "fra": "Nouvelle inscription"
					   },
					   "icon": "NewReg.png",
					   "isActive": true,
					   "autoSelectedGroups": null
					}
	

#publish ui-spec with for new process
publish_spec(domain, 'newProcess', new_spec)


update_spec = {
					   "id": "UPDATE",
					   "order": 2,
					   "flow": "UPDATE"
					   "label": {
					        "eng": "Update UIN",
		              "ara": "تحديث UIN",
		              "fra": "Mettre à jour l'UIN"
					   },
					   "screens": [{
					           "order": 1,
					           "name": "DemographicsDetails",
					           "label": {
					              "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "caption": {
					               "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "fields": demographics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 2,
					           "name": "DocumentDetails",
					           "label": {
					               "ara": "تحميل الوثيقة",
			                  "fra": "Des documents",
			                  "eng": "Document Upload"
					           },
					           "caption": {
					               "ara": "وثائق",
			                  "fra": "Des documents",
			                  "eng": "Documents"
					           },
					           "fields": documents,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 3,
					           "name": "BiometricDetails",
					           "label": {
					              "ara": "التفاصيل البيومترية",
			                  "fra": "Détails biométriques",
			                  "eng": "Biometric Details"
					           },
					           "caption": {
					               "ara": "التفاصيل البيومترية",
				                  "fra": "Détails biométriques",
				                  "eng": "Biometric Details"
					           },
					           "fields": biometrics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       }
					   ],
					   "caption": {
					       "eng": "Update UIN",
		              "ara": "تحديث UIN",
		              "fra": "Mettre à jour l'UIN"
					   },
					   "icon": "UINUpdate.png",
					   "isActive": true,
					   "autoSelectedGroups": null
					}
	

#publish ui-spec with for update process
publish_spec(domain, 'updateProcess', update_spec)


lost_spec = {
					   "id": "LOST",
					   "order": 3,
					   "flow": "LOST"
					   "label": {
					        "eng": "Lost UIN",
		              "ara": "فقدت UIN",
		              "fra": "UIN perdu"
					   },
					   "screens": [{
					           "order": 1,
					           "name": "DemographicsDetails",
					           "label": {
					              "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "caption": {
					               "ara": "التفاصيل الديموغرافية",
			                  "fra": "Détails démographiques",
			                  "eng": "Demographic Details"
					           },
					           "fields": demographics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 2,
					           "name": "DocumentDetails",
					           "label": {
					               "ara": "تحميل الوثيقة",
			                  "fra": "Des documents",
			                  "eng": "Document Upload"
					           },
					           "caption": {
					               "ara": "وثائق",
			                  "fra": "Des documents",
			                  "eng": "Documents"
					           },
					           "fields": documents,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       },
					       {
					           "order": 3,
					           "name": "BiometricDetails",
					           "label": {
					              "ara": "التفاصيل البيومترية",
			                  "fra": "Détails biométriques",
			                  "eng": "Biometric Details"
					           },
					           "caption": {
					               "ara": "التفاصيل البيومترية",
				                  "fra": "Détails biométriques",
				                  "eng": "Biometric Details"
					           },
					           "fields": biometrics,
					           "layoutTemplate": null,
					           "preRegFetchRequired": false,
					           "additionalInfoRequestIdRequired": false,
					           "active": false
					       }
					   ],
					   "caption": {
					       "eng": "Lost UIN",
		              "ara": "فقدت UIN",
		              "fra": "UIN perdu"
					   },
					   "icon": "LostUIN.png",
					   "isActive": true,
					   "autoSelectedGroups": null
					}
	

#publish ui-spec with for update process
publish_spec(domain, 'lostProcess', lost_spec)




def publish_spec(domain, spec_type, specjson):
	print("identity schema id : " + identity_schema_id)
	print("identity spec_type : " + spec_type)
	request_json = {
									  "id": "string",
									  "version": "string",
									  "requesttime": "2022-06-20T09:05:09.304Z",
									  "metadata": {},
									  "request": {
									    "identitySchemaId": identity_schema_id,
									    "domain": domain,
									    "type": spec_type,
									    "title": spec_type + " UI spec",
									    "description": spec_type + " UI spec",
									    "jsonspec": specjson
									  }
									}
	spec_resp = requests.post(uispecURL, json=request_json, headers=req_headers)
	spec_resp_json = spec_resp.json()
	print("UI spec created : " + spec_resp_json)

	spec_resp_json_2 = schema_resp_1['response']
	spec_id = spec_resp_json_2['id']
	dt_now = datetime.now(timezone.utc)
	dt_now = dt_now + timedelta(minutes=2)
	dt_now_str = dt_now.strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3]

	publish_spec_req = {
											  "id": "string",
											  "version": "string",
											  "requesttime": "2022-06-20T09:05:09.312Z",
											  "metadata": {},
											  "request": {
											    "id": spec_id,
											    "effectiveFrom": dt_now_str+'Z'
											  }
											}
	publish_resp = requests.put(uispecPublishURL, json=request_json, headers=req_headers)
	publish_resp_json = publish_resp.json()
	print("UI spec published : " + publish_resp_json)