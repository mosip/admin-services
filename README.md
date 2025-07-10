# MOSIP Admin Services

[![Maven Package upon a push](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml/badge.svg?branch=release-1.3.x)](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_admin-services&id=mosip_admin-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_admin-services)

## Overview

This repository contains the source code for the MOSIP Admin module. For a comprehensive overview, refer to the [official documentation](https://docs.mosip.io/1.2.0/modules/administration). The module exposes API endpoints, and for a reference front-end UI implementation, see the [Admin UI GitHub repository](https://github.com/mosip/admin-ui/).

## Services

The Admin module contains the following services:

1. **[Admin Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/admin-service)** - Core administrative functionality
2. **[Masterdata Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-masterdata-service)** - Master data management
3. **[Sync Data Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-syncdata-service)** - Data synchronization
4. **[Hotlist Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/hotlist-service)** - Hotlist management

## Database

Database SQL scripts are available in the [db_scripts](db_scripts) directory.

## Build & Run (for developers)

### Prerequisites

- **JDK**: 21.0.3
- **Maven**: 3.9.6
- **Docker**: Latest stable version

### Runtime Dependencies

- `kernel-auth-adapter.jar`

### 1. Build

```
git clone <repo-url>
cd admin-services
mvn install -Dmaven.javadoc.skip=true -Dgpg.skip=true
```

### 2. Run Services

#### Option A: Run using Maven

**Prerequisites**
The Config Server must be running before starting any service. Start the config server first:

```
cd config-server
mvn spring-boot:run
```

**Configuration**
The admin module uses configuration files accessible in the [MOSIP Config repository](https://github.com/mosip/mosip-config/tree/master). Please refer to the required released tagged version for configuration:

- [Admin Configuration](https://github.com/mosip/mosip-config/blob/master/admin-default.properties)
- [Application Configuration](https://github.com/mosip/mosip-config/blob/master/application-default.properties)

**Run Service**
```
cd <service-folder>
mvn spring-boot:run
```

**Verify**: Service starts, logs show it is running, and the health endpoint (`http://localhost:8080/actuator/health`) returns status.

#### Option B: Run using Docker

```bash
cd <service-folder>
docker build -t <service-name>:latest -f Dockerfile .
docker run -p <host-port>:<container-port> <service-name>:latest
```

Replace `<service-folder>`, `<service-name>`, `<host-port>`, and `<container-port>` as needed.

**Verify**: Service endpoint is accessible and responds to requests.

## Running the Release Version with Docker

To run the latest release version of the Docker container:

#### 1. Pull the Latest Image

```
docker pull mosipid/admin-service:latest
```

#### 2. Run the Container

**Basic Run:**
```
docker run -d -p 8080:8080 \
  -e SPRING_CLOUD_CONFIG_URI=http://localhost:8888 \
  -e SPRING_PROFILES_ACTIVE=default \
  --name admin-service \
  mosipid/admin-service:latest
```

#### 3. Using Environment File

Create a `.env` file with your configuration:

```
SPRING_CLOUD_CONFIG_URI=http://localhost:8888
SPRING_PROFILES_ACTIVE=default
DATABASE_URL=jdbc:postgresql://localhost:5432/mosip_admin
```

Then run the container:

```
docker run -d -p 8080:8080 \
  --env-file .env \
  --name admin-service \
  mosipid/admin-service:latest
```

#### 4. Verify the Container

Check container status:
```
docker ps
```

Test the health endpoint:
```
curl http://localhost:8080/actuator/health
```

## Deployment

To deploy Admin on a Kubernetes cluster using Docker, refer to the [Sandbox Deployment Guide](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Testing

Automated functional tests are available in the [Functional Tests repository](api-test).

## API Documentation

Comprehensive API documentation is available at [MOSIP Admin Service API Documentation](https://mosip.github.io/documentation/1.2.0/admin-service.html).

## License

This project is licensed under the terms of the [Mozilla Public License 2.0](LICENSE).
