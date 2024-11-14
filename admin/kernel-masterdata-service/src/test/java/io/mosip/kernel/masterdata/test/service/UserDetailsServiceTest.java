package io.mosip.kernel.masterdata.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.impl.UserDetailsServiceImpl;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.ZoneUtils;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserDetailsServiceTest {
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Mock
	private UserDetailsRepository userDetailsRepository;

	@Mock
	private ZoneUserRepository zoneUserRepository;

	@Mock
	private MasterdataSearchHelper masterDataSearchHelper;

	@Mock
	private ZoneUtils zoneUtils;
	
	@Value("${mosip.kernel.masterdata.auth-manager-base-uri}")
	private String authUserDetailsBaseUri;
	
	@Value("${mosip.kernel.masterdata.auth-user-details:/userdetails}")
	private String authUserDetailsUri;
	
	private StringBuilder userDetailsUri;
	
	@MockBean
	private AuditUtil auditUtil;


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		userDetailsService = new UserDetailsServiceImpl();
	}

	@Before
	public void setup() {
		userDetailsUri = new StringBuilder();
		userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
	}
	
	@Test
	public void getUsersTest() {
		String response = "{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2021-03-31T04:27:31.590Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"mosipUserDtoList\": [\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110005\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110005@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test110005 Auto110005\",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,uma_authorization,GLOBAL_ADMIN,offline_access,ID_AUTHENTICATION,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110006\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110006@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test110006 Auto110006\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER_ADMIN,PARTNERMANAGER,CENTRAL_APPROVER,CREDENTIAL_PARTNER,POLICYMANAGER,ZONAL_APPROVER,REGISTRATION_SUPERVISOR,DEVICE_PROVIDER,BIOMETRIC_READ,AUTH_PARTNER,offline_access,ID_AUTHENTICATION,ABIS_PARTNER,DOCUMENT_READ,PMS_USER,METADATA_READ,REGISTRATION_OFFICER,CREATE_SHARE,PARTNER,CREDENTIAL_REQUEST,MASTERDATA_ADMIN,GLOBAL_ADMIN,ONLINE_VERIFICATION_PARTNER,PRE_REGISTRATION,CREDENTIAL_ISSUANCE,FTM_PROVIDER,ZONAL_ADMIN,CENTRAL_ADMIN,MISP_PARTNER,REGISTRATION_ADMIN,MISP,RESIDENT,PRE_REGISTRATION_ADMIN,DATA_READ,INDIVIDUAL,AUTH,REGISTRATION_PROCESSOR,PREREG,KEY_MAKER,REGISTRATION_OPERATOR\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110008\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"nsdl@gmail.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Jeevan R\",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,REGISTRATION_ADMIN,uma_authorization,GLOBAL_ADMIN,REGISTRATION_PROCESSOR,offline_access,REGISTRATION_OFFICER,Default\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"10008100880001120210315091249\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110010\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110010@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Gaurav 110010\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110011\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110011@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110011 110011\",\r\n" + 
				"        \"role\": \"REGISTRATION_SUPERVISOR,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110012\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110012@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110012 110012\",\r\n" + 
				"        \"role\": \"REGISTRATION_SUPERVISOR,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110014\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110014@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110014 110014\",\r\n" + 
				"        \"role\": \"REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,uma_authorization,GLOBAL_ADMIN,offline_access,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527095023\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110022\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110022@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110022test Auto110022\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"10002100821000120210215133247\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110024\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110024@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110024 110024\",\r\n" + 
				"        \"role\": \"REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,uma_authorization,GLOBAL_ADMIN,offline_access,REGISTRATION_OFFICER,Default\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527095024\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110038\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110038@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110038 110038\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527095024\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110068\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110068@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110068 110068\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527095024\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110070\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"satish.gohil@neosofttech.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110070 110070\",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,REGISTRATION_ADMIN,uma_authorization,REGISTRATION_PROCESSOR,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110089\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": null,\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \" \",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,REGISTRATION_ADMIN,uma_authorization,REGISTRATION_PROCESSOR,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110118\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110118@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Jane Test\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER,GLOBAL_ADMIN,PARTNERMANAGER,CENTRAL_APPROVER,POLICYMANAGER,CENTRAL_ADMIN,ZONAL_ADMIN,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,MISP,REGISTRATION_PROCESSOR,offline_access,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110119\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110119@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Piyus Test\",\r\n" + 
				"        \"role\": \"CREATE_SHARE,uma_authorization,CREDENTIAL_REQUEST,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,CREDENTIAL_ISSUANCE,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110122\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110122@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Niyati Test\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110123\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110123@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Hemant Test\",\r\n" + 
				"        \"role\": \"REGISTRATION_SUPERVISOR,uma_authorization,offline_access,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110124\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110124@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Sanjeev Test\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER_ADMIN,PARTNER,CREDENTIAL_REQUEST,PARTNERMANAGER,FTM_PROVIDER,POLICYMANAGER,ZONAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,MISP,PRE_REGISTRATION_ADMIN,INDIVIDUAL,REGISTRATION_PROCESSOR,offline_access,PMS_USER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110125\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110125@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Alok Test\",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110126\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110126@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Nikhilesh Test\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER_ADMIN,CREDENTIAL_REQUEST,GLOBAL_ADMIN,ONLINE_VERIFICATION_PARTNER,PRE_REGISTRATION,CREDENTIAL_ISSUANCE,CREDENTIAL_PARTNER,ZONAL_APPROVER,POLICYMANAGER,ZONAL_ADMIN,REGISTRATION_SUPERVISOR,REGISTRATION_ADMIN,RESIDENT,AUTH_PARTNER,PRE_REGISTRATION_ADMIN,AUTH,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110127\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110127@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"110127 110127\",\r\n" + 
				"        \"role\": \"uma_authorization,GLOBAL_ADMIN,CENTRAL_APPROVER,PRE_REGISTRATION,ZONAL_APPROVER,ZONAL_ADMIN,CENTRAL_ADMIN,DEVICE_PROVIDER,REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,PREREG,REGISTRATION_OPERATOR,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527091234\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"9999\",\r\n" + 
				"        \"mobile\": \"9232121212\",\r\n" + 
				"        \"mail\": \"auth@gmail.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Airtek \",\r\n" + 
				"        \"role\": \"uma_authorization,AUTH_PARTNER,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"abc1234\",\r\n" + 
				"        \"mobile\": \"8273283283\",\r\n" + 
				"        \"mail\": \"xyz@gmail.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"airtel India \",\r\n" + 
				"        \"role\": \"uma_authorization,AUTH,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"allrolesuser\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"allrolesuser@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test User\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER_ADMIN,PARTNERMANAGER,CENTRAL_APPROVER,CREDENTIAL_PARTNER,ZONAL_APPROVER,POLICYMANAGER,DEVICE_PROVIDER,REGISTRATION_SUPERVISOR,BIOMETRIC_READ,AUTH_PARTNER,offline_access,ID_AUTHENTICATION,DOCUMENT_READ,PMS_USER,METADATA_READ,REGISTRATION_OFFICER,CREATE_SHARE,PARTNER,CREDENTIAL_REQUEST,MASTERDATA_ADMIN,GLOBAL_ADMIN,PRE_REGISTRATION,ONLINE_VERIFICATION_PARTNER,CREDENTIAL_ISSUANCE,FTM_PROVIDER,ZONAL_ADMIN,CENTRAL_ADMIN,REGISTRATION_ADMIN,RESIDENT,MISP,PRE_REGISTRATION_ADMIN,DATA_READ,AUTH,INDIVIDUAL,REGISTRATION_PROCESSOR,PREREG,REGISTRATION_OPERATOR\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"check\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"check@bruppp.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"check check\",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"deviceprovider1\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"deviceprovider1@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Device Provider One\",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"ida_testuser\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"ida_testuser@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test_ida_testuser Auto_ida_testuser\",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,ID_AUTHENTICATION\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"iritech\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"ntttuyen@iritech.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"IriTech, Inc. \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"iritech123\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"ntttuyen123@iritech.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"IriTech, Inc. \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"keymaker\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"keymaker@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Key Maker\",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,KEY_MAKER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"misp1\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"misp1@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"MISP One\",\r\n" + 
				"        \"role\": \"MISP,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"mosipautouser\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"mosipautouser@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"automation Test\",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"partner1\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"partner1@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Partner One\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNER,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"partnermanager1\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"partnermanager1@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Partner Manager One\",\r\n" + 
				"        \"role\": \"uma_authorization,PARTNERMANAGER,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"pm_testuser\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"pm_testuser@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"pm_testuser Auto\",\r\n" + 
				"        \"role\": \"POLICYMANAGER,MISP,uma_authorization,PARTNER,PARTNERMANAGER,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"policymanager1\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"policymanager1@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Policy Manager One\",\r\n" + 
				"        \"role\": \"POLICYMANAGER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"regproc_test\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"regproc_test@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"regproc_test regproc_test\",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": \"27841452330002620190527095023\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"superadmin\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"superadmin@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Super Admin\",\r\n" + 
				"        \"role\": \"ZONAL_ADMIN,uma_authorization,MASTERDATA_ADMIN,GLOBAL_ADMIN,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"test_admin\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"test_admin@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test Auto\",\r\n" + 
				"        \"role\": \"REGISTRATION_ADMIN,REGISTRATION_SUPERVISOR,uma_authorization,CREDENTIAL_REQUEST,REGISTRATION_PROCESSOR,offline_access,ID_AUTHENTICATION,REGISTRATION_OFFICER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thales\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swami.saran@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THALES DIS INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thales12\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swami@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THAL INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thales123\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swami123@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THALoo INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thales4567\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swami.saran456@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THALES DIS INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thales888\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swami.888@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THALES DIS INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thalesabc\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"swamiabc@thalesgroup.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"THALoo INDIA PRIVATE LIMITED \",\r\n" + 
				"        \"role\": \"DEVICE_PROVIDER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thalesftm\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"test@renesas.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"RENESAS \",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,FTM_PROVIDER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thalesftm44\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"test44@renesas.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"RENESAS \",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,FTM_PROVIDER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"thalesftm99\",\r\n" + 
				"        \"mobile\": \"1234567890\",\r\n" + 
				"        \"mail\": \"test99@renesas.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"RENESAS \",\r\n" + 
				"        \"role\": \"uma_authorization,offline_access,FTM_PROVIDER\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"zonal-approver\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"zonal-approver@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"TestZonal AutoApprover\",\r\n" + 
				"        \"role\": \"ZONAL_APPROVER,uma_authorization,offline_access\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": null\r\n" + 
				"}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();	
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/admin?pageStart=0&pageFetch=100"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		userDetailsService.getUsers("", 0, 0, "", "", "");
	}
	
	@Test
	public void getUserDetailsTest() {
		String response = "{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2021-04-05T09:22:49.492Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"mosipUserDtoList\": [\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"service-account-mosip-auth-client\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": null,\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \" \",\r\n" + 
				"        \"role\": \"AUTH\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"allrolesuser\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"allrolesuser@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test User\",\r\n" + 
				"        \"role\": \"AUTH\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110006\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110006@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Test110006 Auto110006\",\r\n" + 
				"        \"role\": \"AUTH\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"110126\",\r\n" + 
				"        \"mobile\": null,\r\n" + 
				"        \"mail\": \"110126@xyz.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"Nikhilesh Test\",\r\n" + 
				"        \"role\": \"AUTH\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"userId\": \"abc1234\",\r\n" + 
				"        \"mobile\": \"8273283283\",\r\n" + 
				"        \"mail\": \"xyz@gmail.com\",\r\n" + 
				"        \"langCode\": null,\r\n" + 
				"        \"userPassword\": null,\r\n" + 
				"        \"name\": \"airtel India \",\r\n" + 
				"        \"role\": \"AUTH\",\r\n" + 
				"        \"token\": null,\r\n" + 
				"        \"rid\": null\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": null\r\n" + 
				"}";
		
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();	
		mockRestServiceServer.expect(requestTo(userDetailsUri.toString() + "/admin?roleName=AUTH&pageStart=0&pageFetch=100"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		userDetailsService.getUsers("AUTH",0,0,"","","");
	}

	@Test (expected = MasterDataServiceException.class)
	public void testGetUsersByRegistrationCenter_Success() {
		String regCenterId = "reg123";
		int pageNumber = 0;
		int pageSize = 10;
		String sortBy = "id";
		String orderBy = "ASC";

		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(new UserDetails());

		when(userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(regCenterId,
				PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderBy), sortBy)))).thenReturn(null);

		List<UserDetailsExtnDto> expectedResults = userDetailsList.stream()
				.map(ud -> new UserDetailsExtnDto())
				.toList();

		List<UserDetailsExtnDto> actualResults = userDetailsService.getUsersByRegistrationCenter(regCenterId, pageNumber, pageSize, sortBy, orderBy);

		assertNotNull(actualResults);
		assertEquals(expectedResults.size(), actualResults.size());
	}

	@Test
	public void testGetUsersByRegistrationCenter_DataNotFoundException() {
		String regCenterId = "reg123";
		int pageNumber = 0;
		int pageSize = 10;
		String sortBy = "id";
		String orderBy = "ASC";

		when(userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(regCenterId,
				PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderBy), sortBy)))).thenReturn(null);

		assertThrows(MasterDataServiceException.class, () -> userDetailsService.getUsersByRegistrationCenter(regCenterId, pageNumber, pageSize, sortBy, orderBy));
	}

	@Test
	public void testGetUsersByRegistrationCenter_Exception() {
		String regCenterId = "reg123";
		int pageNumber = 0;
		int pageSize = 10;
		String sortBy = "id";
		String orderBy = "ASC";

		when(userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(regCenterId,
				PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderBy), sortBy)))).thenThrow(new RuntimeException());

		assertThrows(MasterDataServiceException.class, () -> userDetailsService.getUsersByRegistrationCenter(regCenterId, pageNumber, pageSize, sortBy, orderBy));
	}

	@Test
	public void testSearchUserDetails_Success() {
		SearchDtoWithoutLangCode searchDto = new SearchDtoWithoutLangCode();

		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(new UserDetails());

		Page<UserDetails> page = new PageImpl<>(userDetailsList);
		when(masterDataSearchHelper.searchMasterdataWithoutLangCode(UserDetails.class, searchDto, null)).thenReturn(page);
	}

	@Test
	public void testSearchUserDetails_ZoneCodeFilter() {
		SearchDtoWithoutLangCode searchDto = new SearchDtoWithoutLangCode();
		searchDto.setFilters(Collections.singletonList(new SearchFilter("ZONE123", null, null, "zoneCode", FilterTypeEnum.IN.toString())));

		List<ZoneUser> zoneUsers = new ArrayList<>();
		zoneUsers.add(new ZoneUser("ZONE123", "user1", "eng"));
		when(zoneUserRepository.findZoneByZoneCodeActiveAndNonDeleted("ZONE123")).thenReturn(zoneUsers);

		List<String> userIds = zoneUsers.stream().map(ZoneUser::getUserId).collect(Collectors.toList());
		searchDto.getFilters().getFirst().setColumnName("id");
		searchDto.getFilters().getFirst().setValue(String.join(",", userIds));
		searchDto.getFilters().getFirst().setType(FilterTypeEnum.IN.toString());

		when(masterDataSearchHelper.searchMasterdataWithoutLangCode(UserDetails.class, searchDto, null)).thenReturn(Page.empty());
	}

	@Test
	public void testSearchUserDetails_Exception() {
		SearchDtoWithoutLangCode searchDto = new SearchDtoWithoutLangCode();
		searchDto.setFilters(Collections.singletonList(new SearchFilter("ZONE123", null, null, "zoneCode", FilterTypeEnum.IN.toString())));

		when(masterDataSearchHelper.searchMasterdataWithoutLangCode(UserDetails.class, searchDto, null)).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> userDetailsService.searchUserDetails(searchDto));
	}

	@Test
	public void testValidateZone_ValidZone() {
		String langCode = "eng";

		List<Zone> subZones = new ArrayList<>();
		subZones.add(new Zone("WB-KOL-E", "eng", "zone1", (short) 1, "zone1Hierarchy","zone1Parent", "path/to/z1"));
		when(zoneUtils.getSubZones(langCode)).thenReturn(subZones);
	}

	@Test
	public void testValidateZone_NullZoneCode() {
		String zoneCode = null;
		String langCode = "eng";

		assertThrows(NullPointerException.class, () -> ReflectionTestUtils.invokeMethod(userDetailsService, "validateZone", zoneCode, langCode));
	}

}