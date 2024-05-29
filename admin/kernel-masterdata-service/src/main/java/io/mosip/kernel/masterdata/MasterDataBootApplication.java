package io.mosip.kernel.masterdata;

import io.mosip.kernel.auth.defaultadapter.config.BeanConfig;
import io.mosip.kernel.dataaccess.hibernate.config.HibernateDaoConfig;
import io.mosip.kernel.datamapper.orika.impl.DataMapperImpl;
import io.mosip.kernel.masterdata.dto.AuthorizedRolesDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

/**
 * Main class of Master-Data-Service Application. This will have CRUD operations
 * related to master data
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 *
 */
@SpringBootApplication(scanBasePackages = { "${mosip.auth.adapter.impl.basepackage}",
		"io.mosip.kernel.core.logger.config"})
@ComponentScan(value = {"io.mosip.kernel.masterdata.*","io.mosip.kernel.core.datamapper.*",
		"io.mosip.kernel.core.websub.*","io.mosip.kernel.idgenerator.*","io.mosip.kernel.auth.*"
		,"io.mosip.kernel.websub.api.*","io.mosip.kernel.applicanttype.*","io.mosip.kernel.core.idgenerator.*"}, excludeFilters  = {@ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE, classes = {DataMapperImpl.class})})
@EnableCaching
@EnableScheduling
@Import(value = {HibernateDaoConfig.class})
public class MasterDataBootApplication {

	/**
	 * Function to run the Master-Data-Service application
	 * 
	 * @param args The arguments to pass will executing the main function
	 */
	public static void main(String[] args) {
		SpringApplication.run(MasterDataBootApplication.class, args);
	}

}