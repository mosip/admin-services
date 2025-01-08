package io.mosip.kernel.syncdata.test;


import io.mosip.kernel.syncdata.test.config.TestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class of Sync handler Application.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {"io.mosip.kernel.syncdata.*", "${mosip.auth.adapter.impl.basepackage}",
		"io.mosip.kernel.core", "io.mosip.kernel.crypto",
		"io.mosip.kernel.signature.service","io.mosip.kernel.clientcrypto.service.impl",
		"io.mosip.kernel.keymanagerservice.service", "io.mosip.kernel.keymanagerservice.util",
		"io.mosip.kernel.keymanagerservice.helper", "io.mosip.kernel.keymanager",
		"io.mosip.kernel.cryptomanager.util", "io.mosip.kernel.partnercertservice.helper",
		"io.mosip.kernel.partnercertservice.service", "io.mosip.kernel.websub.api.client",
		"io.mosip.kernel.keygenerator.bouncycastle"})
@EnableAsync
@EnableCaching
@Import(TestSecurityConfig.class)
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
