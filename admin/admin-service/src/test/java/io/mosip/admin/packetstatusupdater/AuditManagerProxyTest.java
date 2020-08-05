package io.mosip.admin.packetstatusupdater;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.kernel.core.http.RequestWrapper;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG, printOnlyOnFailure = false)
public class AuditManagerProxyTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Qualifier("restTemplateConfig")
	@MockBean
	private RestTemplate mockRestTemplate;

	@Test
	@WithUserDetails("zonal-admin")
	public void addAuditTest() throws Exception {

		LocalDateTime date = LocalDateTime.of(2019, 12, 11, 1, 22);

		AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
		auditManagerRequestDto.setApplicationId("101");
		auditManagerRequestDto.setApplicationName("appName");
		auditManagerRequestDto.setCreatedBy("admin");
		auditManagerRequestDto.setEventName("login");
		auditManagerRequestDto.setEventType("ui");
		auditManagerRequestDto.setActionTimeStamp(date);
		auditManagerRequestDto.setModuleId("111");
		auditManagerRequestDto.setModuleName("modulaName");
		auditManagerRequestDto.setHostIp("100");
		auditManagerRequestDto.setHostName("hostname");
		auditManagerRequestDto.setDescription("discribtion");
		auditManagerRequestDto.setEventId("1001");
		auditManagerRequestDto.setIdType("idType");
		auditManagerRequestDto.setId("1001");
		auditManagerRequestDto.setSessionUserId("123");
		auditManagerRequestDto.setSessionUserName("sessionName");

		RequestWrapper<AuditManagerRequestDto> requestDto = new RequestWrapper<>();
		requestDto.setId("id");
		requestDto.setVersion("0.0.1");
		requestDto.setRequest(auditManagerRequestDto);
		String jsonContent = mapper.writeValueAsString(requestDto);

		when(mockRestTemplate.postForEntity(Mockito.anyString(), Mockito.any(Object.class), Mockito.any()))
				.thenReturn(new ResponseEntity<Object>("success", HttpStatus.OK));
		mockMvc.perform(post("/auditmanager/log").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void addAuditExceptionTest() throws Exception {

		LocalDateTime date = LocalDateTime.of(2019, 12, 11, 1, 22);

		AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
		auditManagerRequestDto.setApplicationId("101");
		auditManagerRequestDto.setApplicationName("appName");
		auditManagerRequestDto.setCreatedBy("admin");
		auditManagerRequestDto.setEventName("login");
		auditManagerRequestDto.setEventType("ui");
		auditManagerRequestDto.setActionTimeStamp(date);
		auditManagerRequestDto.setModuleId("111");
		auditManagerRequestDto.setModuleName("modulaName");
		auditManagerRequestDto.setHostIp("100");
		auditManagerRequestDto.setHostName("hostname");
		auditManagerRequestDto.setDescription("discribtion");
		auditManagerRequestDto.setEventId("1001");
		auditManagerRequestDto.setIdType("idType");
		auditManagerRequestDto.setId("1001");
		auditManagerRequestDto.setSessionUserId("123");
		auditManagerRequestDto.setSessionUserName("sessionName");

		RequestWrapper<AuditManagerRequestDto> requestDto = new RequestWrapper<>();
		requestDto.setId("id");
		requestDto.setVersion("0.0.1");
		requestDto.setRequest(auditManagerRequestDto);
		String jsonContent = mapper.writeValueAsString(requestDto);
		when(mockRestTemplate.postForEntity(Mockito.anyString(), Mockito.any(Object.class), Mockito.any()))
				.thenThrow(new HttpClientErrorException(HttpStatus.OK));
		mockMvc.perform(post("/auditmanager/log").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isInternalServerError());
	}

}
