package io.mosip.admin.bulkdataupload.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.ValidationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetResponseDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataRequestDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.config.Mapper;
import io.mosip.admin.config.RepositoryListItemWriter;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.kernel.core.util.EmptyCheckUtils;
/**
 * BulkDataUpload service 
 * 
 * @author dhanendra
 *
 */
@Service
public class BulkDataUploadServiceImpl implements BulkDataService{
	
	@Autowired
	ApplicationContext applicationContext;
	
    @Autowired
    JobLauncher jobLauncher;
    
    @Autowired
    JobRepository jobRepository;
    
    @Autowired
    Mapper mapper;

    @Value("${mosip.kernel.packet-reciever-api-url}")
	private String packetRecieverApiUrl;
    
    @Autowired
	private RestTemplate restTemplate;
    
    @Autowired
    BulkUploadTranscationRepository bulkTranscationRepo;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    private  Map<String,Class> entityMap=new HashMap<String, Class>();
    
	@Override
	public BulkDataGetExtnDto getTrascationDetails(UUID transcationId) {
		
		BulkDataGetExtnDto bulkDataGetExtnDto=new BulkDataGetExtnDto();
		try {
			BulkUploadTranscation bulkUploadTranscation=bulkTranscationRepo.getOne(transcationId);
			bulkDataGetExtnDto.setTranscationId(bulkUploadTranscation.getId());
			bulkDataGetExtnDto.setCount(bulkUploadTranscation.getRecordCount());
			bulkDataGetExtnDto.setOperation(bulkUploadTranscation.getUploadOperation());
			bulkDataGetExtnDto.setStatus(bulkUploadTranscation.getStatusCode());
			bulkDataGetExtnDto.setStatusDescription(bulkUploadTranscation.getUploadDescription());
			bulkDataGetExtnDto.setTableName(bulkUploadTranscation.getEntityName());
			bulkDataGetExtnDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
			bulkDataGetExtnDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
		} catch (Exception e) {
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(), 
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(),e);
		}
		
