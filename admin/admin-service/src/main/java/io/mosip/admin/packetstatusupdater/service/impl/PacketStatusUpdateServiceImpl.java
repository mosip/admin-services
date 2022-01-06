package io.mosip.admin.packetstatusupdater.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.mosip.admin.packetstatusupdater.constant.PacketStatusUpdateErrorCode;
import io.mosip.admin.packetstatusupdater.dto.Metadata;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;
import io.mosip.admin.packetstatusupdater.dto.SecretKeyRequest;
import io.mosip.admin.packetstatusupdater.dto.TokenRequestDTO;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.service.PacketStatusUpdateService;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.signatureutil.exception.ParseResponseException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.logger.logback.factory.Logfactory;

/**
 * Packet Status Update service.
 * 
 * @author Srinivasan
 *
 */
@Component
public class PacketStatusUpdateServiceImpl implements PacketStatusUpdateService {
	
	private final Logger logger = Logfactory.getSlf4jLogger(PacketStatusUpdateServiceImpl.class);


	/** The packet update status url. */
	@Value("${mosip.kernel.packet-status-update-url}")
	private String packetUpdateStatusUrl;

	/** The zone validation url. */
	@Value("${mosip.kernel.zone-validation-url}")
	private String zoneValidationUrl;

	@Value("${mosip.supported-languages}")
	private String supportedLang;

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuditUtil auditUtil;
	
	@Autowired
	private Environment environment;

	@Autowired
	private Properties packetProperties;

	private static final String SLASH = "/";
	
	@Value("${mosip.admin.globalproperty.prefix}")
	private String globalPropertyPrefix;
	@Value("${mosip.admin.globalproperty.suffix}")
	private String globalPropertySuffix;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.admin.packetstatusupdater.service.PacketStatusUpdateService#
	 * getStatus(java.lang.String)
	 */
	@Override
	public PacketStatusUpdateResponseDto getStatus(String rId, String langCode) {
		//Any Packet status can be viewed from any admin from any center
		auditUtil.setAuditRequestDto(EventEnum.PACKET_STATUS);
		return getPacketStatus(rId);
	}

	/**
	 * Gets the packet status.
	 *
	 * @param rId
	 *            the r id
	 * @return the packet status
	 */
	@SuppressWarnings({ "unchecked" })
	private PacketStatusUpdateResponseDto getPacketStatus(String rId) {
		try {

			HttpHeaders packetHeaders = new HttpHeaders();
			packetHeaders.setContentType(MediaType.APPLICATION_JSON);
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(packetUpdateStatusUrl).append(SLASH).append(rId);
			ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				List<PacketStatusUpdateDto> packetStatusUpdateDtos = getPacketResponse(ArrayList.class,
						response.getBody());
				PacketStatusUpdateResponseDto regProcPacketStatusRequestDto = new PacketStatusUpdateResponseDto();
				List<PacketStatusUpdateDto> packStautsDto = objectMapper.convertValue(packetStatusUpdateDtos,
						new TypeReference<List<PacketStatusUpdateDto>>() {
						});
				packStautsDto.sort(createdDateTimesResultComparator);
				setStatusMessage(packStautsDto);
				regProcPacketStatusRequestDto.setPacketStatusUpdateList(packStautsDto);
				packStautsDto.stream().forEach(pcksts->{
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumBasedOnPAcketStatus(pcksts));
				});
				return regProcPacketStatusRequestDto;
			}
		} catch (Exception e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw new MasterDataServiceException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage(), e);
		}
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.PACKET_STATUS_ERROR,rId));
		return null;

	}


	/**
	 * Gets the packet response.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param responseBody
	 *            the response body
	 * @return the packet response
	 */

	private <T> T getPacketResponse(Class<T> clazz, String responseBody) {
		List<ServiceError> validationErrorsList = null;
		validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
		T packetStatusUpdateDto = null;
		if (!validationErrorsList.isEmpty()) {
			if (validationErrorsList.size() == 1 && validationErrorsList.get(0).getErrorCode().equals("RPR-RTS-001")) {
				auditUtil.setAuditRequestDto(EventEnum.RID_NOT_FOUND);
				logger.error("SESSIONID", "ADMIN-SERVICE",
						"ADMIN-SERVICE", PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorMessage());
				throw new RequestException(PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorCode(),
						PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorMessage());
			} else if (validationErrorsList.size() == 1
					&& validationErrorsList.get(0).getErrorCode().equals("KER-MSD-042")) {
				auditUtil.setAuditRequestDto(EventEnum.CENTRE_NOT_EXISTS);
				logger.error("SESSIONID", "ADMIN-SERVICE",
						"ADMIN-SERVICE", PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorMessage());
				throw new RequestException(PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorCode(),

						PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorMessage());
			}
			else if (validationErrorsList.size() == 1
					&& validationErrorsList.get(0).getErrorCode().equals("ADM-PKT-001")) {
				logger.error("SESSIONID", "ADMIN-SERVICE",
						"ADMIN-SERVICE", PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorMessage());
				throw new RequestException(PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorCode(),

						PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorMessage());
			}
		}
		ResponseWrapper<T> responseObject = null;
		try {

			responseObject = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<T>>() {
			});
			packetStatusUpdateDto = responseObject.getResponse();
		} catch (NullPointerException | java.io.IOException exception) {
			auditUtil.setAuditRequestDto(EventEnum.PACKET_JSON_PARSE_EXCEPTION);
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", exception.getMessage() + ExceptionUtils.getStackTrace(exception));
			throw new ParseResponseException(PacketStatusUpdateErrorCode.PACKET_JSON_PARSE_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_JSON_PARSE_EXCEPTION.getErrorMessage());

		}
		return packetStatusUpdateDto;
	}

	/*Comparator<PacketStatusUpdateDto> createdDateTimesComparator = new Comparator<PacketStatusUpdateDto>() {
		@Override
		public int compare(PacketStatusUpdateDto o1, PacketStatusUpdateDto o2) {
			LocalDateTime o1CreatedDateTimes = DateUtils.parseToLocalDateTime(o1.getCreatedDateTimes());
			LocalDateTime o2CreatedDateTimes = DateUtils.parseToLocalDateTime(o2.getCreatedDateTimes());
			return o2CreatedDateTimes.compareTo(o1CreatedDateTimes);
		}
	};*/

	Comparator<PacketStatusUpdateDto> createdDateTimesResultComparator = new Comparator<PacketStatusUpdateDto>() {
		@Override
		public int compare(PacketStatusUpdateDto o1, PacketStatusUpdateDto o2) {
			LocalDateTime o1CreatedDateTimes = DateUtils.parseToLocalDateTime(o1.getCreatedDateTimes());
			LocalDateTime o2CreatedDateTimes = DateUtils.parseToLocalDateTime(o2.getCreatedDateTimes());
			return o1CreatedDateTimes.compareTo(o2CreatedDateTimes);
		}
	};
	
	/**
	 * @param packStautsDtos
	 * Description: It's calling the property file and set the comment based on the sub-status-code
	 */
	private void setStatusMessage(List<PacketStatusUpdateDto> packStautsDtos) {
		if (null != packetProperties) {
			packStautsDtos.stream().forEach(packStautsDto -> {
				String subStatusCode = packStautsDto.getSubStatusCode();
				if (subStatusCode != null && !subStatusCode.isEmpty()) {
					packStautsDto.setStatusComment(packetProperties.getProperty(subStatusCode));
				}
			});
		}
	}

}
