package io.mosip.kernel.masterdata.test.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.UISpecErrorCode;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.entity.UISpec;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.repository.UISpecRepository;
import io.mosip.kernel.masterdata.service.IdentitySchemaService;
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
@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UISpecServiceTest {

	@MockBean
	private IdentitySchemaRepository identitySchemaRepository;

	@MockBean
	private UISpecRepository uiSpecRepository;

	@Autowired
	private UISpecService uiSpecService;
	List<UISpec> uiSpecs = new ArrayList<UISpec>();
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
		draftedUISpec.setAdditionalProperties(false);
		draftedUISpec.setEffectiveFrom(LocalDateTime.now());
		draftedUISpec.setVersion(0);
		draftedUISpec.setJsonSpec("[]");
		draftedUISpec.setIsActive(false);
		draftedUISpec.setIsDeleted(false);
		draftedUISpec.setStatus(IdentitySchemaService.STATUS_DRAFT);
		uiSpecs.add(draftedUISpec);

		publishedUISpec = new UISpec();
		publishedUISpec.setId("13");
		publishedUISpec.setAdditionalProperties(false);
		publishedUISpec.setEffectiveFrom(LocalDateTime.now());
		publishedUISpec.setVersion(0);
		publishedUISpec.setJsonSpec("[]");
		publishedUISpec.setIsActive(true);
		publishedUISpec.setIsDeleted(false);
		publishedUISpec.setStatus(IdentitySchemaService.STATUS_PUBLISHED);
		uiSpecs.add(publishedUISpec);

		uiSpecPagedResults = new PageImpl<UISpec>(uiSpecs);
		pageRequest = PageRequest.of(0, 10, Sort.by(Direction.fromString("desc"), "cr_dtimes"));
	}

	@Test
	public void defidneUISpecTest() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.create(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test
	public void defidneUISpecTest001() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.create(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("ABC");
		request.setTitle("UISPEC_REG_CLIENT");
		try {
			uiSpecService.defineUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(), e.getErrorCode());
		}
	}

	@Test(expected = DataNotFoundException.class)
	public void defidneUISpecTest_01() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("13")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test(expected = MasterDataServiceException.class)
	public void defidneUISpecTest_02() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenThrow(DataAccessLayerException.class);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT");
		UISpecResponseDto response = uiSpecService.defineUISpec(request);
		assertEquals("DRAFT", response.getStatus());
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateUISpecTest() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateUISpecTest01() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(publishedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		try {
			uiSpecService.updateUISpec(Mockito.anyString(), request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(), e.getErrorCode());
		}
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("global-admin")
	public void updateUISpecTest_01() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.save(Mockito.any(UISpec.class))).thenReturn(draftedUISpec);
		// Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("12");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test(expected = DataNotFoundException.class)
	@WithUserDetails("global-admin")
	public void updateUISpecTest_02() {
		Mockito.when(identitySchemaRepository.findPublishedIdentitySchema("12")).thenReturn(publishedSchema);
		Mockito.when(uiSpecRepository.findUISpecById(Mockito.anyString())).thenReturn(draftedUISpec);
		UISpecDto request = new UISpecDto();
		request.setDescription("UI Spec Description");
		request.setDomain("regclient");
		request.setIdentitySchemaId("13");
		request.setJsonspec("[]");
		request.setTitle("UISPEC_REG_CLIENT_UPDATE");
		UISpecResponseDto response = uiSpecService.updateUISpec(Mockito.anyString(), request);
		assertEquals("UISPEC_REG_CLIENT_UPDATE", response.getTitle());
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpecTest() {
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
	public void publishUISpecTest_01() {
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now());
		try {
			uiSpecService.publishUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpecTest_02() {
		Mockito.when(uiSpecRepository.findUISpecById("13")).thenReturn(publishedUISpec);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now());
		request.setId("13");
		try {
			uiSpecService.publishUISpec(request);
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpecTest_03() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now());
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpecTest_04() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())).thenReturn(uiSpecs);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now());
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishUISpecTest_05() {
		Mockito.when(uiSpecRepository.findUISpecById("11")).thenReturn(draftedUISpec);
		Mockito.when(uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())).thenReturn(null);
		UISpecPublishDto request = new UISpecPublishDto();
		request.setEffectiveFrom(LocalDateTime.now());
		request.setId("11");
		String id = uiSpecService.publishUISpec(request);
		assertEquals("11", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteUISpecTest() {
		Mockito.when(uiSpecRepository.deleteUISpec(Mockito.anyString(), Mockito.any(LocalDateTime.class),
				Mockito.anyString())).thenReturn(1);
		String id = uiSpecService.deleteUISpec("1");
		assertEquals("1", id);
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteUISpecTest_01() {
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
	public void deleteUISpecTest_02() {
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
	public void getLatestUISpecTest_01() {
		Mockito.when((uiSpecRepository.findLatestPublishedUISpec(Mockito.anyString())))
				.thenReturn(new ArrayList<UISpec>());
		try {
			uiSpecService.getLatestUISpec(Mockito.anyString());
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}

	}

	@Test
	@WithUserDetails("global-admin")
	public void getLatestUISpecTest_02() {
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
	public void getUISpecTest() {
		List<UISpec> publishedUISpecs = new ArrayList<UISpec>();
		publishedUISpecs.add(publishedUISpec);
		Mockito.when(uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString()))
				.thenReturn(publishedUISpecs);
		List<UISpecResponseDto> response = uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString());
		assertEquals(UISpecService.STATUS_PUBLISHED, response.get(0).getStatus());
	}

	@Test
	@WithUserDetails("global-admin")
	public void getUISpecTest_01() {
		Mockito.when(uiSpecRepository.findPublishedUISpec(Mockito.anyDouble(), Mockito.anyString())).thenReturn(null);
		try {
			uiSpecService.getUISpec(Mockito.anyDouble(), Mockito.anyString());
		} catch (DataNotFoundException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(), e.getErrorCode());
		}
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllUISpecsTest() {
		Mockito.when(uiSpecRepository.findAllUISpecs(true, pageRequest)).thenReturn(uiSpecPagedResults);
		PageDto<UISpecResponseDto> resp = uiSpecService.getAllUISpecs(0, 10, "cr_dtimes", "desc");
		assertEquals(2, resp.getTotalItems());
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllUISpecsTest_01() {
		Mockito.when(uiSpecRepository.findAllUISpecs(true, pageRequest)).thenThrow(DataAccessLayerException.class);
		try {
			uiSpecService.getAllUISpecs(0, 10, "cr_dtimes", "desc");
		} catch (MasterDataServiceException e) {
			assertEquals(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(), e.getErrorCode());
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
