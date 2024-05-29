package io.mosip.kernel.masterdata.test.config;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@TestPropertySource("classpath:application.properties")
public class TestConfig {


	@Bean
	public RestTemplate restTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.disableCookieManagement();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClientBuilder.build());
		RestTemplate restTemplate =  new RestTemplate(requestFactory);

		MockRestServiceServer.createServer(restTemplate)
				.expect(requestTo("http://localhost/config/kernel-masterdata-service/mz/develop/applicanttype.mvel"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body("def getApplicantType() { return '000'; }"));

		return restTemplate;
	}

	@Bean
	public RestTemplate selfTokenRestTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		var connnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});
		connnectionManagerBuilder.setSSLSocketFactory(csf);
		var connectionManager = connnectionManagerBuilder.build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.disableCookieManagement();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClientBuilder.build());
		RestTemplate restTemplate =  new RestTemplate(requestFactory);
		MockRestServiceServer.createServer(restTemplate)
				.expect(requestTo("http://localhost/config/kernel-masterdata-service/mz/develop/applicanttype.mvel"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body("def getApplicantType() { return '000'; }"));

		return restTemplate;
	}
	
//	@Bean
//	public ApplicantValidDocumentRepository applicantValidDocumentRepository() {
//		return Mockito.mock(ApplicantValidDocumentRepository.class);
//	}
//	
//	@Bean
//	public ApplicationRepository applicationRepository() {
//		return Mockito.mock(ApplicationRepository.class);
//	}
//
//	@Bean
//	public BiometricAttributeRepository biometricAttributeRepository() {
//		return Mockito.mock(BiometricAttributeRepository.class);
//	}
//	
//	@Bean
//	public BiometricTypeRepository biometricTypeRepository() {
//		return Mockito.mock(BiometricTypeRepository.class);
//	}
//	
//	@Bean
//	public BlocklistedWordsRepository blocklistedWordsRepository() {
//		return Mockito.mock(BlocklistedWordsRepository.class);
//	}
//	
//	@Bean
//	public DaysOfWeekListRepo daysOfWeekRepo() {
//		return Mockito.mock(DaysOfWeekListRepo.class);
//	}
//	@Bean
//	public DeviceHistoryRepository deviceHistoryRepository() {
//		return Mockito.mock(DeviceHistoryRepository.class);
//	}
//	@Bean
//	public DeviceProviderHistoryRepository deviceProviderHistoryRepository() {
//		return Mockito.mock(DeviceProviderHistoryRepository.class);
//	}
//	@Bean
//	public DeviceRepository deviceRepository() {
//		return Mockito.mock(DeviceRepository.class);
//	}
//	@Bean
//	public DeviceProviderRepository deviceProviderRepository() {
//		return Mockito.mock(DeviceProviderRepository.class);
//	}
//	@Bean
//	public DeviceRegisterHistoryRepository deviceRegisterHistoryRepository() {
//		return Mockito.mock(DeviceRegisterHistoryRepository.class);
//	}
//	@Bean
//	public DeviceRegisterRepository deviceRegisterRepository() {
//		return Mockito.mock(DeviceRegisterRepository.class);
//	}
//	@Bean
//	public DeviceSpecificationRepository deviceSpecificationRepository() {
//		return Mockito.mock(DeviceSpecificationRepository.class);
//	}
//	@Bean
//	public DeviceTypeRepository deviceTypeRepository() {
//		return Mockito.mock(DeviceTypeRepository.class);
//	}
//	@Bean
//	public DocumentCategoryRepository documentCategoryRepository() {
//		return Mockito.mock(DocumentCategoryRepository.class);
//	}
//	@Bean
//	public DocumentTypeRepository documentTypeRepository() {
//		return Mockito.mock(DocumentTypeRepository.class);
//	}
//	@Bean
//	public DynamicFieldRepository dynamicFieldRepository() {
//		return Mockito.mock(DynamicFieldRepository.class);
//	}
//	@Bean
//	public ExceptionalHolidayRepository exceptionalHolidayRepository() {
//		return Mockito.mock(ExceptionalHolidayRepository.class);
//	}
//	@Bean
//	public FoundationalTrustProviderRepository foundationalTrustProviderRepository() {
//		return Mockito.mock(FoundationalTrustProviderRepository.class);
//	}
//	@Bean
//	public FoundationalTrustProviderRepositoryHistory foundationalTrustProviderRepositoryHistory() {
//		return Mockito.mock(FoundationalTrustProviderRepositoryHistory.class);
//	}
//	@Bean
//	public GenderTypeRepository genderTypeRepository() {
//		return Mockito.mock(GenderTypeRepository.class);
//	}
//	@Bean
//	public HolidayRepository holidayRepository() {
//		return Mockito.mock(HolidayRepository.class);
//	}
//	@Bean
//	public IdentitySchemaRepository identitySchemaRepository() {
//		return Mockito.mock(IdentitySchemaRepository.class);
//	}
//	@Bean
//	public IdTypeRepository idTypeRepository() {
//		return Mockito.mock(IdTypeRepository.class);
//	}
//	@Bean
//	public IndividualTypeRepository individualTypeRepository() {
//		return Mockito.mock(IndividualTypeRepository.class);
//	}
//	@Bean
//	public LanguageRepository languageRepository() {
//		return Mockito.mock(LanguageRepository.class);
//	}
//	@Bean
//	public LocationHierarchyRepository locationHierarchyRepository() {
//		return Mockito.mock(LocationHierarchyRepository.class);
//	}
//	@Bean
//	public LocationRepository locationRepository() {
//		return Mockito.mock(LocationRepository.class);
//	}
//	@Bean
//	public MachineHistoryRepository machineHistoryRepository() {
//		return Mockito.mock(MachineHistoryRepository.class);
//	}
//	@Bean
//	public MachineRepository machineRepository() {
//		return Mockito.mock(MachineRepository.class);
//	}
//	@Bean
//	public MachineSpecificationRepository machineSpecificationRepository() {
//		return Mockito.mock(MachineSpecificationRepository.class);
//	}
//	@Bean
//	public MachineTypeRepository machineTypeRepository() {
//		return Mockito.mock(MachineTypeRepository.class);
//	}
//	@Bean
//	public ModuleRepository moduleRepository() {
//		return Mockito.mock(ModuleRepository.class);
//	}
//	@Bean
//	public MOSIPDeviceServiceHistoryRepository mosipDeviceServiceHistoryRepository() {
//		return Mockito.mock(MOSIPDeviceServiceHistoryRepository.class);
//	}
//	@Bean
//	public MOSIPDeviceServiceRepository mosipDeviceServiceRepository() {
//		return Mockito.mock(MOSIPDeviceServiceRepository.class);
//	}
//	@Bean
//	public ReasonCategoryRepository reasonCategoryRepository() {
//		return Mockito.mock(ReasonCategoryRepository.class);
//	}
//	@Bean
//	public ReasonListRepository reasonListRepository() {
//		return Mockito.mock(ReasonListRepository.class);
//	}
//	@Bean
//	public RegExceptionalHolidayRepository regExceptionalHolidayRepository() {
//		return Mockito.mock(RegExceptionalHolidayRepository.class);
//	}
//	@Bean
//	public RegisteredDeviceHistoryRepository registeredDeviceHistoryRepository() {
//		return Mockito.mock(RegisteredDeviceHistoryRepository.class);
//	}
//	@Bean
//	public RegisteredDeviceRepository registeredDeviceRepository() {
//		return Mockito.mock(RegisteredDeviceRepository.class);
//	}
//	@Bean
//	public RegistrationCenterHistoryRepository registrationCenterHistoryRepository() {
//		return Mockito.mock(RegistrationCenterHistoryRepository.class);
//	}
//	@Bean
//	public RegistrationCenterRepository registrationCenterRepository() {
//		return Mockito.mock(RegistrationCenterRepository.class);
//	}
//	@Bean
//	public RegistrationCenterTypeRepository registrationCenterTypeRepository() {
//		return Mockito.mock(RegistrationCenterTypeRepository.class);
//	}
//	@Bean
//	public RegistrationDeviceSubTypeRepository registrationDeviceSubTypeRepository() {
//		return Mockito.mock(RegistrationDeviceSubTypeRepository.class);
//	}
//	@Bean
//	public RegistrationDeviceTypeRepository registrationDeviceTypeRepository() {
//		return Mockito.mock(RegistrationDeviceTypeRepository.class);
//	}
//	@Bean
//	public RegWorkingNonWorkingRepo workingDaysRepo() {
//		return Mockito.mock(RegWorkingNonWorkingRepo.class);
//	}
//	@Bean
//	public TemplateFileFormatRepository templateFileFormatRepository() {
//		return Mockito.mock(TemplateFileFormatRepository.class);
//	}
//	@Bean
//	public TemplateRepository templateRepository() {
//		return Mockito.mock(TemplateRepository.class);
//	}
//	@Bean
//	public TemplateTypeRepository templateTypeRepository() {
//		return Mockito.mock(TemplateTypeRepository.class);
//	}
//	@Bean
//	public TitleRepository titleRepository() {
//		return Mockito.mock(TitleRepository.class);
//	}
//	@Bean
//	public UISpecRepository uiSpecRepository() {
//		return Mockito.mock(UISpecRepository.class);
//	}
//	@Bean
//	public UserDetailsHistoryRepository userDetailsHistoryRepository() {
//		return Mockito.mock(UserDetailsHistoryRepository.class);
//	}
//	@Bean
//	public UserDetailsRepository userDetailsRepository() {
//		return Mockito.mock(UserDetailsRepository.class);
//	}
//	@Bean
//	public ValidDocumentRepository validDocumentRepository() {
//		return Mockito.mock(ValidDocumentRepository.class);
//	}
//	@Bean
//	public ZoneRepository zoneRepository() {
//		return Mockito.mock(ZoneRepository.class);
//	}
//	@Bean
//	public ZoneUserHistoryRepository zoneUserHistoryRepository() {
//		return Mockito.mock(ZoneUserHistoryRepository.class);
//	}
//	@Bean
//	public ZoneUserRepository zoneUserRepository() {
//		return Mockito.mock(ZoneUserRepository.class);
//	}
//	@Bean
//	public MachineIdGenerator<String> machineIdGenerator() {
//		return Mockito.mock(MachineIdGenerator.class);
//	}
//	@Bean
//	public RegistrationCenterIdGenerator<String> registrationCenterIdGenerator() {
//		return Mockito.mock(RegistrationCenterIdGenerator.class);
//	}
//	
	

}