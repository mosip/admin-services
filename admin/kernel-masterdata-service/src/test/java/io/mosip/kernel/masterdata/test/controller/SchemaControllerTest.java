package io.mosip.kernel.masterdata.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.RequestErrorCode;
import io.mosip.kernel.masterdata.constant.SchemaErrorCode;
import io.mosip.kernel.masterdata.dto.IdSchemaPublishDto;
import io.mosip.kernel.masterdata.dto.IdentitySchemaDto;
import io.mosip.kernel.masterdata.entity.DocumentCategory;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchemaControllerTest extends AbstractTest {

	private RequestWrapper<IdentitySchemaDto> identitySchemaRequestWrapper;
	
	private RequestWrapper<IdentitySchemaDto> identitySchemaRequestWrapper1;
	
	private RequestWrapper<IdSchemaPublishDto> idSchemaPublishRequestWrapper;
	
	private Page<IdentitySchema> pagedResult;
	
//	private IdentitySchemaDto identitySchemaDto;
	
	private IdentitySchema entity;
	private IdentitySchema entity1;
	
	private String id = UUID.randomUUID().toString();
	private String id1 = "6b11840a-96e5-4b21-95d8-8d7177ab9749";
	
	@MockBean
	private IdentitySchemaRepository identitySchemaRepository;
	
	@Before
	public void setUp() {
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		identitySchemaRequestWrapper = new RequestWrapper<>();
		IdentitySchemaDto identitySchemaDto = new IdentitySchemaDto();
		String title = "Mosip Identity", description = "Mosip Sample identity", 
//				schema = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-07\\/schema#\",\"description\":\"MOSIP Sample identity\",\"additionalProperties\":false,\"title\":\"MOSIP identity\",\"type\":\"object\",\"definitions\":{\"simpleType\":{\"uniqueItems\":true,\"additionalItems\":false,\"type\":\"array\",\"items\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"language\",\"value\"],\"properties\":{\"language\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"}}}},\"documentType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"type\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"},\"refNumber\":{\"type\":\"string\"}}},\"biometricsType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"version\":{\"type\":\"number\",\"minimum\":0},\"value\":{\"type\":\"string\"}}}},\"properties\":{\"identity\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"IDSchemaVersion\",\"fullName\",\"dateOfBirth\",\"gender\",\"addressLine1\",\"addressLine2\",\"addressLine3\",\"region\",\"province\",\"city\",\"zone\",\"postalCode\",\"phone\",\"email\",\"proofOfIdentity\",\"individualBiometrics\"],\"properties\":{\"proofOfAddress\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"gender\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"city\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"postalCode\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[(?i)A-Z0-9]{5}$|^NA$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"proofOfException-1\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"referenceIdentityNumber\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^([0-9]{10,30})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"kyc\",\"type\":\"string\",\"fieldType\":\"default\"},\"individualBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"province\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"zone\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfDateOfBirth\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"addressLine1\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine2\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"residenceStatus\":{\"bioAttributes\":[],\"fieldCategory\":\"kyc\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine3\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"email\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[A-Za-z0-9_\\\\-]+(\\\\.[A-Za-z0-9_]+)*@[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_]+)*(\\\\.[a-zA-Z]{2,})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerRID\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"fullName\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"dateOfBirth\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(1869|18[7-9][0-9]|19[0-9][0-9]|20[0-9][0-9])\\/([0][1-9]|1[0-2])\\/([0][1-9]|[1-2][0-9]|3[01])$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"individualAuthBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"introducerUIN\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"proofOfIdentity\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"IDSchemaVersion\":{\"bioAttributes\":[],\"fieldCategory\":\"none\",\"format\":\"none\",\"type\":\"number\",\"fieldType\":\"default\",\"minimum\":0},\"proofOfException\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"phone\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[+]*([0-9]{1})([0-9]{9})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerName\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfRelationship\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"UIN\":{\"bioAttributes\":[],\"fieldCategory\":\"none\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"region\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"}}}}}";
		schema = "{\"$schema\":\"schema\",\"description\":\"MOSIP Sample identity\",\"additionalProperties\":false,\"title\":\"MOSIP identity\",\"type\":\"object\",\"definitions\":{\"simpleType\":{\"uniqueItems\":true,\"additionalItems\":false,\"type\":\"array\",\"items\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"language\",\"value\"],\"properties\":{\"language\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"}}}},\"documentType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"type\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"},\"refNumber\":{\"type\":\"string\"}}},\"biometricsType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"version\":{\"type\":\"number\",\"minimum\":0},\"value\":{\"type\":\"string\"}}}},\"properties\":{\"identity\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"IDSchemaVersion\",\"fullName\",\"dateOfBirth\",\"gender\",\"addressLine1\",\"addressLine2\",\"addressLine3\",\"region\",\"province\",\"city\",\"zone\",\"postalCode\",\"phone\",\"email\",\"proofOfIdentity\",\"individualBiometrics\"],\"properties\":{\"proofOfAddress\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"gender\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"city\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"postalCode\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[(?i)A-Z0-9]{5}$|^NA$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"proofOfException-1\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"referenceIdentityNumber\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^([0-9]{10,30})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"kyc\",\"type\":\"string\",\"fieldType\":\"default\"},\"individualBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"province\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"zone\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfDateOfBirth\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"addressLine1\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine2\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"residenceStatus\":{\"bioAttributes\":[],\"fieldCategory\":\"kyc\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine3\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"email\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[A-Za-z0-9_\\\\-]+(\\\\.[A-Za-z0-9_]+)*@[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_]+)*(\\\\.[a-zA-Z]{2,})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerRID\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"fullName\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{3,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"dateOfBirth\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(1869|18[7-9][0-9]|19[0-9][0-9]|20[0-9][0-9])\\/([0][1-9]|1[0-2])\\/([0][1-9]|[1-2][0-9]|3[01])$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"individualAuthBiometrics\":{\"bioAttributes\":[\"leftEye\",\"rightEye\",\"rightIndex\",\"rightLittle\",\"rightRing\",\"rightMiddle\",\"leftIndex\",\"leftLittle\",\"leftRing\",\"leftMiddle\",\"leftThumb\",\"rightThumb\",\"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"introducerUIN\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"proofOfIdentity\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"IDSchemaVersion\":{\"bioAttributes\":[],\"fieldCategory\":\"none\",\"format\":\"none\",\"type\":\"number\",\"fieldType\":\"default\",\"minimum\":0},\"proofOfException\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"phone\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^[+]*([0-9]{1})([0-9]{9})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"introducerName\":{\"bioAttributes\":[],\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfRelationship\":{\"bioAttributes\":[],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"UIN\":{\"bioAttributes\":[],\"fieldCategory\":\"none\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"region\":{\"bioAttributes\":[],\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"}}}}}";
		identitySchemaDto.setTitle(title);
		identitySchemaDto.setDescription(description);
		identitySchemaDto.setSchema(schema);
		identitySchemaRequestWrapper.setRequest(identitySchemaDto);
		idSchemaPublishRequestWrapper = new RequestWrapper<>();
		IdSchemaPublishDto idSchemaPublishDto = new IdSchemaPublishDto();
		entity = new IdentitySchema();
		entity.setTitle(title);
		entity.setDescription(description);
		entity.setSchemaJson(schema);
		
		entity.setIsActive(false);
		entity.setStatus("DRAFT");
		entity.setIdVersion(0);
		entity.setId(id);
		entity.setLangCode("eng");
		entity.setIsDeleted(false);
		entity.setAdditionalProperties(false);
		entity1 = new IdentitySchema();
		entity1.setTitle(title);
		entity1.setDescription(description);
		entity1.setSchemaJson(schema);
		
		entity1.setIsActive(false);
		entity1.setStatus("PUBLISHED");
		entity1.setIdVersion(0);
		entity1.setId(id);
		entity1.setLangCode("eng");
		entity1.setIsDeleted(false);
		entity1.setAdditionalProperties(false);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void notFoundWhenGetLatestSchema() throws Exception {
		//given
		String schemaVersion = "1";
		//when
		String uri = "/idschema/latest";
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get(uri).param("schemaVersion", schemaVersion)
						.contentType(MediaType.APPLICATION_JSON)).andReturn(),
				SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void insertExceptionWhenSaveSchema() throws Exception {
		//when
		String uri = "/idschema";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper1));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_INSERT_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void sucessWhenSaveSchema() throws Exception {
		
		when(identitySchemaRepository.create(Mockito.any())).thenReturn(entity);
		MasterDataTest.checkResponse(
				mockMvc.perform(post("/idschema").contentType(MediaType.APPLICATION_JSON)
						.content(mapToJson(identitySchemaRequestWrapper)))
						.andExpect(status().isOk()).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void parseErrorWhenSaveSchema() throws Exception {
		//when
		String uri = "/idschema";
		String schema = "{schema: schema}";
		identitySchemaRequestWrapper.getRequest().setSchema(schema);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				SchemaErrorCode.VALUE_PARSE_ERROR.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void invalidFormatWhenSaveSchema() throws Exception {
		//when
		String uri = "/idschema";
		String schema = null;
		identitySchemaRequestWrapper.getRequest().setSchema(schema);
		when(identitySchemaRepository.create(Mockito.any())).thenReturn(entity);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn(),
				RequestErrorCode.REQUEST_DATA_NOT_VALID.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateExceptionWhenUpdateSchema() throws Exception {
		//when
		String uri = "/idschema";
		String id = "1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri).param("id", id)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper1));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void schemaEffectiveFromIsOlderExceptionWhenUpdateSchema() throws Exception {
		//when
		String uri = "/idschema";
		String id = "1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri).param("id", id)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper1));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_EFFECTIVE_FROM_IS_OLDER.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tAlreadyPublishedWhenUpdateSchema() throws Exception {
		//when
		String uri = "/idschema";
		when(identitySchemaRepository.findIdentitySchemaById(Mockito.any())).thenReturn(entity1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri).param("id", id1)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void successWhenUpdateSchema() throws Exception {
		//when
		String uri = "/idschema";
		when(identitySchemaRepository.findIdentitySchemaById(Mockito.any())).thenReturn(entity);
		when(identitySchemaRepository.save(Mockito.any())).thenReturn(entity);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri).param("id", entity.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void schemaNotFoundWhenUpdateSchema() throws Exception {
		//when
		String uri = "/idschema";
		String id = "1001";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri).param("id", id)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(identitySchemaRequestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void fetchExceptionWhenGetAllSchema() throws Exception {
		//given
		//when
		String uri = "/idschema/all";
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)).andReturn(),
				SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void schemaNotFoundWhenGetLatestSchema() throws Exception {
		//given
		String domain = "registration-client", schemaVersion = "", type = "";
		//when
		String uri = "/idschema/latest";
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get(uri).param("domain", domain)
						.param("schemaVersion", schemaVersion).param("type", type)
						.contentType(MediaType.APPLICATION_JSON)).andReturn(),
				SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t0SuccessWhengetLatestSchema() throws Exception {
		//given
		String domain = "registration-client", schemaVersion = "0";
		//when
		String uri = "/idschema/latest?schemaVersion=" + schemaVersion;
		//then
		
		when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(entity);
		MvcResult result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		
		ResponseWrapper<?> responseWrapper = mapFromJson(result.getResponse().getContentAsString(),
				ResponseWrapper.class);
		LinkedHashMap<String, Object> obj =  (LinkedHashMap<String, Object>) responseWrapper.getResponse();
		id1 = (String) obj.get("id");
		MasterDataTest.checkResponse(result, null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void schemaNotFoundExceptionWhenGetLatestSchema() throws Exception {
		//given
		String schemaVersion = "1", type = "";
		//when
		String uri = "/idschema/latest";
		when(identitySchemaRepository.findPublishedIdentitySchema(schemaVersion)).thenReturn(entity);
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(get(uri)
						.param("schemaVersion", schemaVersion).param("type", type)
						.contentType(MediaType.APPLICATION_JSON)).andReturn(),
				SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
}
