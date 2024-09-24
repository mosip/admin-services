# Hotlist Service

## About

Hotlist service provides functionality to block/unblock any ids with option of expiry. This hotlisted information will also be published to MOSIP_HOTLIST websub topic.

## Build & run (for developers)
The project requires JDK 1.21.
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```

### Remove the version-specific suffix (PostgreSQL95Dialect) from the Hibernate dialect configuration
   ```
   hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```
This is for better compatibility with future PostgreSQL versions.

### Configure ANT Path Matcher for Spring Boot 3.x compatibility.
   ```
   spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER
   ```
This is to maintain compatibility with existing ANT-style path patterns.

### Add auth-adapter in a class-path to run a hotlist-service
   ```
   <dependency>
       <groupId>io.mosip.kernel</groupId>
       <artifactId>kernel-auth-adapter</artifactId>
       <version>${kernel.auth.adapter.version}</version>
   </dependency>
   ```
## Configuration files
Hotlist Service uses the following configuration files:
```
application-default.properties
hotlist-default.properties
```

## Configuration
[Configuration-Hotlist](https://github.com/mosip/mosip-config/blob/develop/hotlist-default.properties) and
[Configuration-Application](https://github.com/mosip/mosip-config/blob/develop/application-default.properties) defined here.

## Default context-path and port
Refer [`bootstrap.properties`](src/main/resources/bootstrap.properties)