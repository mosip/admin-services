package io.mosip.kernel.syncdata;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Main class of Sync handler Application.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = { "io.mosip.kernel.syncdata.*", "${mosip.auth.adapter.impl.basepackage}",
		"io.mosip.kernel.core", "io.mosip.kernel.crypto",
		"io.mosip.kernel.signature.service","io.mosip.kernel.clientcrypto.service.impl",
		"io.mosip.kernel.keymanagerservice.service", "io.mosip.kernel.keymanagerservice.util",
		"io.mosip.kernel.keymanagerservice.helper", "io.mosip.kernel.keymanager",
		"io.mosip.kernel.cryptomanager.util", "io.mosip.kernel.partnercertservice.helper",
		"io.mosip.kernel.partnercertservice.service", "io.mosip.kernel.websub.api.client"})
@EnableAsync
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
