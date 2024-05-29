package io.mosip.admin.config;

import io.mosip.admin.bulkdataupload.batch.CustomChunkListener;
import io.mosip.admin.bulkdataupload.batch.JobResultListener;
import io.mosip.admin.bulkdataupload.batch.PacketJobResultListener;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

/**
 * Spring batch configuration
 * 
 * @author dhanendra
 *
 */
@Configuration
public class SpringBatchConfig {

    @Autowired
	ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private BulkUploadTranscationRepository bulkUploadTranscationRepository;

    @Autowired
    private AuditUtil auditUtil;
    
    @Bean
    public CustomChunkListener customChunkListener() {
        return new CustomChunkListener(bulkUploadTranscationRepository);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JobResultListener jobResultListener() {
        return new JobResultListener(bulkUploadTranscationRepository, auditUtil, new JobExecutionSecurityContextListener());
    }
    
    @Bean
    public PacketJobResultListener packetjobResultListener() {
        return new PacketJobResultListener(bulkUploadTranscationRepository, auditUtil, new JobExecutionSecurityContextListener());
    }

    @Bean(name = "customStepBuilderFactory")
    public StepBuilder customStepBuilderFactory(JobRepository jobRepository) {
        return  new StepBuilder("step", jobRepository);
    }

    /*@Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }*/
   /* @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("jobBuilder", jobRepository)

                .incrementer(new RunIdIncrementer())
                .listener(jobResultListener())
                .listener(packetjobResultListener())
                .flow(step)
                .end()
                .build();
    }*/
    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    


}
