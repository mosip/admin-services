package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.dto.RegCenterPutReqDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.utils.RegistrationCenterValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RegistrationCenterValidatorTest {

    @Mock
    RegistrationCenterValidator registrationCenterValidator;

    @Test
    public void testRegCenterPutReqDto_ValidData() {

        RegistrationCenter registrationCenter = new RegistrationCenter();
        registrationCenter.setId("regCenterId");
        registrationCenter.setName("Center Name");
        registrationCenter.setLangCode("eng");

        List<RegistrationCenter> newregistrationCenterList = new ArrayList<>();
        newregistrationCenterList.add(registrationCenter);

        RegCenterPutReqDto reqDto = new RegCenterPutReqDto();
        reqDto.setId("regCenterId");
        reqDto.setName("Center124");
        reqDto.setAddressLine1("Address Line 1");
        reqDto.setAddressLine2("Address Line 2");
        reqDto.setLangCode("eng");
        reqDto.setNumberOfKiosks((short) 5);
        reqDto.setContactPerson("John Doe");
        reqDto.setCenterTypeCode("centerTypeCode");
        reqDto.setLatitude("12.3456");
        reqDto.setLongitude("78.9012");
        reqDto.setLocationCode("locCode123");
        reqDto.setHolidayLocationCode("holidayLocCode");
        reqDto.setContactPhone("1234567890");
        reqDto.setWorkingHours("09:00:00-17:00:00");
        reqDto.setPerKioskProcessTime(LocalTime.of(0, 15, 0));
        reqDto.setCenterStartTime(LocalTime.of(8, 0, 0));
        reqDto.setCenterEndTime(LocalTime.of(18, 0, 0));
        reqDto.setLunchStartTime(LocalTime.of(12, 30, 0));
        reqDto.setLunchEndTime(LocalTime.of(13, 30, 0));
        reqDto.setTimeZone("Asia/Kolkata");
        reqDto.setZoneCode("zoneCode123");
        reqDto.setWorkingNonWorkingDays(Collections.singletonMap("MONDAY", true));
        reqDto.setExceptionalHolidayPutPostDto(Collections.emptyList());

        assertEquals("eng", registrationCenter.getLangCode());
        assertEquals("Center124", reqDto.getName());
        assertNotNull(reqDto);
        assertNotNull(registrationCenter);

        registrationCenterValidator.createRegCenterPut(newregistrationCenterList, registrationCenter, reqDto);
    }

}
