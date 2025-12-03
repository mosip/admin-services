# MOSIP Admin Services

[![Maven Package upon a push](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml/badge.svg?branch=release-1.3.x)](https://github.com/mosip/admin-services/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_admin-services&id=mosip_admin-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_admin-services)

## Overview

The **Admin module** provides a secure and configurable system for:

- Managing master data including zones, registration centers, devices, and machines
- Resource creation
- Bulk upload of packets
- Packet status tracking
- RID recovery
- Resuming paused registrations

It exposes a set of APIs that enable administrators to manage operational data efficiently. A reference front-end implementation is available in the **[Admin UI repository](https://github.com/mosip/admin-ui/)**.

For a complete functional overview and capabilities, refer to the **[official documentation](https://docs.mosip.io/1.2.0/modules/administration)**.

## Services

The Admin module contains the following services:

1. **[Admin Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/admin-service)** - Core administrative functionality
2. **[Masterdata Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-masterdata-service)** - Master data management
3. **[Sync Data Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/kernel-syncdata-service)** - Data synchronization
4. **[Hotlist Service](https://github.com/mosip/admin-services/tree/release-1.3.x/admin/hotlist-service)** - Hotlist management

## Database

Database SQL scripts are available in the [db_scripts](db_scripts) directory.

## Local Setup

The project can be set up in two ways:

1. [Local Setup (for Development or Contribution)](#local-setup-for-development-or-contribution)
2. [Local Setup with Docker (Easy Setup for Demos)](#local-setup-with-docker-easy-setup-for-demos)

### Prerequisites

Before you begin, ensure you have the following installed:

- **JDK**: 21.0.3
- **Maven**: 3.9.6
- **Docker**: Latest stable version

### Runtime Dependencies

- Add `kernel-auth-adapter.jar` to the classpath, or include it as a Maven dependency — [Download](https://central.sonatype.com/artifact/io.mosip.kernel/kernel-auth-adapter)

## Installation

### Local Setup (for Development or Contribution)

1. Make sure the config server is running.

2. Clone the repository:
```text
git clone <repo-url>
cd admin-services
```

3. Build the project:
```text
mvn clean install -Dmaven.javadoc.skip=true -Dgpg.skip=true
```

4. Start the application:
    - Click the Run button in your IDE, or
    - Run via command: `mvn spring-boot:run`

### Local Setup with Docker (Easy Setup for Demos)

#### Option 1: Pull from Docker Hub

Pull the pre-built images from Docker Hub:
```text
docker pull mosipid/admin-service
docker pull mosipid/kernel-masterdata-service
docker pull mosipid/kernel-syncdata-service
docker pull mosipid/admin-hotlist-service
```

#### Option 2: Build Docker Images Locally

1. Clone and build the project:
```text
git clone <repo-url>
cd admin-services
mvn clean install -Dmaven.javadoc.skip=true -Dgpg.skip=true
```

2. Navigate to each service directory and build the Docker image:
```text
cd admin/<service-directory>
docker build -t <service-name> .
```

#### Running the Services

Start each service using Docker:
```text
docker run -d -p <port>:<port> --name <service-name> <service-name>
```

#### Verify Installation

Check that all containers are running:
```text
docker ps
```

Access the services at `http://localhost:<port>` using the port mappings listed above.

## Deployment

### Kubernetes

To deploy Admin services on a Kubernetes cluster, refer to the [Sandbox Deployment Guide](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Usage

### Admin UI

For the complete Admin UI implementation and usage instructions, refer to the [Admin UI GitHub repository](https://github.com/mosip/admin-ui/).

## Documentation

### API Documentation

API endpoints, base URL, and mock server details are available via Stoplight and Swagger documentation: [MOSIP Admin Service API Documentation](https://mosip.github.io/documentation/1.2.0/admin-service.html).

### Product Documentation

To learn more about admin services from a functional perspective and use case scenarios, refer to our main documentation: [Click here]()

## Testing

Automated functional tests are available in the [Functional Tests repository](api-test).

## Contribution & Community

• To learn how you can contribute code to this application, [click here](https://docs.mosip.io/1.2.0/community/code-contributions).

• If you have questions or encounter issues, visit the [MOSIP Community](https://community.mosip.io/) for support.

• For any GitHub issues: [Report here](https://github.com/mosip/admin-services/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).