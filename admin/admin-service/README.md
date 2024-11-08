# Admin Service
Admin Service can be accessed only by the privileged group of admin personnel, its used to perform bulk data operation.

## Build & run (for developers)
The project requires JDK 21.0
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```

## Configuration files
Admin Service uses the following configuration files:
```
application-default.properties
admin-default.properties
```

## Configuration
[Configuration-Admin](https://github.com/mosip/mosip-config/blob/release-1.3.x/admin-default.properties) and
[Configuration-Application](https://github.com/mosip/mosip-config/blob/release-1.3.x/application-default.properties) defined here.

Refer [Module Configuration](https://docs.mosip.io/1.2.0/modules/module-configuration) for location of these files.

