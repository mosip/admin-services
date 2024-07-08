package io.mosip.kernel.masterdata.test.service;

import io.mosip.kernel.masterdata.dto.RegCenterPutReqDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.entity.*;
import io.mosip.kernel.masterdata.repository.*;
import io.mosip.kernel.masterdata.service.impl.RegistrationCenterServiceImpl;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.utils.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RegistrationCenterServiceImplTest {

    @InjectMocks
    private RegistrationCenterServiceImpl registrationCenterServiceImpl;

    @Mock
    private RegistrationCenterHistoryRepository registrationCenterHistoryRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private RegExceptionalHolidayRepository regExceptionalHolidayRepository;

    @Mock
    private ZoneUtils zoneUtils;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteExpHoliday_success() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setLangCode("en_IN");
        LocalDate expHoliday = LocalDate.of(2024, 10, 02);
        RegExceptionalHoliday regExceptionalHolidayEntity = new RegExceptionalHoliday();
        regExceptionalHolidayEntity.setRegistrationCenterId(registrationCenter.getId());
        regExceptionalHolidayEntity.setLangCode(registrationCenter.getLangCode());
        regExceptionalHolidayEntity.setExceptionHolidayDate(expHoliday);

        when(regExceptionalHolidayRepository.findByRegIdAndLangcodeAndExpHoliday(any(), any(), any()))
                .thenReturn(regExceptionalHolidayEntity);

        ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "deleteExpHoliday", registrationCenter, expHoliday);

        verify(regExceptionalHolidayRepository).findByRegIdAndLangcodeAndExpHoliday(registrationCenter.getId(),
                registrationCenter.getLangCode(), expHoliday);
        verify(regExceptionalHolidayRepository).update(regExceptionalHolidayEntity);
    }

    @Test
    public void testDeleteExpHoliday_noRecordFound() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setLangCode("en_IN");
        LocalDate expHoliday = LocalDate.of(2024, 10, 02);

        when(regExceptionalHolidayRepository.findByRegIdAndLangcodeAndExpHoliday(any(), any(), any()))
                .thenReturn(null);

        assertThrows(NullPointerException.class, ()-> ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "deleteExpHoliday", registrationCenter, expHoliday));

        verify(regExceptionalHolidayRepository).findByRegIdAndLangcodeAndExpHoliday(registrationCenter.getId(),
                registrationCenter.getLangCode(), expHoliday);
    }

    @Test
    public void testValidateZoneMachineDevice_sameZone() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setZoneCode("WB-KOL-E");
        RegCenterPutReqDto dto = new RegCenterPutReqDto();
        dto.setId(registrationCenter.getId());
        dto.setZoneCode(registrationCenter.getZoneCode());
        List<Device> deviceList = new ArrayList<>();
        Device device = new Device();
        device.setZoneCode("WB-KOL-E");
        deviceList.add(device);

        when(deviceRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(dto.getId())).thenReturn(deviceList);
        when(zoneUtils.getChildZoneList(any(), any(), any())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "validateZoneMachineDevice", registrationCenter, dto);

        verify(deviceRepository).findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(dto.getId());
        verify(zoneUtils).getChildZoneList(Collections.singletonList(device.getZoneCode()), dto.getZoneCode(), dto.getLangCode());
    }

    @Test
    public void testValidateZoneMachineDevice_noDevices() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setZoneCode("WB-KOL-E");
        RegCenterPutReqDto dto = new RegCenterPutReqDto();
        dto.setId(registrationCenter.getId());
        dto.setZoneCode("WB-MUM-W");
        List<Device> deviceList = Collections.emptyList();
        
        lenient().when(deviceRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(dto.getId())).thenReturn(deviceList);

        ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "validateZoneMachineDevice", registrationCenter, dto);
    }

    @Test
    public void testBuildZoneFilter_emptyZones() {
        List<Zone> zones = Collections.emptyList();
        List<SearchFilter> filters = ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "buildZoneFilter", zones);
        assertEquals(Collections.emptyList(), filters);
    }

    @Test
    public void testBuildZoneFilter_nonEmptyZones() {
        List<Zone> zones = Arrays.asList(new Zone("WB-KOL-E", "eng", "zone1", (short) 1, "zone1Hierarchy","zone1Parent", "path/to/z1"),
                new Zone("KA-BLR-S", "eng", "zone2", (short) 2, "zone2Hierarchy","zone2Parent", "path/to/z1/z2"));
        List<SearchFilter> expectedFilters = Arrays.asList(
                new SearchFilter("WB-KOL-E", null, null, "zoneCode", "EQUALS"),
                new SearchFilter("KA-BLR-S",null, null, "zoneCode", "EQUALS"));
        List<SearchFilter> filters = ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "buildZoneFilter", zones);
        assertEquals(expectedFilters, filters);
    }

    @Test
    public void testUpdateRegistrationCenterHistory_success() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setLangCode("en_IN");
        RegistrationCenterHistory existingHistory = new RegistrationCenterHistory();
        existingHistory.setId(registrationCenter.getId());
        existingHistory.setLangCode(registrationCenter.getLangCode());
        existingHistory.setCreatedDateTime(LocalDate.of(2020,12,1).atStartOfDay());
        RegistrationCenterHistory updateData = new RegistrationCenterHistory();
        updateData.setLangCode("eng");

        lenient().when(registrationCenterHistoryRepository.findByIdAndLangCodeAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(registrationCenter.getId(), registrationCenter.getLangCode(), registrationCenter.getCreatedDateTime())).
                thenReturn(Collections.singletonList(existingHistory));
    }

    @Test
    public void testUpdateRegistrationCenterHistory_noExistingHistory() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setLangCode("en_IN");
        registrationCenter.setCreatedDateTime(LocalDate.of(2020,12,1).atStartOfDay());

        RegistrationCenterHistory updateData = new RegistrationCenterHistory();
        updateData.setLangCode("eng");

        lenient().when(registrationCenterHistoryRepository.findByIdAndLangCodeAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(registrationCenter.getId(), registrationCenter.getLangCode(), registrationCenter.getCreatedDateTime())).thenReturn(null);
    }

    @Test (expected = IllegalStateException.class)
    public void testUpdateRegistrationCenterHistory_validationFailure() {
        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId(String.valueOf(1L));
        registrationCenter.setLangCode("en_IN");
        RegistrationCenterHistory existingHistory = new RegistrationCenterHistory();
        existingHistory.setId(registrationCenter.getId());
        existingHistory.setLangCode(registrationCenter.getLangCode());
        RegistrationCenterHistory updateData = new RegistrationCenterHistory();
        updateData.setLangCode("eng");

        lenient().when(registrationCenterHistoryRepository.findByIdAndLangCodeAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(registrationCenter.getId(), registrationCenter.getLangCode(), registrationCenter.getCreatedDateTime())).
                thenReturn(Collections.singletonList(existingHistory));

        ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "updateRegistartionCenterHistory", registrationCenter, updateData);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateRegistrationCenterHistory_missingRegistrationCenter() {
        RegistrationCenter registrationCenter = null;
        RegistrationCenterHistory updateData = new RegistrationCenterHistory();
        updateData.setId("123");

        ReflectionTestUtils.invokeMethod(registrationCenterServiceImpl, "updateRegistartionCenterHistory", registrationCenter, updateData);
    }

}
