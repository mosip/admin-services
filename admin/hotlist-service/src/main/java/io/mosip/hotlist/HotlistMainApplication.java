package io.mosip.hotlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import io.mosip.kernel.core.retry.RetryAspect;
import io.mosip.kernel.core.retry.RetryConfig;
import io.mosip.kernel.core.retry.RetryListenerImpl;
import io.mosip.kernel.core.util.RetryUtil;

/**
 * The Class HotlistMainApplication.
 *
 * @author Manoj SP
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "io.mosip.hotlist.*", "${mosip.auth.adapter.impl.basepackage}" })
@Import({ RetryConfig.class, RetryUtil.class, RetryListenerImpl.class, RetryAspect.class })
public class HotlistMainApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(HotlistMainApplication.class, args);
	}

}
