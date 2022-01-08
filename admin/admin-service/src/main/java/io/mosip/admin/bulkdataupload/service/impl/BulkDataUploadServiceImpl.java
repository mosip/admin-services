package io.mosip.admin.bulkdataupload.service.impl;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import io.mosip.admin.bulkdataupload.batch.CustomLineMapper;
import io.mosip.admin.bulkdataupload.batch.JobResultListener;
import io.mosip.admin.bulkdataupload.batch.PacketUploadTasklet;
import io.mosip.admin.bulkdataupload.dto.*;
import io.mosip.admin.bulkdataupload.batch.CustomRecordSeparatorPolicy;
import io.mosip.admin.bulkdataupload.service.PacketUploadService;
import io.mosip.kernel.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.config.Mapper;

import io.mosip.admin.bulkdataupload.batch.RepositoryListItemWriter;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;


/**
 * BulkDataUpload service
 * 
 * @author dhanendra
 *
 */
@Service
public class BulkDataUploadServiceImpl implements BulkDataService {

	private static final Logger logger = LoggerFactory.getLogger(BulkDataUploadServiceImpl.class);
	private static Predicate<String> emptyCheck = String::isBlank;
	private static String STATUS_MESSAGE = "SUCCESS: %d, FAILED: %d";
	private static String CSV_UPLOAD_MESSAGE = "FILE: %s, READ: %d, STATUS: %s, MESSAGE: %s";
	private static String PKT_UPLOAD_MESSAGE = "FILE: %s, STATUS: %s, MESSAGE: %s";

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private Mapper mapper;

	@Autowired
	private EntityManager em;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BulkUploadTranscationRepository bulkTranscationRepo;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private PacketUploadService packetUploadService;

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	@Qualifier("customStepBuilderFactory")
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Value("${mosip.mandatory-languages}")
	private String mandatoryLanguages;

	@Value("${mosip.optional-languages}")
	private String optionalLanguages;

	private Map<String, Class> entityMap = new HashMap<String, Class>();


