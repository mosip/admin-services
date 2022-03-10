package io.mosip.admin.config;

import io.mosip.admin.bulkdataupload.batch.ApplicantValidDocumentFieldSetMapper;
import io.mosip.admin.bulkdataupload.batch.CustomChunkListener;
import io.mosip.admin.bulkdataupload.batch.JobResultListener;
import io.mosip.admin.bulkdataupload.batch.PacketJobResultListener;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import org.digibooster.spring.batch.listener.JobExecutionListenerContextSupport;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import javax.sql.DataSource;

/**
 * Spring batch configuration
 * 
 * @author dhanendra
 *
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	ApplicationContext applicationContext;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

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
    public ApplicantValidDocumentFieldSetMapper applicantValidDocumentFieldSetMapper() {
    	return new ApplicantValidDocumentFieldSetMapper();
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
    public StepBuilderFactory customStepBuilderFactory() {
        return  new StepBuilderFactory(jobRepository, platformTransactionManager);
    }


    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    


}
