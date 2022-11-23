package io.mosip.admin.config;

import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import io.mosip.admin.bulkdataupload.batch.PacketJobResultListener;
import io.mosip.admin.bulkdataupload.entity.MachineType;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;

import java.util.List;
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
    private BulkUploadTranscationRepository bulkUploadTranscationRepository;

    @Autowired
    private AuditUtil auditUtil;
	

    @Primary
    @Bean(name = "customStepBuilderFactory")
    public StepBuilderFactory customStepBuilderFactory() {
        return  new StepBuilderFactory(jobRepository, platformTransactionManager);
    }
    
    @Bean
    public PacketJobResultListener packetjobResultListener() {
        return new PacketJobResultListener(bulkUploadTranscationRepository, auditUtil, new JobExecutionSecurityContextListener());
    }
    

}
