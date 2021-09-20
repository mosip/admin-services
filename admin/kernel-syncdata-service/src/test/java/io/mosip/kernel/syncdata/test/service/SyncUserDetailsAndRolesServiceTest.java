package io.mosip.kernel.syncdata.test.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.syncdata.dto.SyncUserDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.ParseResponseException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.SyncJobDefService;
import io.mosip.kernel.syncdata.service.SyncRolesService;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SyncUserDetailsAndRolesServiceTest {
	@Autowired
	private SyncUserDetailsService syncUserDetailsService;

	@MockBean
	MachineRepository machineRespository;
	@MockBean
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private SyncRolesService syncRolesService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@MockBean
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

	// ------------------------------------------UserDetails--------------------------//
	@Test
	public void getAllUserDetail() throws JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[{\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test
	public void getAllUserSaltDetail() throws JsonParseException, JsonMappingException, IOException {
		String responseSalt = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserSaltList\":[{\"userId\":\"110001\",\"salt\":\"9663175928\"}]},\"errors\":null}";

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(responseSalt).contentType(MediaType.APPLICATION_JSON));
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllUserDetailExcp() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withServerError().contentType(MediaType.APPLICATION_JSON));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllUserDetailNoDetail() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withBadRequest());
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = ParseResponseException.class)
	public void getAllUserDetailParseException() {
		String response = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = SyncServiceException.class)
	public void getAllUserDetailServiceException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:40:39.847Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-SNC-303\", \"message\": \"Registration center user not found \" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllUserSaltServiceException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:40:39.847Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-SNC-303\", \"message\": \"Registration center user not found \" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(withBadRequest().body(response));
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = AuthNException.class)
	public void getAllUserDetailServiceAuthNException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-402\", \"message\": \"Token expired\" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest().body(response));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = AuthNException.class)
	public void getAllUserDetailSaltAuthNException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-402\", \"message\": \"Token expired\" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest().body(response));
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = BadCredentialsException.class)
	public void getAllUserDetailServiceBadCredentialsException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest());
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = BadCredentialsException.class)
	public void getAllUserSaltServiceBadCredentialsException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest());
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = AuthZException.class)
	public void getAllUserDetailServiceAuthzException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-403\", \"message\": \"Forbidden\" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN).body(response));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = AuthZException.class)
	public void getUserSaltServiceAuthzException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-403\", \"message\": \"Forbidden\" } ] }";
		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN).body(response));
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = AccessDeniedException.class)
	public void getAllUserDetailServicesAuthNException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN));
		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = AccessDeniedException.class)
	public void getAllUserSaltServicesAuthNException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userSaltsUri.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN));
		syncUserDetailsService.getUserSalts(regId);
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllUserDetailServicesRegUserException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenThrow(DataRetrievalFailureException.class);

		syncUserDetailsService.getAllUserDetail(regId);
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllUserDetailServicesDataNotFoundException() {

		String regId = "10044";

		when(userDetailsRepository.findByUsersByRegCenterId(regId))
				.thenReturn(new ArrayList<UserDetails>());

		syncUserDetailsService.getAllUserDetail(regId);
	}
	// ------------------------------------------AllRolesSync--------------------------//

	@Test
	public void getAllRoles() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:51:42.113Z\", \"metadata\": null, \"response\": { \"lastSyncTime\": \"2019-03-31T11:51:35.458Z\", \"roles\": [ { \"roleId\": \"REGISTRATION_ADMIN\", \"roleName\": \"REGISTRATION_ADMIN\", \"roleDescription\": \"Registration administrator\" } ] }, \"errors\": null }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncRolesService.getAllRoles();
	}

	@Test(expected = SyncDataServiceException.class)
	public void getAllRolesException() {

		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient")).andRespond(withServerError());
		syncRolesService.getAllRoles();
	}

	@Test(expected = SyncServiceException.class)
	public void getAllRolesValidationError() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:40:39.847Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-SNC-303\", \"message\": \"Registration center user not found \" } ] }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncRolesService.getAllRoles();
	}

	@Test(expected = ParseResponseException.class)
	public void getAllRolesParseError() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-03-31T11:51:42.113Z\", \"metadata\": null, \"response\":  \"lastSyncTime\": \"2019-03-31T11:51:35.458Z\", \"roles\": [ { \"roleId\": \"REGISTRATION_ADMIN\", \"roleName\": \"REGISTRATION_ADMIN\", \"roleDescription\": \"Registration administrator\" } ] }, \"errors\": null }";
		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response));
		syncRolesService.getAllRoles();
	}

	@Test(expected = AuthNException.class)
	public void getRolesServiceException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-402\", \"message\": \"Token expired\" } ] }";

		MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest().body(response));
		syncRolesService.getAllRoles();
	}

	@Test(expected = BadCredentialsException.class)
	public void getAllRolesBadCredentialsException() {

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(withUnauthorizedRequest());
		syncRolesService.getAllRoles();
	}

	@Test(expected = AuthZException.class)
	public void getRolesServiceAuthzException() {
		String response = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-05-11T11:02:20.521Z\", \"metadata\": null, \"response\": null, \"errors\": [ { \"errorCode\": \"KER-ATH-403\", \"message\": \"Forbidden\" } ] }";
		String regId = "10044";

//		when(registrationCenterUserRepository.findByRegistrationCenterUserByRegCenterId(regId))
//				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN).body(response));
		syncRolesService.getAllRoles();
	}

	@Test(expected = AccessDeniedException.class)
	public void getRolesAuthNException() {

		String regId = "10044";

//		when(registrationCenterUserRepository.findByRegistrationCenterUserByRegCenterId(regId))
//				.thenReturn(registrationCenterUsers);

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(builder.toString() + "/registrationclient"))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN));
		syncRolesService.getAllRoles();
	}

	//======================

	@Test
	public void getAllUserDetailEncryptedTestCase() {
		List<Object[]> queryResult = new ArrayList<>();
		queryResult.add(new String[] {"regId", "mid", "publickey"});
		TpmCryptoResponseDto tpmCryptoResponseDto = new TpmCryptoResponseDto();
		tpmCryptoResponseDto.setValue("testsetestsetset");
		when(machineRespository.getRegistrationCenterMachineWithKeyIndexWithoutStatusCheck(Mockito.anyString())).thenReturn(queryResult);
		when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString())).thenReturn(registrationCenterUsers);
		when(machineRespository.findByMachineIdAndIsActive(Mockito.anyString())).thenReturn(machines);
		when(clientCryptoManagerService.csEncrypt(Mockito.any())).thenReturn(tpmCryptoResponseDto);
		String response = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[{\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndex(keyIndex);
		Assert.assertNotNull(syncUserDto);
	}
}
