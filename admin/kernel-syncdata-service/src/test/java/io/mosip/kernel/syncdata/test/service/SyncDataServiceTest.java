package io.mosip.kernel.syncdata.test.service;

import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.keymanagerservice.entity.CACertificateStore;
import io.mosip.kernel.keymanagerservice.repository.CACertificateStoreRepository;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.ClientPublicKeyResponseDto;
import io.mosip.kernel.syncdata.dto.response.MasterDataResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncConfigDetailsService;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.service.SyncRolesService;
import io.mosip.kernel.syncdata.utils.LocalDateTimeUtil;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SyncDataServiceTest {

	@MockBean
	private SyncMasterDataServiceHelper masterDataServiceHelper;

	@Mock
	RestTemplate restTemplate;

	//@MockBean
	//private SyncJobDefService registrationCenterUserService;

	@Mock
	MachineRepository machineRespository;

	@Mock
	private SyncRolesService syncRolesService;

	@Mock
	private CryptomanagerUtils cryptomanagerUtils;

	/**
	 * Environment instance
	 */
	@Mock
	private Environment env;

	/**
	 * file name referred from the properties file
	 */
	@Value("${mosip.kernel.syncdata.registration-center-config-file}")
	private String regCenterfileName;

	/**
	 * file name referred from the properties file
	 */
	@Value("${mosip.kernel.syncdata.global-config-file}")
	private String globalConfigFileName;

	@Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
	private String authUserDetailsBaseUri;

	@Value("${mosip.kernel.syncdata.auth-user-details:/userdetails}")
	private String authUserDetailsUri;

	@Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
	private String authBaseUri;

	@Value("${mosip.kernel.syncdata.auth-manager-roles}")
	private String authAllRolesUri;

	@Value("${mosip.kernel.keymanager-service-publickey-url}")
	private String publicKeyUrl;

	private String configServerUri = null;
	private String configLabel = null;
	private String configProfile = null;
	private String configAppName = null;

	private StringBuilder uriBuilder;

	StringBuilder userDetailsUri;

	private StringBuilder builder;

	@Mock
	private SyncConfigDetailsService syncConfigDetailsService;
	private MasterDataResponseDto masterDataResponseDto;
	private List<ApplicationDto> applications;
	List<HolidayDto> holidays;
	List<MachineDto> machines;
	List<MachineSpecificationDto> machineSpecifications;
	List<MachineTypeDto> machineTypes;
	Map<String, String> uriParams = null;

	JSONObject globalConfigMap = null;
	JSONObject regCentreConfigMap = null;
	
	@Mock
	private SyncMasterDataService masterDataService;

	@Mock
	private CACertificateStoreRepository caCertificateStoreRepository;

	@Mock
	LocalDateTimeUtil localDateTimeUtil;

	@Value("${mosip.kernel.syncdata-service-machine-url}")
	private String machineUrl;


	private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
	//private byte[] tpmPublicKey = cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey);


	@Before
	public void setup() {
		masterDataSyncSetup();
		configDetialsSyncSetup();
		userDetailsUri = new StringBuilder();
		userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);

	}

	public void masterDataSyncSetup() {
		masterDataResponseDto = new MasterDataResponseDto();
		applications = new ArrayList<>();
		applications.add(new ApplicationDto("01", "REG FORM", "REG Form"));
		masterDataResponseDto.setApplications(applications);
		holidays = new ArrayList<>();
		holidays.add(new HolidayDto("1", "2018-01-01", "01", "01", "2018", "NEW YEAR", "LOC01"));
		masterDataResponseDto.setHolidays(holidays);
		machines = new ArrayList<>();
		machines.add(new MachineDto("1001", "Laptop", "QWE23456", "1223:23:31:23", "172.12.128.1", "1",
				LocalDateTime.parse("2018-01-01T01:01:01"), null, null, null, "test"));
		masterDataResponseDto.setMachineDetails(machines);
		machineSpecifications = new ArrayList<>();
		machineSpecifications
				.add(new MachineSpecificationDto("1", "lenovo Thinkpad", "Lenovo", "T480", "1", "1.0.1", "Thinkpad"));
		masterDataResponseDto.setMachineSpecification(machineSpecifications);
		machineTypes = new ArrayList<>();
		machineTypes.add(new MachineTypeDto("1", "Laptop", "Laptop"));
		masterDataResponseDto.setMachineType(machineTypes);
	}

	public void configDetialsSyncSetup() {
		globalConfigMap = new JSONObject();
		globalConfigMap.put("archivalPolicy", "arc_policy_2");
		globalConfigMap.put("otpTimeOutInMinutes", 2);
		globalConfigMap.put("numberOfWrongAttemptsForOtp", 5);
		globalConfigMap.put("uinLength", 24);

		regCentreConfigMap = new JSONObject();

		regCentreConfigMap.put("fingerprintQualityThreshold", 120);
		regCentreConfigMap.put("irisQualityThreshold", 25);
		regCentreConfigMap.put("irisRetryAttempts", 10);
		regCentreConfigMap.put("faceQualityThreshold", 25);
		regCentreConfigMap.put("faceRetry", 12);
		regCentreConfigMap.put("supervisorVerificationRequiredForExceptions", true);
		regCentreConfigMap.put("operatorRegSubmissionMode", "fingerprint");
		configServerUri = env.getProperty("spring.cloud.config.uri");
		configLabel = env.getProperty("spring.cloud.config.label");
		configProfile = env.getProperty("spring.profiles.active");
		configAppName = env.getProperty("spring.application.name");
		uriBuilder = new StringBuilder();
		uriBuilder.append("/" + configServerUri + "/").append(configAppName + "/").append(configProfile + "/")
				.append(configLabel + "/");

		builder = new StringBuilder();
		builder.append(authBaseUri).append(authAllRolesUri);

	}

	/*
	 * @Test(expected = SyncDataServiceException.class) public void
	 * syncDataFailure() throws InterruptedException, ExecutionException { Machine
	 * machine = new Machine(); machine.setId("10001"); machine.setLangCode("eng");
	 * List<Machine> machines = new ArrayList<>(); machines.add(machine);
	 * when(machineRespository.findByMachineIdAndIsActive(Mockito.anyString())).
	 * thenReturn(machines);
	 * when(masterDataServiceHelper.getMachines(Mockito.anyString(), Mockito.any(),
	 * Mockito.any())) .thenThrow(SyncDataServiceException.class);
	 * masterDataService.syncData("1001", null, null);
	 * 
	 * }
	 */

	/*@Ignore
	@Test
	public void globalConfigsyncSuccess() {
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(globalConfigFileName).toString())).andRespond(withSuccess());
		syncConfigDetailsService.getGlobalConfigDetails();
	}

	@Ignore
	@Test
	public void registrationConfigsyncSuccess() {
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(regCenterfileName).toString())).andRespond(withSuccess());
		syncConfigDetailsService.getRegistrationCenterConfigDetails("1");
		// Assert.assertEquals(120, jsonObject.get("fingerprintQualityThreshold"));
	}

	@Test(expected = SyncDataServiceException.class)
	public void registrationConfigsyncFailure() {

		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(regCenterfileName).toString())).andRespond(withBadRequest());
		syncConfigDetailsService.getRegistrationCenterConfigDetails("1");
	}

	@Test(expected = SyncDataServiceException.class)
	public void globalConfigsyncFailure() {

		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(globalConfigFileName).toString())).andRespond(withBadRequest());
		syncConfigDetailsService.getGlobalConfigDetails();
	}

	@Test(expected = SyncDataServiceException.class)
	public void globalConfigsyncFileNameNullFailure() {

		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(globalConfigFileName).toString())).andRespond(withBadRequest());
		syncConfigDetailsService.getGlobalConfigDetails();
	}

	@Ignore
	@Test
	public void getConfigurationSuccess() {
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(requestTo(uriBuilder.append(globalConfigFileName).toString())).andRespond(withSuccess());
		uriBuilder = new StringBuilder();
		uriBuilder.append(configServerUri + "/").append(configAppName + "/").append(configProfile + "/")
				.append(configLabel + "/");
		server.expect(requestTo(uriBuilder.append(regCenterfileName).toString())).andRespond(withSuccess());
		syncConfigDetailsService.getConfiguration("1");
	}*/

	// ------------------------------------------UserDetails--------------------------//
	/*
	 * // @Test public void getAllUserDetail() { String response =
	 * "{ \"mosipUserDtoList\": [ { \"userName\": \"individual\", \"mail\": \"individual@mosip.io\", \"mobile\": \"8976394859\", \"langCode\": null, \"userPassword\": \"e1NTSEE1MTJ9TkhVb1c2WHpkZVJCa0drbU9tTk9ZcElvdUlNRGl5ODlJK3RhNm04d0FlTWhMSEoyTG4wSVJkNEJ2dkNqVFg4bTBuV2ZySStneXBTVittbVJKWnAxTkFwT3BWY3MxTVU5\", \"name\": \"individual\", \"role\": \"REGISTRATION_ADMIN,INDIVIDUAL\"  } ] }"
	 * ; String regId = "10044"; RegistrationCenterUserResponseDto
	 * registrationCenterUserResponseDto = new RegistrationCenterUserResponseDto();
	 * List<RegistrationCenterUserDto> registrationCenterUserDtos = new
	 * ArrayList<>(); RegistrationCenterUserDto registrationCenterUserDto = new
	 * RegistrationCenterUserDto(); registrationCenterUserDto.setIsActive(true);
	 * registrationCenterUserDto.setRegCenterId(regId);
	 * registrationCenterUserDto.setUserId("M10411022");
	 * registrationCenterUserDtos.add(registrationCenterUserDto);
	 * registrationCenterUserResponseDto.setRegistrationCenterUsers(
	 * registrationCenterUserDtos);
	 * 
	 * when(registrationCenterUserService.getUsersBasedOnRegistrationCenterId(regId)
	 * ) .thenReturn(registrationCenterUserResponseDto);
	 * 
	 * MockRestServiceServer mockRestServiceServer =
	 * MockRestServiceServer.bindTo(restTemplate).build();
	 * mockRestServiceServer.expect(requestTo(userDetailsUri.toString() +
	 * "/registrationclient"))
	 * .andRespond(withSuccess().body(response).contentType(MediaType.
	 * APPLICATION_JSON)); syncUserDetailsService.getAllUserDetail(regId); }
	 */

	/*
	 * // @Test(expected = SyncDataServiceException.class) public void
	 * getAllUserDetailExcp() { String response =
	 * "{ \"userDetails\": [ { \"userName\": \"individual\", \"mail\": \"individual@mosip.io\", \"mobile\": \"8976394859\", \"langCode\": null, \"userPassword\": \"e1NTSEE1MTJ9TkhVb1c2WHpkZVJCa0drbU9tTk9ZcElvdUlNRGl5ODlJK3RhNm04d0FlTWhMSEoyTG4wSVJkNEJ2dkNqVFg4bTBuV2ZySStneXBTVittbVJKWnAxTkFwT3BWY3MxTVU5\", \"name\": \"individual\", \"roles\": [ \"REGISTRATION_ADMIN\", \"INDIVIDUAL\" ] } ] }"
	 * ; String regId = "10044"; RegistrationCenterUserResponseDto
	 * registrationCenterUserResponseDto = new RegistrationCenterUserResponseDto();
	 * List<RegistrationCenterUserDto> registrationCenterUserDtos = new
	 * ArrayList<>(); RegistrationCenterUserDto registrationCenterUserDto = new
	 * RegistrationCenterUserDto(); registrationCenterUserDto.setIsActive(true);
	 * registrationCenterUserDto.setRegCenterId(regId);
	 * registrationCenterUserDto.setUserId("M10411022");
	 * registrationCenterUserDtos.add(registrationCenterUserDto);
	 * registrationCenterUserResponseDto.setRegistrationCenterUsers(
	 * registrationCenterUserDtos);
	 * 
	 * when(registrationCenterUserService.getUsersBasedOnRegistrationCenterId(regId)
	 * ) .thenReturn(registrationCenterUserResponseDto);
	 * 
	 * MockRestServiceServer mockRestServiceServer =
	 * MockRestServiceServer.bindTo(restTemplate).build();
	 * mockRestServiceServer.expect(requestTo(userDetailsUri.toString() +
	 * "/registrationclient"))
	 * .andRespond(withServerError().body(response).contentType(MediaType.
	 * APPLICATION_JSON)); syncUserDetailsService.getAllUserDetail(regId); }
	 */
	/*
	 * // @Test public void getAllUserDetailNoDetail() { // String response =
	 * "{ \"userDetails\": [] }"; String regId = "10044";
	 * RegistrationCenterUserResponseDto registrationCenterUserResponseDto = new
	 * RegistrationCenterUserResponseDto(); List<RegistrationCenterUserDto>
	 * registrationCenterUserDtos = new ArrayList<>(); RegistrationCenterUserDto
	 * registrationCenterUserDto = new RegistrationCenterUserDto();
	 * registrationCenterUserDto.setIsActive(true);
	 * registrationCenterUserDto.setRegCenterId(regId);
	 * registrationCenterUserDto.setUserId("M10411022");
	 * registrationCenterUserDtos.add(registrationCenterUserDto);
	 * registrationCenterUserResponseDto.setRegistrationCenterUsers(
	 * registrationCenterUserDtos);
	 * 
	 * when(registrationCenterUserService.getUsersBasedOnRegistrationCenterId(regId)
	 * ) .thenReturn(registrationCenterUserResponseDto);
	 * 
	 * MockRestServiceServer mockRestServiceServer =
	 * MockRestServiceServer.bindTo(restTemplate).build();
	 * mockRestServiceServer.expect(requestTo(userDetailsUri.toString() +
	 * "/registrationclient")) .andRespond(withSuccess());
	 * assertNull(syncUserDetailsService.getAllUserDetail(regId)); }
	 */

	// ------------------------------------------AllRolesSync--------------------------//

	@Test
	public void getAllRoles() {

		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient")).andRespond(withSuccess());
		syncRolesService.getAllRoles();
	}

	@Test
	public void getAllRolesException() {

		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient")).andRespond(withServerError());
		syncRolesService.getAllRoles();
	}

	// -----------------------------------------publicKey-----------------------//

	@Test
	public void getPublicKey() {

		try {
			uriParams = new HashMap<>();
			uriParams.put("applicationId", "REGISTRATION");

			// Query parameters
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
					// Add query parameter
					.queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
			MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

			mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
					.andRespond(withSuccess().body(
							"{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, \"response\": { \"lastSyncTime\": \"2019-04-24T09:07:41.771Z\", \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtzi3nWiNMEcaBV2cWO5ZLTBZe1TEGnT95bTvrpEEr-kJLrn80dn9k156zjQpjSzNfEOFVwugTEhEWdxrdrjDUACpA0cF4tUdAM5XJBB0xmzNGS5s7lmcliAOjXbCGU2VJwOUnYV4DSCgrReMCCe6LD_aApwu45OAZ9_sWG6R-jlIUOHLTdDUs6O8zLk8zl7tOX6Rlp25Zk9CLQw1m9drHJqxCbr9Wc9PQKUHBPqhtvCe9ZZeySsZb83dXpKKAZlkjdbrB25i_4O0pbv9LHk0qQlk0twqaef6D5nCTqcB5KQ4QqVYLcrtAhdbMXaDvpSf9syRQ3P3fAeiGkvUIhUWPwIDAQAB\", \"issuedAt\": \"2019-04-23T06:17:46.753\", \"expiryAt\": \"2020-04-23T06:17:46.753\" }, \"errors\": null }"));

			PublicKeyResponse<String> publicKeyResponse = syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");

			assertNotNull(publicKeyResponse.getProfile());
			assertEquals("test", publicKeyResponse.getProfile());
		} catch (Exception ex){
			ex.getCause();
		}
	}

	@Test
	public void getPublicKeyServiceExceptionTest() {

		try {
			uriParams = new HashMap<>();
			uriParams.put("applicationId", "REGISTRATION");

			// Query parameters
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
					// Add query parameter
					.queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
			MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

			mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
					.andRespond(withServerError());

			syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");
		} catch (Exception e){
			e.getCause();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPublicErrorList() throws IOException {
		uriParams = new HashMap<>();
		uriParams.put("applicationId", "REGISTRATION");

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
				// Add query parameter
				.queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
				.andRespond(withSuccess().body(
						"{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T10:24:23.760Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-401\", \"message\": \"JWT expired at 2019-04-17T14:12:05+0000. Current time: 2019-04-24T10:24:23+0000\" } ] }"));

		syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPublicServiceException() throws IOException {
		uriParams = new HashMap<>();
		uriParams.put("applicationId", "REGISTRATION");

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
				// Add query parameter
				.queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
				.andRespond(withSuccess().body(
						" \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, \"response\": { \"lastSyncTime\": \"2019-04-24T09:07:41.771Z\", \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtzi3nWiNMEcaBV2cWO5ZLTBZe1TEGnT95bTvrpEEr-kJLrn80dn9k156zjQpjSzNfEOFVwugTEhEWdxrdrjDUACpA0cF4tUdAM5XJBB0xmzNGS5s7lmcliAOjXbCGU2VJwOUnYV4DSCgrReMCCe6LD_aApwu45OAZ9_sWG6R-jlIUOHLTdDUs6O8zLk8zl7tOX6Rlp25Zk9CLQw1m9drHJqxCbr9Wc9PQKUHBPqhtvCe9ZZeySsZb83dXpKKAZlkjdbrB25i_4O0pbv9LHk0qQlk0twqaef6D5nCTqcB5KQ4QqVYLcrtAhdbMXaDvpSf9syRQ3P3fAeiGkvUIhUWPwIDAQAB\", \"issuedAt\": \"2019-04-23T06:17:46.753\", \"expiryAt\": \"2020-04-23T06:17:46.753\" }, \"errors\": null }"));

		syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");
	}
	
	//machine public key mapping test cases
	@Test
	public void verifyPublicKeyMachineMappingSuccess() {
		String keyIndex = CryptoUtil.computeFingerPrint(cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey), null);
		LocalDateTime localdateTime = LocalDateTime.parse("2018-11-01T01:01:01");
		Machine machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001", "ENG", localdateTime,
				encodedTPMPublicKey, keyIndex, "ZONE","10002", null,encodedTPMPublicKey, keyIndex);
		List<Machine> machines = new ArrayList<Machine>();
		machines.add(machine);
		lenient().when(machineRespository.findByMachineName(Mockito.anyString())).thenReturn(machines);
	}
	
	//machine public key mapping test cases
	@Test
	public void verifyPublicKeyMachineMappingNoMapping() {
		try {
			lenient().when(machineRespository.findByMachineName(Mockito.anyString())).thenReturn(new ArrayList<Machine>());

			UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto("laptop", encodedTPMPublicKey, encodedTPMPublicKey);
			masterDataService.validateKeyMachineMapping(dto);
		} catch (RequestException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void verifyPublicKeyMachineMappingNoKey() {
		try {
			LocalDateTime localdateTime = LocalDateTime.parse("2018-11-01T01:01:01");
			Machine machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001", "ENG", localdateTime,
					null, null, "ZONE", "10002", null, null, null);
			List<Machine> machines = new ArrayList<Machine>();
			machines.add(machine);
			lenient().when(machineRespository.findByMachineName(Mockito.anyString())).thenReturn(machines);

			UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto("laptop", encodedTPMPublicKey, encodedTPMPublicKey);
			masterDataService.validateKeyMachineMapping(dto);
		} catch (RequestException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void verifyPublicKeyMachineMappingInvalidKey() {
		try {
			String keyIndex = CryptoUtil.computeFingerPrint(cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey), null);
			LocalDateTime localdateTime = LocalDateTime.parse("2018-11-01T01:01:01");
			Machine machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001", "ENG", localdateTime,
					encodedTPMPublicKey, keyIndex, "ZONE", "10002", null, encodedTPMPublicKey, keyIndex);
			List<Machine> machines = new ArrayList<Machine>();
			machines.add(machine);
			lenient().when(machineRespository.findByMachineName(Mockito.anyString())).thenReturn(machines);

			UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto("laptop", "invalidKey", "invalidKey");
			masterDataService.validateKeyMachineMapping(dto);
		} catch (RequestException e){
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	 public void fetchClientPublicKey() {
		Machine machine = new Machine();
		machine.setId("10001");
		machine.setLangCode("eng");
		machine.setPublicKey("test");
		machine.setSignPublicKey("test");
		List<Machine> machines = new ArrayList<>();
		machines.add(machine);

		 MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		 UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "10001"));
		 mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
				 .andRespond(withSuccess().body(
						 "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, "
								 + "\"response\": { \"machines\" : [ { \"id\": \"10001\", "
								 + "\"publicKey\": \"test\", \"signPublicKey\": \"test\"}]}, \"errors\": null }"));
		ClientPublicKeyResponseDto clientPublicKeyResponseDto = masterDataService.getClientPublicKey("10001");
		Assert.assertNotNull(clientPublicKeyResponseDto);
		Assert.assertNotNull(clientPublicKeyResponseDto.getSigningPublicKey());
		Assert.assertNotNull(clientPublicKeyResponseDto.getEncryptionPublicKey());
	 }

	@Test
	public void getPartnerCACertificatesSuccess() {
		List<CACertificateStore> cacerts = new ArrayList<CACertificateStore>();
		CACertificateStore caCertificateStore = new CACertificateStore();
		caCertificateStore.setCertId("test");
		caCertificateStore.setCertData("--- BEGIN--- sdsfsdf ---END---");
		caCertificateStore.setCreatedtimes(LocalDateTime.now());
		cacerts.add(caCertificateStore);
		lenient().when(caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(cacerts);
	}
}
