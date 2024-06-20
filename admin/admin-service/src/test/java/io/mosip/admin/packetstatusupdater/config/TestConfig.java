package io.mosip.admin.packetstatusupdater.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import  org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@TestConfiguration
@TestPropertySource("classpath:application-test.properties")
public class TestConfig {


	@Bean
	public RestTemplate restTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.disableCookieManagement();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClientBuilder.build());
		return new RestTemplate(requestFactory);
	}

	@Bean
	public RestTemplate selfTokenRestTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.disableCookieManagement();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClientBuilder.build());
		return new RestTemplate(requestFactory);
	}


}