		return  bulkDataGetExtnDto;
	}

	
	@Override
	public BulkDataGetResponseDto getAllTrascationDetails() {
		BulkDataGetResponseDto bulkDataGetResponseDto=new BulkDataGetResponseDto();
		BulkDataGetExtnDto bulkDataGetExtnDto=new BulkDataGetExtnDto();
		List<BulkDataGetExtnDto> bulkDataGetExtnDtos=new ArrayList<BulkDataGetExtnDto>();
		List<BulkUploadTranscation> bulkUploadTranscations=new ArrayList<BulkUploadTranscation>();
		try{
			
			bulkUploadTranscations=bulkTranscationRepo.findAll();
			for(BulkUploadTranscation bulkUploadTranscation:bulkUploadTranscations){
				
				bulkDataGetExtnDto.setTranscationId(bulkUploadTranscation.getId());
				bulkDataGetExtnDto.setCount(bulkUploadTranscation.getRecordCount());
				bulkDataGetExtnDto.setOperation(bulkUploadTranscation.getUploadOperation());
				bulkDataGetExtnDto.setStatus(bulkUploadTranscation.getStatusCode());
				bulkDataGetExtnDto.setStatusDescription(bulkUploadTranscation.getUploadDescription());
				bulkDataGetExtnDto.setTableName(bulkUploadTranscation.getEntityName());
				bulkDataGetExtnDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
				bulkDataGetExtnDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
				bulkDataGetExtnDtos.add(bulkDataGetExtnDto);	
			}
		}catch (Exception e) {
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(), 
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(),e);
		}
		bulkDataGetResponseDto.setRecords(bulkDataGetExtnDtos);
		return bulkDataGetResponseDto;
	}


    	@Override
    	public  BulkDataResponseDto insertDataToCSVFile(String tableName, String operation, String category,MultipartFile[] files)  {
    		
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		mapper.init();
    		Class<?> entity=mapper.getEntity(tableName);
    		String repoBeanName=mapper.getRepo(entity);
        	JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
        	StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, platformTransactionManager);
            List<String> failureMessage = null;
            int[] numArr = {0};
            String[] status= {"PROCESS"};
            Arrays.asList(files).stream().forEach(file -> {
            	ItemReader<Object> itemReader;  
                JobExecution jobExecution = null;
                
    			try {
    				csvValidator(file.getOriginalFilename());
    				itemReader = itemReader(file,entity);
    				ItemWriter<List<Object>> itemWriter= itemWriter(repoBeanName);
    		        ItemProcessor itemProcessor=processor(operation);
    		        JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
    		        jobExecution = jobLauncher.run(job(jobBuilderFactory, stepBuilderFactory, itemReader,itemProcessor, itemWriter),parameters);
    				JobInstance jobInstence=new JobInstance(jobExecution.getJobId(), "ETL-file-load");
    				StepExecution stepExecution=jobRepository.getLastStepExecution(jobInstence, "ETL-file-load");
    				//failureMessage.add(jobExecution.getAllFailureExceptions().toString());
    				status[0]=jobExecution.getStatus().toString();
    				numArr[0]+=stepExecution.getReadCount();
    			}catch (IOException e) {
    				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			}
    			catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
    					| JobParametersInvalidException e) {
    				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			}
            });
            
            BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(numArr[0],operation,entity.getName(),category,failureMessage,status[0]);
            bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, tableName);
    		return bulkDataResponseDto;
    	}

    	@Override
    	public BulkDataResponseDto bulkDataOperation(String tableName,String operation,String category,MultipartFile[] files) {
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		if(category.equalsIgnoreCase("masterdata")) {
    			bulkDataResponseDto=insertDataToCSVFile(tableName, operation, category, files);
    		}
    		else if(category.equalsIgnoreCase("packet")) {
    			bulkDataResponseDto=uploadPackets(files,category);
    		}
    		else {
    			throw new IllegalArgumentException("Enter correct category");
    		}
            return bulkDataResponseDto;
    	
    	}

    /*	@Override
    	public BulkDataResponseDto deleteData(BulkDataRequestDto bulkDataRequestDto){
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		String tableName=bulkDataRequestDto.getTableName();
    		String operation=bulkDataRequestDto.getOperation();
    		mapper.init();
    		Class<?> entity=mapper.getEntity(tableName);
    		String repoBeanName=mapper.getRepo(entity);
        	JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
        	StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, platformTransactionManager);
            JobExecution jobExecution = null;
            ItemReader<Object> itemReader;*/
          	/*	try {
          			csvValidator(bulkDataRequestDto.getCsvFile());
          			itemReader = itemReader(bulkDataRequestDto.getCsvFile(),entity);
          			ItemWriter<List<Object>> itemWriter= itemWriter(repoBeanName);
          	        ItemProcessor itemProcessor=processor(operation);
          	        JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
          	        jobExecution = jobLauncher.run(job(jobBuilderFactory, stepBuilderFactory, itemReader,itemProcessor, itemWriter),parameters);
          		} catch (IOException e) {
          			throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
          					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
          		}
          		catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
          				| JobParametersInvalidException e) {
          			throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
          					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
          		}    
          		BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(jobExecution,operation,entity.getName());
                bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, tableName);*/
        	//	return bulkDataResponseDto;
    	//}*/
    	
    	@Override
    	public BulkDataResponseDto uploadPackets(MultipartFile[] files, String category) {
    		
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		List<String> fileNames = new ArrayList<>();
    		int[] numArr = {0};
    		String[] msgArr= {"FAILED"};
    	      Arrays.asList(files).stream().forEach(file -> {
    	    	 	
    	    	 HttpHeaders headers = new HttpHeaders();
    	         headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    	         MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
    	         ContentDisposition contentDisposition = ContentDisposition
    	                 .builder("form-data")
    	                 .name("file")
    	                 .filename(file.getOriginalFilename())
    	                 .build();
    	         fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
    	        	         try {
    	        	        	 HttpEntity<byte[]> fileEntity = new HttpEntity<>(file.getBytes(), fileMap);

    	        		         MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    	        		         body.add("file", fileEntity);

    	        		         HttpEntity<MultiValueMap<String, Object>> requestEntity =
    	        		                 new HttpEntity<>(body, headers);

    	             ResponseEntity<String> response = restTemplate.exchange(
    	                     packetRecieverApiUrl,
    	                     HttpMethod.POST,
    	                     requestEntity,
    	                     String.class);
    	             JSONObject josnObject=new JSONObject(response.getBody());
    	             if(!josnObject.get("response").equals(null)) {
    	            	 numArr[0]++;
    	            	 msgArr[0]="Success";
    	             }
    	         } catch (HttpClientErrorException e) {
    	        	 throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    		  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    	         } catch (IOException e) {
    	        	 throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    		  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			} catch (JSONException e) {
    				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			}
    	         fileNames.add(file.getOriginalFilename());
    	      });
    	      BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(numArr[0],null,null,category,null,msgArr[0]);
    	      bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, null);
    		return bulkDataResponseDto;
    	}
        Arrays.asList(files).stream().forEach(file -> {
        	ItemReader<Object> itemReader;  
            JobExecution jobExecution = null;
			try {
				//csvValidator(bulkDataRequestDto.getCsvFile());
				int readCount=0;
				itemReader = itemReader(file,entity);
				ItemWriter<List<Object>> itemWriter= itemWriter(repoBeanName);
		        ItemProcessor itemProcessor=processor(operation);
		        JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		        jobExecution = jobLauncher.run(job(jobBuilderFactory, stepBuilderFactory, itemReader,itemProcessor, itemWriter),parameters);
				JobInstance jobInstence=new JobInstance(jobExecution.getJobId(), "ETL-file-load");
				StepExecution stepExecution=jobRepository.getLastStepExecution(jobInstence, "ETL-file-load");
				readCount=stepExecution.getReadCount();
				System.out.println(">>>>>>>>>>>"+file.getOriginalFilename());
				//failureMessage.add(stepExecution.getExitStatus().getExitDescription());
				numArr[0]+=stepExecution.getReadCount();
			}catch (IOException e) {
				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
			}
			catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
			}
        });
        
        BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(numArr[0],operation,entity.getName(),category,failureMessage);
        bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, tableName);
		return bulkDataResponseDto;
	}

	@Override
	public BulkDataResponseDto bulkDataOperation(String tableName,String operation,String category,MultipartFile[] files) {
		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
		if(category.equalsIgnoreCase("masterdata")) {
			bulkDataResponseDto=insertDataToCSVFile(tableName, operation, category, files);
		}
		else if(category.equalsIgnoreCase("packet")) {
			bulkDataResponseDto=uploadPackets(files);
		}
		else {
			throw new IllegalArgumentException("Enter correct category");
		}
        return bulkDataResponseDto;
	
	}

	@Override
	public BulkDataResponseDto uploadPackets(MultipartFile[] files) {
		
		BulkDataResponseDto packetResponseDto=new BulkDataResponseDto();
		List<String> fileNames = new ArrayList<>();
		
	      Arrays.asList(files).stream().forEach(file -> {
	    	 System.out.println(">>>file>>>"+file.getOriginalFilename());
	    	   /* HttpHeaders headers = new HttpHeaders();
	    	    // set `content-type` header
	    	    headers.setContentType(MediaType.APPLICATION_JSON);
	    	    // set `accept` header
	    	   // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    	    // create a map for post parameters
	    	    Map<String, Object> map = new HashMap<>();
	    	    map.put("file", file);
	    	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);*/
	    	 MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
	    	 
	    	 try {
				formData.add("file", new ByteArrayResource(file.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // this is spring multipart file
	    	 HttpHeaders headers = new HttpHeaders();
	    	 headers.set("Content-Type", "multipart/form-data"); 
	    	 headers.set("Accept", "text/plain"); 
	    	 HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, headers);	    	
	    	    // String result  = restTemplate.postForObject(uploadUri, requestEntity, String.class);
	    	 ResponseEntity<String> response = this.restTemplate.exchange(packetRecieverApiUrl,HttpMethod.POST,requestEntity, String.class);
	    	 System.out.println(">>>>>>>res.>>"+response+">>>>>>>"+response.getStatusCode());
	         fileNames.add(file.getOriginalFilename());
	      });
	      System.out.println(">>>>>>>>>file names>>"+fileNames);
		return packetResponseDto;
	}

/*	@Override
	public BulkDataResponseDto deleteData(BulkDataRequestDto bulkDataRequestDto){
		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
		String tableName=bulkDataRequestDto.getTableName();
		String operation=bulkDataRequestDto.getOperation();
		mapper.init();
		Class<?> entity=mapper.getEntity(tableName);
		String repoBeanName=mapper.getRepo(entity);
    	JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
    	StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, platformTransactionManager);
        JobExecution jobExecution = null;
        ItemReader<Object> itemReader;
      		try {
      			csvValidator(bulkDataRequestDto.getCsvFile());
      			itemReader = itemReader(bulkDataRequestDto.getCsvFile(),entity);
      			ItemWriter<List<Object>> itemWriter= itemWriter(repoBeanName);
      	        ItemProcessor itemProcessor=processor(operation);
      	        JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
      	        jobExecution = jobLauncher.run(job(jobBuilderFactory, stepBuilderFactory, itemReader,itemProcessor, itemWriter),parameters);
      		} catch (IOException e) {
      			throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
      					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
      		}
      		catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
      				| JobParametersInvalidException e) {
      			throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
      					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
      		}    
      		BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(jobExecution,operation,entity.getName());
            bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, tableName);
    		return bulkDataResponseDto;
	}*/
	public Job job(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            ItemReader<Object> itemReader,
            ItemProcessor itemProcessor, // ItemProcessor<User, User> itemProcessor,
            //ItemWriter<AbstractPersistable> itemWriter
            ItemWriter<List<Object>> itemWriter
		) {
		
		 Step step = stepBuilderFactory.get("ETL-file-load")
		         .<Object, List<Object>>chunk(100)
		         .reader(itemReader)
		         .processor(itemProcessor)
		         .writer(itemWriter)
		         .build();
		
		
		 return jobBuilderFactory.get("ETL-Load")
		         .incrementer(new RunIdIncrementer())
		         .start(step)
		         .build();
		}
	 @StepScope
	 private FlatFileItemReader<Object> itemReader(MultipartFile file, Class<?> clazz) throws IOException {
		 
		    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setDelimiter(",");
	        lineTokenizer.setStrict(false);
	       // MultipartFile multi = null;
	        FlatFileItemReader<Object> flatFileItemReader = new FlatFileItemReader<>();
	        flatFileItemReader.setResource(new InputStreamResource(file.getInputStream()));
	        flatFileItemReader.setName("CSV-Reader");
	        flatFileItemReader.setLinesToSkip(1);
	        flatFileItemReader.setSkippedLinesCallback(new LineCallbackHandler() {
	            @Override
	            public void handleLine(String s) {
	                lineTokenizer.setNames(s.split(","));
	            }
	        });
	        BeanWrapperFieldSetMapper<Object> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	        fieldSetMapper.setTargetType(clazz);
	        DefaultLineMapper<Object> defaultLineMapper = new DefaultLineMapper<>();
	        defaultLineMapper.setLineTokenizer(lineTokenizer);
	        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	        flatFileItemReader.setLineMapper(defaultLineMapper);
	        return flatFileItemReader;

	 }
	/*	 @StepScope
	private ItemWriter write() throws Exception{

		//dBWriter.getRepo(clzz);
		//applicationContext.getBean("");
		
		    ItemWriter itemWriter=new ItemWriter() {
			String repoName=null;
			GenericRepo obj=null;
			boolean flag=false;
			@Override
			public void write(List items) throws Exception {
				// TODO Auto-generated method stub@Scope(value = "step", proxyMode = ScopedProxyMode.INTERFACES)
				System.out.println(items);
				for(Object item:items) {
				    repoName=mapper.getRepo(item.getClass());
					obj=(GenericRepo)applicationContext.getBean(repoName);
					
					flag=obj.exists((Example<Object>) item);
					System.out.println(">>>>>>>>>>flag  :"+flag);
					if(!flag) {
					obj.save(item);
					}
				}
				 System.out.println(">>>>>>>ob"+obj);
				// obj.saveAll(items);
				// System.out.println("tes123t"+repoName);
			}
		};
		return itemWriter;	
	}
	*/    
	 public ItemProcessor processor(String operation) {
		
		 
		 ItemProcessor itemprocessor=new ItemProcessor() {
			 
			
			LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));  
			@Override
			public Object process(Object item) throws Exception {
				setCreateMetaData();
				if(operation.equalsIgnoreCase("insert")) {
				((BaseEntity) item).setCreatedBy(setCreateMetaData());
				((BaseEntity) item).setCreatedDateTime(now);
				((BaseEntity)item).setIsActive(true);
				}else if(operation.equalsIgnoreCase("update")) {
					((BaseEntity) item).setCreatedBy(setCreateMetaData());
					((BaseEntity) item).setCreatedDateTime(now);
					((BaseEntity) item).setUpdatedBy(setCreateMetaData());
					((BaseEntity) item).setUpdatedDateTime(now);
				}else if(operation.equalsIgnoreCase("delete")) {
					((BaseEntity) item).setCreatedBy(setCreateMetaData());
					((BaseEntity) item).setCreatedDateTime(now);
					((BaseEntity)item).setIsActive(false);
					((BaseEntity) item).setIsDeleted(true);
					((BaseEntity) item).setDeletedDateTime(now);
				}	
				return item;
			}
		};
		return itemprocessor;
	 }
	    @SuppressWarnings("unchecked")
		public ItemWriter<List<Object>> itemWriter(String repoBeanName){
	        RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>();
	        //System.out.println(">>>>>>>>>>get c"+genericRepo.getRepository(Test.class));
	        writer.setRepository((CrudRepository<?, ?>) applicationContext.getBean(repoBeanName));
	        writer.setMethodName("save");
	        try {
	            writer.afterPropertiesSet();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return writer;
	    }
	    
	    private BulkUploadTranscation saveTranscationDetails(int count,String operation,String entityName,String category,List<String> failureMessage, String status) {
	    	BulkUploadTranscation bulkUploadTranscation=new BulkUploadTranscation();
	    	//JobInstance jobInstence=new JobInstance(jobExecution.getJobId(), "ETL-file-load");
			//StepExecution stepExecution=jobRepository.getLastStepExecution(jobInstence, "ETL-file-load");
	    	LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
	    	bulkUploadTranscation.setIsActive(true);
	    	bulkUploadTranscation.setLangCode("eng");
	    	bulkUploadTranscation.setStatusCode(status);
	    	bulkUploadTranscation.setCreatedBy(setCreateMetaData());
	    	bulkUploadTranscation.setCreatedDateTime(now);
	    	bulkUploadTranscation.setEntityName(entityName);
	    	bulkUploadTranscation.setUploadedBy(setCreateMetaData());
	    	bulkUploadTranscation.setUpdatedDateTime(now);
	    	bulkUploadTranscation.setCategory(category);
	    	//bulkUploadTranscation.setUploadDescription(failureMessage.toString());
	    	bulkUploadTranscation.setUploadOperation(operation);
	    	bulkUploadTranscation.setRecordCount(count);
	    	bulkTranscationRepo.save(bulkUploadTranscation);
			return bulkUploadTranscation;
	    	
	    }
	    private BulkDataResponseDto setResponseDetails(BulkUploadTranscation bulkUploadTranscation,String tableName) {
	    	
	    	BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
	    	bulkDataResponseDto.setTranscationId(bulkUploadTranscation.getId());
	    	bulkDataResponseDto.setOperation(bulkUploadTranscation.getUploadOperation());
	    	bulkDataResponseDto.setStatus(bulkUploadTranscation.getStatusCode());
	    	bulkDataResponseDto.setSuccessCount(bulkUploadTranscation.getRecordCount());
	    	bulkDataResponseDto.setCategory(bulkUploadTranscation.getCategory());
	    	bulkDataResponseDto.setTableName(tableName);
	    	bulkDataResponseDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
	    	bulkDataResponseDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
	    	return bulkDataResponseDto;
	    }    
	    public static String setCreateMetaData() {
	    	String contextUser="superadmin";
			Authentication authN = SecurityContextHolder.getContext().getAuthentication();
			if (!EmptyCheckUtils.isNullEmpty(authN)) {
				contextUser = authN.getName();
			}
			return contextUser;
		}
	    private void csvValidator(String csvFile) throws IOException {
	    	String ext=Files.getFileExtension(csvFile);
	    	if(!ext.equalsIgnoreCase("csv")) {
	    		throw new ValidationException("Supported format are only csv file");
	    	}
	    	int count=0;
	    	String line;
	    	BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {
			    	  String l = line.trim(); // Remove end of line. You can print line here.'
			    	  
			    	  String[] columns = l.split(",");
			    	  count=columns.length;
			    	  break;
				}

				while ((line = br.readLine()) != null) {
					 String l = line.trim(); // Remove end of line. You can print line here.'  
			    	 String[] columns = l.split(",");
					 if (count!=columns.length) {
						 throw new ValidationException("all the rows have same number of element in csv file");
					 }
				}
			    br.close();
			    
			} catch (IOException e) {
				throw new ValidationException("invalid csv file",e);
				
			}
	    	
	    }
}
