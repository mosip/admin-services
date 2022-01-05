package io.mosip.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages= {"io.mosip.admin.*", "io.mosip.commons.*"})
public class TestBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate selfTokenRestTemplate() { return new RestTemplate(); }

}
