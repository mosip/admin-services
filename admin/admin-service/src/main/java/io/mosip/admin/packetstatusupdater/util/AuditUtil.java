package io.mosip.admin.packetstatusupdater.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.packetstatusupdater.constant.AuditErrorCode;
import io.mosip.admin.packetstatusupdater.dto.AuditRequestDto;
import io.mosip.admin.packetstatusupdater.dto.AuditResponseDto;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.ValidationException;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;

/**
 * AuditUtil.
 */

@Component
public class AuditUtil {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuditUtil.class);

	/** The Constant APPLICATION_ID. */
	private static final String APPLICATION_ID = "10009";

	/** The Constant APPLICATION_NAME. */
	private static final String APPLICATION_NAME = "Admin_Portal";

	/** The Constant UNKNOWN_HOST. */
	private static final String UNKNOWN_HOST = "Unknown Host";

	private String hostIpAddress = null;

	private String hostName = null;

	private volatile AtomicInteger eventCounter;

	@Value("${mosip.kernel.masterdata.audit-url}")
	private String auditUrl;

	@Autowired
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	/**
	 * Audit request.
	 *
	 * the audit request dto
	 */
	@PostConstruct
	private void init() {
		if(System.getProperty("seqGen")==null) {
		eventCounter = new AtomicInteger(500);
		}else {
			Integer eventCount=Integer.getInteger(System.getProperty("seqGen"));
			eventCounter=new AtomicInteger(eventCount);
		}
		
	}
	
	/**
	 * Validate security context holder.
	 *
	 * @return true, if successful
	 */
	private boolean validateSecurityContextHolder() {
		Predicate<SecurityContextHolder> contextPredicate = i -> SecurityContextHolder.getContext() != null;
		Predicate<SecurityContextHolder> authPredicate = i -> SecurityContextHolder.getContext()
				.getAuthentication() != null;
		Predicate<SecurityContextHolder> principlePredicate = i -> SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal() != null;
		return contextPredicate.and(authPredicate).and(principlePredicate) != null;

	}

	/**
	 * Gets the server ip.
	 *
	 * @return the server ip
	 */
	public String getServerIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return UNKNOWN_HOST;
		}
	}

	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public String getServerName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return UNKNOWN_HOST;
		}
	}

	/**
	 * To Set the Host Ip & Host Name
	 */
	@PostConstruct
	public void getHostDetails() {
		hostIpAddress = getServerIp();
		hostName = getServerName();
	}

	/**
	 * For Auditing Login Services
	 * 
	 * @param auditRequestDto
	 * @return
	 */
	public void callAuditManager(AuditRequestDto auditRequestDto) {

		RequestWrapper<AuditRequestDto> auditReuestWrapper = new RequestWrapper<>();
		auditReuestWrapper.setRequest(auditRequestDto);
		HttpEntity<RequestWrapper<AuditRequestDto>> httpEntity = new HttpEntity<>(auditReuestWrapper);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(auditUrl, HttpMethod.POST, httpEntity, String.class);

		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			handlException(ex);
		}
		String responseBody = response.getBody();
		getAuditDetailsFromResponse(responseBody);

	}

	private AuditResponseDto getAuditDetailsFromResponse(String responseBody) {

		List<ServiceError> validationErrorsList = null;
		validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
		AuditResponseDto auditResponseDto = null;
		if (!validationErrorsList.isEmpty()) {
			throw new ValidationException(validationErrorsList);
		}
		ResponseWrapper<AuditResponseDto> responseObject = null;
		try {

			responseObject = objectMapper.readValue(responseBody,
					new TypeReference<ResponseWrapper<AuditResponseDto>>() {
					});
			auditResponseDto = responseObject.getResponse();
		} catch (IOException | NullPointerException exception) {
			throw new MasterDataServiceException(AuditErrorCode.AUDIT_PARSE_EXCEPTION.getErrorCode(),
					AuditErrorCode.AUDIT_PARSE_EXCEPTION.getErrorMessage());
		}

		return auditResponseDto;
	}

	private void handlException(HttpStatusCodeException ex) {
		List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(ex.getResponseBodyAsString());

		if (ex.getRawStatusCode() == 401) {
			if (!validationErrorsList.isEmpty()) {
				throw new AuthNException(validationErrorsList);
			} else {
				throw new BadCredentialsException("Authentication failed from AuthManager");
			}
		}
		if (ex.getRawStatusCode() == 403) {
			if (!validationErrorsList.isEmpty()) {
				throw new AuthZException(validationErrorsList);
			} else {
				throw new AccessDeniedException("Access denied from AuthManager");
			}
		}
		throw new MasterDataServiceException(AuditErrorCode.AUDIT_EXCEPTION.getErrorCode(),
				AuditErrorCode.AUDIT_EXCEPTION.getErrorMessage() + ex);

	}


	public  void setAuditRequestDto(EventEnum eventEnum) {
		setAuditRequestDto(eventEnum, null);
	}

	public void setAuditRequestDto(EventEnum eventEnum, String username) {
		if(null==eventEnum)
			return ;
		AuditRequestDto auditRequestDto = new AuditRequestDto();

		auditRequestDto.setHostIp(hostIpAddress);
		auditRequestDto.setHostName(hostName);
		auditRequestDto.setApplicationId(eventEnum.getApplicationId());
		auditRequestDto.setApplicationName(eventEnum.getApplicationName());
		auditRequestDto.setSessionUserId(username == null ?
				SecurityContextHolder.getContext().getAuthentication().getName() : username);
		auditRequestDto.setSessionUserName(username == null ?
				SecurityContextHolder.getContext().getAuthentication().getName() : username);
		auditRequestDto.setCreatedBy(username == null ?
				SecurityContextHolder.getContext().getAuthentication().getName() : username);
		auditRequestDto.setActionTimeStamp(DateUtils.getUTCCurrentDateTime());
		auditRequestDto.setDescription(eventEnum.getDescription());
		auditRequestDto.setEventType(eventEnum.getType());
		auditRequestDto.setEventName(eventEnum.getName());
		auditRequestDto.setModuleId(eventEnum.getModuleId());
		auditRequestDto.setModuleName(eventEnum.getModuleName());
		auditRequestDto.setEventId(eventEnum.getEventId());
		auditRequestDto.setId(eventEnum.getId());
		auditRequestDto.setIdType(eventEnum.getIdType());
		//if current profile is local or dev donot call this method
		if(Arrays.stream(env.getActiveProfiles().length == 0 ?
				env.getDefaultProfiles() : env.getActiveProfiles()).anyMatch(
				environment -> (environment.equalsIgnoreCase("local")) )) {
			LOGGER.info("Recieved Audit : "+auditRequestDto.toString());

		} else {
			callAuditManager(auditRequestDto);
		}
	}

}
