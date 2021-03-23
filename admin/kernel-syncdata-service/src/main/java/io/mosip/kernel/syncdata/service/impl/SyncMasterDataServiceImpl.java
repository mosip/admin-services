package io.mosip.kernel.syncdata.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.keymanagerservice.entity.CACertificateStore;
import io.mosip.kernel.keymanagerservice.repository.CACertificateStoreRepository;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.entity.AppDetail;
import io.mosip.kernel.syncdata.entity.LocationHierarchy;
import io.mosip.kernel.syncdata.exception.*;
import io.mosip.kernel.syncdata.repository.AppDetailRepository;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.service.helper.LocationHierarchyHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.service.helper.ApplicationDataHelper;
import io.mosip.kernel.syncdata.service.helper.DeviceDataHelper;
import io.mosip.kernel.syncdata.service.helper.DocumentDataHelper;
import io.mosip.kernel.syncdata.service.helper.HistoryDataHelper;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import io.mosip.kernel.syncdata.service.helper.IndividualDataHelper;
import io.mosip.kernel.syncdata.service.helper.MachineDataHelper;
import io.mosip.kernel.syncdata.service.helper.MiscellaneousDataHelper;
import io.mosip.kernel.syncdata.service.helper.RegistrationCenterDataHelper;
import io.mosip.kernel.syncdata.service.helper.TemplateDataHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Masterdata sync handler service impl
 * 
 * @author Abhishek Kumar
 * @author Bal Vikash Sharma
 * @author Srinivasan
 * @author Urvil Joshi
 * @since 1.0.0
 */
@Service
public class SyncMasterDataServiceImpl implements SyncMasterDataService {
	
	private Logger logger = LoggerFactory.getLogger(SyncMasterDataServiceImpl.class);

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@Autowired
	private MachineRepository machineRepo;
	
	@Autowired
	private MapperUtils mapper;
	
	@Autowired
	private IdentitySchemaHelper identitySchemaHelper;

	@Autowired
	private KeymanagerHelper keymanagerHelper;

	@Value("${mosip.kernel.syncdata-service-machine-url}")
	private String machineUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CACertificateStoreRepository caCertificateStoreRepository;

	@Autowired
	private AppDetailRepository appDetailRepository;


	@Override
	public SyncDataResponseDto syncClientSettings(String regCenterId, String keyIndex,
			LocalDateTime lastUpdated, LocalDateTime currentTimestamp) 
					throws InterruptedException, ExecutionException {

		logger.info("syncClientSettings invoked for timespan from {} to {}", lastUpdated, currentTimestamp);
				
		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex);
		
		String machineId = regCenterMachineDto.getMachineId();
		String registrationCenterId = regCenterMachineDto.getRegCenterId();

		SyncDataResponseDto response = new SyncDataResponseDto();
		
		List<CompletableFuture> futures = new ArrayList<CompletableFuture>();
		
