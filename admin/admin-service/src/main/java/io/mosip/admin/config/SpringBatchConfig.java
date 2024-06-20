package io.mosip.admin.config;

import io.mosip.admin.bulkdataupload.batch.CustomChunkListener;
import io.mosip.admin.bulkdataupload.batch.JobResultListener;
import io.mosip.admin.bulkdataupload.batch.PacketJobResultListener;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

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
    private BulkUploadTranscationRepository bulkUploadTranscationRepository;

    @Autowired
    private AuditUtil auditUtil;
    
    @Bean
    public CustomChunkListener customChunkListener() {
        return new CustomChunkListener(bulkUploadTranscationRepository);
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

    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }



}
