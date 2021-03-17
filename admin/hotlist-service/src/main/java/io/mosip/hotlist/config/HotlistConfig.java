package io.mosip.hotlist.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

/**
 * The Class HotlistConfig.
 *
 * @author Manoj SP
 */
@Configuration
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class HotlistConfig {

	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistConfig.class);

	/** The topic. */
	@Value("${mosip.hotlist.topic-to-publish}")
	private String topic;

	/** The web sub hub url. */
	@Value("${websub.publish.url}")
	private String webSubHubUrl;

	/** The dialect. */
	@Value("${mosip.hotlist.datasource.dialect}")
	private String dialect;

	/** The publisher. */
	@Autowired
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	/** The interceptor. */
	@Autowired
	private Interceptor interceptor;

	/**
	 * Register topic.
	 */
	@PostConstruct
	public void registerTopic() {
		try {
			publisher.registerTopic(topic, webSubHubUrl);
		} catch (Exception e) {
			mosipLogger.warn(HotlistSecurityManager.getUser(), "HotlistConfig", "registerTopic",
					"IGNORING THIS ERROR AS TOPIC IS ALREADY REGISTERED - " + e.getMessage());
		}
	}

	/**
	 * Entity manager factory.
	 *
	 * @param dataSource the data source
	 * @return the local container entity manager factory bean
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("io.mosip.hotlist.*");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaPropertyMap(additionalProperties());

		return em;
	}

	/**
	 * Additional properties.
	 *
	 * @return the properties
	 */
	private Map<String, Object> additionalProperties() {
		Map<String, Object> jpaProperties = new HashMap<>();
		jpaProperties.put("hibernate.dialect", dialect);
		jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", Boolean.FALSE);
		jpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		jpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
		jpaProperties.put("hibernate.ejb.interceptor", interceptor);
		return jpaProperties;
	}
}
