[![Build Status](https://travis-ci.org/mosip/admin-services.svg)](https://travis-ci.org/mosip/admin-services)  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_admin-services&id=mosip_admin-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_admin-services)

# Admin
Admin module can be accessed only by the privileged group of admin personnel and enables default configurations and seed data to be setup when the MOSIP platform gets initialized.

## Configuration
admin module uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
Please refer to the required released tagged version for configuration.
```
application-default.properties
admin-default.properties
kernel-default.properties
syncdata-default.properties
hotlist-default.properties

```
Refer [Module Configuration](https://docs.mosip.io/1.2.0/modules/module-configuration) for location of these files.

## Databases
Refer to [SQL scripts](https://github.com/mosip/admin-services/tree/release-1.3.x/db_scripts).

## Build & run (for developers)
The project requires JDK 21.0.3
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```
2. Build Docker for a service:
    ```
    $ cd <service folder>
    $ docker build -f Dockerfile
    ```

## Deploy
To deploy Commons services on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).
## Test
Automated functional tests available in [Functional Tests repo](https://github.com/mosip/admin-services/tree/release-1.3.x/api-test).

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](https://github.com/mosip/admin-services/blob/release-1.3.x/LICENSE).
