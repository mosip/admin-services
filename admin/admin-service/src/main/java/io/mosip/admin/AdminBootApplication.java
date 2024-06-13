package io.mosip.admin;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
@ComponentScan(value = {"io.mosip.kernel.auth.*","io.mosip.kernel.dataaccess.*","io.mosip.admin.*","io.mosip.commons.*",
		"${mosip.auth.adapter.impl.basepackage}", "io.mosip.kernel.idvalidator.rid.*","io.mosip.kernel.biometrics.*","io.mosip.kernel.authcodeflowproxy.*"})
public class AdminBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminBootApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(40);
		executor.setThreadNamePrefix("Admin-Async-Thread-");
		executor.initialize();
		return executor;
	}

}
