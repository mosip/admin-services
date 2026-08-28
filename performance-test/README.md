
### Contains
* This folder contains performance Test script of below API endpoint categories.
    01. Auth Token Generation (Setup)
    02. S01 Auth Token Details Encrypted Based On Machine Key (Preparation)
    03. S01 Auth Token Details Encrypted Based On Machine Key (Execution)
	04. S02 Public Key Verify (Execution)
	05. S03 Certificate (Execution)
	06. S04 User Details (Execution)
	07. S05 Client Settings (Execution)
	08. S06 Configs (Execution)
	09. S07 LatestId Schema (Execution)
	10. S08 CaCertificates (Execution)

* Open source Tools used,
    1. [Apache JMeter](https://jmeter.apache.org/)

### How to run performance scripts using Apache JMeter tool
* Download Apache JMeter from https://jmeter.apache.org/download_jmeter.cgi
* Download scripts for the required module.
* Start JMeter by running the jmeter.bat file for Windows or jmeter file for Unix. 
* Validate the scripts for one user.
* Execute a dry run for 10 min.
* Execute performance run with various loads in order to achieve targeted NFR's.

### Setup points for Execution

* We need some jar files which needs to be added in lib folder of jmeter, PFA dependency links for your reference : 

   * bcprov-jdk15on-1.66.jar
      * <!-- https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on -->
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.66</version>
</dependency>

   * jjwt-api-0.11.2.jar
      * <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.2</version>
</dependency>

   * jjwt-impl-0.11.2.jar
       * <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>

   * jjwt-jackson-0.11.2.jar
       * <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>

   * nimbus-jose-jwt-9.25.6.jar  
       * <!-- https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt -->
<dependency>
    <groupId>com.nimbusds</groupId>
    <artifactId>nimbus-jose-jwt</artifactId>
    <version>9.25.6</version>
</dependency>

	* jackson-databind-2.17.1.jar
		* <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.17.1</version>
</dependency>

	* jackson-module-afterburner-2.17.1.jar
		* <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-afterburner -->
<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-afterburner</artifactId>
    <version>2.17.1</version>
</dependency>

	* kernel-core-1.2.0.1.jar
		* <!-- https://mvnrepository.com/artifact/io.mosip.kernel/kernel-core -->
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-core</artifactId>
    <version>1.2.0.1</version>
</dependency>

	* kernel-keymanager-service-1.2.0.1-lib.jar
		* <!-- https://mvnrepository.com/artifact/io.mosip.kernel/kernel-keymanager-service -->
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-keymanager-service</artifactId>
    <version>1.2.0.1-B4</version>
</dependency>

	* kernel-logger-logback-1.2.0.1.jar
		* <!-- https://mvnrepository.com/artifact/io.mosip.kernel/kernel-logger-logback -->
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-logger-logback</artifactId>
    <version>1.2.0.1-B1</version>
</dependency>

	* kernel-syncdata-service-1.2.0.1.jar
		* <!-- https://central.sonatype.com/artifact/io.mosip.kernel/kernel-syncdata-service -->
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-syncdata-service</artifactId>
    <version>1.2.1.0</version>
</dependency>

	* spring-context-6.1.9.jar
		* <!-- https://mvnrepository.com/artifact/org.springframework/spring-context -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.1.9</version>
</dependency>

	* TSS.Java-0.3.0.jar
		* <!-- https://mvnrepository.com/artifact/com.github.microsoft/TSS.Java -->
<dependency>
    <groupId>com.github.microsoft</groupId>
    <artifactId>TSS.Java</artifactId>
    <version>0.3.0</version>
</dependency>


### Execution points for eSignet Authentication API's

*Syncdata_Test_Script.jmx
	
	* Auth Token Generation (Setup) : This thread conatins Auth manager authentication API which will generate auth token value for Registration client. 
	* S01 Auth Token Details Encrypted Based On Machine Key (Preparation) : This thread generates request tokens for user ID password usecase.
	* S01 Auth Token Details Encrypted Based On Machine Key (Execution) : This thread executes Get auth token details encrypted based on machine key usecase.
	* S02 Public Verify Key (Execution) : This thread executes Public Key Verify usecase.
	* S03 Certificate (Execution) : This thread executes Get Certificate usecase.
	* S04 User Details (Execution) : This thread executes Get User Details usecase.
	* S05 Client Settings (Execution) : This thread executes Get Client Settings usecase.
	* S06 Configs (Execution) : This thread executes Get Configs usecase.
	* S07 LatestId Schema (Execution) : This thread executes Get LatestID Schema usecase.
	* S08 CaCertificates (Execution) : This thread executes Get CAcertificates usecase.
 
	
### Downloading Plugin manager jar file for the purpose installing other JMeter specific plugins

* Download JMeter plugin manager from below url links.
	*https://jmeter-plugins.org/get/

* After downloading the jar file place it in below folder path.
	*lib/ext

* Please refer to following link to download JMeter jars.
	https://mosip.atlassian.net/wiki/spaces/PT/pages/1227751491/Steps+to+set+up+the+local+system#PluginManager
		
### Designing the workload model for performance test execution
* Calculation of number of users depending on Transactions per second (TPS) provided by client

* Applying little's law
	* Users = TPS * (SLA of transaction + think time + pacing)
	* TPS --> Transaction per second.
	
* For the realistic approach we can keep (Think time + Pacing) = 1 second for API testing
	* Calculating number of users for 10 TPS
		* Users= 10 X (SLA of transaction + 1)
		       = 10 X (1 + 1)
			   = 20
			   
### Usage of Constant Throughput timer to control Hits/sec from JMeter
* In order to control hits/ minute in JMeter, it is better to use Timer called Constant Throughput Timer.

* If we are performing load test with 10TPS as hits / sec in one thread group. Then we need to provide value hits / minute as in Constant Throughput Timer
	* Value = 10 X 60
			= 600

* Dropdown option in Constant Throughput Timer
	* Calculate Throughput based on as = All active threads in current thread group
		* If we are performing load test with 10TPS as hits / sec in one thread group. Then we need to provide value hits / minute as in Constant Throughput Timer
	 			Value = 10 X 60
					  = 600
		  
	* Calculate Throughput based on as = this thread
		* If we are performing scalability testing we need to calculate throughput for 10 TPS as 
          Value = (10 * 60 )/(Number of users)
