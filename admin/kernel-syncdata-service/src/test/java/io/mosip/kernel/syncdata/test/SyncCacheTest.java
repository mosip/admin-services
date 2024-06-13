package io.mosip.kernel.syncdata.test;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
public class SyncCacheTest {

    @Mock
    CacheManager cacheManager;

    @Mock
    AppAuthenticationMethodRepository appAuthenticationMethodRepository;
    @Mock
    AppRolePriorityRepository appRolePriorityRepository;
    @Mock
    MachineRepository machineRepository;
    @Mock
    RegistrationCenterRepository registrationCenterRepository;
    @Mock
    UserDetailsRepository userDetailsRepository;
    @Mock
    TemplateRepository templateRepository;
    @Mock
    DocumentTypeRepository documentTypeRepository;
    @Mock
    ApplicantValidDocumentRespository applicantValidDocumentRespository;
    @Mock
    ReasonCategoryRepository reasonCategoryRepository;
    @Mock
    ReasonListRepository reasonListRepository;
    @Mock
    ScreenDetailRepository screenDetailRepository;
    @Mock
    ScreenAuthorizationRepository screenAuthorizationRepository;
    @Mock
    BlocklistedWordsRepository blocklistedWordsRepository;
    @Mock
    PermittedLocalConfigRepository permittedLocalConfigRepository;
    @Mock
    ProcessListRepository processListRepository;
    @Mock
    SyncJobDefRepository syncJobDefRepository;
    @Mock
    LocationRepository locationRepository;

    @Mock
    MachineSpecificationRepository machineSpecificationRepository;
    @Mock
    MachineTypeRepository machineTypeRepository;
    @Mock
    RegistrationCenterTypeRepository registrationCenterTypeRepository;

    @Mock
    TemplateFileFormatRepository templateFileFormatRepository;
    @Mock
    ModuleDetailRepository moduleDetailRepository;

    @Mock
    private Cache cache;

    @Before
    public void setUp() {
        when(cacheManager.getCache("delta-sync")).thenReturn(cache);
    }

