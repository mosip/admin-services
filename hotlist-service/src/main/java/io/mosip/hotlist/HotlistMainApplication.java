package io.mosip.hotlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "io.mosip.hotlist.*", "${mosip.auth.adapter.impl.basepackage}" })
public class HotlistMainApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HotlistMainApplication.class, args);
	}

}
