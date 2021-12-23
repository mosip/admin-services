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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.config.Mapper;
import io.mosip.admin.config.RepositoryListItemWriter;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.util.EmptyCheckUtils;

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

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	private AuditUtil auditUtil;

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
		// List<BulkDataGetExtnDto> bulkDataGetExtnDtos=new
		// ArrayList<BulkDataGetExtnDto>();
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

	private BulkDataResponseDto insertDataToCSVFile(String tableName, String operation, String category,
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

		BulkDataResponseDto bulkDataResponseDto = new BulkDataResponseDto();

		String repoBeanName = mapper.getRepo(entity);
		JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
		StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, platformTransactionManager);
		List<String> failureMessage = new ArrayList<String>();
		int[] numArr = { 0 };
		String[] status = { "PROCESS" };
		Arrays.asList(files).stream().forEach(file -> {
		
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV,
					operation + " from " + file.getOriginalFilename()));
			ItemReader<Object> itemReader;
			JobExecution jobExecution = null;

			logger.info("Is file empty ? {}, file-size :{}", file.isEmpty(), file.getSize());

			if(file.isEmpty()) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,
						"{fileName: '" + file.getOriginalFilename() + "',operation:'" + operation + "',error: 'Empty file, nothing to do'}"));
				throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
						BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
			}

			if (!file.getOriginalFilename().endsWith(".csv")) {
				auditUtil.setAuditRequestDto(
						EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_EXT_VALIDATOR_ISSUE, file.getOriginalFilename()));
				throw new RequestException(BulkUploadErrorCode.INVALID_FILE_FORMAT.getErrorCode(),
						BulkUploadErrorCode.INVALID_FILE_FORMAT.getErrorMessage());
			}

			try {
				String csvString = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
				String trimmedString = csvString.trim();
				InputStream csvInputStream = new ByteArrayInputStream(trimmedString.getBytes(StandardCharsets.UTF_8));
				csvValidator(file.getOriginalFilename(), csvInputStream, entity);
				InputStream csvStream = new ByteArrayInputStream(trimmedString.getBytes(StandardCharsets.UTF_8));
				itemReader = itemReader(csvStream, entity);
				ItemWriter<List<Object>> itemWriter = itemWriterMapper(repoBeanName, operationMapper(operation),
						entity);
				ItemProcessor itemProcessor = processor(operation);
				JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
						.toJobParameters();
				jobExecution = jobLauncher.run(
						job(jobBuilderFactory, stepBuilderFactory, itemReader, itemProcessor, itemWriter), parameters);
				JobInstance jobInstence = new JobInstance(jobExecution.getJobId(), "ETL-file-load");

				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_JOBDETAILS,
						jobExecution.getJobId().toString()));

				StepExecution stepExecution = jobRepository.getLastStepExecution(jobInstence, "ETL-file-load");
				status[0] = jobExecution.getStatus().toString();
				numArr[0] += stepExecution.getReadCount();

				if (status[0].equalsIgnoreCase("FAILED")) {
					jobExecution.getStepExecutions().forEach( step -> {
						step.getFailureExceptions().forEach( failure -> {
							if( failure instanceof FlatFileParseException) {
								failureMessage.add("Line: " + ((FlatFileParseException)failure).getLineNumber() +
										" >> Datatype mismatch / Failed to write into object");
							}
							else
								failureMessage.add(failure.getMessage());

							logger.error("Step failed - {}", file.getOriginalFilename(), failure);
						});
					});

					auditUtil.setAuditRequestDto(
							EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV_STATUS_ERROR,
									"{filename: '" + file.getOriginalFilename() + "',operation:'" + operation
											+ "',jobid:'" + jobExecution.getJobId() + "',message: '" +
											failureMessage + "'}"));

				} else
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CSV_STATUS,
							"{fileName: '" + file.getOriginalFilename() + "',operation:'" + operation + "',jobid:'"
									+ jobExecution.getJobId() + "',message: '" + jobExecution.getStatus().toString()
									+ "'}"));

			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException | IOException e) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,
						"{fileName: '" + file.getOriginalFilename() + "',operation:'" + operation + "',error: "
								+ e.getMessage() + "}"));
				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
						BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
			}
		});

		BulkUploadTranscation bulkUploadTranscation = saveTranscationDetails(numArr[0], operation,
				entity.getSimpleName(), category, failureMessage, status[0]);
		bulkDataResponseDto = setResponseDetails(bulkUploadTranscation, tableName);
		return bulkDataResponseDto;
	}

	@Override
	public BulkDataResponseDto bulkDataOperation(String tableName, String operation, String category,
                                                 MultipartFile[] files, String centerId) {

		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_CATEGORY, category));

		switch (category.toLowerCase()) {
			case "masterdata":
				return insertDataToCSVFile(tableName, operation, category, files);

			case "packet":
				return uploadPackets(files, operation, category,centerId);
		}

		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_CATEGORY);
		throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
				BulkUploadErrorCode.INVALID_ARGUMENT.getErrorMessage() + "CATEGORY");
	}

	private BulkDataResponseDto uploadPackets(MultipartFile[] files, String operation, String category, String centerId) {

		if (files == null || files.length == 0) {
			auditUtil.setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT);
			throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
					BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
		}
	
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,
				"{category:'" + category + "',operation:'" + operation + "'}"));
		BulkDataResponseDto bulkDataResponseDto = new BulkDataResponseDto();
		List<String> fileNames = new ArrayList<>();
		int[] numArr = { 0 };

		// Map<String,String> failureMessage=new HashMap<String, String>();
		List<String> failureMessage = new ArrayList<String>();
		String[] msgArr = { "FAILED" };
		Arrays.asList(files).stream().forEach(file -> {
			
			if (!file.getOriginalFilename().endsWith(".zip")) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,
						"{packetid: '" + files.toString()+ "',error: Supported format is only zip file }"));
				throw new RequestException(BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorCode(),
						BulkUploadErrorCode.INVALID_PCK_FILE_FORMAT.getErrorMessage());
			}

			logger.info("Is packet file empty ? {}, file-size :{}", file.isEmpty(), file.getSize());

			if (file.isEmpty()) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,
						"{packetid: '" + files.toString()+ "',error: Empty zip file }"));
				throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
						BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
			}

			auditUtil.setAuditRequestDto(
					EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_PACKET_UPLOAD, file.getOriginalFilename()));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(file.getOriginalFilename()).build();
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			try {
				
				HttpEntity<byte[]> fileEntity = new HttpEntity<>(file.getBytes(), fileMap);

				MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
				body.add("file", fileEntity);

				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

				ResponseEntity<String> response = restTemplate.exchange(packetRecieverApiUrl, HttpMethod.POST,
						requestEntity, String.class);
				JSONObject josnObject = new JSONObject(response.getBody());
				if (!josnObject.get("response").equals(null)) {
					numArr[0]++;
					msgArr[0] = "Success";
					auditUtil
							.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_PACKET_STATUS,
									"{packetid: '" + file.getOriginalFilename() + "', message: 'success'}"));

				} else {
					String str = josnObject.get("errors").toString();
					JSONArray jsonArray = new JSONArray(str);
					JSONObject josnObject1 = new JSONObject(jsonArray.get(0).toString());

					failureMessage.add(failureMessage.contains(josnObject1.get("message").toString()) ? null
							: josnObject1.get("message").toString());
					// failureMessage.put(file.getOriginalFilename(),
					// josnObject1.get("message").toString());
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(
							EventEnum.BULKDATA_UPLOAD_PACKET_STATUS_ERROR, "{packetid: '" + file.getOriginalFilename()
									+ "', message: '" + josnObject1.get("message").toString() + "'}"));
				}

			} catch (HttpClientErrorException | IOException | JSONException e) {
				auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_ERROR,
						"{packetid: '" + file.getOriginalFilename() + "',operation:'" + operation + "',error: "
								+ e.getMessage() + "}"));
				throw new MasterDataServiceException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
						BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorMessage(), e);
			}
			fileNames.add(file.getOriginalFilename());
		});
		BulkUploadTranscation bulkUploadTranscation = saveTranscationDetails(numArr[0], operation, "", category,
				failureMessage, msgArr[0]);
		bulkDataResponseDto = setResponseDetails(bulkUploadTranscation, "");
		return bulkDataResponseDto;
	}

	private Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<Object> itemReader, ItemProcessor itemProcessor, // ItemProcessor<User, User> itemProcessor,
			// ItemWriter<AbstractPersistable> itemWriter
			ItemWriter<List<Object>> itemWriter) {

		Step step = stepBuilderFactory.get("ETL-file-load").<Object, List<Object>>chunk(100).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).build();

		return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer()).start(step).build();
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


		testConversionService.addConverter(new Converter<String, Double>() {
			@Override
			public Double convert(String text) {
				return Double.parseDouble(text);
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

		ItemProcessor itemprocessor = new ItemProcessor() {

			LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

			@Override
			public Object process(Object item) throws Exception {
				setCreateMetaData();
				switch (operation.toLowerCase()) {
					case "insert":
						((BaseEntity) item).setCreatedBy(setCreateMetaData());
						((BaseEntity) item).setCreatedDateTime(now);
						((BaseEntity) item).setIsDeleted(false);
						break;
					case "update":
						((BaseEntity) item).setUpdatedBy(setCreateMetaData());
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

	@SuppressWarnings("unchecked")
	private ItemWriter<List<Object>> insertItemWriter(String repoBeanName, String methodName, Class<?> entity) {
		RepositoryListItemWriter<List<Object>> writer = new RepositoryListItemWriter<>(em, emf, entity, mapper,
				applicationContext);
		writer.setRepoBeanName(repoBeanName);
		writer.setOperation("insert");
		writer.setMethodName(methodName);
		/*try {
			writer.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("error in insertItemWriter", e);
		}*/
		return writer;
	}

	@SuppressWarnings("unchecked")
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
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_TRANSACTION_API_CALLED,
				"{operation:'" + operation + "',category:'" + operation + "',entity:'" + entityName + "'}"));
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
		if (!failureMessage.isEmpty()) {
			bulkUploadTranscation.setUploadDescription(failureMessage.toString());
		}
		bulkUploadTranscation.setUploadOperation(operation);
		bulkUploadTranscation.setRecordCount(count);
		BulkUploadTranscation b = bulkTranscationRepo.save(bulkUploadTranscation);
		auditUtil.setAuditRequestDto(
				EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_TRANSACTION_API_SUCCESS, b.getId()));
		return bulkUploadTranscation;

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

	private void csvValidator(String csvFileName, InputStream csvFile, Class clazz) throws IOException {

		int lineCount = 0;
		Integer langCodeColNo = null;
		String line;
		Map<Integer, Field> fieldMap = new HashMap<>();
		Map<String, Field> allowedFields = new HashMap<>();
		List<String> configuredLanguages = setupLanguages();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(EmbeddedId.class)) {
				for(Field f:field.getType().getDeclaredFields()){
					field.setAccessible(true);
					allowedFields.put(f.getName(), f);
				}
			}
			field.setAccessible(true);
			allowedFields.put(field.getName(), field);
		}
		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			field.setAccessible(true);
			allowedFields.put(field.getName(), field);
		}

		try(BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
			//Reading csv header
			while ((line = br.readLine()) != null) {
				String l = line.trim(); // Remove end of line. You can print line here.
				String[] columns = l.split(",");
				lineCount++;
				for (int i = 0; i < columns.length; i++) {
					String columnName = columns[i];
					Optional<String> result = allowedFields.keySet().stream().filter(k -> k.equals(columnName)).findFirst();
					if(!result.isPresent()) {
						auditUtil.setAuditRequestDto(EventEnum
								.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_INVALID_CSV_FILE, csvFileName));
						throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
								columnName + " - Invalid column name.");
					}
					if(columnName.equals("langCode") || columnName.equals("lang_code")) {
						langCodeColNo = i;
					}
					fieldMap.put(i, allowedFields.get(columnName));
				}
				break;
			}

			List<String> lineList = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String l = line.trim(); // Remove end of line. You can print line here.'
				String[] columns = l.split(",");
				lineCount++;

				StringBuilder tempRowBuilder = new StringBuilder();
				for (int i = 0; i < columns.length; i++) {
					if (columns[i].isBlank()) {
						auditUtil.setAuditRequestDto(EventEnum
								.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
						throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
								"Line:" + lineCount + " >> Field with blank value");
					}

					if(langCodeColNo != null && langCodeColNo == i && !configuredLanguages.contains(columns[i])) {
						auditUtil.setAuditRequestDto(EventEnum
								.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
						throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
								"Line:" + lineCount + " >> Invalid language");
					}
					tempRowBuilder.append(columns[i].trim());
				}

				String tempRow = tempRowBuilder.toString();
				if (lineList.contains(tempRow)) {
					auditUtil.setAuditRequestDto(EventEnum
							.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
					throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(),
							"Line:" + lineCount + " >> Found duplicate record");
				}
				lineList.add(tempRow);
			}

			if(lineList.isEmpty()) {
				auditUtil.setAuditRequestDto(EventEnum
						.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE, csvFileName));
				throw new RequestException(BulkUploadErrorCode.EMPTY_FILE.getErrorCode(),
						BulkUploadErrorCode.EMPTY_FILE.getErrorMessage());
			}

		} catch (IOException | SecurityException e) {
			lineCount++;
			logger.error("Failed to parse row : {}", lineCount, e);
			String reason = "Line:" + lineCount + " >> Failed to parse row, "+ e.getMessage();
			auditUtil.setAuditRequestDto(
					EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_OPERATION_INVALID_CSV_FILE, reason));
			throw new RequestException(BulkUploadErrorCode.INVALID_ARGUMENT.getErrorCode(), reason);
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
			} catch (DateTimeParseException e) {
				return false;
			}
		}
		if (LocalTime.class.getName().equals(fieldType)) {
			try {
				LocalTime.parse(value, DateTimeFormatter.ISO_TIME);

			} catch (DateTimeParseException e) {
				return false;
			}
		}
		if (LocalDate.class.getName().equals(fieldType)) {
			try {

				LocalDate.parse(value, DateTimeFormatter.ISO_DATE);

			} catch (DateTimeParseException e) {
				return false;
			}
		}
		if (Long.class.getName().equals(fieldType)) {
			try {
				Long.parseLong(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (long.class.getName().equals(fieldType)) {
			try {
				Long.parseLong(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (Integer.class.getName().equals(fieldType)) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (int.class.getName().equals(fieldType)) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (Float.class.getName().equals(fieldType)) {
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (float.class.getName().equals(fieldType)) {
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (Double.class.getName().equals(fieldType)) {
			try {
				Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (double.class.getName().equals(fieldType)) {
			try {
				Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (Boolean.class.getName().equals(fieldType)) {
			if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
				return false;
			}
		}
		if (boolean.class.getName().equals(fieldType)) {
			if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
				return false;
			}
		}
		if (Short.class.getName().equals(fieldType)) {
			try {
				Short.valueOf(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (short.class.getName().equals(fieldType)) {
			try {
				Short.valueOf(value);
			} catch (NumberFormatException e) {
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
