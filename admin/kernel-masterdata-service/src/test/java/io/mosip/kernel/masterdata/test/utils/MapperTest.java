package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.dto.*;
import io.mosip.kernel.masterdata.entity.Holiday;
import io.mosip.kernel.masterdata.entity.Language;
import io.mosip.kernel.masterdata.entity.RegisteredDevice;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.mosip.kernel.masterdata.utils.MapperUtils.map;
import static io.mosip.kernel.masterdata.utils.MetaDataUtils.setCreateMetaData;
import static io.mosip.kernel.masterdata.utils.MetaDataUtils.setUpdateMetaData;
import static org.junit.Assert.*;

/**
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MapperTest {
	private static List<RegistrationCenterDeviceDto> rcdDtos = null;
	private static RegistrationCenterDeviceDto rcdDto = null;

	@Before
	public void setup() {
		rcdDtos = new ArrayList<>();
		rcdDto = new RegistrationCenterDeviceDto();
		rcdDto.setRegCenterId("12");
		rcdDto.setDeviceId("4567");
		rcdDto.setIsActive(true);
		rcdDtos.add(rcdDto);

	}

	@Test(expected = NullPointerException.class)
	public void testMapSourceNull() {
		map(null, new Language());
	}

	@Test(expected = NullPointerException.class)
	public void testMapDestinationNull() {
		map(new Language(), null);
	}

	@Test(expected = NullPointerException.class)
	public void testMapMetaDataSourceNull() {
		setCreateMetaData(null, Language.class);
	}

	@Test(expected = NullPointerException.class)
	public void testMapMetaDataDestinationNull() {
		setCreateMetaData(new LanguageDto(), null);
	}

	

	@Test
	public void testCopyEntities() {
		LanguageDto d1 = new LanguageDto();
		LanguageDto d2 = new LanguageDto();
		d1.setCode("HIN");
		d1.setIsActive(true);

		MapperUtils.setBaseFieldValue(d1, d2);

		assertNull(d2.getCode());
		assertNull(d2.getIsActive());
	}

	@Test
	public void testSetUpdateMetaData() {
		LanguageDto dto = new LanguageDto();
		Language entity = new Language();

		dto.setCode("ENG");
		dto.setFamily("English");
		dto.setIsActive(false);

		entity.setCode("eng");
		entity.setFamily("english");
		entity.setName("english");
		entity.setNativeName("english");
		entity.setIsActive(true);
		entity.setCreatedDateTime(LocalDateTime.now());
		entity.setCreatedBy("admin");

		setUpdateMetaData(dto, entity, false);

		assertTrue(entity.getCode().equals(dto.getCode()));
		assertTrue(entity.getFamily().equals(dto.getFamily()));
		assertTrue(entity.getName().equals("english"));
		assertTrue(entity.getNativeName().equals("english"));
		assertTrue(entity.getUpdatedBy() != null);
		assertTrue(entity.getUpdatedDateTime() != null);
		assertTrue(entity.getIsActive());
	}

	@Test
	public void testSetUpdateMetaDataWithIsActive() {
		LanguageDto dto = new LanguageDto();
		Language entity = new Language();

		dto.setCode("ENG");
		dto.setFamily("English");
		dto.setIsActive(true);

		entity.setIsActive(false);
		setUpdateMetaData(dto, entity, false);

		assertTrue(entity.getCode().equals(dto.getCode()));
		assertTrue(entity.getFamily().equals(dto.getFamily()));
		assertFalse(entity.getIsActive());
	}

	@Test
	public void testSetCreateMetaDataWithIsActive() {
		LanguageDto dto = new LanguageDto();

		dto.setCode("ENG");
		dto.setFamily("English");
		dto.setIsActive(true);

		Language entity = setCreateMetaData(dto, Language.class);

		assertNotNull(entity.getCreatedDateTime());
		assertNotNull(entity.getCreatedBy());
		assertFalse(entity.getIsActive());
	}

	@Test
	public void mapUpdateHolidays_validInputs_returnHolidayDtos(){
		List<Holiday> holidays=new ArrayList<>();
		Holiday holiday=new Holiday();
		holiday.setHolidayDate(LocalDate.now());
		holiday.setHolidayId(123);
		holiday.setLangCode("eng");
		holiday.setIsActive(true);
		holidays.add(holiday);

		List<HolidayUpdateDto> holidayDtos = new ArrayList<>();
		HolidayUpdateDto holidayUpdateDto=new HolidayUpdateDto();
		holidayUpdateDto.setHolidayName("name");
		holidayUpdateDto.setHolidayDesc("desc");
		holidayUpdateDto.setHolidayId(345);
		holidayUpdateDto.setHolidayDate(LocalDate.now());
		holidayUpdateDto.setHolidayId(7);
		holidayUpdateDto.setLangCode("eng");
		holidayUpdateDto.setLocationCode("location123");
		holidayDtos.add(holidayUpdateDto);

		List<HolidayUpdateDto> holidayUpdateDtos = MapperUtils.mapUpdateHolidays(holidays);
		assertEquals(holidays.size(),holidayUpdateDtos.size());

		Holiday updatedHoliday = holidays.get(0);
		assertEquals(holidayUpdateDto.getHolidayDate(), updatedHoliday.getHolidayDate());
		assertEquals(holidayUpdateDto.getLangCode(), updatedHoliday.getLangCode());
	}

	@Test
	public void mapRegisteredDeviceDto_validInputs_returnEntity(){
		RegisteredDevicePostReqDto dto=new RegisteredDevicePostReqDto();
		dto.setDeviceId("device_id");
		dto.setStatusCode("status_code");
		dto.setDeviceSubId("device_subid");
		dto.setPurpose("purpose");
		dto.setFirmware("firmware");
		dto.setExpiryDate(LocalDateTime.now());
		dto.setCertificationLevel("certification_level");
		dto.setFoundationalTPId("functional_tpid");
		DigitalIdDeviceRegisterDto digitalIdDto=new DigitalIdDeviceRegisterDto();
		digitalIdDto.setDp("dp");
		digitalIdDto.setMake("make");
		digitalIdDto.setModel("model");
		digitalIdDto.setDpId("dp_id");
		digitalIdDto.setDateTime("date_time");
		digitalIdDto.setDeviceSTypeCode("device_st_type_code");
		digitalIdDto.setDeviceTypeCode("device_type_code");
		digitalIdDto.setSerialNo("serial_no");
		dto.setDigitalIdDto(digitalIdDto);
		RegisteredDevice entity = MapperUtils.mapRegisteredDeviceDto(dto,"digital_id_json");

		assertEquals(dto.getDeviceId(),entity.getDeviceId());
		assertEquals(dto.getStatusCode(),entity.getStatusCode());
		assertEquals(dto.getDeviceSubId(),entity.getDeviceSubId());
		assertEquals(dto.getPurpose(),entity.getPurpose());
		assertEquals(dto.getFirmware(),entity.getFirmware());
		assertEquals(dto.getExpiryDate(),entity.getExpiryDate());
		assertEquals(dto.getCertificationLevel(),entity.getCertificationLevel());
		assertEquals(dto.getFoundationalTPId(),entity.getFoundationalTPId());

		assertEquals(digitalIdDto.getDp(),entity.getDp());
		assertEquals(digitalIdDto.getMake(),entity.getMake());
		assertEquals(digitalIdDto.getModel(),entity.getModel());
		assertEquals(digitalIdDto.getDpId(),entity.getDpId());
		assertEquals(digitalIdDto.getDeviceSTypeCode(),entity.getDeviceSTypeCode());
		assertEquals(digitalIdDto.getDeviceTypeCode(),entity.getDeviceTypeCode());
		assertEquals(digitalIdDto.getSerialNo(),entity.getSerialNo());
	}

}
