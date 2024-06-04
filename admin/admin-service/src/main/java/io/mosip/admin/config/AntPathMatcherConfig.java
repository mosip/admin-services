package io.mosip.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

/*
 * @author Dhanendra Sahu
 *
 */

@Configuration
public class AntPathMatcherConfig {
	@Bean
	public AntPathMatcher antPathMatcher() {
		return new AntPathMatcher();
	}
}