		ApplicationDataHelper applicationDataHelper = new ApplicationDataHelper(lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		applicationDataHelper.retrieveData(serviceHelper, futures);		
		
		MachineDataHelper machineDataHelper = new MachineDataHelper(registrationCenterId, lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		machineDataHelper.retrieveData(serviceHelper, futures);
		
		LocationHierarchyHelper locationHierarchyHelper = new LocationHierarchyHelper(lastUpdated, currentTimestamp,regCenterMachineDto.getPublicKey());
		locationHierarchyHelper.retrieveData(serviceHelper, futures);
		
		//DeviceDataHelper deviceDataHelper = new DeviceDataHelper(registrationCenterId, lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		//deviceDataHelper.retrieveData(serviceHelper, futures);
		
		IndividualDataHelper individualDataHelper = new IndividualDataHelper(lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		individualDataHelper.retrieveData(serviceHelper, futures);
		
		RegistrationCenterDataHelper RegistrationCenterDataHelper = new RegistrationCenterDataHelper(registrationCenterId, machineId, 
				lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		RegistrationCenterDataHelper.retrieveData(serviceHelper, futures);

		AppDetail appDetail = appDetailRepository.findByNameAndLangCode("Registration Client", "eng");
		TemplateDataHelper templateDataHelper = new TemplateDataHelper(lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey(),
				appDetail != null ? appDetail.getId() : "10003");
		templateDataHelper.retrieveData(serviceHelper, futures);
		
		DocumentDataHelper documentDataHelper = new DocumentDataHelper(lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		documentDataHelper.retrieveData(serviceHelper, futures);
		
		//HistoryDataHelper historyDataHelper = new HistoryDataHelper(registrationCenterId, lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		//historyDataHelper.retrieveData(serviceHelper, futures);
		
		MiscellaneousDataHelper miscellaneousDataHelper = new MiscellaneousDataHelper(machineId, lastUpdated, currentTimestamp, regCenterMachineDto.getPublicKey());
		miscellaneousDataHelper.retrieveData(serviceHelper, futures);		
		
		CompletableFuture array [] = new CompletableFuture[futures.size()];
		CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(array));		

		try {
			future.join();
		} catch (CompletionException e) {
			if (e.getCause() instanceof SyncDataServiceException) {
				throw (SyncDataServiceException) e.getCause();
			} else {
				throw (RuntimeException) e.getCause();
			}
		}
		
		List<SyncDataBaseDto> list = new ArrayList<SyncDataBaseDto>();		
		applicationDataHelper.fillRetrievedData(serviceHelper, list);
		machineDataHelper.fillRetrievedData(serviceHelper, list);
		locationHierarchyHelper.fillRetrievedData(serviceHelper, list);
		//deviceDataHelper.fillRetrievedData(serviceHelper, list);
		individualDataHelper.fillRetrievedData(serviceHelper, list);
		RegistrationCenterDataHelper.fillRetrievedData(serviceHelper, list);
		templateDataHelper.fillRetrievedData(serviceHelper, list);
		documentDataHelper.fillRetrievedData(serviceHelper, list);
		//historyDataHelper.fillRetrievedData(serviceHelper, list);
		miscellaneousDataHelper.fillRetrievedData(serviceHelper, list);
		
		//Fills dynamic field data
		identitySchemaHelper.fillRetrievedData(list, regCenterMachineDto.getPublicKey(), lastUpdated);
		
		response.setDataToSync(list);
		return response;
	}
	
	@Override
	public UploadPublicKeyResponseDto validateKeyMachineMapping(UploadPublicKeyRequestDto dto) {
		List<Machine> machines = machineRepo.findByMachineNameAndIsActive(dto.getMachineName());
		
		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());
		
		if(machines.get(0).getPublicKey() == null || machines.get(0).getPublicKey().length() == 0)
			throw new RequestException(MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
					MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
		
		if(Arrays.equals(CryptoUtil.decodeBase64(dto.getPublicKey()), 
				CryptoUtil.decodeBase64(machines.get(0).getPublicKey()))) {
			return new UploadPublicKeyResponseDto(machines.get(0).getKeyIndex());
		}
		
		throw new RequestException(MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
				MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
	}
	
	@Override
	public IdSchemaDto getLatestPublishedIdSchema(LocalDateTime lastUpdated, double schemaVersion) {
		return identitySchemaHelper.getLatestIdentitySchema(lastUpdated, schemaVersion);		
	}

	@Override
	public KeyPairGenerateResponseDto getCertificate(String applicationId, Optional<String> referenceId) {
		return keymanagerHelper.getCertificate(applicationId, referenceId);
	}

	@Override
	public ClientPublicKeyResponseDto getClientPublicKey(String machineId) {
		MachineResponseDto machineResponseDto = getMachineById(machineId);
		List<MachineDto> machines = machineResponseDto.getMachines();

		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		return new ClientPublicKeyResponseDto(machines.get(0).getSignPublicKey(), machines.get(0).getPublicKey());
	}

	@Override
	public CACertificates getPartnerCACertificates(LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		CACertificates caCertificates = new CACertificates();
		caCertificates.setCertificateDTOList(new ArrayList<CACertificateDTO>());

		List<CACertificateStore> certs = caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimestamp);
		if(certs == null)
			return caCertificates;

		for(CACertificateStore caCertificateStore : certs) {
			CACertificateDTO caCertificateDTO = new CACertificateDTO();
			BeanUtils.copyProperties(caCertificateStore, caCertificateDTO);
			caCertificates.getCertificateDTOList().add(caCertificateDTO);
		}
		return caCertificates;
	}

	private MachineResponseDto getMachineById(String machineId) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, machineId));
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

			objectMapper.registerModule(new JavaTimeModule());
			ResponseWrapper<MachineResponseDto> resp = objectMapper.readValue(responseEntity.getBody(),
					new TypeReference<ResponseWrapper<MachineResponseDto>>() {});

			if(resp.getErrors() != null && !resp.getErrors().isEmpty())
				throw new SyncInvalidArgumentException(resp.getErrors());

			return resp.getResponse();
		} catch (Exception e) {
			throw new SyncDataServiceException(MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage(),
					MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage() + " : " +
							ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}

}
