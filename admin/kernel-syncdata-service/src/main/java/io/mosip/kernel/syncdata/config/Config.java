package io.mosip.kernel.syncdata.config;

import javax.servlet.Filter;

import io.mosip.kernel.websub.api.filter.MultipleReadRequestBodyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import io.mosip.kernel.syncdata.httpfilter.CorsFilter;
import io.mosip.kernel.syncdata.httpfilter.ReqResFilter;

import java.util.concurrent.Executor;

/**
 * Config class with beans for modelmapper and request logging
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 *
 */
@Configuration
public class Config {

	@Value("${syncdata.scheduler.pool.size:5}")
	private int schedulerPoolSize;

	@Value("${syncdata.task.max.pool.size:40}")
	private int taskMaxPoolSize;

	@Value("${syncdata.task.core.pool.size:20}")
	private int taskCorePoolSize;

	@Bean
	public FilterRegistrationBean<Filter> registerCORSFilterBean() {
		FilterRegistrationBean<Filter> corsBean = new FilterRegistrationBean<>();
		corsBean.setFilter(registerCORSFilter());
		corsBean.setOrder(2);
		return corsBean;
	}

	@Bean
	public Filter registerCORSFilter() {
		return new CorsFilter();
	}
	
	@Primary
	@Bean(name = "authRestTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


	/**
	 * Creating bean of TaskExecutor to run Async tasks
	 *
	 * @return {@link Executor}
	 */
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(taskCorePoolSize);
		executor.setMaxPoolSize(taskMaxPoolSize);
		executor.setThreadNamePrefix("SYNCDATA-Async-Thread-");
		executor.initialize();
		return executor;
	}


	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		executor.setThreadNamePrefix("SYNCDATA-Scheduler-");
		executor.setPoolSize(schedulerPoolSize);
		executor.initialize();
		return executor;
	}


	@Bean
	public FilterRegistrationBean<MultipleReadRequestBodyFilter> registerMultipleReadRequestBodyFilter() {
		FilterRegistrationBean<MultipleReadRequestBodyFilter> requestBodyReader = new FilterRegistrationBean();
		requestBodyReader.setFilter(new MultipleReadRequestBodyFilter());
		requestBodyReader.setOrder(1);
		return requestBodyReader;
	}
}
