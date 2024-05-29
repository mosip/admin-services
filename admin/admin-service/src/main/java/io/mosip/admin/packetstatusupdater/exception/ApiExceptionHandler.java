package io.mosip.admin.packetstatusupdater.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.servlet.http.HttpServletRequest;

import io.mosip.admin.bulkdataupload.service.impl.BulkDataUploadServiceImpl;
import io.mosip.kernel.authcodeflowproxy.api.exception.AuthCodeProxyExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.constant.AuthAdapterErrorCode;
import io.mosip.admin.packetstatusupdater.constant.RequestErrorCode;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.EmptyCheckUtils;


/**
 * Rest Controller Advice for Admin
 * 
 * @author Srinivasan
 *
 * @since 1.0.0
 */
@RestControllerAdvice
public class ApiExceptionHandler {

	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);


/*	@ExceptionHandler(value = {AuthCodeProxyExceptionHandler.class})
	public ResponseEntity<ResponseWrapper<ServiceError>> defaultErrorHandler(
			final HttpServletRequest httpServletRequest, AuthCodeProxyExceptionHandler e) throws IOException {
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		errorResponse.getErrors().addAll(e.getList());
		return new ResponseEntity<>(errorResponse, e.getHttpStatus());
	}*/
	@ExceptionHandler(RequestException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> controlRequestException(
			final HttpServletRequest httpServletRequest, final RequestException e) throws IOException {
		logger.info("exception : {} ",e);
		return getErrorResponseEntity(e, HttpStatus.OK, httpServletRequest);
	}

	@ExceptionHandler(MasterDataServiceException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> controlDataServiceException(
			final HttpServletRequest httpServletRequest, final MasterDataServiceException e) throws IOException {
		logger.info("exception : {} ",e);
		return getErrorResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, httpServletRequest);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> controlDataNotFoundException(
			final HttpServletRequest httpServletRequest, final DataNotFoundException e) throws IOException {
		return getErrorResponseEntity(e, HttpStatus.OK, httpServletRequest);
	}



	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> onAccessDeniedException(
			final HttpServletRequest httpServletRequest, final AccessDeniedException e) throws IOException {
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		ServiceError error = new ServiceError(AuthAdapterErrorCode.FORBIDDEN.getErrorCode(),
				AuthAdapterErrorCode.FORBIDDEN.getErrorMessage());
		errorResponse.getErrors().add(error);
		ExceptionUtils.logRootCause(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthZException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> onAuthZException(
			final HttpServletRequest httpServletRequest, final AuthZException e) throws IOException {
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		ServiceError error = new ServiceError(AuthAdapterErrorCode.FORBIDDEN.getErrorCode(),
				AuthAdapterErrorCode.FORBIDDEN.getErrorMessage());
		errorResponse.getErrors().add(error);
		ExceptionUtils.logRootCause(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthNException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> onAuthNException(
			final HttpServletRequest httpServletRequest, final AuthNException e) throws IOException {
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		ServiceError error = new ServiceError(AuthAdapterErrorCode.UNAUTHORIZED.getErrorCode(),
				AuthAdapterErrorCode.UNAUTHORIZED.getErrorMessage());
		errorResponse.getErrors().add(error);
		ExceptionUtils.logRootCause(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	public ResponseEntity<ResponseWrapper<ServiceError>> defaultErrorHandler(
			final HttpServletRequest httpServletRequest, Exception e) throws IOException {
		logger.info("exception : {} ",e);
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		ServiceError error = new ServiceError(RequestErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), e.getMessage());
		errorResponse.getErrors().add(error);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ResponseWrapper<ServiceError>> getErrorResponseEntity(BaseUncheckedException e,
			HttpStatus httpStatus, HttpServletRequest httpServletRequest) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = setErrors(httpServletRequest);
		ServiceError error = new ServiceError(e.getErrorCode(), e.getErrorText());
		responseWrapper.getErrors().add(error);
		return new ResponseEntity<>(responseWrapper, httpStatus);
	}

	private ResponseWrapper<ServiceError> setErrors(HttpServletRequest httpServletRequest) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponsetime(LocalDateTime.now(ZoneId.of("UTC")));
		String requestBody = null;
		if (httpServletRequest instanceof ContentCachingRequestWrapper) {
			requestBody = new String(((ContentCachingRequestWrapper) httpServletRequest).getContentAsByteArray());
		}
		if (EmptyCheckUtils.isNullEmpty(requestBody)) {
			return responseWrapper;
		}
		objectMapper.registerModule(new JavaTimeModule());
		JsonNode reqNode = objectMapper.readTree(requestBody);
		responseWrapper.setId(reqNode.path("id").asText());
		responseWrapper.setVersion(reqNode.path("version").asText());
		return responseWrapper;
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResponseWrapper<ServiceError>> validationException(HttpServletRequest httpServletRequest,
			final ValidationException exception) throws IOException {
		ResponseWrapper<ServiceError> errorResponse = setErrors(httpServletRequest);
		errorResponse.getErrors().addAll(exception.getErrors());
		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	}

}
