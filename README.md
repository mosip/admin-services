[![Maven Package upon a push](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml/badge.svg?branch=release-1.3.x)](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_admin-services&id=mosip_admin-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_admin-services)

# Admin

## Overview
This repository contains the source code MOSIP Admin module. For an overview refer [here](https://docs.mosip.io/1.2.0/modules/administration). The module exposes API endpoints. For a reference front-end UI implementation refer to [Admin UI github repo](https://github.com/mosip/admin-ui/)

Admin module contains following services:
1. [Admin Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/admin-service)
2. [Masterdata Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-masterdata-service)
3. [Sync Data Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-syncdata-service)
4. [Hotlist Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/hotlist-service)

## Databases
Refer to [SQL scripts](db_scripts).

## Build & Run (for developers)

### Prerequisites
- JDK 21.0.3
- Maven 3.9.6
- Docker (latest stable version)
- Git

### Dependencies
- Database and configuration files must be set up as per the documentation.
---

### 1. Build Backend Services

#### a. Clone the repository
```
git clone <repo-url>
cd admin-services
```
**Verify**: The repository is cloned and you are in the `admin-services` directory.

#### b. Build all services
```
cd admin
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
```
**Verify**: Build completes without errors and JAR files are generated in the `target` directories.

---

### 2. Run Backend Services

#### a. Run using Maven
```
cd <service-folder>
mvn spring-boot:run
```
Replace `<service-folder>` with the desired service directory (e.g., `admin-service`).

**Verify**: Service starts, logs show it is running, and the health endpoint (e.g., `http://localhost:8080/actuator/health`) returns status.

#### b. Run using Docker
```
cd <service-folder>
docker build -t <service-name>:latest -f Dockerfile .
docker run -p <host-port>:<container-port> <service-name>:latest
```
Replace `<service-folder>`, `<service-name>`, `<host-port>`, and `<container-port>` as needed.

**Verify**: Service endpoint is accessible and responds to requests.

---

### 4. Verification

- After each step, check logs for errors.
- Use browser or API tools to access service endpoints.

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
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/admin-service.html).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).