package io.mosip.admin.bulkdataupload.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.validation.ValidationException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import io.mosip.admin.bulkdataupload.batch.PacketJobResultListener;
import io.mosip.admin.bulkdataupload.batch.PacketUploadTasklet;
import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.entity.DeviceHistory;
import io.mosip.admin.bulkdataupload.entity.MachineHistory;
import io.mosip.admin.bulkdataupload.entity.UserDetailsHistory;
import io.mosip.admin.bulkdataupload.entity.ZoneUserHistory;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.bulkdataupload.service.PacketUploadService;
import io.mosip.admin.config.Mapper;
import io.mosip.admin.config.MapperUtils;
import io.mosip.admin.config.RepositoryListItemWriter;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.core.util.EmptyCheckUtils;
/**
 * BulkDataUpload service 
 * 
 * @author dhanendra
 *
 */
@Service
public class BulkDataUploadServiceImpl implements BulkDataService{
	
	private static final String DATA_READ_ROLE = "ROLE_DATA_READ";
	private static String PKT_UPLOAD_MESSAGE = "FILE: %s, STATUS: %s, MESSAGE: %s";
	
	@Autowired
	ApplicationContext applicationContext;
	
    @Autowired
    JobLauncher jobLauncher;
    
    @Autowired
    JobRepository jobRepository;
    
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	@Qualifier("customStepBuilderFactory")
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private PacketJobResultListener packetJobResultListener;
    
    @Autowired
	private AuditUtil auditUtil;
    
	@Autowired
	private PacketUploadService packetUploadService;
    
    @Autowired
    Mapper mapper;

	@Autowired
	EntityManager em;

