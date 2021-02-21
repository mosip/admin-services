package io.mosip.hotlist.exception;

import static io.mosip.hotlist.constant.HotlistErrorConstants.AUTHORIZATION_FAILED;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_INPUT_PARAMETER;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_REQUEST;
import static io.mosip.hotlist.constant.HotlistErrorConstants.UNKNOWN_ERROR;

import java.time.format.DateTimeParseException;
import java.util.Collections;

import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;

@RestControllerAdvice
public class HotlistExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String REQUEST_TIME = "requesttime";

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//		mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO, ID_REPO_EXCEPTION_HANDLER,
//				"handleAllExceptions - \n" + ExceptionUtils.getStackTrace(Objects.isNull(rootCause) ? ex : rootCause));
		return new ResponseEntity<>(
				buildExceptionResponse(UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getErrorMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
//		mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO, ID_REPO_EXCEPTION_HANDLER,
//				"handleAccessDeniedException - \n"
//						+ ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(
				buildExceptionResponse(AUTHORIZATION_FAILED.getErrorCode(), AUTHORIZATION_FAILED.getErrorMessage()),
				HttpStatus.OK);
	}

	@ExceptionHandler(HotlistAppException.class)
	protected ResponseEntity<Object> handleIdAppException(HotlistAppException ex, WebRequest request) {
//		mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO, ID_REPO_EXCEPTION_HANDLER,
//				"handleIdAppException - \n" + ExceptionUtils.getStackTrace(Objects.isNull(rootCause) ? ex : rootCause));
		return new ResponseEntity<>(
				buildExceptionResponse(UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getErrorMessage()), HttpStatus.OK);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object errorMessage,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO, ID_REPO_EXCEPTION_HANDLER,
//				"handleExceptionInternal - \n"
//						+ ExceptionUtils.getStackTrace(Objects.isNull(rootCause) ? ex : rootCause));
		if (ex instanceof HttpMessageNotReadableException && org.apache.commons.lang3.exception.ExceptionUtils
				.getRootCause(ex).getClass().isAssignableFrom(DateTimeParseException.class)) {
			return new ResponseEntity<>(buildExceptionResponse(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), REQUEST_TIME)), HttpStatus.OK);
		} else if (ex instanceof HttpMessageNotReadableException || ex instanceof ServletException
				|| ex instanceof BeansException) {
			return new ResponseEntity<>(
					buildExceptionResponse(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorMessage()),
					HttpStatus.OK);
		} else {
			return handleAllExceptions(ex, request);
		}
	}

	private Object buildExceptionResponse(String errorCode, String errorMessage) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		response.setErrors(Collections.singletonList(new ServiceError(errorCode, errorMessage)));
		return response;
	}
}
