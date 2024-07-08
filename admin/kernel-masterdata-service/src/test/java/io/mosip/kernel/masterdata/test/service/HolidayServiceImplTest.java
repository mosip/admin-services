package io.mosip.kernel.masterdata.test.service;

import io.mosip.kernel.masterdata.dto.HolidayIDDto;
import io.mosip.kernel.masterdata.dto.HolidayUpdateDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.HolidayExtnDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.response.HolidaySearchDto;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.service.impl.HolidayServiceImpl;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class HolidayServiceImplTest {

    @InjectMocks
    HolidayServiceImpl holidayService;

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
    public void testBindDtoToMap() {
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
    public void testMapToHolidayIdDto() {
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
        List<SearchFilter> expectedFilters = Collections.singletonList(buildExpectedSearchFilter());

        List<SearchFilter> filters = ReflectionTestUtils.invokeMethod(holidayService, "buildLocationSearchFilter", locations);

        assertNotNull(filters);
        assertEquals(expectedFilters, filters);
    }

    @Test
    public void testSetMetaData() {
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

    private SearchFilter buildExpectedSearchFilter() {
        SearchFilter filter = new SearchFilter();
        filter.setColumnName("locationCode");
        filter.setType(FilterTypeEnum.EQUALS.name());
        filter.setValue("LOC123");
        return filter;
    }

}
