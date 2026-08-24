package io.mosip.kernel.masterdata.test.service;

import io.mosip.kernel.masterdata.dto.HolidayDto;
import io.mosip.kernel.masterdata.dto.HolidayIDDto;
import io.mosip.kernel.masterdata.dto.HolidayUpdateDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.HolidayExtnDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.response.HolidaySearchDto;
import io.mosip.kernel.masterdata.entity.Holiday;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.repository.HolidayRepository;
import io.mosip.kernel.masterdata.repository.LocationRepository;
import io.mosip.kernel.masterdata.service.impl.HolidayServiceImpl;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class HolidayServiceImplTest {

    @InjectMocks
    HolidayServiceImpl holidayService;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private LocationRepository  locationRepository;

    @Mock
    private AuditUtil auditUtil;


    private HolidayUpdateDto holidayUpdateDto;
    private Location location;


    @Before
    public void setUp() {
        holidayUpdateDto = new HolidayUpdateDto();
        holidayUpdateDto.setHolidayId(1);
        holidayUpdateDto.setHolidayDesc("Test Description");
        holidayUpdateDto.setHolidayDate(LocalDate.from(LocalDateTime.now()));
        holidayUpdateDto.setHolidayName("Test Holiday");
        holidayUpdateDto.setLocationCode("LOC123");
        holidayUpdateDto.setLangCode("en");

        location = new Location();
        location.setCode("LOC123");
    }

    @Test
    public void testBindDtoToMap_Success() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("holidayId", 1L);
        expectedMap.put("holidayDesc", "Test Description");
        expectedMap.put("holidayDate", LocalDateTime.now());
        expectedMap.put("holidayName", "Test Holiday");
        expectedMap.put("locationCode", "LOC123");
        expectedMap.put("langCode", "en");
        expectedMap.put("updatedDateTime", LocalDateTime.now(ZoneId.of("UTC")));

        assertThrows(NullPointerException.class, () -> ReflectionTestUtils.invokeMethod(holidayService, "bindDtoToMap", holidayUpdateDto));
    }

    @Test
    public void testMapToHolidayIdDto_Success() {
        HolidayIDDto expectedDto = new HolidayIDDto();
        expectedDto.setHolidayId(1);
        expectedDto.setHolidayName("Test Holiday");
        expectedDto.setHolidayDate(LocalDate.from(LocalDateTime.now()));
        expectedDto.setLocationCode("LOC123");
        expectedDto.setLangCode("en");

        HolidayIDDto resultDto = ReflectionTestUtils.invokeMethod(holidayService, "mapToHolidayIdDto", holidayUpdateDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
    }

    @Test
    public void testBuildLocationSearchFilter_EmptyList() {
        List<Location> emptyList = Collections.emptyList();
        List<SearchFilter> filters = ReflectionTestUtils.invokeMethod(holidayService, "buildLocationSearchFilter", emptyList);

        assertNotNull(filters);
        assertEquals(0, filters.size());
    }

    @Test
    public void testBuildLocationSearchFilter_ValidList() {
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        List<SearchFilter> expectedFilters = Collections.singletonList(buildExpectedSearchFilter_Success());

        List<SearchFilter> filters = ReflectionTestUtils.invokeMethod(holidayService, "buildLocationSearchFilter", locations);

        assertNotNull(filters);
        assertEquals(expectedFilters, filters);
    }

    @Test
    public void testSetMetaData_Success() {
        List<HolidayExtnDto> holidays = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        HolidayExtnDto holiday1 = new HolidayExtnDto();
        holiday1.setLocationCode("LOC123");
        holidays.add(holiday1);
        HolidayExtnDto holiday2 = new HolidayExtnDto();
        holiday2.setLocationCode("LOC456");
        holidays.add(holiday2);

        HolidaySearchDto searchDto = new HolidaySearchDto();

        ReflectionTestUtils.invokeMethod(holidayService, "setMetaData", holidays, locations, searchDto);

        assertNotNull(holidays);
        assertNotNull(locations);
    }

    @Test
    public void saveHoliday_reusesExistingHolidayId_whenSameDateAndLocationDifferentLanguage() {
        // GIVEN
        final int holidayId = 123;
        final String existingHolidayName = "Test Holiday";
        final String existingHolidayDesc = "Test Description";
        final String holidayName = "Test Holiday Clone";
        final String holidayDesc = "Test Description Clone";
        final LocalDate date = LocalDate.now();
        final String locationCode = "10036";
        final String englishLanguageCode = "eng";
        final String arabicLanguageCode = "ara";
        final boolean activeStatus = true;
        // mock audit call
        doNothing().when(auditUtil).auditRequest(anyString(), anyString(), anyString(), anyString());
        // Existing location with code = "10036"
        final Location locEntity = new Location();
        locEntity.setCode(locationCode);
        locEntity.setLangCode(englishLanguageCode);
        final List<Location> locationsResult = Collections.singletonList(locEntity);
        when(locationRepository.findByCode(locationCode)).thenReturn(locationsResult);
        // Existing holiday (any language, e.g. ENG) for same (date, locationCode) with holidayId = 123
        final Holiday existing = new Holiday();
        existing.setHolidayId(holidayId);
        existing.setHolidayName(existingHolidayName);
        existing.setHolidayDesc(existingHolidayDesc);
        existing.setHolidayDate(date);
        existing.setLocationCode(locationCode);
        existing.setLangCode(englishLanguageCode);
        existing.setIsActive(activeStatus);
        // No existing ARABIC row (same date+locationCode+lang)
        when(holidayRepository.findFirstByHolidayByHolidayDateLocationCodeLangCode(
                date, locationCode, arabicLanguageCode
        )).thenReturn(Optional.empty());
        // But an existing row exists for same (date, locationCode) in another language → reuse its holidayId
        when(holidayRepository.findFirstByHolidayDateAndLocationCode(
                date, locationCode
        )).thenReturn(Optional.of(existing));
        // Avoid “empty table” branch if the service checks it
        when(holidayRepository.count()).thenReturn(1L);
        // Capture entity passed to save(...)
        ArgumentCaptor<Holiday> captor = ArgumentCaptor.forClass(Holiday.class);
        when(holidayRepository.save(captor.capture())).thenAnswer(inv -> {
            final Holiday h = captor.getValue();
            final Holiday persisted = new Holiday();
            persisted.setHolidayId(h.getHolidayId());
            persisted.setHolidayDate(h.getHolidayDate());
            persisted.setLocationCode(h.getLocationCode());
            persisted.setLangCode(h.getLangCode());
            persisted.setHolidayName(h.getHolidayName());
            persisted.setHolidayDesc(h.getHolidayDesc());
            persisted.setIsActive(h.getIsActive());
            return persisted;
        });
        when(holidayRepository.findHolidayByHolidayNameHolidayDateLocationCodeLangCode(
                holidayName,
                date,
                locationCode,
                arabicLanguageCode
        )).thenReturn(null);
        // DTO to create for Arabic, same (date, locationCode)
        HolidayDto dto = new HolidayDto();
        dto.setHolidayDate(date);
        dto.setLocationCode(locationCode);
        dto.setLangCode(arabicLanguageCode);
        dto.setHolidayName(holidayName);
        dto.setHolidayDesc(holidayDesc);
        // WHEN
        HolidayIDDto out = holidayService.saveHoliday(dto);
        // THEN — the new row must reuse holidayId = 123
        Holiday saved = captor.getValue();
        assertNotNull(saved);
        assertEquals(holidayId, saved.getHolidayId());
        assertEquals(arabicLanguageCode, saved.getLangCode());
        assertEquals(date, saved.getHolidayDate());
        assertEquals(locationCode, saved.getLocationCode());
        assertEquals(activeStatus, saved.getIsActive());
        assertEquals(holidayName, saved.getHolidayName());
        assertEquals(holidayDesc, saved.getHolidayDesc());
        assertNotNull(out);
        assertEquals(holidayId, out.getHolidayId());
        assertEquals(arabicLanguageCode, out.getLangCode());
        assertEquals(holidayName, out.getHolidayName());
        // Should not request a new max id
        verify(holidayRepository, never()).findMaxHolidayId();
        // Verification of flow
        verify(holidayRepository).count();
        verify(locationRepository).findByCode(locationCode);
        verify(holidayRepository).findFirstByHolidayByHolidayDateLocationCodeLangCode(date, locationCode, arabicLanguageCode);
        verify(holidayRepository).findFirstByHolidayDateAndLocationCode(date, locationCode);
        verify(holidayRepository).findHolidayByHolidayNameHolidayDateLocationCodeLangCode(
                holidayName,
                date,
                locationCode,
                arabicLanguageCode
        );
        verify(holidayRepository).save(any(Holiday.class));
    }


    private SearchFilter buildExpectedSearchFilter_Success() {
        SearchFilter filter = new SearchFilter();
        filter.setColumnName("locationCode");
        filter.setType(FilterTypeEnum.EQUALS.name());
        filter.setValue("LOC123");
        return filter;
    }

}
