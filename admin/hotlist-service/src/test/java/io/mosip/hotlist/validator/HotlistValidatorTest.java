package io.mosip.hotlist.validator;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.util.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalDateTime;
import java.util.Collections;

import static io.mosip.hotlist.constant.HotlistErrorConstants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Manoj SP
 *
 */
public class HotlistValidatorTest {

	HotlistValidator validator = new HotlistValidator();

	@Before
	public void init() {
		ReflectionTestUtils.setField(validator, "allowedIdTypes", Collections.singletonList("idType"));
	}

	@Test
	public void testNullId_withMissingInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateId(null, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(MISSING_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "id")));
	}

	@Test
	public void testInvalidId_withInvalidInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateId(" ", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "id")));
	}

	@Test
	public void testNullIdType_withMissingInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType(null, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(MISSING_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "idType")));
	}

	@Test
	public void testInvalidIdType_withInvalidInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType(" ", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "idType")));
	}

	@Test
	public void testIdTypeNotAllowed_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType("1234", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(ID_TYPE_NOT_ALLOWED.getErrorCode()));
		assertTrue(
				errors.getAllErrors().get(0).getDefaultMessage().contentEquals(ID_TYPE_NOT_ALLOWED.getErrorMessage()));
	}

	@Test
	public void testIdTypeAllowed_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType("idType", errors);
		assertFalse(errors.hasErrors());
	}

	@Ignore
	@Test
	public void testNullStatus_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(null);
		DateUtils.getUTCCurrentDateTime();
		var expiryTimestamp = LocalDateTime.now().withYear(2024);
		request.setExpiryTimestamp(expiryTimestamp);
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		System.err.println(errors.getAllErrors());
		assertFalse(errors.hasErrors());
	}

	@Test
	public void testInvalidStatus_withInvalidInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(" ");
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime());
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "status")));
	}

	@Ignore
	@Test
	public void testValidBlockedStatus_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(HotlistStatus.BLOCKED);
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime());
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		System.err.println(errors.getAllErrors());
		assertFalse(errors.hasErrors());
	}

	@Ignore
	@Test
	public void testValidUnblockedStatus_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(HotlistStatus.UNBLOCKED);
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime());
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		System.err.println(errors.getAllErrors());
		assertFalse(errors.hasErrors());
	}

	@Test
	public void testNullExpiryTimestamp_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(HotlistStatus.BLOCKED);
		request.setExpiryTimestamp(null);
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void testPastDatedExpiryTimestamp_withInvalidInputParameter() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(HotlistStatus.BLOCKED);
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime().withYear(2000));
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "expiryTimestamp")));
	}

	@Test
	public void testFutureDatedExpiryTimestamp_Success() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(HotlistStatus.BLOCKED);
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime().withYear(9999));
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		assertFalse(errors.hasErrors());
	}
}
