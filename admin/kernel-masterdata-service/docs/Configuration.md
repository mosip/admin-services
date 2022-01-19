# Masterdata services Configuration Guide
## Overview
The guide here lists down some of the important properties that may be customised for a given installation. Note that the listing here is not exhaustive, but a checklist to review properties that are likely to be different from default.  If you would like to see all the properties, then refer to the files listed below.

## Configuration files
Masterdata services uses the following configuration files:
```
application-default.properties
kernel-default.properties
```
The above files are located in [mosip-config](https://github.com/mosip/mosip-config/blob/develop2-v2/) repo

## DB
* `masterdata_database_username`
* `masterdata_database_password`
* `masterdata_database_url`

Point the above to your DB .  Default is set to point to in-cluster Postgres installed with sandbox.

## Admin UI config
* `mosip.admin.ui.configs`
Above property is used for UI configuration of admin. Value of the property is a key value pairs. Example:

* `mosip.admin.ui.configs=version:${application.configuration.level.version};HierarchyLevel:${mosip.recommended.centers.locCode}`

## Value limit on fields
* `mosip.kernel.registrationcenterid.length:specify length of registration center id`
* `mosip.kernel.machineid.length:specify length of machine id`
* `mosip.recommended.centers.locCode:specify hierarchy location code`
* `master.search.maximum.rows:specify maximum number of rows to be fetched in the search and filter queries`
* `masterdata.registerdevice.timestamp.validate:specify value with + or - to validate device time stamp`
* `application.configuration.level.version:specify version of application`

Above properties are used for limiting values on fields , can be varied based on country.

## Applicanttype mvel config
*`mosip.kernel.config.server.file.storage.uri:specify location of mvel file to be fetched`
*`mosip.kernel.applicantType.mvel.file:specify name of mvel file`

Above properties are used to locate and fetch applicant type file while fetching applicant code.

## Topics and websub config
*`mosip.kernel.masterdata.template_idauthentication_event:specify topic used in Template service`
*`mosip.kernel.masterdata.title_event:specify topic used in Title service`
*`websub.publish.url:specify web sub url`
*`masterdata.websub.resubscription.delay.millis:specify fixed delay`
*`masterdata.subscriptions-delay-on-startup:specify initial delay`

Above properties are used for the topic configuration

##Cron job config
*`scheduling.job.cron `
Above property is used to define value to start cron job for clearing cache.

## Roles for the API
All the API's of masterdata service is associated with roles , only authorized person can access concerned API.
Example:
*`mosip.role.admin.masterdata.getapplicationconfigs=GLOBAL_ADMIN,ZONAL_ADMIN`
In the above example only global or zonal admin can access getapplicationconfigs API, similarly authorization is given for all the API's
