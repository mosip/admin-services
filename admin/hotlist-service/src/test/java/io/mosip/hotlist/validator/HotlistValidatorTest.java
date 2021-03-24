package io.mosip.hotlist.validator;

import static io.mosip.hotlist.constant.HotlistErrorConstants.ID_TYPE_NOT_ALLOWED;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_INPUT_PARAMETER;
import static io.mosip.hotlist.constant.HotlistErrorConstants.MISSING_INPUT_PARAMETER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import io.mosip.hotlist.constant.HotlistStatus;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.util.DateUtils;

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
	public void testNullId() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateId(null, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(MISSING_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "id")));
	}

	@Test
	public void testInvalidId() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateId(" ", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "id")));
	}

	@Test
	public void testNullIdType() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType(null, errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(MISSING_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "idType")));
	}

	@Test
	public void testInvalidIdType() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType(" ", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(errors.getAllErrors().get(0).getDefaultMessage()
				.contentEquals(String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "idType")));
	}

	@Test
	public void testIdTypeNotAllowed() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType("1234", errors);
		assertTrue(errors.getAllErrors().get(0).getCode().contentEquals(ID_TYPE_NOT_ALLOWED.getErrorCode()));
		assertTrue(
				errors.getAllErrors().get(0).getDefaultMessage().contentEquals(ID_TYPE_NOT_ALLOWED.getErrorMessage()));
	}

	@Test
	public void testIdTypeAllowed() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateIdType("idType", errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	@Ignore
	public void testNullStatus() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		request.setStatus(null);
		request.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime());
		RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		validator.validate(requestWrapper, errors);
		System.err.println(errors.getAllErrors());
		assertFalse(errors.hasErrors());
	}

	@Test
	public void testInvalidStatus() {
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

	@Test
	@Ignore
	public void testValidBlockedStatus() {
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

	@Test
	@Ignore
	public void testValidUnblockedStatus() {
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
	public void testNullExpiryTimestamp() {
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
	public void testPastDatedExpiryTimestamp() {
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
	public void testFutureDatedExpiryTimestamp() {
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
