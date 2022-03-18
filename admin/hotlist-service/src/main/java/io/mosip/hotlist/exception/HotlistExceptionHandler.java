package io.mosip.hotlist.exception;

import static io.mosip.hotlist.constant.HotlistErrorConstants.AUTHORIZATION_FAILED;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_INPUT_PARAMETER;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_REQUEST;
import static io.mosip.hotlist.constant.HotlistErrorConstants.UNKNOWN_ERROR;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * The Class HotlistExceptionHandler.
 *
 * @author Manoj SP
 */
@RestControllerAdvice
public class HotlistExceptionHandler extends ResponseEntityExceptionHandler {

	/** The Constant REQUEST_TIME. */
	private static final String REQUEST_TIME = "requesttime";

	/** The Constant EXPIRY_TIMESTAMP. */
	private static final CharSequence EXPIRY_TIMESTAMP = "expiryTimestamp";

	/** The Constant EXPIRY_TIMESTAMP_PATH. */
	private static final String EXPIRY_TIMESTAMP_PATH = "request/" + EXPIRY_TIMESTAMP;

	/** The Constant HOTLIST_SERVICE. */
	private static final String HOTLIST_SERVICE = "Hotlist-service";

	/** The Constant HOTLIST_EXCEPTION_HANDLER. */
	private static final String HOTLIST_EXCEPTION_HANDLER = "HotlistExceptionHandler";

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistExceptionHandler.class);

	/**
	 * Handle all exceptions.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
		mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE, HOTLIST_EXCEPTION_HANDLER,
				"handleAllExceptions - \n" + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(
				buildExceptionResponse(UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getErrorMessage()), HttpStatus.OK);
	}

	/**
	 * Handle access denied exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ResponseWrapper<HotlistRequestResponseDTO>> handleAccessDeniedException(Exception ex) {
		mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE, HOTLIST_EXCEPTION_HANDLER,
				"handleAccessDeniedException - \n" + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(
				buildExceptionResponse(AUTHORIZATION_FAILED.getErrorCode(), AUTHORIZATION_FAILED.getErrorMessage()),
				HttpStatus.OK);
	}

	/**
	 * Handle id app exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(HotlistAppException.class)
	protected ResponseEntity<ResponseWrapper<HotlistRequestResponseDTO>> handleHotlistAppException(
			HotlistAppException ex) {
		mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE, HOTLIST_EXCEPTION_HANDLER,
				"handleIdAppException - \n" + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(
				buildExceptionResponse(UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getErrorMessage()), HttpStatus.OK);
	}

	/**
	 * Handle exception internal.
	 *
	 * @param ex           the ex
	 * @param errorMessage the error message
	 * @param headers      the headers
	 * @param status       the status
	 * @param request      the request
	 * @return the response entity
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object errorMessage,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE, HOTLIST_EXCEPTION_HANDLER,
				"handleExceptionInternal - \n" + (ex instanceof MethodArgumentNotValidException
						? ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()
						: ExceptionUtils.getStackTrace(ex)));
		if (ex instanceof MethodArgumentNotValidException) {
			ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
			response.setErrors(((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().stream()
					.map(objectError -> new ServiceError(objectError.getCode(), objectError.getDefaultMessage()))
					.collect(Collectors.toList()));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else if (ex instanceof HttpMessageNotReadableException && Objects.nonNull(ex.getCause())
				&& org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(ex).getClass()
						.isAssignableFrom(DateTimeParseException.class)) {
			String expMsg=((HttpMessageNotReadableException) ex).getMessage() ;
			if (null!=expMsg && expMsg.contains(EXPIRY_TIMESTAMP)) {
				return new ResponseEntity<>(
						buildExceptionResponse(INVALID_INPUT_PARAMETER.getErrorCode(),
								String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), EXPIRY_TIMESTAMP_PATH)),
						HttpStatus.OK);
			} else if (null!=expMsg && expMsg.contains(REQUEST_TIME)) {
				return new ResponseEntity<>(buildExceptionResponse(INVALID_INPUT_PARAMETER.getErrorCode(),
						String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), REQUEST_TIME)), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						buildExceptionResponse(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorMessage()),
						HttpStatus.OK);
			}
		} else if (ex instanceof HttpMessageNotReadableException || ex instanceof ServletException
				|| ex instanceof BeansException) {
			return new ResponseEntity<>(
					buildExceptionResponse(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorMessage()),
					HttpStatus.OK);
		} else {
			return handleAllExceptions(ex);
		}
	}

	/**
	 * Builds the exception response.
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 * @return the object
	 */
	private ResponseWrapper<HotlistRequestResponseDTO> buildExceptionResponse(String errorCode, String errorMessage) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		response.setErrors(Collections.singletonList(new ServiceError(errorCode, errorMessage)));
		return response;
	}
}
