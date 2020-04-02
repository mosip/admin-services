### Introduction

If you want to start admin in MOSIP, first you need to start the kernel core components in file [StartKernelCoreComponents_instructions.md](https://github.com/mosip/commons/blob/master/StartKernelCoreComponents_instructions.md)
  
### Prerequisite:

Before you start any of the steps, you should be aware of the following technical stuff, 

1. Kernel architecture

2. Admin architecture

3. How OAuth2 and JWT works?

4. Springboot services

5. Postman tool or any such similar tools to test the web service

6. Basic knowledge about PostgreSQL server 

7. Basic knowledge about pgadmin4. This is a client tool to connect to PostgreSQL server

8. Java 8 should have been installed in your development machine

9. Maven build tool should be installed in your development machine

10. Clone git@github.com:mosip/admin-services.git
  

#### a. Admin Service

1.Go to admin-env file in config templates  and modify some properties

```
// this one property will be modify after we will work in registration processor
mosip.kernel.packet-status-update-url=http://<IP>:<PORT>/registrationprocessor/v1/registrationtransaction/search

mosip.kernel.zone-validation-url=http://localhost:8086/v1/masterdata/zones/authorize
mosip.kernel.registrationcenterid.length=5 

mosip.kernel.audit.manager.api=http://localhost:8081/v1/auditmanager/audits
mosip.kernel.masterdata.audit-url=http://localhost:8081/v1/auditmanager/audits

```

2. Restart config server

3. Modify bootstrap.properties

```
spring.cloud.config.uri=http://localhost:51000/config
spring.cloud.config.label=master
spring.cloud.config.name=admin
spring.application.name=admin-service
spring.profiles.active=env
```

4. Build and start admin service

```
java -jar {adminjarname}.jar
```

5. Verify admin service(NOTE: you will need a token from your auth service to hit this url)

```
1. Open http://localhost:8098/v1/admin/swagger-ui.html

2. Go to /auditmanager/log

3. Try it out

4. Try this Request

"id": "string",
  "metadata": {},
  "request": {
    "actionTimeStamp": "2020-04-01T06:12:52.994Z",
    "applicationId": "IDA",
    "applicationName": "IDA",
    "createdBy": "admin",
    "description": "admin",
    "eventId": "admin",
    "eventName": "admin",
    "eventType": "admin",
    "hostIp": "admin",
    "hostName": "admin",
    "id": "admin",
    "idType": "admin",
    "moduleId": "admin",
    "moduleName": "admin",
    "sessionUserId": "admin",
    "sessionUserName": "admin"
  },
  "requesttime": "2020-04-01T06:12:52.994Z",
  "version": "string"
}

5. Please check the response for this

{
  "id": "string",
  "version": "string",
  "responsetime": "2020-04-01T11:17:26.877Z",
  "metadata": null,
  "response": {
    "status": "Success",
    "message": "Audit logged successfuly"
  },
  "errors": null
}
```