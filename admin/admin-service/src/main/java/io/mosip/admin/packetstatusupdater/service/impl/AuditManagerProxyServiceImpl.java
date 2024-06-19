package io.mosip.admin.packetstatusupdater.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import io.mosip.admin.packetstatusupdater.constant.AdminManagerProxyErrorCode;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerResponseDto;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.service.AuditManagerProxyService;
import io.mosip.kernel.core.http.RequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Megha Tanga
 *
 */
@Service
public class AuditManagerProxyServiceImpl implements AuditManagerProxyService {

	private static final Logger logger = LoggerFactory.getLogger(AuditManagerProxyServiceImpl.class);
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	@Value("${mosip.kernel.audit.manager.api}")
	String auditmanagerapi;

	@Value("#{'${mosip.admin.audit.manager.allowed.moduleId:ADM-NAV,KER-MSD}'.split(',')}")
	private List<String> allowedModuleIds;

	@Value("#{'${mosip.admin.audit.manager.allowed.moduleName:Kernel-Masterdata,Navigation}'.split(',')}")
	private List<String> allowedModuleNames;

	@Value("#{'${mosip.admin.audit.manager.allowed.eventTypes:Navigation: Click Event,Navigation: Page View Event}'.split(',')}")
	private List<String> allowedEventTypes;

	@Value("#{'${mosip.admin.audit.manager.allowed.ids:NO_ID}'.split(',')}")
	private List<String> allowedIds;

	@Value("#{'${mosip.admin.audit.manager.allowed.idTypes:ADMIN}'.split(',')}")
	private List<String> allowedIdTypes;

	@Value("${mosip.admin.audit.manager.eventId.pattern:^ADM-[0-9]{3}$}")
	private String eventIdPattern;

	@Value("${mosip.admin.audit.manager.eventName.pattern:^(Click|Page View): ([\\W|\\w]{1,100}$)}")
	private String eventNamePattern;

	@Value("${mosip.admin.audit.manager.application.id:10009}")
	private String applicationId;

	@Value("${mosip.admin.audit.manager.application.name:Admin Portal}")
	private String applicationName;

	@Value("${mosip.admin.audit.manager.actiontime.maxlimit.mins:-5}")
	private int maxMinutes;

	@Value("${mosip.admin.audit.manager.actiontime.minlimit.mins:5}")
	private int minMinutes;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	HttpServletRequest request;

	private String getHeaderValue(String headerName) {
		Iterator<String> headerNames = request.getHeaderNames().asIterator();
		while(headerNames.hasNext()) {
			String current = headerNames.next();
			if(current.equalsIgnoreCase(headerName))
				return request.getHeader(current);
		}
		return null;
	}

	@Override
	public AuditManagerResponseDto logAdminAudit(AuditManagerRequestDto auditManagerRequestDto) {

		String hostName = getHeaderValue(X_FORWARDED_FOR);
		if(hostName == null) {
			hostName = getHeaderValue(HttpHeaders.ORIGIN);
		}

		validateAuditRequestDto(auditManagerRequestDto, hostName);

		auditManagerRequestDto.setHostIp(hostName);
		auditManagerRequestDto.setHostName(hostName);
		auditManagerRequestDto.setApplicationId(applicationId);
		auditManagerRequestDto.setApplicationName(applicationName);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		auditManagerRequestDto.setSessionUserId(username);
		auditManagerRequestDto.setSessionUserName(username);
		auditManagerRequestDto.setCreatedBy(getHeaderValue(HttpHeaders.REFERER));

		RequestWrapper<AuditManagerRequestDto> request = new RequestWrapper<>();
		request.setId("mosip.admin.audit");
		request.setVersion("0.1");
		request.setRequesttime(LocalDateTime.now());
		request.setRequest(auditManagerRequestDto);

		HttpEntity<RequestWrapper<AuditManagerRequestDto>> entity = new HttpEntity<>(request);

		try {
			restTemplate.postForEntity(auditmanagerapi, entity, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			throw new MasterDataServiceException(AdminManagerProxyErrorCode.ADMIN_LOG_FAILED.getErrorCode(),
					AdminManagerProxyErrorCode.ADMIN_LOG_FAILED.getErrorMessage(), ex);
		}

		AuditManagerResponseDto auditManagerResponseDto = new AuditManagerResponseDto();
		auditManagerResponseDto.setStatus("Success");
		auditManagerResponseDto.setMessage("Audit logged successfuly");
		return auditManagerResponseDto;

	}

	private void validateAuditRequestDto(AuditManagerRequestDto auditManagerRequestDto, String hostName) {
		if(hostName == null || hostName.isBlank()) {
			logger.error("Audit log hostname validation failed");
			throw new MasterDataServiceException(AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorCode(),
					AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorMessage());
		}

		validateRequestTimestamp(auditManagerRequestDto.getActionTimeStamp());

		if(!allowedModuleIds.contains(auditManagerRequestDto.getModuleId()) ||
				!allowedModuleNames.contains(auditManagerRequestDto.getModuleName()) ||
				!allowedEventTypes.contains(auditManagerRequestDto.getEventType()) ||
				!allowedIds.contains(auditManagerRequestDto.getId()) ||
				!allowedIdTypes.contains(auditManagerRequestDto.getIdType())) {
			logger.error("Audit log allowed values validation failed");
			throw new MasterDataServiceException(AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorCode(),
					AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorMessage());
		}

		if(!Pattern.matches(eventIdPattern, auditManagerRequestDto.getEventId()) ||
				!Pattern.matches(eventNamePattern, auditManagerRequestDto.getEventName())) {
			logger.error("Audit log pattern check validation failed");
			throw new MasterDataServiceException(AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorCode(),
					AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorMessage());
		}
	}

	private void validateRequestTimestamp(@NotNull LocalDateTime actionTimestamp) {
		long value = actionTimestamp.until(LocalDateTime.now(ZoneOffset.UTC), ChronoUnit.MINUTES);
		if(value <= minMinutes && value >= maxMinutes)  { return; }

		logger.error("Audit log action timestamp validation failed : {}", actionTimestamp);
		throw new MasterDataServiceException(AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorCode(),
				AdminManagerProxyErrorCode.INVALID_ADMIN_LOG.getErrorMessage());
	}

}