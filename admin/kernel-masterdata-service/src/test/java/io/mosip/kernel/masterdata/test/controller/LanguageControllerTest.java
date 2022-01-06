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
import com.fasterxml.jackson.core.JsonParser.Feature;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.LanguageDto;
import io.mosip.kernel.masterdata.dto.LanguagePutDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LanguageControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;
	private RequestWrapper<LanguageDto> language;

	private RequestWrapper<LanguagePutDto> languagePutdto=new RequestWrapper<LanguagePutDto>();

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		language = new RequestWrapper<LanguageDto>();
		LanguageDto dto = new LanguageDto();
		dto.setCode("fra");
		dto.setFamily("indo european");
		dto.setIsActive(true);
		dto.setNativeName("english");
		dto.setName("french");
		language.setRequest(dto);
		
		LanguagePutDto dto2 = new LanguagePutDto();
		dto2.setCode("eng");
		dto2.setFamily("indo european");
		//dto2.setIsActive(true);
		dto2.setNativeName("english");
		dto2.setName("english");
		languagePutdto.setRequest(dto2);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t7getAllLaguagesTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/languages")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t1getAllLaguagesFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/languages")).andReturn(),
				"KER-MSD-24");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t2saveLanguageFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/languages")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(language))).andReturn(),
				"KER-MSD-049");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t0saveLanguageFailTest() throws Exception {
		LanguageDto dto1 = new LanguageDto();
		dto1.setCode("eng");
		dto1.setFamily("indo european1");
		dto1.setIsActive(true);
		dto1.setNativeName("english1");
		dto1.setName("English1");
		
		language.setRequest(dto1);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/languages")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(language))).andReturn(),
				"KER-MSD-999");
	}
	@Test
	@WithUserDetails("global-admin")
	public void t0saveLanguageFailTest1() throws Exception {
			MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/languages")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(language))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t3updateLanguageFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/languages").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(languagePutdto))).andReturn(),	"KER-MSD-701");
	}
	@Test
	@WithUserDetails("global-admin")
	public void t3updateLanguageFailTest2() throws Exception {
		languagePutdto.getRequest().setCode("fra");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/languages").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(languagePutdto))).andReturn(),	"KER-MSD-701");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t3updateLanguageTest() throws Exception {
		languagePutdto.getRequest().setCode("ara");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/languages").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(languagePutdto))).andReturn(),null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t3updateLanguageTest1() throws Exception {
		languagePutdto.getRequest().setCode("tam");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/languages").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(languagePutdto))).andReturn(),"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t4updateLanguageFailTest() throws Exception {
		languagePutdto.getRequest().setCode("eng1");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/languages").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(languagePutdto))).andReturn(),
				"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t8deleteLanguageTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/languages/ara")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t9deleteLanguageFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/languages/eng1")).andReturn(),
				"KER-MSD-24");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t5updateLanguageStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/languages").param("code", "eng").param("isActive", "true"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t6updateLanguageStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/languages").param("code", "eng1").param("isActive", "true"))
				.andReturn(), "KER-MSD-24");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t7updateLanguageStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/languages").param("code", "").param("isActive", "true"))
				.andReturn(), "KER-MSD-24");

	}
}