    @Test
    public void whenFindAllAppAuthMethods_thenResultShouldBePutInCache() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        List<AppAuthenticationMethod> list = appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "app_authentication_method").size());
    }

    @Test
    public void whenFindChangedAppAuthMethods_thenResultShouldNotBePutInCache() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "app_authentication_method").size());
    }

    @Test
    public void findMaxChangedDate_AppAuthMethod_thenOnlyCreatedUpdatedDateTimeIsCached() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        EntityDtimes result = appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "app_authentication_method"));
    }

    @Test
    public void findMaxChangedDate_AppAuthMethod_thenBothCreatedUpdatedDateTimeIsCached() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        AppAuthenticationMethod entity = getAppAuthMethods("test1");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        appAuthenticationMethodRepository.save(entity);

        EntityDtimes result = appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "app_authentication_method"));
    }

    

    @Test
    public void whenFindAllAppRolePriority_thenResultShouldBePutInCache() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        List<AppRolePriority> list = appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "app_role_priority").size());
    }

    @Test
    public void whenFindChangedAppRolePriority_thenResultShouldNotBePutInCache() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "app_role_priority").size());
    }

    @Test
    public void findMaxChangedDate_AppRolePriority_thenOnlyCreatedUpdatedDateTimeIsCached() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        EntityDtimes result = appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "app_role_priority"));
    }

    @Test
    public void findMaxChangedDate_AppRolePriority_thenBothCreatedUpdatedDateTimeIsCached() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        AppRolePriority entity = getAppRolePriority("test1");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        appRolePriorityRepository.save(entity);

        EntityDtimes result = appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "app_role_priority"));
    }

    

    @Test
    public void whenFindAllTemplate_thenResultShouldBePutInCache() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        List<Template> list = templateRepository.findAllLatestCreatedUpdateDeletedByModule(lastUpdatedTime, currentTime, "test");
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "template").size());
    }

    @Test
    public void whenFindChangedTemplate_thenResultShouldNotBePutInCache() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        templateRepository.findAllLatestCreatedUpdateDeletedByModule(lastUpdatedTime, currentTime, "test");
        assertEquals(0, getInitialCachedValue("delta-sync", "template").size());
    }

    @Test
    public void findMaxChangedDate_Template_thenOnlyCreatedUpdatedDateTimeIsCached() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        
        EntityDtimes result = templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "template"));
    }

    @Test
    public void findMaxChangedDate_Template_thenBothCreatedUpdatedDateTimeIsCached() {
        saveModuleDetail();
        Template entity = getTemplate("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        templateRepository.save(entity);

        
        EntityDtimes result = templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "template"));
    }
    

    @Test
    public void whenFindAllJobDef_thenResultShouldBePutInCache() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<SyncJobDef> list = syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "sync_job_def").size());
    }

    @Test
    public void whenFindChangedJobDef_thenResultShouldNotBePutInCache() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "sync_job_def").size());
    }

    @Test
    public void findMaxChangedDate_JobDef_thenOnlyCreatedUpdatedDateTimeIsCached() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        
        EntityDtimes result = syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "sync_job_def"));
    }

    @Test
    public void findMaxChangedDate_JobDef_thenBothCreatedUpdatedDateTimeIsCached() {
        SyncJobDef entity = getSyncJobDef("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        syncJobDefRepository.save(entity);

        
        EntityDtimes result = syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "sync_job_def"));
    }

    

    @Test
    public void whenFindAllScreenDetail_thenResultShouldBePutInCache() {
        screenDetailRepository.save(getScreenDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ScreenDetail> list = screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "screen_detail").size());
    }

    @Test
    public void whenFindChangedScreenDetail_thenResultShouldNotBePutInCache() {
        screenDetailRepository.save(getScreenDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "screen_detail").size());
    }

    @Test
    public void findMaxChangedDate_ScreenDetail_thenOnlyCreatedUpdatedDateTimeIsCached() {
        screenDetailRepository.save(getScreenDetail("test"));
        
        EntityDtimes result = screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "screen_detail"));
    }

    @Test
    public void findMaxChangedDate_ScreenDetail_thenBothCreatedUpdatedDateTimeIsCached() {
        ScreenDetail entity = getScreenDetail("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        screenDetailRepository.save(entity);

        
        EntityDtimes result = screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        
        assertEquals(result, getCachedValue("delta-sync", "screen_detail"));
    }

    

    @Test
    public void whenFindAllScreenAuth_thenResultShouldBePutInCache() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ScreenAuthorization> list = screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "screen_authorization").size());
    }

    @Test
    public void whenFindChangedScreenAuth_thenResultShouldNotBePutInCache() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "screen_authorization").size());
    }

    @Test
    public void findMaxChangedDate_ScreenAuth_thenOnlyCreatedUpdatedDateTimeIsCached() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        
        EntityDtimes result = screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "screen_authorization"));
    }

    @Test
    public void findMaxChangedDate_ScreenAuth_thenBothCreatedUpdatedDateTimeIsCached() {
        ScreenAuthorization entity = getScreenAuthorization("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        screenAuthorizationRepository.save(entity);

        
        EntityDtimes result = screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "screen_authorization"));
    }

    

    @Test
    public void whenFindAllPLC_thenResultShouldBePutInCache() {
        permittedLocalConfigRepository.save(getPermittedLocalConfig("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<PermittedLocalConfig> list = permittedLocalConfigRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "permitted_local_config").size());
    }

    @Test
    public void whenFindChangedPLC_thenResultShouldNotBePutInCache() {
        permittedLocalConfigRepository.save(getPermittedLocalConfig("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        permittedLocalConfigRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "permitted_local_config").size());
    }

    @Test
    public void findMaxChangedDate_PLC_thenOnlyCreatedUpdatedDateTimeIsCached() {
        permittedLocalConfigRepository.save(getPermittedLocalConfig("test"));
        
        EntityDtimes result = permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "permitted_local_config"));
    }

    @Test
    public void findMaxChangedDate_PLC_thenBothCreatedUpdatedDateTimeIsCached() {
        PermittedLocalConfig entity = getPermittedLocalConfig("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        permittedLocalConfigRepository.save(entity);

        
        EntityDtimes result = permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "permitted_local_config"));
    }


    

    @Test
    public void whenFindAllPL_thenResultShouldBePutInCache() {
        processListRepository.save(getProcessList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ProcessList> list = processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "process_list").size());
    }

    @Test
    public void whenFindChangedPL_thenResultShouldNotBePutInCache() {
        processListRepository.save(getProcessList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "process_list").size());
    }

    @Test
    public void findMaxChangedDate_PL_thenOnlyCreatedUpdatedDateTimeIsCached() {
        processListRepository.save(getProcessList("test"));
        
        EntityDtimes result = processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "process_list"));
    }

    @Test
    public void findMaxChangedDate_PL_thenBothCreatedUpdatedDateTimeIsCached() {
        ProcessList entity = getProcessList("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        processListRepository.save(entity);
        
        EntityDtimes result = processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "process_list"));

    }

    

    @Test
    public void whenFindAllBW_thenResultShouldBePutInCache() {
        blocklistedWordsRepository.save(getBlockListedWords("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<BlocklistedWords> list = blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("data-sync", "blocklisted_words").size());
    }

    @Test
    public void whenFindChangedBW_thenResultShouldNotBePutInCache() {
        blocklistedWordsRepository.save(getBlockListedWords("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("data-sync", "blocklisted_words").size());
    }

    @Test
    public void findMaxChangedDate_BW_thenOnlyCreatedUpdatedDateTimeIsCached() {
        blocklistedWordsRepository.save(getBlockListedWords("test"));
        
        EntityDtimes result = blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "blocklisted_words"));
    }

    @Test
    public void findMaxChangedDate_BW_thenBothCreatedUpdatedDateTimeIsCached() {
        BlocklistedWords entity = getBlockListedWords("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        blocklistedWordsRepository.save(entity);

        EntityDtimes result = blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "blocklisted_words"));
    }

    

    @Test
    public void whenFindChangedMachine_thenResultShouldNotBePutInCache() {
        machineRepository.save(getMachine("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        machineRepository.findMachineLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime, "test");
        assertEquals(0, getInitialCachedValue("delta-sync", "machine_master_test").size());
    }

    @Test
    public void findMaxChangedDate_Machine_thenOnlyCreatedUpdatedDateTimeIsCached() {
        machineRepository.save(getMachine("test"));
        
        EntityDtimes result = machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "machine_master"));
    }

    @Test
    public void findMaxChangedDate_Machine_thenBothCreatedUpdatedDateTimeIsCached() {
        Machine entity = getMachine("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        machineRepository.save(entity);

        EntityDtimes result = machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "machine_master"));
    }

    

    @Test
    public void whenFindAllLocation_thenResultShouldBePutInCache() {
        locationRepository.save(getLocation("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<Location> list = locationRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "location").size());
    }

    @Test
    public void whenFindChangedLocation_thenResultShouldNotBePutInCache() {
        locationRepository.save(getLocation("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        locationRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "location").size());
    }

    @Test
    public void findMaxChangedDate_Location_thenOnlyCreatedUpdatedDateTimeIsCached() {
        locationRepository.save(getLocation("test"));
        
        EntityDtimes result = locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "location"));
    }

    @Test
    public void findMaxChangedDate_Location_thenBothCreatedUpdatedDateTimeIsCached() {
        Location entity = getLocation("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        locationRepository.save(entity);

        EntityDtimes result = locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "location"));
    }

    

    @Test
    public void whenFindAllRC_thenResultShouldBePutInCache() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ReasonCategory> list = reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "reason_category").size());
    }

    @Test
    public void whenFindChangedRC_thenResultShouldNotBePutInCache() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "reason_category").size());
    }

    @Test
    public void findMaxChangedDate_RC_thenOnlyCreatedUpdatedDateTimeIsCached() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        
        EntityDtimes result = reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "reason_category"));
    }

    @Test
    public void findMaxChangedDate_RC_thenBothCreatedUpdatedDateTimeIsCached() {
        ReasonCategory entity = getReasonCategory("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        reasonCategoryRepository.save(entity);

        EntityDtimes result = reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "reason_category"));
    }

    

    @Test
    public void whenFindAllRL_thenResultShouldBePutInCache() {
        reasonListRepository.save(getReasonList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ReasonList> list = reasonListRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "reason_list").size());
    }

    @Test
    public void whenFindChangedRL_thenResultShouldNotBePutInCache() {
        reasonListRepository.save(getReasonList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        reasonListRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "reason_list").size());
    }

    @Test
    public void findMaxChangedDate_RL_thenOnlyCreatedUpdatedDateTimeIsCached() {
        reasonListRepository.save(getReasonList("test"));
        
        EntityDtimes result = reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "reason_list"));
    }

    @Test
    public void findMaxChangedDate_RL_thenBothCreatedUpdatedDateTimeIsCached() {
        ReasonList entity = getReasonList("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        reasonListRepository.save(entity);

        EntityDtimes result = reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "reason_list"));
    }

    

    @Test
    public void whenFindAllDT_thenResultShouldBePutInCache() {
        documentTypeRepository.save(getDocumentType("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<DocumentType> list = documentTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "doc_type").size());
    }

    @Test
    public void whenFindChangedDT_thenResultShouldNotBePutInCache() {
        documentTypeRepository.save(getDocumentType("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        documentTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "doc_type").size());
    }

    @Test
    public void findMaxChangedDate_DT_thenOnlyCreatedUpdatedDateTimeIsCached() {
        documentTypeRepository.save(getDocumentType("test"));
        
        EntityDtimes result = documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "doc_type"));
    }

    @Test
    public void findMaxChangedDate_DT_thenBothCreatedUpdatedDateTimeIsCached() {
        DocumentType entity = getDocumentType("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        documentTypeRepository.save(entity);

        EntityDtimes result = documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "doc_type"));
    }

    

    @Test
    public void whenFindAllAVD_thenResultShouldBePutInCache() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        List<ApplicantValidDocument> list = applicantValidDocumentRespository.findAllByTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(list.size(), getInitialCachedValue("delta-sync", "applicant_valid_document").size());
    }

    @Test
    public void whenFindChangedAVD_thenResultShouldNotBePutInCache() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        applicantValidDocumentRespository.findAllByTimeStamp(lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "applicant_valid_document").size());
    }

    @Test
    public void findMaxChangedDate_AVD_thenOnlyCreatedUpdatedDateTimeIsCached() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        
        EntityDtimes result = applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "applicant_valid_document"));
    }

    @Test
    public void findMaxChangedDate_AVD_thenBothCreatedUpdatedDateTimeIsCached() {
        ApplicantValidDocument entity = getApplicantValidDocument("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        applicantValidDocumentRespository.save(entity);

        EntityDtimes result = applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "applicant_valid_document"));
    }


    @Test
    public void findMaxChangedDate_RCenter_thenOnlyCreatedUpdatedDateTimeIsCached() {
        registrationCenterRepository.save(getRegistrationCenter("test"));
        
        EntityDtimes result = registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "registration_center"));
    }

    @Test
    public void findMaxChangedDate_RCenter_thenBothCreatedUpdatedDateTimeIsCached() {
        RegistrationCenter entity = getRegistrationCenter("test");
        entity.setUpdatedBy("test");
        entity.setLocationCode("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        registrationCenterRepository.save(entity);

        EntityDtimes result = registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "registration_center"));
    }

    

    @Test
    public void whenFindAllUD_thenResultShouldBePutInCache() {
        userDetailsRepository.save(getUserDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        userDetailsRepository.findAllLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "user_detail_test").size());
    }

    @Test
    public void whenFindChangedUD_thenResultShouldNotBePutInCache() {
        userDetailsRepository.save(getUserDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        
        userDetailsRepository.findAllLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime);
        assertEquals(0, getInitialCachedValue("delta-sync", "user_detail_test").size());
    }

    @Test
    public void findMaxChangedDate_UD_thenOnlyCreatedUpdatedDateTimeIsCached() {
        userDetailsRepository.save(getUserDetail("test"));
        
        EntityDtimes result = userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "user_detail"));
    }

    @Test
    public void findMaxChangedDate_UD_thenBothCreatedUpdatedDateTimeIsCached() {
        UserDetails entity = getUserDetail("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        userDetailsRepository.save(entity);

        EntityDtimes result = userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        assertEquals(result, getCachedValue("delta-sync", "user_detail"));
    }

    private EntityDtimes getCachedValue(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null) {
            return cache.get(key, EntityDtimes.class) == null ? null : cache.get(key, EntityDtimes.class);
        }
        return null;
    }

    private List getInitialCachedValue(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null) {
            return cache.get(key, List.class) == null ? Collections.emptyList() : cache.get(key, List.class);
        }
        return Collections.emptyList();
    }

    private void evictAllKeys(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.clear();
    }

    private void saveModuleDetail() {
        ModuleDetail moduleDetail = new ModuleDetail();
        moduleDetail.setName("test");
        moduleDetail.setDescription("test");
        moduleDetail.setId("test");
        moduleDetail.setLangCode("ara");
        moduleDetail.setIsActive(true);
        moduleDetail.setCreatedBy("test");
        moduleDetail.setCreatedDateTime(LocalDateTime.now());
        moduleDetailRepository.save(moduleDetail);

        TemplateFileFormat templateFileFormat = new TemplateFileFormat();
        templateFileFormat.setCode("test");
        templateFileFormat.setDescription("test");
        templateFileFormat.setLangCode("eng");
        templateFileFormat.setIsActive(true);
        templateFileFormat.setCreatedBy("test");
        templateFileFormat.setCreatedDateTime(LocalDateTime.now());
        templateFileFormatRepository.save(templateFileFormat);
    }

    private AppAuthenticationMethod getAppAuthMethods(String value) {
        AppAuthenticationMethod method1 = new AppAuthenticationMethod();
        method1.setAppId(value);
        method1.setMethodSequence(1);
        method1.setAuthMethodCode(value);
        method1.setLangCode("eng");
        method1.setProcessId(value);
        method1.setRoleCode(value);
        method1.setIsActive(true);
        method1.setCreatedBy(value);
        method1.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return method1;
    }

    private AppRolePriority getAppRolePriority(String value) {
        AppRolePriority appRolePriority = new AppRolePriority();
        appRolePriority.setAppId(value);
        appRolePriority.setPriority(1);
        appRolePriority.setLangCode("eng");
        appRolePriority.setProcessId(value);
        appRolePriority.setRoleCode(value);
        appRolePriority.setIsActive(true);
        appRolePriority.setCreatedBy(value);
        appRolePriority.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return appRolePriority;
    }

    private Machine getMachine(String value) {
        MachineType machineType = new MachineType();
        machineType.setCode(value);
        machineType.setName(value);
        machineType.setLangCode("eng");
        machineType.setDescription(value);
        machineType.setIsActive(true);
        machineType.setCreatedBy(value);
        machineType.setCreatedDateTime(LocalDateTime.now());
        machineTypeRepository.save(machineType);

        MachineSpecification machineSpecification = new MachineSpecification();
        machineSpecification.setId(value);
        machineSpecification.setMachineTypeCode(value);
        machineSpecification.setDescription(value);
        machineSpecification.setLangCode("eng");
        machineSpecification.setBrand(value);
        machineSpecification.setMachineType(machineType);
        machineSpecification.setModel(value);
        machineSpecification.setIsActive(true);
        machineSpecification.setName(value);
        machineSpecification.setMinDriverversion(value);
        machineSpecification.setCreatedBy(value);
        machineSpecification.setCreatedDateTime(LocalDateTime.now());
        machineSpecificationRepository.save(machineSpecification);

        Machine entity = new Machine();
        entity.setId(value);
        entity.setName(value);
        entity.setPublicKey(value);
        entity.setSignPublicKey(value);
        entity.setKeyIndex(value);
        entity.setSignKeyIndex(value);
        entity.setRegCenterId(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setMachineSpecId(value);
        entity.setLangCode("eng");
        entity.setMachineSpecification(machineSpecification);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        entity.setZoneCode("NTH");
        return entity;
    }

    private RegistrationCenter getRegistrationCenter(String value) {
        RegistrationCenterType registrationCenterType = new RegistrationCenterType();
        registrationCenterType.setCode(value);
        registrationCenterType.setName(value);
        registrationCenterType.setDescr(value);
        registrationCenterType.setLangCode("eng");
        registrationCenterType.setIsActive(true);
        registrationCenterType.setCreatedBy(value);
        registrationCenterType.setCreatedDateTime(LocalDateTime.now());
        registrationCenterTypeRepository.save(registrationCenterType);

        RegistrationCenter entity = new RegistrationCenter();
        entity.setId(value);
        entity.setLangCode("eng");
        entity.setCenterTypeCode(value);
        entity.setName(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setLocationCode("test");
        entity.setHolidayLocationCode(value);
        entity.setRegistrationCenterType(registrationCenterType);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private UserDetails getUserDetail(String value) {
        UserDetails entity = new UserDetails();
        entity.setId(value);
        entity.setRegCenterId(value);
        entity.setLangCode("eng");
        entity.setStatusCode(value);
        entity.setName(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private Template getTemplate(String value) {
        Template entity = new Template();
        entity.setId(value);
        entity.setModuleId(value);
        entity.setFileText(value);
        entity.setModuleName(value);
        entity.setDescription(value);
        entity.setFileFormatCode(value);
        entity.setLangCode("eng");
        entity.setName(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setTemplateTypeCode("test");
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private DocumentType getDocumentType(String value) {
        DocumentType entity = new DocumentType();
        entity.setName(value);
        entity.setCode(value);
        entity.setDescription(value);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ApplicantValidDocument getApplicantValidDocument(String value) {
        ApplicantValidDocument entity = new ApplicantValidDocument();
        ApplicantValidDocumentID applicantValidDocumentID = new ApplicantValidDocumentID();
        applicantValidDocumentID.setAppTypeCode(value);
        applicantValidDocumentID.setDocTypeCode(value);
        applicantValidDocumentID.setDocCatCode(value);
        entity.setApplicantValidDocumentId(applicantValidDocumentID);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private Location getLocation(String value) {
        Location entity = new Location();
        entity.setCode(value);
        entity.setName(value);
        entity.setHierarchyLevel(1);
        entity.setHierarchyName("Country");
        entity.setParentLocCode("Country");
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ReasonCategory getReasonCategory(String value) {
        ReasonCategory entity = new ReasonCategory();
        entity.setCode(value);
        entity.setDescription(value);
        entity.setName(value);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ReasonList getReasonList(String value) {
        ReasonCategory reasonCategory = getReasonCategory(value);
        reasonCategoryRepository.save(reasonCategory);

        ReasonList entity = new ReasonList();
        entity.setCode(value);
        entity.setRsnCatCode(value);
        entity.setReasonCategory(reasonCategory);
        entity.setName(value);
        entity.setDescription(value);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private Holiday getHoliday(String value) {
        Holiday entity = new Holiday();
        HolidayID holidayID = new HolidayID();
        holidayID.setHolidayName(value);
        holidayID.setHolidayDate(LocalDate.now());
        holidayID.setLocationCode(value);
        holidayID.setLangCode("eng");
        entity.setHolidayId(holidayID);
        entity.setHolidayDesc(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private BlocklistedWords getBlockListedWords(String value) {
        BlocklistedWords entity = new BlocklistedWords();
        entity.setWord(value);
        entity.setDescription(value);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ScreenAuthorization getScreenAuthorization(String value) {
        ScreenAuthorization entity = new ScreenAuthorization();
        entity.setLangCode("eng");
        entity.setScreenId(value);
        entity.setRoleCode(value);
        entity.setIsPermitted(false);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ScreenDetail getScreenDetail(String value) {
        ScreenDetail entity = new ScreenDetail();
        entity.setAppId(value);
        entity.setDescr(value);
        entity.setId(value);
        entity.setName(value);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private SyncJobDef getSyncJobDef(String value) {
        SyncJobDef entity = new SyncJobDef();
        entity.setId(value);
        entity.setName(value);
        entity.setLangCode("eng");
        entity.setApiName(value);
        entity.setSyncFreq(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }

    private ProcessList getProcessList(String value) {
        ProcessList processList = new ProcessList();
        processList.setName(value);
        processList.setDescr(value);
        processList.setLangCode("eng");
        processList.setId(value);
        processList.setCreatedBy(value);
        processList.setIsActive(true);
        processList.setCreatedDateTime(LocalDateTime.now());
        processListRepository.save(processList);
        return processList;
    }

    private PermittedLocalConfig getPermittedLocalConfig(String value) {
        PermittedLocalConfig entity = new PermittedLocalConfig();
        entity.setCode(value);
        entity.setName(value);
        entity.setType(value);
        entity.setIsActive(true);
        entity.setCreatedBy(value);
        entity.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return entity;
    }
}
