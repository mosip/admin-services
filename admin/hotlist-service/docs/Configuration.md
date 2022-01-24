# Hotlist service Configuration Guide
## Overview
The guide here lists down some of the important properties that may be customized for a given installation. Note that the listing here is not exhaustive, but a checklist to review properties that are likely to be different from default.  If you would like to see all the properties, then refer to the files listed below.

## Configuration files
Hotlist services uses the following configuration files:
```
Hotlist-default.properties
```
The above files are located in [mosip-config](https://github.com/mosip/mosip-config/blob/develop2-v2/) repo

## DB
* `mosip.hotlist.datasource.url`
* `mosip.hotlist.datasource.username`
* `mosip.hotlist.datasource.password`

Point the above to your DB .  Default is set to point to in-cluster Postgres installed with sandbox.

## Data Validation
* `mosip.hotlist.allowedIdTypes`
Above property is used to specify ID type.Example:

* `mosip.hotlist.allowedIdTypes= UIN,VID,MACHINE_ID,PARTNER_ID,OPERATOR_ID,CENTER_ID,DEVICE,DEVICE_MODEL,FTM_PUBLIC_KEY`
## Websub topic configuration
* `mosip.hotlist.topic-to-publish: specify websub topic name`

## Encryption/decyrption configuration
* `mosip.hotlist.crypto.app-id:specify application id`
* `mosip.hotlist.crypto.ref-id:specify reference id` 

## Cleanup schedule
* `mosip.hotlist.cleanup-schedule.init-delay:specify initial delay in Hours`
* `mosip.hotlist.cleanup-schedule.fixed-rat:specify fixed delay in which cleanup will be done in Hours` 

Above properties are used to specify initial and fixed delay for a clean up job

## REST services
* `mosip.hotlist.encryptor.rest.uri: specify keymanager encrypt url`
* `mosip.hotlist.encryptor.rest.httpMethod: specify method type`
* `mosip.hotlist.encryptor.rest.headers.mediaType: specify media type`
* `mosip.hotlist.encryptor.rest.timeout: specify time out for encrypt response`

* `mosip.hotlist.decryptor.rest.uri=specify keymanager decrypt url`
* `mosip.hotlist.decryptor.rest.httpMethod: specify method type`
* `mosip.hotlist.decryptor.rest.headers.mediaType: specify media type`
* `mosip.hotlist.decryptor.rest.timeout: specify time out for decrypt response`

* `mosip.hotlist.audit.rest.uri=specify audit url`
* `mosip.hotlist.audit.rest.httpMethod: specify method type`
* `mosip.hotlist.audit.rest.headers.mediaType: specify media type`

## Roles for the API
All the API's of hotlist service is associated with roles , only authorized person can access concerned API.
Example:
*`mosip.role.admin.hotlist.postHotlistUnblock=HOTLIST_ADMIN`
In the above example only zonal admin can access postHotlistUnblock API, similarly authorization is given for all the API's


