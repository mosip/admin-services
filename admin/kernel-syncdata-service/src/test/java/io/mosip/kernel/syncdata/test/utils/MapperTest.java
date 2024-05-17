package io.mosip.kernel.syncdata.test.utils;

import io.mosip.kernel.syncdata.dto.ApplicationDto;
import io.mosip.kernel.syncdata.dto.MachineDto;
import io.mosip.kernel.syncdata.entity.RegistrationCenter;
import io.mosip.kernel.syncdata.utils.ExceptionUtils;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MapperTest {
	
	@Mock
	private MapperUtils mapperUtils;


	@Test
	public void testObjectMapperWithNullArg() {
		try {
			assertNull(mapperUtils.getObjectAsJsonString(null));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testObjectMapperWithValidArg() {
		try {
			ApplicationDto dto = new ApplicationDto("AO1", "app1", "app desc");
			String expectedJson = "{\"code\":\"AO1\",\"name\":\"app1\",\"description\":\"app desc\"}";
			when(mapperUtils.getObjectAsJsonString(dto)).thenReturn(expectedJson);

			String actualJson = mapperUtils.getObjectAsJsonString(dto);
			JSONObject expected = new JSONObject(expectedJson);
			JSONObject actual = new JSONObject(actualJson);

			assertEquals(expected.getString("code"), actual.getString("code"));
			assertEquals(expected.getString("name"), actual.getString("name"));
			assertEquals(expected.getString("description"), actual.getString("description"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test 
	public void testObjectMapperForLocalDateTimeFormat() { 
		try { 
			MachineDto dto = new MachineDto();
			dto.setName("Test machine");
			dto.setValidityDateTime(LocalDateTime.MIN);

			JSONObject expected = new JSONObject("{\"validityDateTime\":\"-999999999-01-01T00:00:00\"}");
			when(mapperUtils.getObjectAsJsonString(dto)).thenReturn(expected.toString());

			String actualJson = mapperUtils.getObjectAsJsonString(dto);
			JSONObject actual = new JSONObject(actualJson);

			assertEquals(expected.getString("validityDateTime"), actual.getString("validityDateTime"));
		} catch (Exception e) {
			Assert.fail(e.getMessage()); 
		}
	}
	
	@Test
	public void testLocalTimeFormat() {
		LocalTime localTime = LocalTime.parse("09:30:04");
		RegistrationCenter registrationCenter = new RegistrationCenter();
		registrationCenter.setId("1011");
		registrationCenter.setAddressLine1("address-line1");
		registrationCenter.setAddressLine2("address-line2");
		registrationCenter.setAddressLine3("address-line3");
		registrationCenter.setCenterEndTime(localTime);
		registrationCenter.setCenterStartTime(localTime);
		registrationCenter.setCenterTypeCode("T1011");
		registrationCenter.setContactPerson("admin");
		registrationCenter.setContactPhone("9865123456");
		registrationCenter.setHolidayLocationCode("LOC01");
		registrationCenter.setIsActive(true);
		registrationCenter.setLangCode("ENG");
		registrationCenter.setWorkingHours("9");
		registrationCenter.setLunchEndTime(localTime);
		registrationCenter.setLunchStartTime(localTime);
		
		try {
			JSONObject expected = new JSONObject("{\"lunchEndTime\":\"09:30:04\",\"lunchStartTime\":\"09:30:04\",\"centerEndTime\":\"09:30:04\",\"centerStartTime\":\"09:30:04\"}");
			when(mapperUtils.getObjectAsJsonString(registrationCenter)).thenReturn(expected.toString());

			String actualJson = mapperUtils.getObjectAsJsonString(registrationCenter);
			JSONObject actual = new JSONObject(actualJson);

			assertEquals(expected.getString("lunchEndTime"), actual.getString("lunchEndTime"));
			assertEquals(expected.getString("lunchStartTime"), actual.getString("lunchStartTime"));
			assertEquals(expected.getString("centerEndTime"), actual.getString("centerEndTime"));
			assertEquals(expected.getString("centerStartTime"), actual.getString("centerStartTime"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void exceptionUtilsTest() {
		String message = "test exception";
		String exception = ExceptionUtils.parseException(new NullPointerException(message));
		Assert.assertEquals(message.trim(), exception.trim());
	}
}
