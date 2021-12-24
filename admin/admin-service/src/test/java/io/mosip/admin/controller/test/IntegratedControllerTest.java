package io.mosip.admin.controller.test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.dto.FilterInfo;
import io.mosip.admin.dto.LostRidDto;
import io.mosip.admin.dto.LostRidResponseDto;
import io.mosip.admin.dto.SearchInfo;
import io.mosip.admin.dto.SortInfo;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;
import io.mosip.admin.packetstatusupdater.service.AuditManagerProxyService;
import io.mosip.admin.packetstatusupdater.service.PacketStatusUpdateService;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegratedControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@MockBean
	private AuditUtil auditUtil;

	@MockBean
	private BulkDataService bulkDataService;

	@MockBean
	private AuditManagerProxyService proxyService;

	private BulkDataResponseDto bulkDataResponseDto;

	private RequestWrapper<AuditManagerRequestDto> requestDto = new RequestWrapper<AuditManagerRequestDto>();

	private PacketStatusUpdateResponseDto packetUpdateResDto = new PacketStatusUpdateResponseDto();

	@MockBean
	private PacketStatusUpdateService packetUpdateStatusService;

	private AuditManagerRequestDto auditManagerRequestDto;

	@MockBean
	RestClient restClient;
	SearchInfo info;
	LostRidResponseDto lDto = new LostRidResponseDto();
	private RequestWrapper<SearchInfo> searchInfoReq = new RequestWrapper<SearchInfo>();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());

		bulkDataResponseDto = new BulkDataResponseDto();
		bulkDataResponseDto.setCategory("masterdata");
		bulkDataResponseDto.setLogs("test");
		bulkDataResponseDto.setOperation("update");
		bulkDataResponseDto.setStatus("success");
		bulkDataResponseDto.setStatusDescription("success");
		bulkDataResponseDto.setSuccessCount(10);
		bulkDataResponseDto.setTableName("Gender");
		bulkDataResponseDto.setTimeStamp(LocalDateTime.now().toString());
		bulkDataResponseDto.setTranscationId("10");
		bulkDataResponseDto.setUploadedBy("superadmin");
		List<PacketStatusUpdateDto> lst = new ArrayList<PacketStatusUpdateDto>();
		PacketStatusUpdateDto dto = new PacketStatusUpdateDto();
		dto.setCreatedDateTimes(LocalDateTime.now().toString());
		dto.setId("1");
		dto.setParentTransactionId("122");
		dto.setRegistrationId("1234");
		dto.setStatusComment("Success");
		dto.setStatusCode("Success");
		dto.setSubStatusCode("Pass");
		dto.setTransactionTypeCode("Completed");
		lst.add(dto);
		packetUpdateResDto.setPacketStatusUpdateList(lst);

		info = new SearchInfo();
		List<FilterInfo> lsts = new ArrayList<>();
		FilterInfo e = new FilterInfo();
		e.setColumnName("registrationId");
		e.setType("contains");
		e.setValue("1234");
		lsts.add(e);
		info.setFilters(lsts);
		List<SortInfo> s = new ArrayList<>();
		SortInfo sf = new SortInfo();
		sf.setSortField("registrationDate");
		sf.setSortType("desc");
		s.add(sf);
		info.setSort(s);
		searchInfoReq.setRequest(info);
		LostRidDto l = new LostRidDto();
		l.setRegistartionDate(LocalDate.now().toString());
		l.setRegistrationId("1234");
		List<LostRidDto> lstRid = new ArrayList<>();
		lstRid.add(l);
		lDto.setErrors(new ArrayList<>());
		lDto.setResponse(lstRid);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getTranscationDetailTest() throws Exception {
		String content = "code,genderName,langCode,isActive\r\n" + "MLO,Test,eng,FALSE\r\n" + "AAA,AAA,ara,TRUE\r\n"
				+ "BBB,BBB,eng,TRUE";
		MockMultipartFile gender = new MockMultipartFile("data", "filename.txt", "text/plain", content.getBytes());
		MockMultipartFile[] f = new MockMultipartFile[] { gender };
		Mockito.when(bulkDataService.bulkDataOperation("Gender", "update", "masterdata", f, "10006"))
				.thenReturn(bulkDataResponseDto);

		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/bulkupload/transcation/123455")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002validatePacketTest() throws Exception {
		Mockito.when(packetUpdateStatusService.getStatus("1234", "eng")).thenReturn(packetUpdateResDto);

		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/packetstatusupdate").param("rid", "1234")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails(value = "zonal-admin")
	public void t002lostRidTest1() throws Exception {

		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \""
				+ LocalDate.now().toString()
				+ "\",\r\n    \"metadata\": null,\r\n    \"response\": [{\"registrationId\":\"1234\",\"registrationDate\":\""
				+ LocalDate.now().toString()
				+ "\"}],\r\n    \"errors\": [{\"errorCode\":\"ADMN-LRID-001\",\"errorMessage\":\"unable to get rid\"}]\r\n}";
		searchInfoReq.getRequest().setSort(new ArrayList<SortInfo>());
		when(restClient.postApi(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any())).thenReturn(str);

		AdminDataUtil.checkResponse(
				(mockMvc.perform(MockMvcRequestBuilders.post("/lostRid").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(searchInfoReq))).andReturn()),
				"ADMN-LRID-001");

	}

}
