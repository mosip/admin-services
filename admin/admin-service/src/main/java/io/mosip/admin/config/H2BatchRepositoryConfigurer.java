package io.mosip.admin.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class H2BatchRepositoryConfigurer extends DefaultBatchConfigurer{

	
	    @Autowired
	    private PlatformTransactionManager platformTransactionManager;

	    @Override
	    protected JobRepository createJobRepository() throws Exception {
	        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
	        factoryBean.setDatabaseType(DatabaseType.H2.getProductName());
	        factoryBean.setTablePrefix("BATCH_");
	        factoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
	        factoryBean.setDataSource(dataSourceH2());
	        factoryBean.setTransactionManager(platformTransactionManager);
	        factoryBean.afterPropertiesSet();
	        return factoryBean.getObject();
	    }

	    @Override
	    protected JobExplorer createJobExplorer() throws Exception {
	        JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
	        factoryBean.setDataSource(dataSourceH2());
	        factoryBean.setTablePrefix("BATCH_");
	        factoryBean.afterPropertiesSet();
	        return factoryBean.getObject();
	    }

	    @Bean(destroyMethod = "shutdown")
	    public EmbeddedDatabase dataSourceH2() {
	    	HikariDataSource dataSource = new HikariDataSource();
			dataSource.setDriverClassName("org.h2.Driver");
			dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
			dataSource.setUsername("sa");
			dataSource.setPassword("");
	        return new EmbeddedDatabaseBuilder()
	                .setType(EmbeddedDatabaseType.H2)
	                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
	                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
	                .build();
	    }
}
