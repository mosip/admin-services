# Admin Service
Admin Service can be accessed only by the privileged group of admin personnel, its used to perform bulk data operation.

## Build & run (for developers)
The project requires JDK 21.0.3
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```

## Configuration
Admin Service uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
Please refer to the required released tagged version for configuration.
1. [Configuration-Admin](https://github.com/mosip/mosip-config/blob/master/admin-default.properties)
2. [Configuration-Application](https://github.com/mosip/mosip-config/blob/master/application-default.properties)

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/admin-service.html).

Refer [Module Configuration](https://docs.mosip.io/1.2.0/modules/module-configuration) for location of these files.

