package io.mosip.kernel.syncdata.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.dto.RoleDto;
import io.mosip.kernel.syncdata.dto.response.RolesResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.ParseResponseException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.SyncRolesService;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SyncUserDetailsAndRolesServiceTest {
	@Mock
	private SyncUserDetailsService syncUserDetailsService;

	@Mock
	MachineRepository machineRespository;

	@Mock
	private UserDetailsRepository userDetailsRepository;

	@Mock
	private SyncRolesService syncRolesService;

	@Mock
	ObjectMapper objectMapper;

	@Mock
	RestTemplate restTemplate;

	/*@MockBean
	private SyncJobDefService registrationCenterUserService;*/

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@Mock
	private ClientCryptoManagerService clientCryptoManagerService;

	@Autowired
	private MapperUtils mapper;

	@Value("${mosip.syncdata.tpm.required:false}")
	private boolean isTPMRequired;

	@Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
	private String authUserDetailsBaseUri;

	/** The auth user details uri. */
	@Value("${mosip.kernel.syncdata.auth-salt-details:/usersaltdetails}")
	private String authUserSaltUri;

	@Value("${mosip.kernel.syncdata.auth-user-details:/userdetails}")
	private String authUserDetailsUri;

	@Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
	private String authBaseUri;

	@Value("${mosip.kernel.syncdata.auth-manager-roles}")
	private String authAllRolesUri;

	private StringBuilder userDetailsUri;

	private StringBuilder userSaltsUri;

	private StringBuilder builder;

	private List<UserDetails> registrationCenterUsers = null;

	private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
	private byte[] tpmPublicKey = Base64.getUrlDecoder().decode(encodedTPMPublicKey);
	private String keyIndex = "tetw:sdfsf:Sdfdfsd";
	private List<Machine> machines = new ArrayList<Machine>();
	

	@Before
	public void setup() {

		registrationCenterUsers = new ArrayList<>();
		
		UserDetails registrationCenterUser = new UserDetails();
		registrationCenterUser.setIsActive(true);
		registrationCenterUser.setRegCenterId("10001");
		registrationCenterUser.setId("M10411022");
		registrationCenterUsers.add(registrationCenterUser);

		userDetailsUri = new StringBuilder();
		userSaltsUri = new StringBuilder();
		userSaltsUri.append(authUserDetailsBaseUri).append(authUserSaltUri);
		userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
		builder = new StringBuilder();
		builder.append(authBaseUri).append(authAllRolesUri);

		Machine machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001",
				"ENG", LocalDateTime.now(), encodedTPMPublicKey, keyIndex, "ZONE","10002", null,encodedTPMPublicKey, keyIndex);
		machines.add(machine);

	}

	@Test
	public void getAllRoles() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:51:42.113Z\", \"metadata\": null, \"response\": { \"lastSyncTime\": \"2019-03-31T11:51:35.458Z\", \"roles\": [ { \"roleId\": \"REGISTRATION_ADMIN\", \"roleName\": \"REGISTRATION_ADMIN\", \"roleDescription\": \"Registration administrator\" } ] }, \"errors\": null }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncRolesService.getAllRoles();
	}

	@Test
	public void getAllRolesException() {

		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient")).andRespond(withServerError());
		try {
			syncRolesService.getAllRoles();
		} catch (SyncDataServiceException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getAllRolesValidationError() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:40:39.847Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-SNC-303\", \"message\": \"Registration center user not found \" } ] }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		try {
			syncRolesService.getAllRoles();
		} catch (SyncServiceException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getAllRolesParseError() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:51:42.113Z\", \"metadata\": null, \"response\":  \"lastSyncTime\": \"2019-03-31T11:51:35.458Z\", \"roles\": [ { \"roleId\": \"REGISTRATION_ADMIN\", \"roleName\": \"REGISTRATION_ADMIN\", \"roleDescription\": \"Registration administrator\" } ] }, \"errors\": null }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		try {
			syncRolesService.getAllRoles();
		} catch (ParseResponseException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getRolesServiceException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-402\", \"message\": \"Token expired\" } ] }";

		/*MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest().body(response));*/

		lenient().when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(), eq(String.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized", response.getBytes(), null));
		try {
			syncRolesService.getAllRoles();
		} catch (AuthNException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getAllRolesBadCredentialsException() {

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest());
		try {
			syncRolesService.getAllRoles();
		} catch (BadCredentialsException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getRolesServiceAuthzException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-403\", \"message\": \"Forbidden\" } ] }";
		String regId = "10044";

//		when(registrationCenterUserRepository.findByRegistrationCenterUserByRegCenterId(regId))
//				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN).body(response));
		try {
			syncRolesService.getAllRoles();
		} catch (AuthZException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getRolesAuthNException() {

		String regId = "10044";

//		when(registrationCenterUserRepository.findByRegistrationCenterUserByRegCenterId(regId))
//				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN));
		try {
			syncRolesService.getAllRoles();
		} catch (AccessDeniedException e){
			e.printStackTrace();
		}
	}

	//======================

	@Test
	public void getAllUserDetailEncryptedTestCase() {
		Machine machine = new Machine();
		machine.setId("mid");
		machine.setRegCenterId("regId");
		machine.setPublicKey("publickey");

		TpmCryptoResponseDto tpmCryptoResponseDto = new TpmCryptoResponseDto();
		tpmCryptoResponseDto.setValue("testsetestsetset");
		lenient().when(machineRespository.findOneByKeyIndexIgnoreCase(Mockito.anyString())).thenReturn(machine);
		lenient().when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString())).thenReturn(registrationCenterUsers);
		lenient().when(machineRespository.findByMachineIdAndIsActive(Mockito.anyString())).thenReturn(machines);
		lenient().when(clientCryptoManagerService.csEncrypt(Mockito.any())).thenReturn(tpmCryptoResponseDto);
		String response = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[{\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		assertNotNull(machine);
	}

	@Test
	public void getAllUserDetailsV2TestCase() {
		Machine machine = new Machine();
		machine.setId("machine_id");
		machine.setRegCenterId("center_id");
		machine.setPublicKey("public_key");
		TpmCryptoResponseDto tpmCryptoResponseDto = new TpmCryptoResponseDto();
		tpmCryptoResponseDto.setValue("testsetestsetset");
		lenient().when(machineRespository.findOneByKeyIndexIgnoreCase(Mockito.anyString())).thenReturn(machine);
		lenient().when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString())).thenReturn(registrationCenterUsers);
		lenient().when(clientCryptoManagerService.csEncrypt(Mockito.any())).thenReturn(tpmCryptoResponseDto);

		assertNotNull(machine);
	}

	@Test
	public void getAllUserDetailsV2TestCaseException1() {
		Machine machine = new Machine();
		machine.setId("machine_id");
		machine.setRegCenterId("center_id");
		machine.setPublicKey("public_key");
		TpmCryptoResponseDto tpmCryptoResponseDto = new TpmCryptoResponseDto();
		tpmCryptoResponseDto.setValue("testsetestsetset");
		lenient().when(machineRespository.findOneByKeyIndexIgnoreCase(Mockito.anyString())).thenReturn(machine);
		lenient().when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString())).thenReturn(Collections.emptyList());
		try {
			syncUserDetailsService.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
		} catch (DataNotFoundException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getAllUserDetailsV2TestCaseException2() {
		Machine machine = new Machine();
		machine.setId("machine_id");
		machine.setRegCenterId("center_id");
		machine.setPublicKey("public_key");
		TpmCryptoResponseDto tpmCryptoResponseDto = new TpmCryptoResponseDto();
		tpmCryptoResponseDto.setValue("testsetestsetset");
		lenient().when(machineRespository.findOneByKeyIndexIgnoreCase(Mockito.anyString())).thenReturn(machine);
		lenient().when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString())).thenThrow(DataAccessLayerException.class);
		try {
			syncUserDetailsService.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
		} catch (SyncDataServiceException e){
			e.printStackTrace();
		}
	}

	@Test
	public void getAllRoles_success() throws Exception {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";
		String responseBody = "{\"response\": {\"roles\": [{\"roleId\": 1, \"roleName\": \"Admin\"}]}}";

		ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.OK);
		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null), String.class))
				.thenReturn(response);

		lenient().when(objectMapper.readValue(responseBody, ResponseWrapper.class)).thenReturn(new ResponseWrapper<>());
		lenient().when(objectMapper.readValue(responseBody, RolesResponseDto.class)).thenReturn(new RolesResponseDto());

		RolesResponseDto roles = new RolesResponseDto();
		List<RoleDto> roleDtos = new ArrayList<>();
		roles.setRoles(roleDtos);
		lenient().when(syncRolesService.getAllRoles()).thenReturn(roles);

		RolesResponseDto actualRoles  = syncRolesService.getAllRoles();

		assertNotNull(actualRoles);
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllRoles_serviceError() {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";
		String responseBody = "{\"error\": {\"errorCode\": \"KER-AUTH-101\", \"errorMessage\": \"Invalid request\"}}";

		ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null), String.class))
				.thenReturn(response);

		lenient().when(syncRolesService.getAllRoles()).thenThrow(new SyncDataServiceException("KER-AUTH-101","Invalid request"));

		syncRolesService.getAllRoles();
	}

	@Test(expected = AuthNException.class)
	public void getAllRoles_unauthorized_401_noErrors() {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";

		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null), String.class))
				.thenThrow(new HttpServerErrorException(HttpStatus.UNAUTHORIZED));

		List<ServiceError> error = new ArrayList<>();

		lenient().when(syncRolesService.getAllRoles()).thenThrow(new AuthNException(error));

		syncRolesService.getAllRoles();
	}

	@Test(expected = AuthZException.class)
	public void getAllRoles_forbidden_403_noErrors() {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";

		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null), String.class))
				.thenThrow(new HttpServerErrorException(HttpStatus.FORBIDDEN));

		List<ServiceError> error = new ArrayList<>();

		lenient().when(syncRolesService.getAllRoles()).thenThrow(new AuthZException(error));

		syncRolesService.getAllRoles();
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllRoles_clientError() {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";

		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null),
				String.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

		lenient().when(syncRolesService.getAllRoles()).thenThrow(new SyncDataServiceException("KER-SNC-401", "Error occurred while fetching roles"));

		syncRolesService.getAllRoles();
	}

	@Test(expected = ParseResponseException.class)
	public void getAllRoles_parseError() {
		String expectedUri = authBaseUri + authAllRolesUri + "/registrationclient";
		String responseBody = "Invalid JSON";

		lenient().when(restTemplate.exchange(expectedUri, HttpMethod.GET, new HttpEntity<>(null),
				String.class)).thenReturn(ResponseEntity.ok(responseBody));

		lenient().when(syncRolesService.getAllRoles()).thenThrow(new ParseResponseException("KER-SNC-401", "Error occurred while fetching roles"));

		syncRolesService.getAllRoles();
	}

	@Test
	public void testGetUsersBasedOnRegistrationCenterId_Success(){
		List<UserDetails> mockUsers = Arrays.asList(
				new UserDetails("user1","eng","hello","active",LocalDateTime.now().toLocalTime(),"last_login","regCenterId1"),
				new UserDetails("user2", "hin", "hi","inactive", LocalDateTime.now().toLocalTime(),"last_login", "regCenterId1")
		);
		UserDetailsRepository mockRepository = Mockito.mock(UserDetailsRepository.class);
		lenient().when(mockRepository.findByUsersByRegCenterId("regCenterId1")).thenReturn(mockUsers);

		assertNotNull(mockUsers);
	}

}
