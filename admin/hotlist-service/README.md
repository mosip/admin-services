# Hotlist Service

## About

Hotlist service provides functionality to block/unblock any ids with option of expiry. This hotlisted information will also be published to MOSIP_HOTLIST websub topic.

## Build & run (for developers)
The project requires JDK 21.0.3
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```

## Configuration files
Hotlist Service uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
```
application-default.properties
hotlist-default.properties
```

## Configuration
[Configuration-Hotlist](https://github.com/mosip/mosip-config/blob/master/hotlist-default.properties) and
[Configuration-Application](https://github.com/mosip/mosip-config/blob/master/application-default.properties) defined here.

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/hotlist-service.html).

## Default context-path and port
Refer [`bootstrap.properties`](src/main/resources/bootstrap.properties)