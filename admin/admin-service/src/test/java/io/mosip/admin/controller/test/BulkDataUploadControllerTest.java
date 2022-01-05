package io.mosip.admin.controller.test;

import static org.mockito.Mockito.doNothing;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

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

	@Autowired
	RestTemplate restTemplate;
	MockRestServiceServer mockRestServiceServer;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		//doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
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
		MockMultipartFile gender = new MockMultipartFile("data", "filename.txt", "text/plain", content.getBytes());
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

}
