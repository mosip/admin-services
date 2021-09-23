package io.mosip.kernel.masterdata.test;

import io.mosip.kernel.masterdata.test.config.TestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Main class of Sync handler Application.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */

@SpringBootApplication(scanBasePackages = "io.mosip.kernel.masterdata.*")
@Import(TestConfig.class)
//@Profile("test")
//@Import(TestSecurityConfig.class)
public class TestBootApplication {

	/**
	 * Function to run the Master-Data-Service application
	 * 
	 * @param args The arguments to pass will executing the main function
	 */
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}

}