package io.mosip.kernel.masterdata.test.controller;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.MachinePutReqDto;
import io.mosip.kernel.masterdata.dto.MachinePostReqDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MachineControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	private RequestWrapper<MachinePostReqDto> machineRequest = new RequestWrapper<>();
	private RequestWrapper<MachinePutReqDto> machineCenterDto = new RequestWrapper<MachinePutReqDto>();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		MachinePostReqDto dto = new MachinePostReqDto();
		//dto.setId("50");
		dto.setIpAddress("192.168.0.122");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setMacAddress("E8-A9-64-1F-27-E6");
		dto.setMachineSpecId("1001");
		dto.setName("machine11");
		dto.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto.setRegCenterId("10001");
		dto.setSerialNum("NM10037379");
		dto.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto.setZoneCode("NTH");
		machineRequest.setRequest(dto);

		MachinePutReqDto dto2 = new MachinePutReqDto();
		dto2.setId("10");
		dto2.setIpAddress("192.168.0.122");
		dto2.setIsActive(true);
		dto2.setLangCode("eng");
		dto2.setMacAddress("E8-A9-64-1F-27-E6");
		dto2.setMachineSpecId("1001");
		dto2.setName("machine11");
		dto2.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto2.setRegCenterId("10001");
		dto2.setSerialNum("NM10037379");
		dto2.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto2.setZoneCode("NTH");
		machineCenterDto.setRequest(dto2);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getMachineAllTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/machines")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getMachineAllTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/machines/10")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getMachineAllFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/machines/100")).andReturn(), "KER-MSD-030");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getMachinesByRegistrationCenterTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/machines/mappedmachines/10001")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005getMachinesByRegistrationCenterFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/machines/mappedmachines/10002")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006decommissionMachineTest1() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines/decommission/40")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006decommissionMachineTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines/decommission/20")).andReturn(), "KER-MSD-256");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t007decommissionMachineFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines/decommission/100")).andReturn(), "KER-MSD-214");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008updateMachineStatusTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machines").param("id", "10").param("isActive", "true"))
						.andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009updateMachineStatusFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machines").param("id", "100").param("isActive", "false"))
						.andReturn(),
				"KER-MSD-030");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t010searchMachineTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/machines/search").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(
								MasterDataTest.commonSearchDtoWithoutLangCode("name","ASC", "name", "alm2009", "contains"))))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineTest1() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/machines/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest
								.commonSearchDtoWithoutLangCode("name","ASC", "name", "alm2009", "startsWith"))))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t012searchMachineTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/machines/search").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(
								MasterDataTest.commonSearchDtoWithoutLangCode("name","ASC", "name", "alm2009", "endsWith"))))
				.andReturn(), "KER-MSD-318");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013searchMachineTest3() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc
						.perform(
								MockMvcRequestBuilders.post("/machines/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(MasterDataTest
												.commonSearchDtoWithoutLangCode("name", "ASC","name", "alm2009", "equals"))))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t014searchMachineFailTest1() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc
						.perform(
								MockMvcRequestBuilders.post("/machines/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(MasterDataTest
												.commonSearchDtoWithoutLangCode("name","ASC", "namee", "alm2009", "equals"))))
						.andReturn(), "KER-MSD-317");
	}


	@Test
	@WithUserDetails("global-admin")
	public void t017machineFilterValuesTest1() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.post("/machines/filtervalues").contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(
										MasterDataTest.commonFilterValueDto("name", "alm2009", "empty"))))
						.andReturn(),
				"KER-MSD-324");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018machineFilterValuesTest2() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.post("/machines/filtervalues").contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(
										MasterDataTest.commonFilterValueDto("name", "alm2009", "unique"))))
						.andReturn(),
				null);
	}


	@Test
	@WithUserDetails("global-admin")
	public void t019machineFilterValuesFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/machines/filtervalues").contentType(MediaType.APPLICATION_JSON)
						.content(mapper
								.writeValueAsString(MasterDataTest.commonFilterValueDto("nameee", "alm2009", "empty"))))
				.andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t022createMachineTest() throws Exception {
		machineRequest.getRequest().setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		machineRequest.getRequest().setSignPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
	
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t022createMachineTest1() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-413");
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t022createMachineTest2() throws Exception {
		machineRequest.getRequest().setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setName("test");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-414");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t023createMachineFailTest2() throws Exception {
		machineRequest.getRequest().setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setName("test1");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-414");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023createMachineFailTest3() throws Exception {
		machineRequest.getRequest().setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setSignPublicKey("MIIBIjANBgkqhkiG9w0BQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineRequest.getRequest().setName("test16");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-257");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t024updateMachineAdminTest() throws Exception {
		machineCenterDto.getRequest().setId("30");
		machineCenterDto.getRequest().setPublicKey("AAEACwACAHIAIINxl2dEhLP4GpDMjUal1yT9UtduBlILZPKh2hszFGmqABAAFwALCAAAAQABAQDVJl94GSGzgwtgul0P1I189SsZaZIfMXnXg4dvEjUhc-1H6Ht9Es2UFJ5jM1ahgOF046cbGhjjcdL59E8UFtBOk8ITbXwhHi7hE3Lj9n1boEkWoWI6MTT9IwC3qqe7fXmePsSLfJ9wssS5yB4Gxa6pcPDLWNp6mxfE84ecQZJTO8W1DanYcR5_clLmSOIGqj6cQeMOXxegy4FucGCbo1TyXzolCMEWPAEQP0Rhme75aN7cUj81GJp4aCbmXS00ru2RKKuo3fSyyM1YlE--pK4IhWvgllqJB-nrzBo9D1Kj6xNmYt0LKgABRY69h1VFwwvF8Y0Uilvs9lxw7TRj52-R");
		machineCenterDto.getRequest().setSignPublicKey("AAEACwACAHIAIINxl2dEhLP4GpDMjUal1yT9UtduBlILZPKh2hszFGmqABAAFwALCAAAAQABAQDVJl94GSGzgwtgul0P1I189SsZaZIfMXnXg4dvEjUhc-1H6Ht9Es2UFJ5jM1ahgOF046cbGhjjcdL59E8UFtBOk8ITbXwhHi7hE3Lj9n1boEkWoWI6MTT9IwC3qqe7fXmePsSLfJ9wssS5yB4Gxa6pcPDLWNp6mxfE84ecQZJTO8W1DanYcR5_clLmSOIGqj6cQeMOXxegy4FucGCbo1TyXzolCMEWPAEQP0Rhme75aN7cUj81GJp4aCbmXS00ru2RKKuo3fSyyM1YlE--pK4IhWvgllqJB-nrzBo9D1Kj6xNmYt0LKgABRY69h1VFwwvF8Y0Uilvs9lxw7TRj52-R");

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t024updateMachineAdminFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				"KER-MSD-257");
	}
	
	

	@Test
	@WithUserDetails("global-admin")
	public void t025updateMachineAdminFailTest1() throws Exception {
		machineCenterDto.getRequest().setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineCenterDto.getRequest().setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
	
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				"KER-MSD-413");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t026updateMachineAdminFailTest1() throws Exception {
		machineCenterDto.getRequest().setPublicKey("MIIBIjANBgkhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		machineCenterDto.getRequest().setSignPublicKey("MIIBIjANgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw+MtNqjqLWaOZU9focDPT+nwMdw9vOs6S+Szw9Vd+zKVQ3AWkRSyfVD0qxHsPX5N6M6eS/UXvz72WF336MbbInfwzNP+uGfkprMQMt5qg21/rPSqWPU1NA9xN8lO2uPmUH4JNRBGRyvq6X1ETTDhqPsuKDwl9ciBScCMJxf/0bv2Dx7yI8lvYUaApqpoHNbBGVgDcq4f/KRZIU2kO0Ng1ESbj6D5fm0F8ZmFx3NVCKaSbBC8NUeltIRJ6+c9Csw1o23WSFTotViWeIDelsfQDq+tMmx9i9qlX3bcPZdcb7g2wm+4cywK1K5oOf3BEBxwIDAQAB");
		
		machineCenterDto.getRequest().setName("tes");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				"KER-MSD-257");
	}

}
