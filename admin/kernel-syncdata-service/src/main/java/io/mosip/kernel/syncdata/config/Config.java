package io.mosip.kernel.syncdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.mosip.kernel.syncdata.httpfilter.CorsFilter;
import io.mosip.kernel.websub.api.filter.MultipleReadRequestBodyFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Spring configuration class for the syncdata module, defining beans for filters, task execution, and Jackson optimization.
 * <p>
 * This class configures essential beans for the MOSIP syncdata module, including CORS and request body filters,
 * task executors for asynchronous processing, a task scheduler, and Jackson's Afterburner module for performance
 * optimization. It is designed to be lightweight and efficient, using cached configuration properties and optimized
 * thread pool settings to ensure high performance in high-throughput environments.
 * </p>
 *
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 */
@Configuration
public class Config {

	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

	/**
	 * Constant for the SecurityContextHolder strategy to support async methods.
	 */
	private static final String SECURITY_CONTEXT_STRATEGY = SecurityContextHolder.MODE_INHERITABLETHREADLOCAL;

	/**
	 * Pool size for the task scheduler, defaulting to 5.
	 */
	@Value("${syncdata.scheduler.pool.size:5}")
	private int schedulerPoolSize;

	/**
	 * Maximum pool size for the task executor, defaulting to 40.
	 */
	@Value("${syncdata.task.max.pool.size:40}")
	private int taskMaxPoolSize;

	/**
	 * Core pool size for the task executor, defaulting to 20.
	 */
	@Value("${syncdata.task.core.pool.size:20}")
	private int taskCorePoolSize;

	/**
	 * Queue capacity for the task executor, defaulting to 100.
	 */
	@Value("${syncdata.task.queue.capacity:100}")
	private int queueCapacity;

	/**
	 * Thread name prefix for the task executor, defaulting to "SYNCDATA-Async-Thread-".
	 */
	@Value("${syncdata.task.thread.name.prefix:SYNCDATA-Async-Thread-}")
	private String taskThreadNamePrefix;

	/**
	 * Thread name prefix for the task scheduler, defaulting to "SYNCDATA-Scheduler-".
	 */
	@Value("${syncdata.scheduler.thread.name.prefix:SYNCDATA-Scheduler-}")
	private String schedulerThreadNamePrefix;

	/**
	 * Configures a {@link FilterRegistrationBean} for the {@link CorsFilter}.
	 * <p>
	 * This bean registers the CORS filter with an order of 2, enabling CORS support for HTTP requests.
	 * The filter is optimized to minimize overhead during registration.
	 * </p>
	 *
	 * @return the configured {@link FilterRegistrationBean} for the CORS filter
	 */
	@Bean
	public FilterRegistrationBean<Filter> registerCORSFilterBean() {
		try {
			FilterRegistrationBean<Filter> corsBean = new FilterRegistrationBean<>();
			corsBean.setFilter(new CorsFilter());
			corsBean.setOrder(2);
			return corsBean;
		} catch (Exception e) {
			LOGGER.error("Failed to register CORS filter: {}", e.getMessage());
			throw new IllegalStateException("CORS filter registration failed", e);
		}
	}

	/**
	 * Creates a {@link CorsFilter} instance for CORS support.
	 * <p>
	 * This bean provides a {@link CorsFilter} instance that can be used by the
	 * {@link #registerCORSFilterBean()} bean or other components requiring CORS functionality.
	 * </p>
	 *
	 * @return the configured {@link CorsFilter}
	 */
	@Bean
	public Filter registerCORSFilter() {
		try {
			return new CorsFilter();
		} catch (Exception e) {
			LOGGER.error("Failed to create CORS filter: {}", e.getMessage());
			throw new IllegalStateException("CORS filter creation failed", e);
		}
	}


