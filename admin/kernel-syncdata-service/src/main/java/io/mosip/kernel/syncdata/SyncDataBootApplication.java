package io.mosip.kernel.syncdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class of Sync handler Application.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan( value = {"io.mosip.kernel.syncdata.*", "${mosip.auth.adapter.impl.basepackage}",
		"io.mosip.kernel.core", "io.mosip.kernel.crypto", "io.mosip.kernel.clientcrypto.service.impl",
		"io.mosip.kernel.keymanagerservice.service", "io.mosip.kernel.keymanagerservice.util",
		"io.mosip.kernel.keymanagerservice.helper", "io.mosip.kernel.keymanager",
		"io.mosip.kernel.cryptomanager.util", "io.mosip.kernel.partnercertservice.helper",
		"io.mosip.kernel.partnercertservice.service", "io.mosip.kernel.websub.api.client",
		"io.mosip.kernel.keygenerator.bouncycastle", "io.mosip.kernel.websub.api.config"  },
	excludeFilters  = {@ComponentScan.Filter(
		type = FilterType.ASPECTJ, pattern = {"io.mosip.kernel.signature.*"})})
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class, H2ConsoleAutoConfiguration.class })
public class SyncDataBootApplication {
	/**
	 * Function to run the Master-Data-Service application
	 * 
	 * @param args The arguments to pass will executing the main function
	 */
	public static void main(String[] args) {
		SpringApplication.run(SyncDataBootApplication.class, args);
	}
}
