package io.mosip.kernel.masterdata.test.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.UISpecErrorCode;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.entity.UISpec;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.repository.UISpecRepository;
import io.mosip.kernel.masterdata.service.IdentitySchemaService;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecPublishDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Nagarjuna
 *
 */
@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UISpecServiceTest {
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;
	
	@MockBean
	private TemplateService templateService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@MockBean
	private IdentitySchemaRepository identitySchemaRepository;

	@MockBean
	private UISpecRepository uiSpecRepository;

	@Autowired
	private UISpecService uiSpecService;
	List<UISpec> uiSpecs = new ArrayList<>();
	UISpec draftedUISpec = null;
	UISpec publishedUISpec = null;
	private IdentitySchema publishedSchema = null;
	private Page<UISpec> uiSpecPagedResults = null;
	PageRequest pageRequest = null;

	@Before
	public void setUp() {
		publishedSchema = new IdentitySchema();
		publishedSchema.setId("12");
		publishedSchema.setAdditionalProperties(false);
		publishedSchema.setEffectiveFrom(LocalDateTime.now());
		publishedSchema.setIdVersion(0.1);		
		publishedSchema.setIsActive(true);
		publishedSchema.setIsDeleted(false);
		publishedSchema.setSchemaJson("{}");
		publishedSchema.setStatus(IdentitySchemaService.STATUS_PUBLISHED);

		draftedUISpec = new UISpec();
		draftedUISpec.setId("11");
		draftedUISpec.setEffectiveFrom(LocalDateTime.now());
		draftedUISpec.setVersion(0);
		draftedUISpec.setJsonSpec("[]");
		draftedUISpec.setIsActive(false);
		draftedUISpec.setIsDeleted(false);
		draftedUISpec.setStatus(IdentitySchemaService.STATUS_DRAFT);
		uiSpecs.add(draftedUISpec);

		publishedUISpec = new UISpec();
		publishedUISpec.setId("13");
		publishedUISpec.setEffectiveFrom(LocalDateTime.now());
		publishedUISpec.setVersion(0);
		publishedUISpec.setJsonSpec("[]");
		publishedUISpec.setIsActive(true);
		publishedUISpec.setIsDeleted(false);
		publishedUISpec.setStatus(IdentitySchemaService.STATUS_PUBLISHED);
		uiSpecs.add(publishedUISpec);

		uiSpecPagedResults = new PageImpl<>(uiSpecs);
		pageRequest = PageRequest.of(0, 10, Sort.by(Direction.fromString("desc"), "cr_dtimes"));
	}

	@Test
	public void testDefineUISpec_validRequest_shouldCreateDraft() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.create(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test
	public void testDefineUISpec_emptyJsonSpec_shouldReturnError() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.create(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson(""));
		request.setTitle("UISPEC_REG_CLIENT");
		try {
			uiSpecService.defineUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(), e.getErrorCode());
		}
	}

	@Test(expected = DataNotFoundException.class)
	public void testDefineUISpec_invalidIdentitySchemaId_shouldThrowException() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("13")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test(expected = MasterDataServiceException.class)
	public void testDefineUISpec_saveFails_shouldThrowException() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenThrow(DataAccessLayerException.class);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateUISpec_validRequest_shouldUpdateDetails() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateUISpec_publishedSpec_throwsException() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(publishedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		try {
			uiSpecService.updateUISpec(Mockito.anyString(), request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(), e.getErrorCode());
		}
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("global-admin")
	public void testUpdateUISpec_notFound_shouldThrowException() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		// Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test(expected = DataNotFoundException.class)
	@WithUserDetails("global-admin")
	public void testUpdateUISpec_invalidIdentitySchemaId_shouldThrowException() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("13");
		request.setJsonspec(getValidJson("[]"));
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_nonExistentSpec_shouldThrowException() {
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now().minusDays(1));
		try {
			uiSpecService.publishUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_EFFECTIVE_FROM_IS_OLDER.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_invalidEffectiveFrom_shouldThrowException() {
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())).plusHours(5));
		try {
			uiSpecService.publishUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_alreadyPublished_throwsException() {
		Mockito.when(uiSpecRepository.findUISpecById("13")).thenReturn(publishedUISpec);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())).plusHours(5));
		request.setId("13");
		try {
			uiSpecService.publishUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_successfulPublish_updatesSpec() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())).plusHours(5));
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_successfulPublish_updatesSpec_Success() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())).thenReturn(uiSpecs);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())).plusHours(5));
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testPublishUISpec_noPreviousVersion_updatesSpec() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())).thenReturn(null);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())).plusHours(5));
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteUISpec_shouldDeleteSuccessfully() {
		Mockito.when(uiSpecRepository.deleteUISpec(Mockito.anyString(), Mockito.any(LocalDateTime.class),
				Mockito.anyString())).thenReturn(1);
		String id = uiSpecService.deleteUISpec("1");
		assertEquals("1", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteUISpec_nonExistentSpec_throwsException() {
		Mockito.when(uiSpecRepository.deleteUISpec(Mockito.anyString(), Mockito.any(LocalDateTime.class),
				Mockito.anyString())).thenReturn(0);
		try {
			uiSpecService.deleteUISpec("1");
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteUISpec_dataAccessError_shouldThrowException() {
		Mockito.when(uiSpecRepository.deleteUISpec(Mockito.anyString(), Mockito.any(LocalDateTime.class),
				Mockito.anyString())).thenThrow(DataAccessLayerException.class);
		try {
			uiSpecService.deleteUISpec("1");
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetLatestUISpec_noPublishedSpec_throwsException() {
		Mockito.when((uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())))
				.thenReturn(new ArrayList<>());
		try {
			uiSpecService.getLatestUISpec(Mockito.anyString());
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetLatestUISpec_dataAccessError_throwsException() {
		Mockito.when((uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())))
				.thenThrow(DataAccessLayerException.class);
		try {
			uiSpecService.getLatestUISpec(Mockito.anyString());
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetUISpec_shouldRetrievePublishedSpecs() {
		List<UISpec> publishedUISpecs = new ArrayList<>();
		publishedUISpecs.add(publishedUISpec);
		Mockito.when(uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString()))
				.thenReturn(publishedUISpecs);
		List<UISpecResponseDto> response = uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString());
		assertEquals(UISpecService.STATUS_PUBLISHED, response.get(0).getStatus());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetUISpec_noPublishedSpecs_throwsException() {
		Mockito.when(uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString())).thenReturn(null);
		try {
			uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString());
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetUISpecs_shouldRetrieveMultipleSpecs() {
		Mockito.when(uiSpecRepository.findAllUISpecs(true, pageRequest)).thenReturn(uiSpecPagedResults);
		PageDto<UISpecResponseDto> resp = uiSpecService.getAllUISpecs(0, 10, "cr_dtimes", "desc");
		assertEquals(2, resp.getTotalItems());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetUISpecs_dataAccessError_throwsException() {
		Mockito.when(uiSpecRepository.findAllUISpecs(true, pageRequest)).thenThrow(DataAccessLayerException.class);
		try {
			uiSpecService.getAllUISpecs(0, 10, "cr_dtimes", "desc");
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	private JsonNode getValidJson(String jsonInString) {
		try {
			return objectMapper.readTree(jsonInString);
		} catch (IOException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorMessage());
		}
	}

//	@Test
//	@WithUserDetails("global-admin")
//	public void getUISpecTest_02() {		
//		Mockito.when(
//				uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(publishedUISpec);
//		List<UISpecResponseDto> response = uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString(),Mockito.anyString());
//		assertEquals(UISpecService.STATUS_PUBLISHED, response.get(0).getStatus());
//	}
//
//	@Test
//	@WithUserDetails("global-admin")
//	public void getUISpecTest_03() {
//		Mockito.when(
//				uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(null);
//		try {
//			uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString());
//		} catch (DataNotFoundException e) {
//			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
//		}
//	}
//
//	@Test
//	@WithUserDetails("global-admin")
//	public void getUISpecTest_04() {
//		List<UISpec> specsFromDb = new ArrayList<UISpec>();
//		specsFromDb.add(publishedUISpec);
//		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(specsFromDb);
//		List<UISpecResponseDto> response = uiSpecService.getUISpec(Mockito.anyString(), Mockito.anyString());
//		assertEquals(UISpecService.STATUS_PUBLISHED, response.get(0).getStatus());
//	}
//
//	@Test
//	@WithUserDetails("global-admin")
//	public void getUISpecTest_05() {
//		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(null);
//		try {
//			uiSpecService.getUISpec(Mockito.anyString(), Mockito.anyString());
//		} catch (DataNotFoundException e) {
//			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
//		}
//	}
}
