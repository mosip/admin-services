# Admin module

## Deployment in K8 cluster with other MOSIP services:
### Pre-requisites
* Set KUBECONFIG variable to point to existing K8 cluster kubeconfig file:
    ```
    export KUBECONFIG=~/.kube/<k8s-cluster.config>
    ```
### Install Admin module
 ```
    $ ./install.sh
   ```
### Delete
  ```
    $ ./delete.sh
   ```
### Restart
  ```
    $ ./restart.sh
   ```
### Install Keycloak client
  ```
    cd deploy/keycloak
    $ ./keycloak_init.sh
   ```

### Install Apitestrig
```
    cd deploy/apitest-masterdata
    $ ./install.sh
```
Note:
* Script prompts for below mentioned inputs please provide as and when needed:
  * Enter the time (hr) to run the cronjob every day (0–23): Specify the hour you want the cronjob to run (e.g., 6 for 6 AM)
  * Do you have a public domain and valid SSL certificate? (Y/n):
  * Y – If you have a public domain and valid SSL certificate
  * n – If you do not have one (recommended only for development environments)
  * Retention days to remove old reports (Default: 3): Press Enter to accept the default or specify another value (e.g., 5).
  * Provide Slack Webhook URL to notify server issues on your Slack channel: (change the URL to your channel one)
     ```
      https://hooks.slack.com/services/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 
       ```
  * Is the eSignet service deployed? (yes/no):
    * no – If eSignet is not deployed, related test cases will be skipped.
      * Is values.yaml for the apitestrig chart set correctly as part of the prerequisites? (Y/n):
        * Enter Y if this step is already completed.
  * Do you have S3 details for storing API-Testrig reports? (Y/n):
  * Enter Y to proceed with S3 configuration.
  * S3 Host: eg. `http://minio.minio:9000`
  * S3 Region:(Leave blank or enter your specific region, if applicable)
    S3 Access Key:admin

## Admin proxy
Admin service accesses other services like Materdata and Keymanager and currently there is only one URL that is used to connect to both these services. This will get fixed in future versions, but as a an interim solution, Admin Proxy docker has been created, which is basically an Nginx proxy connecting to the above services with these URLs: 
```
http://admin-proxy/v1/masterdata
http://admin-proxy/v1/keymanager
```
The proxy is installed as part of `install.sh` script.

## Admin user
1. In Keycloak, create a user in `mosip` realm called `globaladmin` and assign role `GLOBAL_ADMIN`.  Make sure this user has strong credentials. 
2. Use this user to login into Admin portal via Keycloak. (Note that this user is already on-boarded as default user while uploading masterdata XLS in Kernel module)
3. _Strongly Recommended_: Create another user in keycloak with authentic name, email, details, strong password and `GLOBAL_ADMIN` role.  Assign global zone to this user via Admin portal, and then delete `globaladmin` from Keycloak and masterdata DB.  

## Admin portal
Access the portal with following URL:
```
https://<your-internal-api-host>/admin-ui/

Example:
https://api-internal.sandbox.xyz.net/admin-ui/
```
Your wireguard client must be running for this access.

## Onboarding
Use the portal to onboard user, machine, center.

Note that for onboarding a user (like a Zonal Admin or Registration Officer),
1. Create user in Keycloak with appropriate role. 
1. Map the user to a Zone using Admin portal.
1. Map user to a registration center (in case of Registration Officer/Supervisor) using Admin portal.

