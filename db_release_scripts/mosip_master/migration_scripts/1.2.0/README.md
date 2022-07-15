## migration-dynamicfield.py

This script should be executed after DB upgrade.

1. Provide execute permission to `migration-dynamicfield.py`
2. Run the script with arguments as shown below:

./migration-dynamicfield.py <db-username> <db-password> <db-domain-name> <db-port>

	Ex: ./migration-dynamicfield.py postgres mosip1234 dev.mosip.net 30090

3. On successful execution of the script delete the `dynamic_field_migr_bkp` backup table.


### Revoke script On Failure:

1. Provide execute permission to `revoke-migration-dynamicfield.py`
2. Run the script with arguments as shown below:

./revoke-migration-dynamicfield.py <db-username> <db-password> <db-domain-name> <db-port>

	Ex: ./revoke-migration-dynamicfield.py postgres mosip1234 dev.mosip.net 30090


Note: db-username, should be a user with permission to run DDL and DML statements


## UI Spec migration

In 1.1.5* both Identity schema and UI spec was stored in identity_schema table. From 1.2.0 it is split into 2 different tables, identity_schema and ui_spec.
As part of sql migration script `1.2.0_master-scripts_release.sql` data split is taken care.

1. Pre-Registration UI-spec `pre-registration-demographic.json` is part of mosip-config repo in 1.1.5.* and should be manually published using masterdata UI-spec API.
2. For 1.2.0, Registration-Client UI spec is defined per process, Refer this documentation for more details 
https://docs.mosip.io/1.2.0/modules/registration-client/registration-client-ui-specifications 


### Refer below API documentation to define and publish UI spec

https://mosip.github.io/documentation/1.2.0/kernel-masterdata-service.html#operation/defineUISpec
https://mosip.github.io/documentation/1.2.0/kernel-masterdata-service.html#operation/publishUISpec


