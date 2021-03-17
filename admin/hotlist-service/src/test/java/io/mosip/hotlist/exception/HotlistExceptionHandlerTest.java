package io.mosip.hotlist.exception;

import static org.junit.Assert.assertTrue;

import java.time.format.DateTimeParseException;

import javax.naming.NoPermissionException;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.kernel.core.http.ResponseWrapper;

/**
 * @author Manoj SP
 *
 */
public class HotlistExceptionHandlerTest {

	HotlistExceptionHandler handler = new HotlistExceptionHandler();

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleAllExceptions() {
		ResponseEntity<Object> response = handler
				.handleAllExceptions(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorCode()));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorMessage()));
	}

	@Test
	public void testHandleAccessDeniedException() {
		ResponseEntity<ResponseWrapper<HotlistRequestResponseDTO>> response = handler
				.handleAccessDeniedException(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
		assertTrue(response.getBody().getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.AUTHORIZATION_FAILED.getErrorCode()));
		assertTrue(response.getBody().getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.AUTHORIZATION_FAILED.getErrorMessage()));
	}

	@Test
	public void testHandleHotlistAppException() {
		ResponseEntity<ResponseWrapper<HotlistRequestResponseDTO>> response = handler
				.handleHotlistAppException(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
		assertTrue(response.getBody().getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorCode()));
		assertTrue(response.getBody().getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorMessage()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleExceptionInternalMethodArgNotValid() {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		errors.reject("", "");
		ResponseEntity<Object> response = handler
				.handleExceptionInternal(new MethodArgumentNotValidException(null, errors), null, null, null, null);
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode().contentEquals(""));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage().contentEquals(""));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleExceptionInternalInvalidExpiryTimestamp() {
		ResponseEntity<Object> response = handler.handleExceptionInternal(
				new HttpMessageNotReadableException("expiryTimestamp", new DateTimeParseException("", "", 0)), null,
				null, null, null);
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage().contentEquals(String
				.format(HotlistErrorConstants.INVALID_INPUT_PARAMETER.getErrorMessage(), "request/expiryTimestamp")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleExceptionInternalInvalidRequestTime() {
		ResponseEntity<Object> response = handler.handleExceptionInternal(
				new HttpMessageNotReadableException("requesttime", new DateTimeParseException("", "", 0)), null, null,
				null, null);
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode()));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage().contentEquals(String
				.format(HotlistErrorConstants.INVALID_INPUT_PARAMETER.getErrorMessage(), "requesttime")));
	}

	@SuppressWarnings({ "unchecked", "serial" })
	@Test
	public void testHandleExceptionInternalBeanException() {
		ResponseEntity<Object> response = handler.handleExceptionInternal(new BeansException("") {
		}, null, null, null, null);
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.INVALID_REQUEST.getErrorCode()));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.INVALID_REQUEST.getErrorMessage()));
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testHandleExceptionInternalUnknownException() {
		ResponseEntity<Object> response = handler.handleExceptionInternal(new NoPermissionException(), null, null, null,
				null);
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorCode()));
		assertTrue(((ResponseWrapper<Object>) response.getBody()).getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorMessage()));
	}
}
