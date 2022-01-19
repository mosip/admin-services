package io.mosip.admin.packetstatusupdater.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.packetstatusupdater.constant.PacketStatusUpdateErrorCode;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.service.PacketStatusUpdateService;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.idvalidator.spi.RidValidator;
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
	
	@Autowired
	private RidValidator<String> ridValidatorImpl;

	private static final String SLASH = "/";

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
			if (!ridValidatorImpl.validateId(rId)) {
				throw new MasterDataServiceException(PacketStatusUpdateErrorCode.RID_INVALID.getErrorCode(),
						PacketStatusUpdateErrorCode.RID_INVALID.getErrorMessage());
			}

			HttpHeaders packetHeaders = new HttpHeaders();
			packetHeaders.setContentType(MediaType.APPLICATION_JSON);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(packetUpdateStatusUrl)
					.path(rId);
			ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toUriString(), String.class);
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
				packStautsDto.stream().forEach(pcksts -> {
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumBasedOnPAcketStatus(pcksts));
				});
				return regProcPacketStatusRequestDto;
			}
		}
		catch (RequestException e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw new RequestException(e.getErrorCode(),
					e.getErrorText(), e);
		}catch (Exception e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw new MasterDataServiceException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage(), e);
		}
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.PACKET_STATUS_ERROR,rId));
		throw new RequestException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
				PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage());
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

		if(validationErrorsList != null && !validationErrorsList.isEmpty()) {
			for (ServiceError serviceError : validationErrorsList) {
				switch (serviceError.getErrorCode()) {
					case "RPR-RTS-001":
						auditUtil.setAuditRequestDto(EventEnum.RID_NOT_FOUND);
						logger.error(PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorMessage());
						throw new RequestException(PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorCode(),
								PacketStatusUpdateErrorCode.RID_NOT_FOUND.getErrorMessage());

					case "KER-MSD-042":
						auditUtil.setAuditRequestDto(EventEnum.CENTRE_NOT_EXISTS);
						logger.error(PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorMessage());
						throw new RequestException(PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorCode(),
								PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorMessage());

					case "ADM-PKT-001":
						auditUtil.setAuditRequestDto(EventEnum.USER_NOT_AUTHORIZED);
						logger.error(PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorMessage());
						throw new RequestException(PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorCode(),
								PacketStatusUpdateErrorCode.ADMIN_UNAUTHORIZED.getErrorMessage());
				}
			}
			auditUtil.setAuditRequestDto(EventEnum.PACKET_STATUS_ERROR);
			logger.error("Unknown error from get packet status API : {}", validationErrorsList);
			throw new RequestException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage());
		}

		try {
			ResponseWrapper<T> responseObject = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<T>>() {});
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
