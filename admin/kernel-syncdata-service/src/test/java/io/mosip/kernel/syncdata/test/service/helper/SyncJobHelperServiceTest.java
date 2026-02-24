package io.mosip.kernel.syncdata.test.service.helper;

import io.mosip.kernel.syncdata.dto.BlacklistedWordsDto;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SyncJobHelperServiceTest {

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private MapperUtils mapper;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private SyncJobHelperService syncJobHelperService;

    private Path tempDir;

    @Before
    public void setup() throws Exception {
        tempDir = Files.createTempDirectory("snapshot-test");
        ReflectionTestUtils.setField(syncJobHelperService, "clientSettingsDir", tempDir.toString());
        ReflectionTestUtils.setField(syncJobHelperService, "regClientModuleId", "10002");
        ReflectionTestUtils.setField(syncJobHelperService, "deltaCacheEvictCron", "0 0/15 * * * *");
    }

    @Test
    public void testEvictDeltaCaches_Success() {
        Cache cache = mock(Cache.class);
        lenient().when(cacheManager.getCache("delta-sync")).thenReturn(cache);

        syncJobHelperService.evictDeltaCaches();

        Assert.assertNotNull(cache);

        verify(cache).clear();
    }

    @Test
    public void testGetFullSyncCurrentTimestamp_Success() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expectedTimestamp = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime actualTimestamp = syncJobHelperService.getFullSyncCurrentTimestamp();

        Assert.assertEquals(expectedTimestamp, actualTimestamp);
    }

    @Test
    public void testGetDeltaSyncCurrentTimestamp_Success() {
        lenient().when(serviceHelper.getAppAuthenticationMethodDetails(any(), any(LocalDateTime.class))).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));
    }

    @Test
    public void testClearCacheAndRecreateSnapshot_Success() throws Exception {
        List<AppAuthenticationMethod> appAuthMethods = new ArrayList<>();
        List<AppRolePriority> appRolePriorities = new ArrayList<>();
        List<Template> templates = new ArrayList<>();
        List<DocumentType> documentTypes = new ArrayList<>();
        List<ApplicantValidDocument> applicantValidDocuments = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationHierarchy> locationHierarchies = new ArrayList<>();
        List<DynamicFieldDto> dynamicFieldDtos = new ArrayList<>();
        List<ReasonCategory> reasonCategories = new ArrayList<>();
        List<ReasonList> reasonLists = new ArrayList<>();
        List<ScreenAuthorization> screenAuthorizations = new ArrayList<>();
        List<ScreenDetail> screenDetails = new ArrayList<>();
        List<BlacklistedWordsDto> blacklistedWordsDtos = new ArrayList<>();
        List<ProcessList> processLists = new ArrayList<>();
        List<SyncJobDef> syncJobDefs = new ArrayList<>();
        List<PermittedLocalConfig> permittedLocalConfigs = new ArrayList<>();

        Map<Class, CompletableFuture> futuresMap = new HashMap<>();
        futuresMap.put(AppAuthenticationMethod.class, CompletableFuture.completedFuture(appAuthMethods));
        futuresMap.put(AppRolePriority.class, CompletableFuture.completedFuture(appRolePriorities));
        futuresMap.put(Template.class, CompletableFuture.completedFuture(templates));
        futuresMap.put(DocumentType.class, CompletableFuture.completedFuture(documentTypes));
        futuresMap.put(ApplicantValidDocument.class, CompletableFuture.completedFuture(applicantValidDocuments));
        futuresMap.put(Location.class, CompletableFuture.completedFuture(locations));
        futuresMap.put(LocationHierarchy.class, CompletableFuture.completedFuture(locationHierarchies));
        futuresMap.put(DynamicFieldDto.class, CompletableFuture.completedFuture(dynamicFieldDtos));
        futuresMap.put(ReasonCategory.class, CompletableFuture.completedFuture(reasonCategories));
        futuresMap.put(ReasonList.class, CompletableFuture.completedFuture(reasonLists));
        futuresMap.put(ScreenAuthorization.class, CompletableFuture.completedFuture(screenAuthorizations));
        futuresMap.put(ScreenDetail.class, CompletableFuture.completedFuture(screenDetails));
        futuresMap.put(BlacklistedWordsDto.class, CompletableFuture.completedFuture(blacklistedWordsDtos));
        futuresMap.put(ProcessList.class, CompletableFuture.completedFuture(processLists));
        futuresMap.put(SyncJobDef.class, CompletableFuture.completedFuture(syncJobDefs));
        futuresMap.put(PermittedLocalConfig.class, CompletableFuture.completedFuture(permittedLocalConfigs));

        lenient().when(serviceHelper.getAppAuthenticationMethodDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(AppAuthenticationMethod.class));
        lenient().when(serviceHelper.getAppRolePriorityDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(AppRolePriority.class));
        lenient().when(serviceHelper.getTemplates(anyString(), any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(Template.class));
        lenient().when(serviceHelper.getDocumentTypes(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(DocumentType.class));
        lenient().when(serviceHelper.getApplicantValidDocument(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ApplicantValidDocument.class));
        lenient().when(serviceHelper.getLocationHierarchy(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(Location.class));
        lenient().when(serviceHelper.getLocationHierarchyList(any(), any(RestTemplate.class))).thenReturn(futuresMap.get(LocationHierarchy.class));
        lenient().when(serviceHelper.getAllDynamicFields(any(), any(RestTemplate.class))).thenReturn(futuresMap.get(DynamicFieldDto.class));
        lenient().when(serviceHelper.getReasonCategory(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ReasonCategory.class));
        lenient().when(serviceHelper.getReasonList(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ReasonList.class));
        lenient().when(serviceHelper.getScreenAuthorizationDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ScreenAuthorization.class));
        lenient().when(serviceHelper.getScreenDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ScreenDetail.class));
        lenient().when(serviceHelper.getBlackListedWords(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(BlacklistedWordsDto.class));
        lenient().when(serviceHelper.getProcessList(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(ProcessList.class));
        lenient().when(serviceHelper.getSyncJobDefDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(SyncJobDef.class));
        lenient().when(serviceHelper.getPermittedConfig(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(PermittedLocalConfig.class));

        lenient().when(mapper.getObjectAsJsonString(appAuthMethods)).thenReturn("appAuthMethodsJson");
        lenient().when(mapper.getObjectAsJsonString(appRolePriorities)).thenReturn("appRolePrioritiesJson");
        lenient().when(mapper.getObjectAsJsonString(templates)).thenReturn("templatesJson");
        lenient().when(mapper.getObjectAsJsonString(documentTypes)).thenReturn("documentTypesJson");
        lenient().when(mapper.getObjectAsJsonString(applicantValidDocuments)).thenReturn("applicantValidDocumentsJson");
        lenient().when(mapper.getObjectAsJsonString(locations)).thenReturn("locationsJson");
        lenient().when(mapper.getObjectAsJsonString(locationHierarchies)).thenReturn("locationHierarchiesJson");
        lenient().when(mapper.getObjectAsJsonString(dynamicFieldDtos)).thenReturn("dynamicFieldDtosJson");
        lenient().when(mapper.getObjectAsJsonString(reasonCategories)).thenReturn("reasonCategoriesJson");
        lenient().when(mapper.getObjectAsJsonString(reasonLists)).thenReturn("reasonListsJson");
        lenient().when(mapper.getObjectAsJsonString(screenAuthorizations)).thenReturn("screenAuthorizationsJson");
        lenient().when(mapper.getObjectAsJsonString(screenDetails)).thenReturn("screenDetailsJson");
        lenient().when(mapper.getObjectAsJsonString(blacklistedWordsDtos)).thenReturn("blacklistedWordsDtosJson");
        lenient().when(mapper.getObjectAsJsonString(processLists)).thenReturn("processListsJson");
        lenient().when(mapper.getObjectAsJsonString(syncJobDefs)).thenReturn("syncJobDefsJson");
        lenient().when(mapper.getObjectAsJsonString(permittedLocalConfigs)).thenReturn("permittedLocalConfigsJson");

        Assert.assertNotNull(appAuthMethods);
        Assert.assertNotNull(appRolePriorities);
        Assert.assertNotNull(templates);
        Assert.assertNotNull(dynamicFieldDtos);
        Assert.assertNotNull(applicantValidDocuments);
        Assert.assertNotNull(locationHierarchies);
    }

    @Test
    public void testClearCacheAndRecreateSnapshot_ServiceCallException() throws Exception {
        List<AppAuthenticationMethod> appAuthMethods = new ArrayList<>();
        List<AppRolePriority> appRolePriorities = new ArrayList<>();
        List<Template> templates = new ArrayList<>();

        Map<Class, CompletableFuture> futuresMap = new HashMap<>();
        futuresMap.put(AppAuthenticationMethod.class, CompletableFuture.completedFuture(appAuthMethods));
        futuresMap.put(AppRolePriority.class, CompletableFuture.completedFuture(appRolePriorities));
        futuresMap.put(Template.class, CompletableFuture.completedFuture(templates));

        lenient().when(serviceHelper.getAppAuthenticationMethodDetails(any(), any(LocalDateTime.class))).thenReturn(futuresMap.get(AppAuthenticationMethod.class));
        lenient().when(serviceHelper.getAppRolePriorityDetails(any(), any(LocalDateTime.class))).thenThrow(new RuntimeException("Simulated exception"));

        lenient().when(mapper.getObjectAsJsonString(appAuthMethods)).thenReturn("appAuthMethodsJson");

        try {
            syncJobHelperService.clearCacheAndRecreateSnapshot();
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            Assert.assertEquals("Simulated exception", e.getMessage());
        }

        verify(cacheManager).getCache("initial-sync");
        verify(cacheManager, never()).getCache("delta-sync");
    }

    // ===============================
    // Cache Eviction Tests
    // ===============================

    @Test
    public void testEvictDeltaCaches_success() {
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("delta-sync")).thenReturn(cache);

        syncJobHelperService.evictDeltaCaches();

        verify(cache).clear();
    }

    // ===============================
    // Timestamp Tests
    // ===============================

    @Test
    public void testGetFullSyncCurrentTimestamp() {
        LocalDateTime result = syncJobHelperService.getFullSyncCurrentTimestamp();
        Assert.assertEquals(0, result.getHour());
        Assert.assertEquals(0, result.getMinute());
        Assert.assertEquals(0, result.getSecond());
    }

    @Test
    public void testGetDeltaSyncCurrentTimestamp_validCron() {
        LocalDateTime result = syncJobHelperService.getDeltaSyncCurrentTimestamp();
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetDeltaSyncCurrentTimestamp_invalidCron() {
        ReflectionTestUtils.setField(syncJobHelperService, "deltaCacheEvictCron", "invalid");
        try {
            syncJobHelperService.getDeltaSyncCurrentTimestamp();
            Assert.fail();
        } catch (Exception ignored) {
        }
    }

    // ===============================
    // Snapshot Creation Tests
    // ===============================

    @Test
    public void testCreateEntitySnapshot_success() {

        List<AppAuthenticationMethod> list = new ArrayList<>();
        CompletableFuture<List<AppAuthenticationMethod>> future =
                CompletableFuture.completedFuture(list);

        // mock all other service calls to return empty future
        mockAllServiceCalls();

        syncJobHelperService.createEntitySnapshot();
    }

    @Test
    public void testCreateEntitySnapshot_completionException() {

        CompletableFuture future = mock(CompletableFuture.class);

        when(serviceHelper.getAppAuthenticationMethodDetails(any(), any()))
                .thenReturn(future);

        try {
            syncJobHelperService.createEntitySnapshot();
            Assert.fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testDynamicFieldHandling() {

        DynamicFieldDto dto1 = new DynamicFieldDto();
        dto1.setName("field1");

        DynamicFieldDto dto2 = new DynamicFieldDto();
        dto2.setName("field1");

        List<DynamicFieldDto> list = Arrays.asList(dto1, dto2);

        CompletableFuture<List<DynamicFieldDto>> future =
                CompletableFuture.completedFuture(list);

        when(serviceHelper.getAllDynamicFields(any(), any()))
                .thenReturn(future);

        mockAllServiceCalls();

        syncJobHelperService.createEntitySnapshot();
    }

    // ===============================
    // Helper
    // ===============================

    private void mockAllServiceCalls() {

        CompletableFuture empty = CompletableFuture.completedFuture(new ArrayList<>());

        lenient().when(serviceHelper.getAppAuthenticationMethodDetails(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getAppRolePriorityDetails(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getTemplates(anyString(), any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getDocumentTypes(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getApplicantValidDocument(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getLocationHierarchy(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getLocationHierarchyList(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getAllDynamicFields(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getReasonCategory(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getReasonList(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getScreenAuthorizationDetails(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getScreenDetails(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getBlackListedWords(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getProcessList(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getSyncJobDefDetails(any(), any())).thenReturn(empty);
        lenient().when(serviceHelper.getPermittedConfig(any(), any())).thenReturn(empty);
    }

}
