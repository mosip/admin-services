package io.mosip.kernel.syncdata.test;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
public class SyncCacheTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    AppAuthenticationMethodRepository appAuthenticationMethodRepository;
    @Autowired
    AppRolePriorityRepository appRolePriorityRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    RegistrationCenterRepository registrationCenterRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    DocumentTypeRepository documentTypeRepository;
    @Autowired
    ApplicantValidDocumentRespository applicantValidDocumentRespository;
    @Autowired
    ReasonCategoryRepository reasonCategoryRepository;
    @Autowired
    ReasonListRepository reasonListRepository;
    @Autowired
    ScreenDetailRepository screenDetailRepository;
    @Autowired
    ScreenAuthorizationRepository screenAuthorizationRepository;
    @Autowired
    BlacklistedWordsRepository blacklistedWordsRepository;
    @Autowired
    ProcessListRepository processListRepository;
    @Autowired
    SyncJobDefRepository syncJobDefRepository;
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    MachineSpecificationRepository machineSpecificationRepository;
    @Autowired
    MachineTypeRepository machineTypeRepository;
    @Autowired
    RegistrationCenterTypeRepository registrationCenterTypeRepository;

    @Autowired
    TemplateFileFormatRepository templateFileFormatRepository;
    @Autowired
    ModuleDetailRepository moduleDetailRepository;


    @Test
    public void whenFindAllAppAuthMethods_thenResultShouldBePutInCache() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<AppAuthenticationMethod> list = appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "app_authentication_method").size());
    }

    @Test
    public void whenFindChangedAppAuthMethods_thenResultShouldNotBePutInCache() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "app_authentication_method").size());
    }

    @Test
    public void findMaxChangedDate_AppAuthMethod_thenOnlyCreatedUpdatedDateTimeIsCached() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        appAuthenticationMethodRepository.save(getAppAuthMethods("test1"));
        evictAllKeys("delta-sync");
        EntityDtimes result = appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "app_authentication_method"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "app_authentication_method"));
    }

    @Test
    public void findMaxChangedDate_AppAuthMethod_thenBothCreatedUpdatedDateTimeIsCached() {
        appAuthenticationMethodRepository.save(getAppAuthMethods("test"));
        AppAuthenticationMethod entity = getAppAuthMethods("test1");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        appAuthenticationMethodRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "app_authentication_method"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "app_authentication_method"));
    }

    //

    @Test
    public void whenFindAllAppRolePriority_thenResultShouldBePutInCache() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<AppRolePriority> list = appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "app_role_priority").size());
    }

    @Test
    public void whenFindChangedAppRolePriority_thenResultShouldNotBePutInCache() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "app_role_priority").size());
    }

    @Test
    public void findMaxChangedDate_AppRolePriority_thenOnlyCreatedUpdatedDateTimeIsCached() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        appRolePriorityRepository.save(getAppRolePriority("test1"));
        evictAllKeys("delta-sync");
        EntityDtimes result = appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "app_role_priority"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "app_role_priority"));
    }

    @Test
    public void findMaxChangedDate_AppRolePriority_thenBothCreatedUpdatedDateTimeIsCached() {
        appRolePriorityRepository.save(getAppRolePriority("test"));
        AppRolePriority entity = getAppRolePriority("test1");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        appRolePriorityRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "app_role_priority"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "app_role_priority"));
    }

    //

    @Test
    public void whenFindAllTemplate_thenResultShouldBePutInCache() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<Template> list = templateRepository.findAllLatestCreatedUpdateDeletedByModule(lastUpdatedTime, currentTime, "test");
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "template").size());
    }

    @Test
    public void whenFindChangedTemplate_thenResultShouldNotBePutInCache() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        templateRepository.findAllLatestCreatedUpdateDeletedByModule(lastUpdatedTime, currentTime, "test");
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "template").size());
    }

    @Test
    public void findMaxChangedDate_Template_thenOnlyCreatedUpdatedDateTimeIsCached() {
        saveModuleDetail();
        templateRepository.save(getTemplate("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "template"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "template"));
    }

    @Test
    public void findMaxChangedDate_Template_thenBothCreatedUpdatedDateTimeIsCached() {
        saveModuleDetail();
        Template entity = getTemplate("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        templateRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "template"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "template"));
    }
    //

    @Test
    public void whenFindAllJobDef_thenResultShouldBePutInCache() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<SyncJobDef> list = syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "sync_job_def").size());
    }

    @Test
    public void whenFindChangedJobDef_thenResultShouldNotBePutInCache() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "sync_job_def").size());
    }

    @Test
    public void findMaxChangedDate_JobDef_thenOnlyCreatedUpdatedDateTimeIsCached() {
        syncJobDefRepository.save(getSyncJobDef("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "sync_job_def"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "sync_job_def"));
    }

    @Test
    public void findMaxChangedDate_JobDef_thenBothCreatedUpdatedDateTimeIsCached() {
        SyncJobDef entity = getSyncJobDef("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        syncJobDefRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "sync_job_def"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "sync_job_def"));
    }

    //

    @Test
    public void whenFindAllScreenDetail_thenResultShouldBePutInCache() {
        screenDetailRepository.save(getScreenDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ScreenDetail> list = screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "screen_detail").size());
    }

    @Test
    public void whenFindChangedScreenDetail_thenResultShouldNotBePutInCache() {
        screenDetailRepository.save(getScreenDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "screen_detail").size());
    }

    @Test
    public void findMaxChangedDate_ScreenDetail_thenOnlyCreatedUpdatedDateTimeIsCached() {
        screenDetailRepository.save(getScreenDetail("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "screen_detail"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "screen_detail"));
    }

    @Test
    public void findMaxChangedDate_ScreenDetail_thenBothCreatedUpdatedDateTimeIsCached() {
        ScreenDetail entity = getScreenDetail("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        screenDetailRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "screen_detail"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "screen_detail"));
    }

    //

    @Test
    public void whenFindAllScreenAuth_thenResultShouldBePutInCache() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ScreenAuthorization> list = screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "screen_authorization").size());
    }

    @Test
    public void whenFindChangedScreenAuth_thenResultShouldNotBePutInCache() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "screen_authorization").size());
    }

    @Test
    public void findMaxChangedDate_ScreenAuth_thenOnlyCreatedUpdatedDateTimeIsCached() {
        screenAuthorizationRepository.save(getScreenAuthorization("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "screen_authorization"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "screen_authorization"));
    }

    @Test
    public void findMaxChangedDate_ScreenAuth_thenBothCreatedUpdatedDateTimeIsCached() {
        ScreenAuthorization entity = getScreenAuthorization("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        screenAuthorizationRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "screen_authorization"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "screen_authorization"));
    }


    //

    @Test
    public void whenFindAllPL_thenResultShouldBePutInCache() {
        processListRepository.save(getProcessList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ProcessList> list = processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "process_list").size());
    }

    @Test
    public void whenFindChangedPL_thenResultShouldNotBePutInCache() {
        processListRepository.save(getProcessList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "process_list").size());
    }

    @Test
    public void findMaxChangedDate_PL_thenOnlyCreatedUpdatedDateTimeIsCached() {
        processListRepository.save(getProcessList("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "process_list"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "process_list"));
    }

    @Test
    public void findMaxChangedDate_PL_thenBothCreatedUpdatedDateTimeIsCached() {
        ProcessList entity = getProcessList("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        processListRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "process_list"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "process_list"));
    }

    //

    @Test
    public void whenFindAllBW_thenResultShouldBePutInCache() {
        blacklistedWordsRepository.save(getBlackListedWords("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<BlacklistedWords> list = blacklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "blacklisted_words").size());
    }

    @Test
    public void whenFindChangedBW_thenResultShouldNotBePutInCache() {
        blacklistedWordsRepository.save(getBlackListedWords("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        blacklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "blacklisted_words").size());
    }

    @Test
    public void findMaxChangedDate_BW_thenOnlyCreatedUpdatedDateTimeIsCached() {
        blacklistedWordsRepository.save(getBlackListedWords("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = blacklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "blacklisted_words"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "blacklisted_words"));
    }

    @Test
    public void findMaxChangedDate_BW_thenBothCreatedUpdatedDateTimeIsCached() {
        BlacklistedWords entity = getBlackListedWords("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        blacklistedWordsRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = blacklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "blacklisted_words"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "blacklisted_words"));
    }

    //

    /*@Test
    public void whenFindAllMachine_thenResultShouldBePutInCache() {
        machineRepository.save(getMachine("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<Machine> list = machineRepository.findMachineLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime, "test");
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "machine_master_test").size());
    }*/

    @Test
    public void whenFindChangedMachine_thenResultShouldNotBePutInCache() {
        machineRepository.save(getMachine("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        machineRepository.findMachineLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime, "test");
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "machine_master_test").size());
    }

    @Test
    public void findMaxChangedDate_Machine_thenOnlyCreatedUpdatedDateTimeIsCached() {
        machineRepository.save(getMachine("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "machine_master"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "machine_master"));
    }

    @Test
    public void findMaxChangedDate_Machine_thenBothCreatedUpdatedDateTimeIsCached() {
        Machine entity = getMachine("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        machineRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "machine_master"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "machine_master"));
    }

    //

    @Test
    public void whenFindAllLocation_thenResultShouldBePutInCache() {
        locationRepository.save(getLocation("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<Location> list = locationRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "location").size());
    }

    @Test
    public void whenFindChangedLocation_thenResultShouldNotBePutInCache() {
        locationRepository.save(getLocation("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        locationRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "location").size());
    }

    @Test
    public void findMaxChangedDate_Location_thenOnlyCreatedUpdatedDateTimeIsCached() {
        locationRepository.save(getLocation("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull( getCachedValue("delta-sync", "location"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "location"));
    }

    @Test
    public void findMaxChangedDate_Location_thenBothCreatedUpdatedDateTimeIsCached() {
        Location entity = getLocation("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        locationRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "location"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "location"));
    }

    //

    @Test
    public void whenFindAllRC_thenResultShouldBePutInCache() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ReasonCategory> list = reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "reason_category").size());
    }

    @Test
    public void whenFindChangedRC_thenResultShouldNotBePutInCache() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "reason_category").size());
    }

    @Test
    public void findMaxChangedDate_RC_thenOnlyCreatedUpdatedDateTimeIsCached() {
        reasonCategoryRepository.save(getReasonCategory("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "reason_category"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "reason_category"));
    }

    @Test
    public void findMaxChangedDate_RC_thenBothCreatedUpdatedDateTimeIsCached() {
        ReasonCategory entity = getReasonCategory("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        reasonCategoryRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "reason_category"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "reason_category"));
    }

    //

    @Test
    public void whenFindAllRL_thenResultShouldBePutInCache() {
        reasonListRepository.save(getReasonList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ReasonList> list = reasonListRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "reason_list").size());
    }

    @Test
    public void whenFindChangedRL_thenResultShouldNotBePutInCache() {
        reasonListRepository.save(getReasonList("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        reasonListRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "reason_list").size());
    }

    @Test
    public void findMaxChangedDate_RL_thenOnlyCreatedUpdatedDateTimeIsCached() {
        reasonListRepository.save(getReasonList("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "reason_list"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "reason_list"));
    }

    @Test
    public void findMaxChangedDate_RL_thenBothCreatedUpdatedDateTimeIsCached() {
        ReasonList entity = getReasonList("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        reasonListRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "reason_list"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "reason_list"));
    }

    //

    @Test
    public void whenFindAllDT_thenResultShouldBePutInCache() {
        documentTypeRepository.save(getDocumentType("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<DocumentType> list = documentTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "doc_type").size());
    }

    @Test
    public void whenFindChangedDT_thenResultShouldNotBePutInCache() {
        documentTypeRepository.save(getDocumentType("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        documentTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "doc_type").size());
    }

    @Test
    public void findMaxChangedDate_DT_thenOnlyCreatedUpdatedDateTimeIsCached() {
        documentTypeRepository.save(getDocumentType("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "doc_type"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "doc_type"));
    }

    @Test
    public void findMaxChangedDate_DT_thenBothCreatedUpdatedDateTimeIsCached() {
        DocumentType entity = getDocumentType("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        documentTypeRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "doc_type"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "doc_type"));
    }

    //

    @Test
    public void whenFindAllAVD_thenResultShouldBePutInCache() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<ApplicantValidDocument> list = applicantValidDocumentRespository.findAllByTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(list.size(), getInitialCachedValue("initial-sync", "applicant_valid_document").size());
    }

    @Test
    public void whenFindChangedAVD_thenResultShouldNotBePutInCache() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        applicantValidDocumentRespository.findAllByTimeStamp(lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "applicant_valid_document").size());
    }

    @Test
    public void findMaxChangedDate_AVD_thenOnlyCreatedUpdatedDateTimeIsCached() {
        applicantValidDocumentRespository.save(getApplicantValidDocument("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "applicant_valid_document"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "applicant_valid_document"));
    }

    @Test
    public void findMaxChangedDate_AVD_thenBothCreatedUpdatedDateTimeIsCached() {
        ApplicantValidDocument entity = getApplicantValidDocument("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        applicantValidDocumentRespository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "applicant_valid_document"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "applicant_valid_document"));
    }


    @Test
    public void findMaxChangedDate_RCenter_thenOnlyCreatedUpdatedDateTimeIsCached() {
        registrationCenterRepository.save(getRegistrationCenter("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "registration_center"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "registration_center"));
    }

    @Test
    public void findMaxChangedDate_RCenter_thenBothCreatedUpdatedDateTimeIsCached() {
        RegistrationCenter entity = getRegistrationCenter("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        registrationCenterRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "registration_center"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "registration_center"));
    }

    //

    @Test
    public void whenFindAllUD_thenResultShouldBePutInCache() {
        userDetailsRepository.save(getUserDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        List<UserDetails> list = userDetailsRepository.findAllLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "user_detail_test").size());
    }

    @Test
    public void whenFindChangedUD_thenResultShouldNotBePutInCache() {
        userDetailsRepository.save(getUserDetail("test"));
        LocalDateTime lastUpdatedTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(10);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        evictAllKeys("initial-sync");
        userDetailsRepository.findAllLatestCreatedUpdatedDeleted("test", lastUpdatedTime, currentTime);
        Assert.assertEquals(0, getInitialCachedValue("initial-sync", "user_detail_test").size());
    }

    @Test
    public void findMaxChangedDate_UD_thenOnlyCreatedUpdatedDateTimeIsCached() {
        userDetailsRepository.save(getUserDetail("test"));
        evictAllKeys("delta-sync");
        EntityDtimes result = userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "user_detail"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "user_detail"));
    }

    @Test
    public void findMaxChangedDate_UD_thenBothCreatedUpdatedDateTimeIsCached() {
        UserDetails entity = getUserDetail("test");
        entity.setUpdatedBy("test");
        entity.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        userDetailsRepository.save(entity);

        evictAllKeys("delta-sync");
        EntityDtimes result = userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
        Assert.assertNotNull(getCachedValue("delta-sync", "user_detail"));
        Assert.assertEquals(result, getCachedValue("delta-sync", "user_detail"));
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

    private BlacklistedWords getBlackListedWords(String value) {
        BlacklistedWords entity = new BlacklistedWords();
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

}