	@Override
	public BulkDataGetExtnDto getTrascationDetails(String transcationId) {

		BulkDataGetExtnDto bulkDataGetExtnDto = new BulkDataGetExtnDto();
		try {
			BulkUploadTranscation bulkUploadTranscation = bulkTranscationRepo.findTransactionById(transcationId);
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
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_TRANSACTION_ERROR,
					transcationId + " - " + e.getMessage()));
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(),
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(), e);
		}

		return bulkDataGetExtnDto;
	}

	@Override
	public PageDto<BulkDataGetExtnDto> getAllTrascationDetails(int pageNumber, int pageSize, String sortBy,
			String orderBy, String category) {
		Page<BulkUploadTranscation> pageData = null;
		List<BulkDataGetExtnDto> bulkDataGetExtnDtos2 = new ArrayList<BulkDataGetExtnDto>();
		PageDto<BulkDataGetExtnDto> pageDto2 = new PageDto<BulkDataGetExtnDto>();
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy));
			pageData = bulkTranscationRepo.findByCategory(category, pageable);
			for (BulkUploadTranscation bulkUploadTranscation : pageData.getContent()) {
				BulkDataGetExtnDto bulkDataGetExtnDto = new BulkDataGetExtnDto();

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
		} catch (Exception e) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_TRANSACTION_ALL_ERROR);
			throw new DataNotFoundException(BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorCode(),
					BulkUploadErrorCode.UNABLE_TO_RETRIEVE_TRANSCATION.getErrorMessage(), e);
		}
		pageDto2 = new PageDto<BulkDataGetExtnDto>(pageData.getNumber(), pageData.getTotalPages(),
				pageData.getTotalElements(), bulkDataGetExtnDtos2);
		return pageDto2;
	}

	private BulkDataResponseDto importDataFromCSVFile(String tableName, String operation, String category,
			MultipartFile[] files) {

		if(!isValidOperation(operation)) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_OPERATION);
			throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
					BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage() + "OPERATION");
		}

		if (files == null || files.length == 0) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
			throw new RequestException(BulkUploadErrorCode.NO_FILE.getErrorCode(),
					BulkUploadErrorCode.NO_FILE.getErrorMessage());
		}

		if (tableName == null || tableName.isBlank()) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
			throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
					BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage() + "TABLENAME");
		}

		Class<?> entity = mapper.getEntity(tableName);
		if (entity == null) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
			throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
					BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage() + "TABLENAME");
		}

		logger.info("category {}, tablename: {} , operation: {}, Uploaded files : {}", category, tableName, operation,
				files.length);

		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,
				"{category:'" + category + "',tablename:'" + tableName + "',operation:'" + operation + "'}"));

		BulkUploadTranscation bulkUploadTranscation = saveTranscationDetails(0, operation,
				entity.getSimpleName(), category, Collections.EMPTY_LIST, "PROCESSING");

		Arrays.stream(new MultipartFile[]{files[0]}).forEach(file -> {
			String message = null;
			try {
				logger.info("Is file empty ? {}, file-size :{}", file.isEmpty(), file.getSize());
				if (file.isEmpty()) {
					throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
							BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
				}

				if (!file.getOriginalFilename().endsWith(".csv")) {
					throw new RequestException(BulkUploadErrorCode.INVALID_FILE_FORMAT.getErrorCode(),
							BulkUploadErrorCode.INVALID_FILE_FORMAT.getErrorMessage());
				}

				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV,
						operation + " from " + file.getOriginalFilename()));

				JobParameters jobParameters = new JobParametersBuilder()
						.addString("transactionId", bulkUploadTranscation.getId())
						.addString("username", SecurityContextHolder.getContext().getAuthentication().getName())
						.addLong("time", System.currentTimeMillis())
						.toJobParameters();
				jobLauncher.run(getJob(file,operation, mapper.getRepo(entity), setCreateMetaData(), entity),
								jobParameters);

				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_JOBDETAILS,
						bulkUploadTranscation.getId()));

			} catch (Throwable e) {
				logger.error("Failed to import data from CSV", e);
				message = String.format(CSV_UPLOAD_MESSAGE, file.getOriginalFilename(), 0, "FAILED", e.getMessage());
			}

			//On failure of launching job
			if(message != null) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
						bulkUploadTranscation.getId() + " --> " + message));

				bulkUploadTranscation.setUploadDescription(message);
				bulkUploadTranscation.setRecordCount(0);
				updateBulkUploadTransaction(bulkUploadTranscation);
			}

		});
		return setResponseDetails(bulkUploadTranscation, tableName);
	}

	@Override
	public BulkDataResponseDto bulkDataOperation(String tableName, String operation, String category,
                                                 MultipartFile[] files, String centerId, String source, String process,
												 String supervisorStatus) {

		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CATEGORY, category));

		switch (category.toLowerCase()) {
			case "masterdata":
				return importDataFromCSVFile(tableName, operation, category, files);

			case "packet":
				return uploadPackets(files, operation, category, centerId, source, process, supervisorStatus);
		}

		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_CATEGORY);
		throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
				BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage() + "CATEGORY");
	}


	private void updateBulkUploadTransaction(BulkUploadTranscation bulkUploadTranscation) {
		bulkUploadTranscation.setStatusCode("COMPLETED");
		bulkUploadTranscation.setUploadedDateTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
		bulkUploadTranscation.setUpdatedBy("JOB");
		bulkTranscationRepo.save(bulkUploadTranscation);
	}

	private BulkDataResponseDto uploadPackets(MultipartFile[] files, String operation, String category, String centerId,
											  String source, String process, String supervisorStatus) {

		if (files == null || files.length == 0) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
			throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
					BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
		}
	
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,
				"{category:'" + category + "',operation:'" + operation + "'}"));

		BulkUploadTranscation bulkUploadTranscation = saveTranscationDetails(0,
				operation, "", category, null, "PROCESSING");

		Arrays.stream(files).forEach( file -> {
			String message = null;
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_PACKET_UPLOAD, file.getOriginalFilename()));
			try {
				if (!file.getOriginalFilename().endsWith(".zip")) {
					throw new RequestException(BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorCode(),
							BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorMessage());
				}

				logger.info("Is packet file empty ? {}, file-size :{}", file.isEmpty(), file.getSize());

				if (file.isEmpty()) {
					throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
							BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
				}

				Job job = jobBuilderFactory.get("ETL-Load")
						.listener(new JobResultListener(this.dataSource, this.auditUtil))
						.incrementer(new RunIdIncrementer())
						.start(stepBuilderFactory.get("packet-upload")
								.tasklet(new PacketUploadTasklet(file, packetUploadService, centerId, supervisorStatus,
										source, process))
								.build())
						.build();

				JobParameters jobParameters = new JobParametersBuilder()
						.addString("transactionId", bulkUploadTranscation.getId())
						.addString("username", SecurityContextHolder.getContext().getAuthentication().getName())
						.addLong("time", System.currentTimeMillis())
						.toJobParameters();

				jobLauncher.run(job, jobParameters);

			} catch (Throwable e) {
				logger.error("Failed to sync and upload packet", e);
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

	private Job getJob(MultipartFile file, String operation, String repositoryName, String contextUser,
					   Class<?> entity) throws IOException {
		Step step = stepBuilderFactory.get("ETL-file-load")
				.<Object, List<Object>>chunk(100)
				.reader(itemReader(file, entity))
				.processor(processor(operation, contextUser))
				.writer(itemWriterMapper(repositoryName, operationMapper(operation), entity))
				.build();

		return jobBuilderFactory.get("ETL-Load")
				.listener(new JobResultListener(this.dataSource, this.auditUtil))
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
	}

	private ConversionService customConversionService() {
		DefaultConversionService customConversionService = new DefaultConversionService();
		DefaultConversionService.addDefaultConverters(customConversionService);
		customConversionService.addConverter(new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String text) {
				return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
			}
		});
		customConversionService.addConverter(new Converter<String, LocalDate>() {

			@Override
			public LocalDate convert(String text) {
				return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
			}

		});

		customConversionService.addConverter(new Converter<String, LocalTime>() {

			@Override
			public LocalTime convert(String text) {
				return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
			}

		});

		customConversionService.addConverter(new Converter<String, Double>() {
			@Override
			public Double convert(String text) {
				return Double.parseDouble(text);
			}
		});

		return customConversionService;
	}

	@StepScope
	private FlatFileItemReader<Object> itemReader(MultipartFile file, Class<?> clazz) throws IOException {

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);

		FlatFileItemReader<Object> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setEncoding("UTF-8");
		flatFileItemReader.setResource(new InputStreamResource(file.getInputStream()));
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setRecordSeparatorPolicy(new CustomRecordSeparatorPolicy());
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setSkippedLinesCallback(new LineCallbackHandler() {
			@Override
			public void handleLine(String s) {
				lineTokenizer.setNames(s.split(","));
			}
		});
		BeanWrapperFieldSetMapper<Object> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(clazz);
		fieldSetMapper.setConversionService(customConversionService());

		CustomLineMapper<Object> lineMapper = new CustomLineMapper<Object>(setupLanguages(), null);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		flatFileItemReader.setLineMapper(lineMapper);
		return flatFileItemReader;

	}

	private ItemProcessor processor(String operation, String contextUser) {
		ItemProcessor itemprocessor = new ItemProcessor() {
			LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
			@Override
			public Object process(Object item) throws Exception {
				switch (operation.toLowerCase()) {
					case "insert":
						((BaseEntity) item).setCreatedBy(contextUser);
						((BaseEntity) item).setCreatedDateTime(now);
						((BaseEntity) item).setIsDeleted(false);
						break;
					case "update":
						((BaseEntity) item).setUpdatedBy(contextUser);
						((BaseEntity) item).setUpdatedDateTime(now);
						break;
					case "delete":
						((BaseEntity) item).setIsActive(false);
						((BaseEntity) item).setIsDeleted(true);
						((BaseEntity) item).setDeletedDateTime(now);
						break;
				}
				return item;
			}
		};
		return itemprocessor;
	}


	private ItemWriter<List<Object>> insertItemWriter(String repoBeanName, String methodName, Class<?> entity) {
		RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>(em, emf, entity, mapper,
				applicationContext);
		writer.setRepoBeanName(repoBeanName);
		writer.setOperation("insert");
		writer.setMethodName(methodName);
		return writer;
	}

	private <T extends BaseEntity, S> ItemWriter<List<Object>> updateItemWriter(String repoName, Class<?> entity) {
		RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>(em, emf, entity, mapper,
				applicationContext);
		writer.setRepoBeanName(repoName);
		writer.setOperation("update");
		return writer;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity, S> ItemWriter<List<Object>> deleteItemWriter(String repoName, Class<?> entity) {
		RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>(em, emf, entity, mapper,
				applicationContext);
		writer.setRepoBeanName(repoName);
		writer.setOperation("delete");
		return writer;
	}

	private BulkUploadTranscation saveTranscationDetails(int count, String operation, String entityName,
			String category, List<String> failureMessage, String status) {
		BulkUploadTranscation bulkUploadTranscation = new BulkUploadTranscation();
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
		bulkUploadTranscation.setUploadDescription((failureMessage !=null && !failureMessage.isEmpty()) ?
				failureMessage.toString() : "");
		bulkUploadTranscation.setUploadOperation(operation);
		bulkUploadTranscation.setRecordCount(count);
		return  bulkTranscationRepo.save(bulkUploadTranscation);
	}

	private BulkDataResponseDto setResponseDetails(BulkUploadTranscation bulkUploadTranscation, String tableName) {

		BulkDataResponseDto bulkDataResponseDto = new BulkDataResponseDto();
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
		String contextUser = "superadmin";
		Authentication authN = SecurityContextHolder.getContext().getAuthentication();
		if (!EmptyCheckUtils.isNullEmpty(authN)) {
			contextUser = authN.getName();
		}
		return contextUser;
	}


	private static String operationMapper(String operationName) {
		if (operationName.equalsIgnoreCase("insert"))
			operationName = "create";

		return operationName;
	}

	ItemWriter<List<Object>> itemWriterMapper(String repoBeanName, String operationName, Class<?> entity) {
		ItemWriter<List<Object>> item = null;
		if (operationName.equalsIgnoreCase("create"))
			item = insertItemWriter(repoBeanName, operationName, entity);
		else if (operationName.equalsIgnoreCase("update"))
			item = updateItemWriter(repoBeanName, entity);
		else if (operationName.equalsIgnoreCase("delete"))
			item = deleteItemWriter(repoBeanName, entity);
		return item;
	}
	
	private boolean isValidOperation(String operation) {
		return operation != null && (operation.equalsIgnoreCase("insert")
				|| operation.equalsIgnoreCase("update")
				|| operation.equalsIgnoreCase("delete"));
	}

	private List<String> setupLanguages() {
		List<String> configuredLanguages = new ArrayList<>();
		if(mandatoryLanguages != null && !mandatoryLanguages.isBlank()) {
			configuredLanguages.addAll(Arrays.asList(mandatoryLanguages.split(","))
					.stream()
					.filter(emptyCheck.negate())
					.collect(Collectors.toList()));
		}

		if(optionalLanguages != null && !optionalLanguages.isBlank()) {
			configuredLanguages.addAll(Arrays.asList(optionalLanguages.split(","))
					.stream()
					.filter(emptyCheck.negate())
					.collect(Collectors.toList()));
		}
		return configuredLanguages;
	}
}
