[![Maven Package upon a push](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml/badge.svg?branch=master)](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_admin-services&id=mosip_admin-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_admin-services)


# Admin

## Overview
This repository contains the source code MOSIP Admin module. For an overview refer [here](https://docs.mosip.io/1.2.0/modules/administration). The module exposes API endpoints. For a reference front-end UI implementation refer to [Admin UI github repo](https://github.com/mosip/admin-ui/)

Admin module contains following services:
1. Admin Service
2. Masterdata Service
3. Sync Data Service
4. Hotlist Service

## Databases
Refer to [SQL scripts](db_scripts).

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

## Configuration
admin module uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
Please refer to the required released tagged version for configuration.
[Configuration-Admin](https://github.com/mosip/mosip-config/blob/master/admin-default.properties) and
[Configuration-Application](https://github.com/mosip/mosip-config/blob/master/application-default.properties) defined here.

## Deploy
To deploy Admin on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Test
Automated functional tests available in [Functional Tests repo](api-test).

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/1.2.0.html).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).
