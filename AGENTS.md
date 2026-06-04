# AGENTS.md

This file provides guidance to AI agents when working with code in this repository.

## What This Repo Is

MOSIP Admin Services is the backend for the MOSIP identity platform's administrative layer. It manages the operational data (zones, registration centers, devices, machines, documents) that registration clients sync to operate offline, and controls hotlisted IDs. The front-end counterpart lives in the [admin-ui](https://github.com/mosip/admin-ui/) repo.

The four services and their roles:
- **admin-service** (port 8098) — Bulk upload, packet status tracking, RID recovery, resume paused registrations.
- **kernel-masterdata-service** (port 8086) — CRUD for all reference/master data. The largest service.
- **kernel-syncdata-service** (port 8089) — Serves delta-sync payloads to registration clients. Reads from masterdata DB.
- **hotlist-service** — Block/unblock IDs (documents, VIDs, etc.) in real time.

## Prerequisites

- JDK 21.0.3
- Maven 3.9.6
- PostgreSQL 10.2
- Keycloak (for IAM/token validation)
- `kernel-auth-adapter.jar` on the classpath (see README for download)
- A running Spring Cloud Config Server pointing to [mosip-config](https://github.com/mosip/mosip-config)

## Build & Run

```bash
# Build all modules (from repo root or admin/ directory)
cd admin
mvn clean install -Dmaven.javadoc.skip=true -Dgpg.skip=true

# Build a single module
cd admin/kernel-masterdata-service
mvn clean install -Dmaven.javadoc.skip=true -Dgpg.skip=true

# Run a service (after build)
java -jar admin/kernel-masterdata-service/target/kernel-masterdata-service-*.jar

# Swagger UI (masterdata example)
http://localhost:8086/v1/masterdata/swagger-ui/index.html
```

## Tests

```bash
# Run unit tests for all modules
cd admin
mvn test

# Run unit tests for a single module
cd admin/kernel-masterdata-service
mvn test

# Run a single test class
mvn test -Dtest=ApplicantTypeControllerTest

# Run a single test method
mvn test -Dtest=ApplicantTypeControllerTest#getApplicantTypeTest

# Build the API integration test suite (separate module, TestNG + REST Assured)
cd api-test
mvn clean package -DskipTests
```

Unit tests use H2 in-memory DB via `application-test.properties`. No external services needed for unit tests.

API integration tests (`api-test/`) require a fully running MOSIP environment and are run as test-rig containers in CI.

## Architecture

### Request/Response Contract

All APIs wrap inputs and outputs in standard envelopes:
```java
RequestWrapper<T>   // inbound: { id, version, requesttime, request: T }
ResponseWrapper<T>  // outbound: { id, version, responsetime, response: T, errors: [...] }
```
Every controller method takes `@RequestBody RequestWrapper<SomeDto>` and returns `ResponseWrapper<SomeResponseDto>`. Never bypass these wrappers.

### Layering

```
Controller → Service (interface) → ServiceImpl → Repository (extends BaseRepository<E, ID>)
```

- `BaseRepository` is a MOSIP custom interface extending Spring Data JPA. Use it — don't extend `JpaRepository` directly.
- Service interfaces live in `service/`, implementations in `service/impl/`.
- DTOs are in `dto/`, entities in `entity/`, custom exceptions in `exception/`.

### Authentication

All services are stateless and validate tokens via `kernel-auth-adapter` (OpenID/OAuth2 against Keycloak). The adapter is loaded as a runtime JAR (`${loader_path_env}/kernel-auth-adapter.jar`) in containers — it is `provided` scope in Maven. Role-based access is enforced at the controller level.

### Configuration

All properties are externalized to a Spring Cloud Config Server. Local development uses `application-local.properties` or `application-local1.properties`. Secrets (DB passwords, Keycloak client secrets) must come via environment variables, never in property files:

| Property | Purpose |
|---|---|
| `mosip.kernel.database.hostname` | PostgreSQL host |
| `db.dbuser.password` | DB password (env var) |
| `keycloak.internal.url` / `keycloak.external.url` | IAM endpoints |
| `mosip.admin.client.secret` | Keycloak client secret (env var) |

### Database

- Production: PostgreSQL. Schema/data scripts are in `db_scripts/mosip_master/` and `db_scripts/mosip_hotlist/`.
- Tests: H2 in-memory, bootstrapped from `schema.sql` in test resources.
- ORM: Hibernate via `kernel-dataaccess-hibernate`. Entities use `@Table`, `@Entity`, composite PKs with `@IdClass`.
- Upgrade scripts between versions: `db_upgrade_scripts/`.

### Sync Data Flow

`kernel-syncdata-service` does NOT have its own write APIs — it reads from the same `mosip_master` PostgreSQL database that masterdata writes to. Registration clients call syncdata to pull down changes since their last sync timestamp. Any schema change to masterdata entities can affect the sync payload structure.

### Hotlist Integration

`hotlist-service` stores hotlisted IDs in `mosip_hotlist` DB and publishes events via WebSub so other services (registration processor, auth) can subscribe and react in real time.

## Key Dependencies

| Library | Version | Why |
|---|---|---|
| Spring Boot | 3.2.3 | Core framework |
| springdoc-openapi | 2.5.0 | OpenAPI 3 / Swagger UI |
| PostgreSQL driver | 42.2.2 | Production DB |
| H2 | 1.4.197 | Test DB only |
| PowerMock + Mockito | 2.0.7 / 2.23.4 | Unit test mocking (incl. static methods) |

## CI/CD

Workflow: `.github/workflows/push-trigger.yml` (uses reusable workflows from `mosip/kattu`).

Triggers on push to: `release-1*`, `master`, `develop*`, `MOSIP*` branches, and on PRs.

Jobs in order: build → publish to Nexus (non-master) → Docker build → Sonar analysis → api-test build → api-test Docker.

## Sonar Coverage

Files excluded from coverage (do not add tests for these): repositories, constants, config classes, DTOs, entities, exceptions, `*Util.java`, boot application entry points, enums. Coverage-required files are service implementations and controllers.
