package io.mosip.admin.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doNothing;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.kernel.core.http.ResponseWrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkDataUploadControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@Autowired
	private AuditUtil auditUtil;
	
	private static String transactionId = ""; 

	@Mock
	private BulkUploadTranscationRepository bulkDataOperation;

	@Autowired
	RestTemplate restTemplate;
	MockRestServiceServer mockRestServiceServer;
   
	BulkUploadTranscation bulkUploadTranscation;

	@Before
	public void setUp() throws Exception {
		bulkUploadTranscation =new BulkUploadTranscation();
		bulkUploadTranscation.setId("123456");
		bulkUploadTranscation.setCategory("masterdata");
		bulkUploadTranscation.setRecordCount(3);
		bulkUploadTranscation.setEntityName("gender");
		bulkUploadTranscation.setStatusCode("FAILED");
		bulkUploadTranscation.setCreatedDateTime(LocalDateTime.now());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		//doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		String content="code,genderName,langCode,isActive\r\n" +
				"TST,Test,eng,FALSE\r\n" +
				"BBB,BBB,ara,TRUE\r\n" +
				"DDD,DDD,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
		mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","masterdata"));

	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithUserDetails("global-admin")
	public void t012checkCountUpload() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" +
				"MLIO,Test,eng,FALSE\r\n" +
				"AABA,AAA,ara,TRUE\r\n" +
				"BBCB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","masterdata")).andReturn();
		String transaction = response.getResponse().getContentAsString();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(transaction);
		JSONObject jsonResponse = (JSONObject) parser.parse(json.get("response").toString());
		transactionId = jsonResponse.get("transcationId").toString();
		AdminDataUtil.checkResponse(response,null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getTranscationDetailTest() throws Exception {
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/transcation/1234")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void BlocklistedWordsConstraintvalidationNegativeTest() throws Exception {
		String content="word,description,langCode,isActive,isDeleted\r\n" +
				"Some Random Words,Test,eng,TRUE,FALSE\r\n";
		MockMultipartFile blocklisted_words = new MockMultipartFile("data", "filename.csv", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(blocklisted_words).param("tableName","blocklisted_words").param("operation","insert").param("category","masterdata")).andReturn(),
				"ADM-BLK-007");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void BlocklistedWordsConstraintvalidationPositiveTest() throws Exception {
	String content="word,description,langCode,isActive\r\n" +
			"SomeWord,DDD,eng,TRUE";
	MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
	AdminDataUtil.checkResponse(
	mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","blocklisted_words").param("operation","insert").param("category","masterdata")).andReturn(),null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getTranscationDetailTestFail() throws Exception {
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/transcation/12")).andReturn(),
				"ADMN-BLK-TRNSCTNS-001");

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t002getTranscationDetailTest() throws Exception {
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/getAllTransactions")).andReturn(),
				null);

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t003uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" + 
				"MLO,Test,eng,FALSE\r\n" + 
				"AAA,AAA,ara,TRUE\r\n" + 
				"BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("data", "filename.txt", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","masterdata")).andReturn(),
				"ADM-BLK-007");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" + 
				"MLO,Test,eng,FALSE\r\n" + 
				"AAA,AAA,ara,TRUE\r\n" + 
				"BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("data", "filename.txt", "text/plain", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","masterdataa")).andReturn(),
				"KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" + 
				"MLO,Test,eng,FALSE\r\n" + 
				"AAA,AAA,ara,TRUE\r\n" + 
				"BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("data", "gender.csv", "text/plain", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insertt").param("category","masterdata")).andReturn(),
				"KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" + 
				"MLO,Test,eng,FALSE\r\n" + 
				"AAA,AAA,ara,TRUE\r\n" + 
				"BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("data", "gender.csv", "text/plain", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","packet")).andReturn(),
				"ADM-BLK-006");

	}
	@Test
	@WithUserDetails("global-admin")
	public void t007uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" +
				"MLO,Test,eng,FALSE\r\n" +
				"AAA,AAA,ara,TRUE\r\n" +
				"BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","masterdata")).andReturn(),
				null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t008uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" +
				"TST,Test1,eng,TRUE\r\n" +
				"BBB,AAA,ara,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","update").param("category","masterdata")).andReturn(),
				null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t009uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" +
				"TST,Test1,eng,TRUE\r\n" +
				"BBB,AAA,ara,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.csv", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","delete").param("category","masterdata")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010getTranscationDetailTest() throws Exception {
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/getAllTransactions")).andReturn(), null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t011uploadDataTest() throws Exception {
		String content="code,genderName,langCode,isActive\r\n" +
				"TST,Test1,eng,TRUE\r\n" +
				"BBB,AAA,ara,TRUE";
		MockMultipartFile gender = new MockMultipartFile("files", "gender.zip", "multipart/form-data", content.getBytes());
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.multipart("/bulkupload").file(gender).param("tableName","gender").param("operation","insert").param("category","packet")).andReturn(),
				null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	@WithUserDetails("global-admin")
	public void t013checkCount() throws Exception {
		Thread.sleep(20000);
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/transcation/"+transactionId)).andReturn();
		String transaction = response.getResponse().getContentAsString();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(transaction);
		JSONObject jsonResponse = (JSONObject) parser.parse(json.get("response").toString());
		int count = Integer.valueOf(jsonResponse.get("count").toString());
		assertSame(count,0);
	}

}
