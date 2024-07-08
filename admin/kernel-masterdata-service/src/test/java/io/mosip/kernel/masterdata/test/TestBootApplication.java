package io.mosip.kernel.masterdata.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import io.mosip.kernel.dataaccess.hibernate.config.HibernateDaoConfig;
import io.mosip.kernel.masterdata.test.config.TestConfig;
import io.mosip.kernel.masterdata.test.config.TestSecurityConfig;

/**
 * Main class of Sync handler Application.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */

@SpringBootApplication
@ComponentScan(basePackages = {"io.mosip.kernel.masterdata.*","io.mosip.kernel.core.datamapper.*",
		"io.mosip.kernel.core.websub.*","io.mosip.kernel.idgenerator.*"
		,"io.mosip.kernel.websub.api.*","io.mosip.kernel.applicanttype.*","io.mosip.kernel.core.idgenerator.*"}
//,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {HibernateDaoConfig.class})
)
@Import(value = {TestConfig.class, TestSecurityConfig.class, HibernateDaoConfig.class})
//@Profile("test")
//@Import(TestSecurityConfig.class)
//@EnableJpaRepositories(basePackages = {"io.mosip.kernel.masterdata.repository"})
//@EntityScan(basePackages = {"io.mosip.kernel.masterdata.entity"})
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