package io.mosip.kernel.masterdata.test.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.service.IdentitySchemaService;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;

/**
 * 
 * @author anusha
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class IdentitySchemaControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;
	
	@Qualifier("restTemplateConfig")
	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private IdentitySchemaService identitySchemaService;

	@MockBean
	private DynamicFieldService dynamicFieldService;
	
	@MockBean
	private IdentitySchemaRepository identitySchemaRepository;
	
	@MockBean
	private DynamicFieldRepository dynamicFieldRepository;
	
	@MockBean
	private UISpecService uiSpecService;
	
	private Page<DynamicField> fieldPagedResult;
	private Page<IdentitySchema> schemaPagedResult;
	private IdentitySchema publishedSchema;
	private IdentitySchema draftSchema;
	private DynamicField bloodTypeField;
	private DynamicField mstatusField;	
	PageRequest pageRequest = null;
	IdentitySchema is=null;
	List<UISpecResponseDto> lstui=new ArrayList<UISpecResponseDto>();
@Before
	public void setup() {
		
		 is=new IdentitySchema();
		is.setAdditionalProperties(false);
		is.setCreatedBy("superuser");
		is.setCreatedDateTime(LocalDateTime.now());
		is.setDescription("test");
		is.setEffectiveFrom(LocalDateTime.now());
		is.setId("test");
		is.setIdVersion(0.1);
		is.setIsActive(false);
		is.setIsDeleted(false);
		is.setLangCode("eng");
		is.setSchemaJson("{\"code\":\"test\"}");
		is.setStatus("active");
		is.setTitle("test");
		
		List<DynamicField> list = new ArrayList<DynamicField>();		
		bloodTypeField = new DynamicField();
		bloodTypeField.setDataType("simpleType");
		bloodTypeField.setDescription("test");
		bloodTypeField.setId("1233");
		bloodTypeField.setIsActive(true);
		bloodTypeField.setLangCode("eng");
		bloodTypeField.setName("bloodType");
		bloodTypeField.setValueJson("[{\"value\":\"Optv\",\"code\":\"BT1\",\"isActive\":true},{\"value\":\"Ontv\",\"code\":\"BT2\",\"isActive\":true}]");
		
		mstatusField = new DynamicField();
		mstatusField.setDataType("string");
		mstatusField.setDescription("test desc");
		mstatusField.setId("1234");
		mstatusField.setIsActive(true);
		mstatusField.setLangCode("eng");
		mstatusField.setName("martialStatus");
		mstatusField.setValueJson("[{\"value\":\"married\",\"code\":\"MS1\",\"isActive\":true},{\"value\":\"single\",\"code\":\"MS2\",\"isActive\":true}]");
		list.add(mstatusField);
		
		fieldPagedResult = new PageImpl<DynamicField>(list);		
		
		List<IdentitySchema> schemaList = new ArrayList<IdentitySchema>();
		draftSchema = new IdentitySchema();
		draftSchema.setId("11");
		draftSchema.setAdditionalProperties(false);
		draftSchema.setEffectiveFrom(LocalDateTime.now());
		draftSchema.setIdVersion(0);
		//draftSchema.setIdAttributeJson("[]");
		draftSchema.setIsActive(true);
		draftSchema.setIsDeleted(false);
		draftSchema.setSchemaJson("{}");
		draftSchema.setStatus(IdentitySchemaService.STATUS_DRAFT);
		schemaList.add(draftSchema);
		
		publishedSchema = new IdentitySchema();
		publishedSchema.setId("11");
		publishedSchema.setAdditionalProperties(false);
		publishedSchema.setEffectiveFrom(LocalDateTime.now());
		publishedSchema.setIdVersion(0.1);
		//publishedSchema.setIdAttributeJson("[]");
		publishedSchema.setIsActive(true);
		publishedSchema.setIsDeleted(false);
		publishedSchema.setSchemaJson("{}");
		publishedSchema.setStatus(IdentitySchemaService.STATUS_PUBLISHED);
		schemaList.add(publishedSchema);
		
		schemaPagedResult = new PageImpl<IdentitySchema>(schemaList);
		
		 lstui=new ArrayList<UISpecResponseDto>();
		UISpecResponseDto u=new UISpecResponseDto();
		
		u.setCreatedBy("superuser");
		u.setCreatedOn(LocalDateTime.now());
		u.setDescription("test");
		u.setEffectiveFrom(LocalDateTime.now());
		u.setId("test");
		u.setIdentitySchemaId("test");u.setIdSchemaVersion(0.1);
	
		
		u.setStatus("active");
		u.setTitle("test");
		lstui.add(u);
		
		pageRequest = PageRequest.of(0, 10, Sort.by(Direction.fromString("desc"), "cr_dtimes"));
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void fetchAllDynamicFields() throws Exception {		
		Mockito.when(dynamicFieldRepository.findAllDynamicFields(pageRequest)).thenReturn(fieldPagedResult);		
		mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllSchema() throws Exception {		
		Mockito.when(identitySchemaRepository.findAllIdentitySchema(Mockito.anyBoolean(),Mockito.any())).thenThrow(new DataAccessException("...") {});		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/all")).andReturn(),"KER-SCH-004");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchema() throws Exception {		
		Mockito.when(uiSpecService.getUISpec(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(lstui);
		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(is);		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion", "0").param("domain", "").param("type","")).andReturn(),null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchema1() throws Exception {		
		IdentitySchema is=null;
				
		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(is);		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion", "0").param("domain", "").param("type","")).andReturn(),"KER-SCH-007");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchema2() throws Exception {		
	
		Mockito.when(uiSpecService.getUISpec(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(lstui);
		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(is);		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion", "0").param("domain", "").param("type","a")).andReturn(),null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void fetchAllDynamicFieldsByLangCode() throws Exception {		
		Mockito.when(dynamicFieldRepository.findAllDynamicFieldsByLangCode("eng", pageRequest)).thenReturn(fieldPagedResult);		
		mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields")
				.param("langCode", "eng")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void createDynamicField() throws Exception {		
		Mockito.when(dynamicFieldRepository.create(Mockito.any(DynamicField.class))).thenReturn(bloodTypeField);		
		mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+"{\"name\": \"bloodType\",\"dataType\":\"simpleType\",\"description\":\"test desc\",\"isActive\":true,\"langCode\":\"eng\"}}"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateDynamicField() throws Exception {		
		Mockito.when(dynamicFieldRepository.updateDynamicField(Mockito.anyString(), Mockito.anyString(),  Mockito.anyString(), 
				Mockito.anyString(), Mockito.any(LocalDateTime.class), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields")
				.param("id", "1122")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"+
						"{\"name\": \"bloodType\",\"dataType\":\"simpleType\",\"description\":\"test desc\",\"isActive\":false,\"langCode\":\"eng\"}}"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void fetchAllIdentitySchema() throws Exception {		
		Mockito.when(identitySchemaRepository.findAllIdentitySchema(true, pageRequest)).thenReturn(schemaPagedResult);		
		mockMvc.perform(MockMvcRequestBuilders.get("/idschema/all")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void fetchLatestIdentitySchema() throws Exception {		
		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(publishedSchema);		
		mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void createIdentitySchema() throws Exception {		
		Mockito.when(identitySchemaRepository.create(Mockito.any(IdentitySchema.class))).thenReturn(publishedSchema);		
		mockMvc.perform(MockMvcRequestBuilders.post("/idschema")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+"{\"title\":\"title\",\"description\":\"test desc\",\"schema\":[{\"id\":\"IDSchemaVersion\",\"inputRequired\":false,"
						+ "\"type\":\"number\",\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"IDSchemaVersion\",\"language\":\"eng\"}],\"controlType\":\"none\","
						+ "\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":false},"
						+ "{\"id\":\"UIN\",\"inputRequired\":false,\"type\":\"integer\",\"minimum\":0,\"maximum\":0,\"description\":\"\","
						+ "\"label\":[{\"value\":\"UIN\",\"language\":\"eng\"}],\"controlType\":\"none\",\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],"
						+ "\"fieldCategory\":\"Pvt\",\"required\":false},{\"id\":\"fullName\",\"inputRequired\":true,\"type\":\"simpleType\","
						+ "\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"Full Name\",\"language\":\"eng\"}],\"controlType\":\"textbox\",\"fieldType\":\"default\","
						+ "\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":true}],"
						+ "\"effectiveFrom\":\"2018-12-17T07:15:06.724Z\"}}")
				).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
		
	@Test
	@WithUserDetails("global-admin")
	public void updateIdentitySchema() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/idschema")
				.param("id", "test-test-test-test")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+"{\"title\":\"MOSIP ID Schema\",\"description\":\"test desc\",\"schema\":[{\"id\":\"IDSchemaVersion\",\"inputRequired\":false,"
						+ "\"type\":\"number\",\"minimum\":0,\"maximum\":0,\"description\":\"\",\"labelName\":[{\"value\":\"IDSchemaVersion\",\"language\":\"eng\"}],\"controlType\":\"none\","
						+ "\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":false},"
						+ "{\"id\":\"UIN\",\"inputRequired\":false,\"type\":\"integer\",\"minimum\":0,\"maximum\":0,\"description\":\"\","
						+ "\"labelName\":[{\"value\":\"UIN\",\"language\":\"eng\"}],\"controlType\":\"none\",\"fieldType\":\"default\",\"format\":\"none\",\"validators\":[],"
						+ "\"fieldCategory\":\"Pvt\",\"required\":false},{\"id\":\"fullName\",\"inputRequired\":true,\"type\":\"simpleType\","
						+ "\"minimum\":0,\"maximum\":0,\"description\":\"\",\"label\":[{\"value\":\"Full Name\",\"language\":\"eng\"}],\"controlType\":\"textbox\",\"fieldType\":\"default\","
						+ "\"format\":\"none\",\"validators\":[],\"fieldCategory\":\"Pvt\",\"required\":true}],"
						+ "\"effectiveFrom\":\"2018-12-17T07:15:06.724Z\"}}")
				).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void publishIdentitySchema() throws Exception {		
		Mockito.when(identitySchemaRepository.publishIdentitySchema(Mockito.anyString(), Mockito.anyString(), 
				Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.anyString(), Mockito.anyDouble())).thenReturn(1);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/idschema/publish")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"string\",\"version\":\"string\",\"requesttime\":\"2018-12-17T07:15:06.724Z\",\"request\":"
						+"{\"id\":\"test-test-test-test\",\"effectiveFrom\":\"2048-12-17T07:15:06.724Z\"}}")
				).andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void deleteIdentitySchema() throws Exception {		
		Mockito.when(identitySchemaRepository.publishIdentitySchema(Mockito.anyString(), Mockito.anyString(), 
				Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.anyString(), Mockito.anyDouble())).thenReturn(1);		
		mockMvc.perform(MockMvcRequestBuilders.delete("/idschema")
				.param("id", "test-test-test-test")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