	@Autowired
	EntityManagerFactory emf;

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
	public BulkDataGetExtnDto getTrascationDetails(String transcationId) {
		
		BulkDataGetExtnDto bulkDataGetExtnDto=new BulkDataGetExtnDto();
		try {
			BulkUploadTranscation bulkUploadTranscation=bulkTranscationRepo.findTransactionById(transcationId);
			bulkDataGetExtnDto.setTranscationId(bulkUploadTranscation.getId());
			bulkDataGetExtnDto.setCount(bulkUploadTranscation.getRecordCount());
			bulkDataGetExtnDto.setOperation(bulkUploadTranscation.getUploadOperation());
			bulkDataGetExtnDto.setStatus(bulkUploadTranscation.getStatusCode());
			bulkDataGetExtnDto.setStatusDescription(bulkUploadTranscation.getUploadDescription());
			bulkDataGetExtnDto.setEntityName(bulkUploadTranscation.getEntityName());
			bulkDataGetExtnDto.setCategory(bulkUploadTranscation.getCategory());
			bulkDataGetExtnDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
			bulkDataGetExtnDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
		} catch (Exception e) {
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_TRANSACTION_ERROR,transcationId +" - "+e.getMessage()));
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(), 
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(),e);
		}
		
		return  bulkDataGetExtnDto;
	}


	@Override
	public PageDto<BulkDataGetExtnDto> getAllTrascationDetails(int pageNumber, int pageSize, String sortBy,String orderBy, String category) {
		Page<BulkUploadTranscation> pageData = null;
		List<BulkDataGetExtnDto> bulkDataGetExtnDtos2=new ArrayList<BulkDataGetExtnDto>();
		PageDto<BulkDataGetExtnDto> pageDto2=new PageDto<BulkDataGetExtnDto>();
		try{
			Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy),sortBy));
			pageData=bulkTranscationRepo.findByCategory(category,pageable);
			for(BulkUploadTranscation bulkUploadTranscation:pageData.getContent()){
				BulkDataGetExtnDto bulkDataGetExtnDto=new BulkDataGetExtnDto();
				
					bulkDataGetExtnDto.setTranscationId(bulkUploadTranscation.getId());
					bulkDataGetExtnDto.setCount(bulkUploadTranscation.getRecordCount());
					bulkDataGetExtnDto.setOperation(bulkUploadTranscation.getUploadOperation());
					bulkDataGetExtnDto.setStatus(bulkUploadTranscation.getStatusCode());
					bulkDataGetExtnDto.setStatusDescription(bulkUploadTranscation.getUploadDescription());
					bulkDataGetExtnDto.setCategory(bulkUploadTranscation.getCategory());
					bulkDataGetExtnDto.setEntityName(bulkUploadTranscation.getEntityName());
					bulkDataGetExtnDto.setCategory(bulkUploadTranscation.getCategory());
					bulkDataGetExtnDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
					bulkDataGetExtnDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
					bulkDataGetExtnDtos2.add(bulkDataGetExtnDto);
				
			}
		}catch (Exception e) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_TRANSACTION_ALL_ERROR);
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(), 
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(),e);
		}
		pageDto2 = new PageDto<BulkDataGetExtnDto>(pageData.getNumber(), pageData.getTotalPages(), pageData.getTotalElements(),
				bulkDataGetExtnDtos2);
		return pageDto2;
	}


	private BulkDataResponseDto insertDataToCSVFile(String tableName, String operation, String category,
			MultipartFile[] files) {
    		
			if (tableName.isBlank() || operation.isBlank() || files==null ||files.length==0) {
				auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
				throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
						BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage());
			}
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,"{category:'"+category+"',tablename:'"+tableName+"',operation:'"+operation+"'}"));
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		mapper.init();
    		Class<?> entity=mapper.getEntity(tableName);
    		String repoBeanName=mapper.getRepo(entity);
        	JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
        	StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, platformTransactionManager);
        	List<String> failureMessage = new ArrayList<String>();
            int[] numArr = {0};
            String[] status= {"PROCESS"};
            Arrays.asList(files).stream().forEach(file -> {
            	auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV,operation + " from "+file.getOriginalFilename()));
            	ItemReader<Object> itemReader;  
                JobExecution jobExecution = null;
                
    			try {

					String csvString = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
					String trimmedString = csvString.trim();
					InputStream csvInputStream = new ByteArrayInputStream(
							trimmedString.getBytes(StandardCharsets.UTF_8));
					csvValidator(file.getOriginalFilename(), csvInputStream, entity);
					InputStream csvStream = new ByteArrayInputStream(trimmedString.getBytes(StandardCharsets.UTF_8));
					itemReader = itemReader(csvStream, entity);
					ItemWriter<List<Object>> itemWriter = itemWriterMapper(repoBeanName, operationMapper(operation),
							entity);
    		        ItemProcessor itemProcessor=processor(operation);
    		        JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
    		        jobExecution = jobLauncher.run(job(jobBuilderFactory, stepBuilderFactory, itemReader,itemProcessor, itemWriter),parameters);
    				JobInstance jobInstence=new JobInstance(jobExecution.getJobId(), "ETL-file-load");
    				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_JOBDETAILS,jobExecution.getJobId().toString()));
    				StepExecution stepExecution=jobRepository.getLastStepExecution(jobInstence, "ETL-file-load");
    				status[0]=jobExecution.getStatus().toString();
    				numArr[0]+=stepExecution.getReadCount();
					if (status[0].equalsIgnoreCase("FAILED")) {
						String msg = stepExecution.getExitStatus().getExitDescription().toString();
						if (msg.length() >= 256) {
							failureMessage.add(msg.substring(0, 250));
							auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV_STATUS_ERROR,"{filename: '"+file.getOriginalFilename()+"',operation:'"+operation+"',jobid:'"+jobExecution.getJobId()+"', message: '"+msg.substring(0, 250)+"'}"));
						} else {
							failureMessage.add(msg);
							auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV_STATUS_ERROR,"{filename: '"+file.getOriginalFilename()+"',operation:'"+operation+"',jobid:'"+jobExecution.getJobId()+"',message: '"+msg+"'}"));
						}
    				}
    				else
    				    auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV_STATUS,"{fileName: '"+file.getOriginalFilename()+"',operation:'"+operation+"',jobid:'"+jobExecution.getJobId()+"',message: '"+jobExecution.getStatus().toString()+"'}"));
    			}catch (IOException e) {
    				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,"{fileName: '"+file.getOriginalFilename()+"',operation:'"+operation+"',error: "+e.getMessage()+"}"));
    				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			}
    			catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
    					| JobParametersInvalidException e) {
    				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,"{fileName: '"+file.getOriginalFilename()+"',operation:'"+operation+"',error: "+e.getMessage()+"}"));
    				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
    	  					BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
    			}
            });
            
            BulkUploadTranscation bulkUploadTranscation=saveTranscationDetails(numArr[0],operation,entity.getSimpleName(),category,failureMessage.toString(),status[0]);
            bulkDataResponseDto=setResponseDetails(bulkUploadTranscation, tableName);
    		return bulkDataResponseDto;
    	}

    	@Override
    	public BulkDataResponseDto bulkDataOperation(String tableName, String operation, String category,
                MultipartFile[] files, String centerId, String source, String process,
				 String supervisorStatus) {
    		BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
    		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CATEGORY,category));
    		if(category.equalsIgnoreCase("masterdata")) {
    			bulkDataResponseDto=insertDataToCSVFile(tableName, operation, category, files);
    		}
    		else if(category.equalsIgnoreCase("packet")) {
    			bulkDataResponseDto=uploadPackets(files, operation, category, centerId, source, process, supervisorStatus);
    		}
    		else {
    			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_CATEGORY);
				throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
						BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage());
				
    		}
            return bulkDataResponseDto;
     	}
    	
    	private void updateBulkUploadTransaction(BulkUploadTranscation bulkUploadTranscation) {
    		bulkUploadTranscation.setStatusCode("COMPLETED");
    		bulkUploadTranscation.setUploadedDateTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
    		bulkUploadTranscation.setUpdatedBy("JOB");
    		bulkTranscationRepo.save(bulkUploadTranscation);
    	}

    	
		private BulkDataResponseDto uploadPackets(MultipartFile[] files, String operation, String category, String centerId,
				  String source, String process, String supervisorStatus) {
    		
    		if ( files==null ||files.length==0) {
    			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
				throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
						BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
			}
    		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,"{category:'"+category+"',operation:'"+operation+"'}"));
    		boolean hasDataReadRole = hasDataReadRole();
    		
    		BulkUploadTranscation bulkUploadTranscation = saveTranscationDetails(0,
    				operation, "", category, hasDataReadRole ? "NOTE: Only after successful RID sync, Packet upload will be attempted" :
    				"NOTE: Only packet upload is attempted. DATA_READ role is not present to perform RID sync", "PROCESSING");

    		Arrays.stream(files).forEach( file -> {
    			String message = null;
    			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_PACKET_UPLOAD, file.getOriginalFilename()));
    			try {
    				if (!file.getOriginalFilename().endsWith(".zip")) {
    					throw new RequestException(BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorCode(),
    							BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorMessage());
    				}

    				if (file.isEmpty()) {
    					throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
    							BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
    				}

    				Job job = jobBuilderFactory.get("ETL-Load")
    						.listener(packetJobResultListener)
    						.incrementer(new RunIdIncrementer())
    						.start(stepBuilderFactory.get("packet-upload")
    								.tasklet(new PacketUploadTasklet(file.getOriginalFilename(), file.getBytes(),
    										packetUploadService, centerId, supervisorStatus,
    										source, process, hasDataReadRole ? "SYNC-UPLOAD" : "UPLOAD"))
    								.build())
    						.build();

    				JobParameters jobParameters = new JobParametersBuilder()
    						.addString("transactionId", bulkUploadTranscation.getId())
    						.addString("username", SecurityContextHolder.getContext().getAuthentication().getName())
    						.addLong("time", System.currentTimeMillis())
    						.toJobParameters();

    				jobLauncher.run(job, jobParameters);

    			} catch (Throwable e) {
    				message = String.format(PKT_UPLOAD_MESSAGE, file.getOriginalFilename(), "FAILED", e.getMessage());
    			}

    			if(message != null) {
    				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_PACKET_STATUS, message));
    				bulkUploadTranscation.setStatusCode("FAILED");
    				bulkUploadTranscation.setUploadDescription(message);
    				updateBulkUploadTransaction(bulkUploadTranscation);
    			}
    		});
    		return setResponseDetails(bulkUploadTranscation, "");    	
    	}
        
		private boolean hasDataReadRole() {
			Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) SecurityContextHolder
					.getContext().getAuthentication().getAuthorities();
			return grantedAuthorities.stream().anyMatch( ga -> ga.getAuthority().equalsIgnoreCase(DATA_READ_ROLE));
		}

		private Job job(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            ItemReader<Object> itemReader,
            ItemProcessor itemProcessor,
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

		private ConversionService testConversionService() {
			DefaultConversionService testConversionService = new DefaultConversionService();
			DefaultConversionService.addDefaultConverters(testConversionService);
			testConversionService.addConverter(new Converter<String, LocalDateTime>() {
				@Override
				public LocalDateTime convert(String text) {
					return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
				}
			});
			testConversionService.addConverter(new Converter<String, LocalDate>() {

				@Override
				public LocalDate convert(String text) {
					return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
				}

			});

			testConversionService.addConverter(new Converter<String, LocalTime>() {

				@Override
				public LocalTime convert(String text) {
					return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
				}

			});


			return testConversionService;
		}
		
	 @StepScope
		private FlatFileItemReader<Object> itemReader(InputStream file, Class<?> clazz) throws IOException {
		 
		    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setDelimiter(",");
	        lineTokenizer.setStrict(false);
	        FlatFileItemReader<Object> flatFileItemReader = new FlatFileItemReader<>();
			flatFileItemReader.setResource(new InputStreamResource(file));
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
			fieldSetMapper.setConversionService(testConversionService());
	        DefaultLineMapper<Object> defaultLineMapper = new DefaultLineMapper<>();
	        defaultLineMapper.setLineTokenizer(lineTokenizer);
	        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	        flatFileItemReader.setLineMapper(defaultLineMapper);
	        return flatFileItemReader;

	 }

		private ItemProcessor processor(String operation) {
		
		 
		 ItemProcessor itemprocessor=new ItemProcessor() {
			 
			
			LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));  
			@Override
			public Object process(Object item) throws Exception {
				setCreateMetaData();
				if(operation.equalsIgnoreCase("insert")) {
				((BaseEntity) item).setCreatedBy(setCreateMetaData());
				((BaseEntity) item).setCreatedDateTime(now);
				}else if(operation.equalsIgnoreCase("update")) {
					((BaseEntity) item).setUpdatedBy(setCreateMetaData());
					((BaseEntity) item).setUpdatedDateTime(now);
				}else if(operation.equalsIgnoreCase("delete")) {
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
		private ItemWriter<List<Object>> insertItemWriter(String repoBeanName, String methodName, Class<?> entity) {
			RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>(em,emf,entity,mapper,applicationContext);
			writer.setRepository((BaseRepository<?, ?>) applicationContext.getBean(repoBeanName));
			writer.setMethodName(methodName);
			try {
				writer.afterPropertiesSet();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return writer;
		}

		@SuppressWarnings("unchecked")
		private <T extends BaseEntity, S> ItemWriter<List<Object>> updateItemWriter(String repoName, Class<?> entity) {

			ItemWriter<List<Object>> writer = new ItemWriter<List<Object>>() {

				@Autowired(required = false)
				@Override
				public void write(List<? extends List<Object>> items) throws Exception {
					// TODO Auto-generated method stub
					Iterator i$ = items.iterator();
					BaseRepository baserepo = (BaseRepository) applicationContext.getBean(repoName);

					while (i$.hasNext()) {

						T object = (T) i$.next();
						PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
						Object projectId = util.getIdentifier(object);
						T machin = (T) em.find(entity, projectId);
						try {
						if (!machin.equals(null)) {
							object.setCreatedBy(machin.getCreatedBy());
							object.setCreatedDateTime(machin.getCreatedDateTime());
							baserepo.save(object);
							String repoBeanName1;
							BaseRepository baserepo1;
				            switch(entity.getCanonicalName()) {
				            case "io.mosip.admin.bulkdataupload.entity.ZoneUser":
				        		repoBeanName1=mapper.getRepo(ZoneUserHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		ZoneUserHistory userHistory = new ZoneUserHistory();
				        		MapperUtils.map(object, userHistory);
								MapperUtils.setBaseFieldValue(object, userHistory);
								userHistory.setEffDTimes(userHistory.getUpdatedDateTime());
								baserepo1.save(userHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.UserDetails":
				        		repoBeanName1=mapper.getRepo(UserDetailsHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		UserDetailsHistory userDetailHistory = new UserDetailsHistory();
				        		MapperUtils.map(object, userDetailHistory);
								MapperUtils.setBaseFieldValue(object, userDetailHistory);
								userDetailHistory.setEffDTimes(userDetailHistory.getUpdatedDateTime());
								baserepo1.save(userDetailHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.Machine":
				        		repoBeanName1=mapper.getRepo(MachineHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		MachineHistory machineHistory = new MachineHistory();
				        		MapperUtils.map(object, machineHistory);
								MapperUtils.setBaseFieldValue(object, machineHistory);
								machineHistory.setEffectDateTime(machineHistory.getUpdatedDateTime());
								baserepo1.save(machineHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.Device":
				        		repoBeanName1=mapper.getRepo(DeviceHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		DeviceHistory deviceHistory = new DeviceHistory();
				        		MapperUtils.map(object, deviceHistory);
								MapperUtils.setBaseFieldValue(object, deviceHistory);
								deviceHistory.setEffectDateTime(deviceHistory.getUpdatedDateTime());
								baserepo1.save(deviceHistory);
							break;
							default:
							break;
				            }
						} else {
							throw new MasterDataServiceException(
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorCode(),
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorMessage());
						}
						}catch(NullPointerException e) {
							throw new MasterDataServiceException(
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorCode(),
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorMessage(),e);
						}
					}
				}
			};

			return writer;
		}

		@SuppressWarnings("unchecked")
		private <T extends BaseEntity, S> ItemWriter<List<Object>> deleteItemWriter(String repoName, Class<?> entity) {
			ItemWriter<List<Object>> writer = new ItemWriter<List<Object>>() {

				@Autowired(required = false)
				@Override
				public void write(List<? extends List<Object>> items) throws Exception {
					// TODO Auto-generated method stub
					Iterator i$ = items.iterator();
					BaseRepository baserepo = (BaseRepository) applicationContext.getBean(repoName);

					while (i$.hasNext()) {

						T object = (T) i$.next();
						PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
						Object projectId = util.getIdentifier(object);
						T machin = (T) em.find(entity, projectId);
						try {
						if (!machin.equals(null)) {
							object.setCreatedBy(machin.getCreatedBy());
							object.setCreatedDateTime(machin.getCreatedDateTime());
							object.setUpdatedBy(machin.getUpdatedBy());
							object.setUpdatedDateTime(machin.getUpdatedDateTime());
							baserepo.save(object);
							String repoBeanName1;
				            BaseRepository baserepo1;
				            switch(entity.getCanonicalName()) {
				            case "io.mosip.admin.bulkdataupload.entity.ZoneUser":
				        		repoBeanName1=mapper.getRepo(ZoneUserHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		ZoneUserHistory userHistory = new ZoneUserHistory();
				        		MapperUtils.map(object, userHistory);
								MapperUtils.setBaseFieldValue(object, userHistory);
								userHistory.setEffDTimes(userHistory.getDeletedDateTime());
								baserepo1.save(userHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.UserDetails":
				        		repoBeanName1=mapper.getRepo(UserDetailsHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		UserDetailsHistory userDetailHistory = new UserDetailsHistory();
				        		MapperUtils.map(object, userDetailHistory);
								MapperUtils.setBaseFieldValue(object, userDetailHistory);
								userDetailHistory.setEffDTimes(userDetailHistory.getDeletedDateTime());
								baserepo1.save(userDetailHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.Machine":
				        		repoBeanName1=mapper.getRepo(MachineHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		MachineHistory machineHistory = new MachineHistory();
				        		MapperUtils.map(object, machineHistory);
								MapperUtils.setBaseFieldValue(object, machineHistory);
								machineHistory.setEffectDateTime(machineHistory.getDeletedDateTime());
								baserepo1.save(machineHistory);
				            break;
				            case "io.mosip.admin.bulkdataupload.entity.Device":
				        		repoBeanName1=mapper.getRepo(DeviceHistory.class);
				        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
				        		DeviceHistory deviceHistory = new DeviceHistory();
				        		MapperUtils.map(object, deviceHistory);
								MapperUtils.setBaseFieldValue(object, deviceHistory);
								deviceHistory.setEffectDateTime(deviceHistory.getDeletedDateTime());
								baserepo1.save(deviceHistory);
							break;
							default:
							break;
				            }

						} else {
							throw new MasterDataServiceException(
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorCode(),
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorMessage());
						}
						} catch (NullPointerException e) {
							throw new MasterDataServiceException(
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorCode(),
									BulkUploadErrorCode.BULK_UPDATE_OPERATION_ERROR.getErrorMessage(), e);
						}
					}

				}
			};

			return writer;
		}
	    
	    private BulkUploadTranscation saveTranscationDetails(int count,String operation,String entityName,String category,String failureMessage, String status) {
	    	auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_TRANSACTION_API_CALLED,"{operation:'"+operation+"',category:'"+operation+"',entity:'"+entityName+"'}"));
	    	BulkUploadTranscation bulkUploadTranscation=new BulkUploadTranscation();
	    	LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
	    	bulkUploadTranscation.setIsActive(true);
	    	bulkUploadTranscation.setLangCode("eng");
	    	bulkUploadTranscation.setStatusCode(status);
	    	bulkUploadTranscation.setCreatedBy(setCreateMetaData());
	    	bulkUploadTranscation.setCreatedDateTime(now);
	    	bulkUploadTranscation.setEntityName(entityName);
	    	bulkUploadTranscation.setUploadedBy(setCreateMetaData());
	    	bulkUploadTranscation.setUploadedDateTime(Timestamp.valueOf(now));
	    	bulkUploadTranscation.setCategory(category);
			bulkUploadTranscation.setUploadDescription(failureMessage);
	    	bulkUploadTranscation.setUploadOperation(operation);
	    	bulkUploadTranscation.setRecordCount(count);
	    	BulkUploadTranscation b=bulkTranscationRepo.save(bulkUploadTranscation);
	    	auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_TRANSACTION_API_SUCCESS,b.getId()));
			return bulkUploadTranscation;
	    	
	    }
	    private BulkDataResponseDto setResponseDetails(BulkUploadTranscation bulkUploadTranscation,String tableName) {
	    	
	    	BulkDataResponseDto bulkDataResponseDto=new BulkDataResponseDto();
	    	bulkDataResponseDto.setTranscationId(bulkUploadTranscation.getId());
	    	bulkDataResponseDto.setOperation(bulkUploadTranscation.getUploadOperation());
	    	bulkDataResponseDto.setStatus(bulkUploadTranscation.getStatusCode());
	    	bulkDataResponseDto.setSuccessCount(bulkUploadTranscation.getRecordCount());
	    	bulkDataResponseDto.setCategory(bulkUploadTranscation.getCategory());
	    	bulkDataResponseDto.setStatusDescription(bulkUploadTranscation.getUploadDescription());
	    	bulkDataResponseDto.setTableName(tableName);
	    	bulkDataResponseDto.setTimeStamp(bulkUploadTranscation.getCreatedDateTime().toString());
	    	bulkDataResponseDto.setUploadedBy(bulkUploadTranscation.getUploadedBy());
	    	return bulkDataResponseDto;
	    }    

		private static String setCreateMetaData() {
	    	String contextUser="superadmin";
			Authentication authN = SecurityContextHolder.getContext().getAuthentication();
			if (!EmptyCheckUtils.isNullEmpty(authN)) {
				contextUser = authN.getName();
			}
			return contextUser;
		}

		private void csvValidator(String csvFileName, InputStream csvFile, Class clazz) throws IOException {
	    	String ext=Files.getFileExtension(csvFileName);
	    	if(!ext.equalsIgnoreCase("csv")) {
	    		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_EXT_VALIDATOR_ISSUE, csvFileName));

	    		throw new ValidationException("Supported format are only csv file");

	    	}
	    	int count=0;
	    	int lineCount=0;
	    	String line;
	    	BufferedReader br;
	    	Map<Integer,Field> fieldMap=new HashMap<>();
	    	List<String> clazzfields=new ArrayList<>();
	    	List<String> superclazzfields=new ArrayList<>();
	    	for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				clazzfields.add(field.getName());
	    	}
	    	for (Field field : clazz.getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				superclazzfields.add(field.getName());
	    	}
			try {
				br = new BufferedReader(new InputStreamReader(csvFile));
				while ((line = br.readLine()) != null) {
			    	  String l = line.trim(); // Remove end of line. You can print line here.'
			    	  
			    	  String[] columns = l.split(",");
			    	  count=columns.length;
			    	  lineCount++;
			    	  for(int i=0;i<count;i++) {
			    		  if(clazzfields.contains(columns[i])) {
			    			  fieldMap.put(i, clazz.getDeclaredField(columns[i]));
			    		  }
			    		  else if(superclazzfields.contains(columns[i])) {
			    			  fieldMap.put(i, clazz.getSuperclass().getDeclaredField(columns[i]));
			    		  }
			    		  else {
			    			 auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_INVALID_CSV_FILE, csvFileName));
			  				throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"invalid field mentioned.The exception occured at line number "+lineCount);
			  				
			    		  }
			    	  }
			    	  
			    	  break;
				}
				List<String> linelist=new ArrayList<>();
				Set<String> lineSet=new HashSet<>();
				while ((line = br.readLine()) != null) {
					 String l = line.trim(); // Remove end of line. You can print line here.'  
			    	 String[] columns = l.split(",");
			    	 lineCount++;
			    	 
						if (count != columns.length) {
						 auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));

						 throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"all the rows should have same number of element in csv file.The exception occured at line number "+lineCount); 
						

						}
					 String il="";
					 for(int i=0;i<columns.length;i++) {
							if (columns[i].isBlank()) {
							 auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
							 throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"Field is missing.The exception occured at line number "+lineCount); 
							}
						 
						 if(!validateDataType(fieldMap.get(i),columns[i].trim())) {
							 auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
							 throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"Invalid data type.The exception occured at line number "+lineCount); 
						  
						 }
						 il=il+columns[i].trim();
					 }
					 linelist.add(il);
					 lineSet.add(il);
					 if(linelist.size()!=lineSet.size()) {
						 auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
						 throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"Duplicate records found.The exception occured at line number "+lineCount);
					 }
				}
				br.close();
			} catch (IOException e) {
				lineCount++;
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_INVALID_CSV_FILE, csvFileName));
				throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"invalid csv file.The exception occured at line number "+lineCount,e);
				
			} catch (NoSuchFieldException | SecurityException e) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_INVALID_CSV_FILE, csvFileName));
				throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),"invalid field mentioned.The exception occured at line number "+lineCount,e);
				
			} 
	    	
	    }

	    private boolean validateDataType(Field field, String value) {
			String fieldType = field.getType().getTypeName();
			if (value.equalsIgnoreCase("NULL")) {
				return true;
			}
			if (LocalDateTime.class.getName().equals(fieldType)) {
				try {
					LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
				}catch(DateTimeParseException e) {
					return false;
				}
			}
			if (LocalTime.class.getName().equals(fieldType)) {
				try {
					LocalTime.parse(value,DateTimeFormatter.ISO_TIME);

				}catch(DateTimeParseException e) {
					return false;
				}
			}
			if (LocalDate.class.getName().equals(fieldType)) {
				try {

					LocalDate.parse(value,DateTimeFormatter.ISO_DATE);

				}catch(DateTimeParseException e) {
					return false;
				}
			}
			if (Long.class.getName().equals(fieldType)) {
				try {
					Long.parseLong(value);
				}catch(NumberFormatException e) {
					return false;
				}
			}
			if (long.class.getName().equals(fieldType)) {
				try {
					Long.parseLong(value);
				}catch(NumberFormatException e) {
					return false;
				}
			}
			if (Integer.class.getName().equals(fieldType)) {
				try {
					Integer.parseInt(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (int.class.getName().equals(fieldType)) {
				try {
					Integer.parseInt(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (Float.class.getName().equals(fieldType)) {
				try {
					Float.parseFloat(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (float.class.getName().equals(fieldType)) {
				try {
					Float.parseFloat(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (Double.class.getName().equals(fieldType)) {
				try {
					Double.parseDouble(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (double.class.getName().equals(fieldType)) {
				try {
					Double.parseDouble(value);
				}catch(NumberFormatException  e) {
					return false;
				}
			}
			if (Boolean.class.getName().equals(fieldType)) {
				if(!value.equalsIgnoreCase("true")&&!value.equalsIgnoreCase("false")) {
					return false;
				}
			}
			if (boolean.class.getName().equals(fieldType)) {
				if(!value.equalsIgnoreCase("true")&&!value.equalsIgnoreCase("false")) {
					return false;
				}
			}
			if (Short.class.getName().equals(fieldType)) {
				try {
					Short.valueOf(value);
				}catch(NumberFormatException  e) {
					return false;
				} 
			}
			if (short.class.getName().equals(fieldType)) {
				try {
					Short.valueOf(value);
				}catch(NumberFormatException  e) {
					return false;
				} 
			}
			return true;
		}

		private static String operationMapper(String operationName) {
			if (operationName.equalsIgnoreCase("insert"))
				operationName = "create";

			return operationName;
		}

		ItemWriter<List<Object>> itemWriterMapper(String repoBeanName, String operationName, Class<?> entity) {
			ItemWriter<List<Object>> item = null;
			if (operationName.equalsIgnoreCase("create"))
				item = insertItemWriter(repoBeanName, operationName,entity);
			else if (operationName.equalsIgnoreCase("update"))
				item = updateItemWriter(repoBeanName, entity);
			else if (operationName.equalsIgnoreCase("delete"))
				item = deleteItemWriter(repoBeanName, entity);
			return item;
		}
}
