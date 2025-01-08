# Sync Data Service 
Sync Data Service can be accessed only by the privileged group of admin personnel and enables default configurations and seed data to be setup when the MOSIP platform gets initialized.

## Build & run (for developers)
The project requires JDK 21.0.3
and mvn version - 3.9.6
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```

## Configuration
Sync Data Service uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
Please refer to the required released tagged version for configuration.
1. [Configuration-Application](https://github.com/mosip/mosip-config/blob/master/application-default.properties)
2. [Configuration-Kernel](https://github.com/mosip/mosip-config/blob/master/kernel-default.properties)
3. [Configuration-Sync](https://github.com/mosip/mosip-config/blob/master/syncdata-default.properties)

Need to run the config-server along with the files mentioned above in order to run the sync-data service.

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/kernel-syncdata-service.html).

Refer [Module Configuration](https://docs.mosip.io/1.2.0/modules/module-configuration) for location of these files.

