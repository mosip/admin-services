//package io.mosip.admin.controller;
//
//import static io.mosip.kernel.core.util.JsonUtils.javaObjectToJsonString;
//
//import java.math.BigInteger;
//import java.time.LocalDateTime;
//import java.util.Collections;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
//import io.mosip.admin.TestBootApplication;
//import io.mosip.admin.bulkdataupload.dto.RIDSyncDto;
//import io.mosip.admin.bulkdataupload.dto.RegistrationPacketSyncDTO;
//import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
//import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
//import io.mosip.admin.packetstatusupdater.util.AuditUtil;
//import io.mosip.admin.util.AdminDataUtil;
//import io.mosip.admin.util.RestClient;
//import io.mosip.commons.packet.spi.IPacketCryptoService;
//import io.mosip.kernel.core.util.CryptoUtil;
//import io.mosip.kernel.core.util.DateUtils;
//import io.mosip.kernel.core.util.HMACUtils2;
//import io.mosip.kernel.core.websub.model.EventModel;
//import io.mosip.kernel.core.websub.spi.PublisherClient;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TestBootApplication.class)
//@AutoConfigureMockMvc
//public class BulkUploadControllerTest {
//
//	@Autowired
//	public MockMvc mockMvc;
//
//	@MockBean
//	private PublisherClient<String, EventModel, HttpHeaders> publisher;
//
//	private ObjectMapper mapper;
//
//	@Autowired
//	private AuditUtil auditUtil;
//	
//	private static String transactionId = ""; 
//    private static final String PACKET_SYNC_STATUS_ID = "mosip.registration.sync";
//    private static final String PACKET_SYNC_VERSION = "1.0";
//
//	 @MockBean
//    @Qualifier("OnlinePacketCryptoServiceImpl")
//    private IPacketCryptoService onlineCrypto;
//
//	@MockBean
//	private CryptoUtil cryptoUtil;
//	 
//    @MockBean
//    RestClient restClient;
//
//    @Value("${RETRIEVE_IDENTITY_API}")
//    String retrieveIdentityUrl;
//
//	@Mock
//	private BulkUploadTranscationRepository bulkDataOperation;
//
//	@Autowired
//	RestTemplate restTemplate;
//	MockRestServiceServer mockRestServiceServer;
//   
//	BulkUploadTranscation bulkUploadTranscation;
//	
//	
////	@Before
////	public void setUp() throws Exception {
////		bulkUploadTranscation =new BulkUploadTranscation();
////		bulkUploadTranscation.setId("123456");
////		bulkUploadTranscation.setCategory("packet");
////		bulkUploadTranscation.setRecordCount(3);
////		bulkUploadTranscation.setStatusCode("FAILED");
////		bulkUploadTranscation.setCreatedDateTime(LocalDateTime.now());
////		mapper = new ObjectMapper();
////		mapper.registerModule(new JavaTimeModule());
////		 RIDSyncDto syncdto = new RIDSyncDto();
////		 String id = "10002300090003320220811103855";
////         syncdto.setRegistrationId(id);
////         syncdto.setRegistrationType("NEW");
////         syncdto.setPacketHashValue("vbhsjicuygwbnsjdicudhdebwnkaosd98e74y3h2nqws9d8f7ryh43");
////         syncdto.setPacketSize(BigInteger.valueOf(12334));
////         syncdto.setSupervisorStatus("APPROVED");
////         syncdto.setName("Name");
////         syncdto.setPhone("phone");
////         syncdto.setEmail("email");
////         syncdto.setLangCode("eng");
////         syncdto.setSupervisorComment("Uploaded from admin portal with transactionId : " + transactionId);
////         
////         RegistrationPacketSyncDTO registrationPacketSyncDTO = new RegistrationPacketSyncDTO();
////         registrationPacketSyncDTO
////                 .setRequesttime(DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime()));
////         registrationPacketSyncDTO.setRequest(Collections.singletonList(syncdto));
////         registrationPacketSyncDTO.setId(PACKET_SYNC_STATUS_ID);
////         registrationPacketSyncDTO.setVersion(PACKET_SYNC_VERSION);
////         byte[] file = javaObjectToJsonString(registrationPacketSyncDTO).getBytes();
////         Mockito.when(onlineCrypto.encrypt(Mockito.anyString(),Mockito.any())).thenReturn(file);
////         Mockito.when(CryptoUtil.encodeToURLSafeBase64(Mockito.any()));
////         String encodedData = CryptoUtil.encodeToURLSafeBase64(onlineCrypto.encrypt(id,file));
////         MockMultipartFile packet = new MockMultipartFile("data", "packet.zip", "multipart/form-data", encodedData.getBytes());
////		//doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());
////		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
////		mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(packet).param("process","NEW").param("centerId","10002").param("supervisorStatus","APPROVED").param("category","packet"));
////
////	}
//	
//	@Test
//	@WithUserDetails("global-admin")
//	public void t001getTranscationDetailTest() throws Exception {
//		AdminDataUtil.checkResponse(
//				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/transcation/123456")).andReturn(),
//				null);
//
//	}
//	
//	
//	@Test
//	@WithUserDetails("global-admin")
//	public void t003uploadDataTest() throws Exception {
//		 RIDSyncDto syncdto = new RIDSyncDto();
//		 String id = "10002300090003320220811103855";
//         syncdto.setRegistrationId(id);
//         syncdto.setRegistrationType("NEW");
//         syncdto.setPacketHashValue("vbhsjicuygwbnsjdicudhdebwnkaosd98e74y3h2nqws9d8f7ryh43");
//         syncdto.setPacketSize(BigInteger.valueOf(12334));
//         syncdto.setSupervisorStatus("APPROVED");
//         syncdto.setName("Name");
//         syncdto.setPhone("phone");
//         syncdto.setEmail("email");
//         syncdto.setLangCode("eng");
//         syncdto.setSupervisorComment("Uploaded from admin portal with transactionId : " + transactionId);
//         
//         RegistrationPacketSyncDTO registrationPacketSyncDTO = new RegistrationPacketSyncDTO();
//         registrationPacketSyncDTO
//                 .setRequesttime(DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime()));
//         registrationPacketSyncDTO.setRequest(Collections.singletonList(syncdto));
//         registrationPacketSyncDTO.setId(PACKET_SYNC_STATUS_ID);
//         registrationPacketSyncDTO.setVersion(PACKET_SYNC_VERSION);
//         byte[] file = javaObjectToJsonString(registrationPacketSyncDTO).getBytes();
//         Mockito.when(onlineCrypto.encrypt(Mockito.anyString(),Mockito.any())).thenReturn(file);
//         Mockito.when(CryptoUtil.encodeToURLSafeBase64(Mockito.any()));
//         String encodedData = CryptoUtil.encodeToURLSafeBase64(onlineCrypto.encrypt(id,file));
//         MockMultipartFile packet = new MockMultipartFile("data", "packet.zip", "multipart/form-data", encodedData.getBytes());
//		AdminDataUtil.checkResponse(
//				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(packet).param("process","NEW").param("centerId","10002").param("supervisorStatus","APPROVED").param("category","packet")).andReturn(),
//				"ADM-BLK-007");
//
//	}
//	
//	
//}
