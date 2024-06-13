package io.mosip.kernel.syncdata.config;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.mosip.kernel.syncdata.httpfilter.CorsFilter;
import io.mosip.kernel.websub.api.filter.MultipleReadRequestBodyFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;

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

	/**
	 * This is required to get securityContext in Async methods
	 * @return
	 */
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
		methodInvokingFactoryBean.setTargetMethod("setStrategyName");
		methodInvokingFactoryBean.setArguments(new String[]{SecurityContextHolder.MODE_INHERITABLETHREADLOCAL});
		return methodInvokingFactoryBean;
	}

	@Bean
	public AfterburnerModule afterburnerModule() {
		return new AfterburnerModule();
	}

}
