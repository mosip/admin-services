package io.mosip.kernel.syncdata.test.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executor;

@Configuration
public class TestConfig {

	@Value("${syncdata.scheduler.pool.size:2}")
	private int schedulerPoolSize;

	@Value("${syncdata.task.max.pool.size:30}")
	private int taskMaxPoolSize;

	@Value("${syncdata.task.core.pool.size:15}")
	private int taskCorePoolSize;

	@Bean
	public RestTemplate selfTokenRestTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager);

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClientBuilder.build());
		return new RestTemplate(requestFactory);

	}

	@Bean
	public RestTemplate restTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager);

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClientBuilder.build());
		return new RestTemplate(requestFactory);

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

}
