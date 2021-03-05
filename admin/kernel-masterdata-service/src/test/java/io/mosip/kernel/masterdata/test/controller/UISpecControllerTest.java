package io.mosip.kernel.masterdata.test.controller;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.repository.UISpecRepository;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecPublishDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;

/**
 * 
 * @author Nagarjuna
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class UISpecControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private UISpecService uiSpecService;

	@MockBean
	private UISpecRepository uiSpecRepository;

	@Test
	@WithUserDetails("global-admin")
	public void defineUISpecTest() throws Exception {
		Mockito.when(uiSpecService.defineUISpec(Mockito.any(UISpecDto.class))).thenReturn(new UISpecResponseDto());
		mockMvc.perform(MockMvcRequestBuilders.post("/uispec").contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+ "{\"title\":\"title\",\"description\":\"test desc\",\"schema\":[{\"id\":\"IDSchemaVersion\",\"inputRequired\":false,"
						+ "\"type\":\"number\",\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"IDSchemaVersion\",\"language\":\"eng\"}],\"controlType\":\"none\","
						+ "\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":false},"
						+ "{\"id\":\"UIN\",\"inputRequired\":false,\"type\":\"integer\",\"minimum\":0,\"maximum\":0,\"description\":\"\","
						+ "\"label\":[{\"value\":\"UIN\",\"language\":\"eng\"}],\"controlType\":\"none\",\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],"
						+ "\"fieldCategory\":\"Pvt\",\"required\":false},{\"id\":\"fullName\",\"inputRequired\":true,\"type\":\"simpleType\","
						+ "\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"Full Name\",\"language\":\"eng\"}],\"controlType\":\"textbox\",\"fieldType\":\"default\","
						+ "\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":true}],"
						+ "\"effectiveFrom\":\"2018-12-17T07:15:06.724Z\"}}"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateUISpec() throws Exception {
		Mockito.when(uiSpecService.updateUISpec(Mockito.anyString(), Mockito.any(UISpecDto.class)))
				.thenReturn(new UISpecResponseDto());
		mockMvc.perform(MockMvcRequestBuilders.put("/uispec").param("id", "test-test-test-test")
				.contentType(MediaType.APPLICATION_JSON).content(
						"{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
								+ "{\"title\":\"MOSIP ID Schema\",\"description\":\"test desc\",\"schema\":[{\"id\":\"IDSchemaVersion\",\"inputRequired\":false,"
								+ "\"type\":\"number\",\"minimum\":0,\"maximum\":0,\"description\":\"\",\"labelName\":[{\"value\":\"IDSchemaVersion\",\"language\":\"eng\"}],\"controlType\":\"none\","
								+ "\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":false},"
								+ "{\"id\":\"UIN\",\"inputRequired\":false,\"type\":\"integer\",\"minimum\":0,\"maximum\":0,\"description\":\"\","
								+ "\"labelName\":[{\"value\":\"UIN\",\"language\":\"eng\"}],\"controlType\":\"none\",\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],"
								+ "\"fieldCategory\":\"Pvt\",\"required\":false},{\"id\":\"fullName\",\"inputRequired\":true,\"type\":\"simpleType\","
								+ "\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"Full Name\",\"language\":\"eng\"}],\"controlType\":\"textbox\",\"fieldType\":\"default\","
								+ "\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":true}],"
								+ "\"effectiveFrom\":\"2018-12-17T07:15:06.724Z\"}}"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpec() throws Exception {
		Mockito.when(uiSpecService.publishUISpec(Mockito.any(UISpecPublishDto.class))).thenReturn("");
		mockMvc.perform(MockMvcRequestBuilders.put("/uispec/publish").contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+ "{\"id\":\"test-test-test-test\",\"effectiveFrom\":\"2048-12-17T07:15:06.724Z\"}}"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteUISpec() throws Exception {
		Mockito.when(uiSpecService.deleteUISpec(Mockito.anyString())).thenReturn("");
		mockMvc.perform(MockMvcRequestBuilders.delete("/uispec").param("id", "test-test-test-test")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("global-admin")
	public void fetchAllUISpec() throws Exception {
		Mockito.when(uiSpecService.getAllUISpecs(1, 10, "id", "desc")).thenReturn(new PageDto<UISpecResponseDto>());
		mockMvc.perform(MockMvcRequestBuilders.get("/uispec/all")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("global-admin")
	public void getLatestUISpec() throws Exception {
		Mockito.when(uiSpecService.getLatestUISpec(Mockito.anyString())).thenReturn(new ArrayList<>());
		mockMvc.perform(MockMvcRequestBuilders.get("/uispec/regclient/latest"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