	/**
	 * Configures a {@link ThreadPoolTaskExecutor} for asynchronous task execution.
	 * <p>
	 * This bean creates a thread pool executor with configurable core and max pool sizes, using
	 * a caller-runs policy for rejected tasks to ensure graceful handling of overload. The executor
	 * is optimized for high-throughput async processing with a fixed queue capacity to prevent
	 * unbounded growth.
	 * </p>
	 *
	 * @return the configured {@link Executor} for async tasks
	 */
	@Bean
	public Executor taskExecutor() {
		try {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(taskCorePoolSize);
			executor.setMaxPoolSize(taskMaxPoolSize);
			executor.setQueueCapacity(queueCapacity); // Added to prevent unbounded queue growth
			executor.setThreadNamePrefix(taskThreadNamePrefix);
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			executor.initialize();
			return executor;
		} catch (Exception e) {
			LOGGER.error("Failed to configure task executor: {}", e.getMessage());
			throw new IllegalStateException("Task executor configuration failed", e);
		}
	}

	/**
	 * Configures a {@link ThreadPoolTaskScheduler} for scheduled tasks.
	 * <p>
	 * This bean creates a task scheduler with a configurable pool size, optimized for scheduling
	 * tasks in the syncdata module. It uses a caller-runs policy for rejected tasks to handle
	 * overload gracefully.
	 * </p>
	 *
	 * @return the configured {@link TaskScheduler} for scheduled tasks
	 */
	@Bean
	public TaskScheduler taskScheduler() {
		try {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setPoolSize(schedulerPoolSize);
			scheduler.setThreadNamePrefix(schedulerThreadNamePrefix);
			scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			scheduler.initialize();
			return scheduler;
		} catch (Exception e) {
			LOGGER.error("Failed to configure task scheduler: {}", e.getMessage());
			throw new IllegalStateException("Task scheduler configuration failed", e);
		}
	}

	/**
	 * Configures a {@link FilterRegistrationBean} for the {@link MultipleReadRequestBodyFilter}.
	 * <p>
	 * This bean registers the request body filter with an order of 1, enabling multiple reads of
	 * the request body for HTTP requests. The filter is optimized to minimize overhead during
	 * registration.
	 * </p>
	 *
	 * @return the configured {@link FilterRegistrationBean} for the request body filter
	 */
	@Bean
	public FilterRegistrationBean<MultipleReadRequestBodyFilter> registerMultipleReadRequestBodyFilter() {
		try {
			FilterRegistrationBean<MultipleReadRequestBodyFilter> requestBodyReader = new FilterRegistrationBean<>();
			requestBodyReader.setFilter(new MultipleReadRequestBodyFilter());
			requestBodyReader.setOrder(1);
			return requestBodyReader;
		} catch (Exception e) {
			LOGGER.error("Failed to register request body filter: {}", e.getMessage());
			throw new IllegalStateException("Request body filter registration failed", e);
		}
	}

	/**
	 * Configures a {@link MethodInvokingFactoryBean} to set the SecurityContextHolder strategy.
	 * <p>
	 * This bean sets the {@link SecurityContextHolder} strategy to
	 * {@code MODE_INHERITABLETHREADLOCAL} to ensure security context propagation to async methods.
	 * </p>
	 *
	 * @return the configured {@link MethodInvokingFactoryBean}
	 */
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		try {
			MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
			methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
			methodInvokingFactoryBean.setTargetMethod("setStrategyName");
			methodInvokingFactoryBean.setArguments((Object) new String[]{SECURITY_CONTEXT_STRATEGY});
			return methodInvokingFactoryBean;
		} catch (Exception e) {
			LOGGER.error("Failed to configure SecurityContextHolder strategy: {}", e.getMessage());
			throw new IllegalStateException("SecurityContextHolder strategy configuration failed", e);
		}
	}

	/**
	 * Configures Jackson's {@link ObjectMapper} for performance optimization.
	 * <p>
	 * This bean enables the {@link AfterburnerModule} module to enhance Jackson's serialization and
	 * deserialization performance in {@link ObjectMapper}.
	 * </p>
	 *
	 * @return the configured {@link ObjectMapper}
	 */
	@Bean
	@Primary
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper().registerModule(new AfterburnerModule()).registerModule(new JavaTimeModule());
	}
}