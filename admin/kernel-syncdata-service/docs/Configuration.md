# SyncData services Configuration Guide
## Overview
The guide here lists down some of the important properties that may be customized for a given installation. Note that the listing here is not exhaustive, but a checklist to review properties that are likely to be different from default.  If you would like to see all the properties, then refer to the files listed below.

## Configuration files
Syncdata services uses the following configuration files:
```
syncdata.properties
application-default.properties
registration-default.properties
```
The above files are located in [mosip-config](https://github.com/mosip/mosip-config/blob/develop2-v2/) repo

## DB
* `spring.master-datasource.password`
* `spring.master-datasource.username`
* `spring.master-datasource.jdbcUrl`

Point the above to your DB . Default is set to point to in-cluster Postgres installed with sandbox.
  
## Websub config
* `syncdata.websub.topic.ca-cert=specify websub topic`
* `syncdata.websub.callback.secret.ca-cert=secret`
* `syncdata.websub.callback.url.path.ca-cert:specify path of certificate`
* `syncdata.websub.callback.url.ca-cert:specify link of certificate`
* `websub.publish.url:specify websub publish url`
* `websub.hub.url:specify websub hub url`
* `syncdata.websub.resubscription.retry.count:specify retry number`
* `syncdata.websub.resubscription.delay.millis:specify delay time`
* `subscriptions-delay-on-startup:specify startup delay`

Above properties are used for websub configuration

## Syncdata Metadata config
* `mosip.kernel.syncdata.syncdata-request-id:specify syncdata request id`
* `mosip.kernel.syncdata.syncdata-version-id : specify syncdata version id`

## Url used to in application
* `mosip.kernel.keymanager-service-publickey-url : specify public key url`
* `mosip.kernel.masterdata.locationhierarchylevels.uri: specify location hierarchy url`
* `mosip.kernel.keymanager-service-sign-url: specify jwt sign url`
* `mosip.kernel.syncdata-service-idschema-url: specify id schema url`
* `mosip.kernel.syncdata-service-dynamicfield-url :specify dynamic field url`
* `mosip.kernel.keymanager.cert.url: specify key manager url`
* `mosip.kernel.syncdata-service-machine-url: specify machine service url`
* `mosip.kernel.auth.sendotp.url:specify send otp url`

Above properties refer to url's of the services which is called from syncdata service

## Key Manager config
* `mosip.kernel.crypto.asymmetric-algorithm-name:specify crypto asymmetric algorithm name`
* `mosip.kernel.crypto.symmetric-algorithm-name:specify crypto symmetric algorithm name`
* `mosip.kernel.keygenerator.asymmetric-algorithm-name:specify keygenerator asymmetric algorithm name`
* `mosip.kernel.keygenerator.symmetric-algorithm-name:specify Keygenerator symmetric algorithm name`
* `mosip.kernel.keygenerator.asymmetric-key-length:specify Asymmetric algorithm key length`
* `mosip.kernel.keygenerator.symmetric-key-length:specify Symmetric algorithm key length`
* `mosip.kernel.data-key-splitter:specify Encrypted data and encrypted symmetric key separator`
* `mosip.kernel.crypto.gcm-tag-length:specify GCM tag length`
* `mosip.kernel.crypto.hash-algorithm-name:specify Hash algo name`
* `mosip.kernel.crypto.hash-symmetric-key-length:specify Symmtric key length used in hash`
* `mosip.kernel.crypto.hash-iteration:specify number of iterations in hash`
* `mosip.kernel.crypto.sign-algorithm-name:specify Sign algo name`
* `mosip.kernel.certificate.sign.algorithm:specify Certificate Sign algo name`

Above are the properties related to key manager configuration

##Cron job config
* `syncdata.cache.snapshot.cron:schedule job for clearing cache`
* `syncdata.cache.evict.delta-sync.cron:used while sync of client settings`

Above property are related to cron job.

## Roles for the API
All the API's of syncdata service is associated with roles , only authorized person can access concerned API.
Example:
*`mosip.role.admin.syncdata.getclientsettings=REGISTRATION_SUPERVISOR,REGISTRATION_OFFICER`
In the above example only registration officer or registration supervisor can access getclientsettings API, similarly authorization is given for all the API's

