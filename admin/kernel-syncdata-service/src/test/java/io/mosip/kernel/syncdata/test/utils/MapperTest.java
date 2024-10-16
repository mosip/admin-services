package io.mosip.kernel.syncdata.test.utils;

import io.mosip.kernel.syncdata.dto.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
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

	@Test
	public void testMapUserDetailsToUserDetailMapValidInput() {
		List<UserDetailDto> userDetails = new ArrayList<>();
		UserDetailDto userDetailDto = new UserDetailDto();
		userDetailDto.setUserId("userId1");
		userDetailDto.setName("Tester");
		userDetailDto.setMail("syncdata@gamil.com");
		userDetailDto.setMobile("9080706050");
		userDetailDto.setRole("Admin");
		userDetails.add(userDetailDto);

		List<RegistrationCenterUserDto> usersFromDB = new ArrayList<>();
		RegistrationCenterUserDto registrationCenterUserDto = new RegistrationCenterUserDto();
		registrationCenterUserDto.setUserId("UserId1");
		registrationCenterUserDto.setRegCenterId("Center");
		registrationCenterUserDto.setIsActive(true);
		registrationCenterUserDto.setLangCode("eng");
		registrationCenterUserDto.setIsDeleted(false);
		usersFromDB.add(registrationCenterUserDto);

		List<UserDetailMapDto> mappedList = MapperUtils.mapUserDetailsToUserDetailMap(userDetails, usersFromDB);

		assertNotNull(mappedList);
		assertEquals(userDetails.size(), mappedList.size());

		for (int i = 0; i < mappedList.size(); i++) {
			UserDetailMapDto mappedDto = mappedList.get(i);
			UserDetailDto userDetail = userDetails.get(i);
			RegistrationCenterUserDto userFromDB = usersFromDB.stream()
					.filter(u -> u.getUserId().equalsIgnoreCase(userDetail.getUserId()))
					.findFirst().get();

			assertEquals(userDetail.getUserId(), mappedDto.getUserName());
			assertEquals(userDetail.getMail(), mappedDto.getMail());
			assertEquals(userDetail.getMobile(), mappedDto.getMobile());
			assertEquals(userFromDB.getLangCode(), mappedDto.getLangCode());
			assertEquals(userDetail.getName(), mappedDto.getName());
			assertNull(mappedDto.getUserPassword());
			assertEquals(userFromDB.getIsActive(), mappedDto.getIsActive());
			assertEquals(userFromDB.getIsDeleted(), mappedDto.getIsDeleted());
			assertEquals(userFromDB.getRegCenterId(), mappedDto.getRegCenterId());
			assertEquals(Arrays.asList(userDetail.getRole().split(",")), mappedDto.getRoles());
		}
	}

	@Test
	public void testMapUserDetailsToUserDetailMapEmptyInput() {
		List<UserDetailDto> userDetails = new ArrayList<>();
		List<RegistrationCenterUserDto> usersFromDB = new ArrayList<>();

		List<UserDetailMapDto> mappedList = MapperUtils.mapUserDetailsToUserDetailMap(userDetails, usersFromDB);

		assertNotNull(mappedList);
		assertTrue(mappedList.isEmpty());
	}

	@Test
	public void testMapUserDetailsToUserDetailMapUserNotFound() {
		List<UserDetailDto> userDetails = new ArrayList<>();
		UserDetailDto userDetailDto = new UserDetailDto();
		userDetailDto.setUserId("userId1");
		userDetailDto.setName("Tester");
		userDetailDto.setMail("syncdata@gamil.com");
		userDetailDto.setMobile("9080706050");
		userDetailDto.setRole("Admin");
		userDetails.add(userDetailDto);

		List<RegistrationCenterUserDto> usersFromDB = new ArrayList<>();
		RegistrationCenterUserDto registrationCenterUserDto = new RegistrationCenterUserDto();
		registrationCenterUserDto.setUserId("UserId1");
		registrationCenterUserDto.setRegCenterId("Center");
		registrationCenterUserDto.setIsActive(true);
		registrationCenterUserDto.setLangCode("eng");
		registrationCenterUserDto.setIsDeleted(false);
		usersFromDB.add(registrationCenterUserDto);

		List<UserDetailMapDto> mappedList = MapperUtils.mapUserDetailsToUserDetailMap(userDetails, usersFromDB);

		assertFalse(mappedList.isEmpty());
	}

}
