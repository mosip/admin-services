package io.mosip.kernel.masterdata.test.controller;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
import io.mosip.kernel.masterdata.test.TestBootApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicationConfigControllerTest {

	@Autowired
	public MockMvc mockMvc;
	
	@MockBean
	private ApplicationConfigService  applicationConfigService;
	
	@Test
	@WithUserDetails("global-admin")
	public void getConfigValuesTest() throws Exception {
		Mockito.when(applicationConfigService.getConfigValues()).thenReturn(new HashedMap<String,String>());
		mockMvc.perform(MockMvcRequestBuilders.get("/configs"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllApplicationTest() throws Exception {
		Mockito.when(applicationConfigService.getLanguageConfigDetails()).thenReturn(new ApplicationConfigResponseDto());
		mockMvc.perform(MockMvcRequestBuilders.get("/applicationconfigs"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
