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

		if (langCode == null) {
			langCode = supportedLang.split(",")[0];
		}
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.AUTH_RID_WITH_ZONE,rId));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
		for(GrantedAuthority x:authentication.getAuthorities()) {
			if(x.getAuthority().equals("ROLE_GLOBAL_ADMIN")) {		
				auditUtil.setAuditRequestDto(EventEnum.PACKET_STATUS);
				return getPacketStatus(rId, langCode);
			}
		}	
		if(!authorizeRidWithZone(rId)) {
			return null;
		}
		auditUtil.setAuditRequestDto(EventEnum.PACKET_STATUS);
		return getPacketStatus(rId, langCode);
	}

	/**
	 * Gets the packet status.
	 *
	 * @param rId
	 *            the r id
	 * @return the packet status
	 */
	@SuppressWarnings({ "unchecked" })
	private PacketStatusUpdateResponseDto getPacketStatus(String rId, String langCode) {
		try {

			HttpHeaders packetHeaders = new HttpHeaders();
			packetHeaders.setContentType(MediaType.APPLICATION_JSON);
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(packetUpdateStatusUrl).append(SLASH).append(rId);
			RestTemplate restTemplate1=new RestTemplate();
			ResponseEntity<String> response = restTemplate1.exchange(urlBuilder.toString(), HttpMethod.GET, setRequestHeader(),
					String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				List<PacketStatusUpdateDto> packetStatusUpdateDtos = getPacketResponse(ArrayList.class,
						response.getBody());
				PacketStatusUpdateResponseDto regProcPacketStatusRequestDto = new PacketStatusUpdateResponseDto();
				List<PacketStatusUpdateDto> packStautsDto = objectMapper.convertValue(packetStatusUpdateDtos,
						new TypeReference<List<PacketStatusUpdateDto>>() {
						});
				packStautsDto.sort(createdDateTimesResultComparator);
				setStatusMessage(packStautsDto, langCode);
				regProcPacketStatusRequestDto.setPacketStatusUpdateList(packStautsDto);
				packStautsDto.stream().forEach(pcksts->{
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumBasedOnPAcketStatus(pcksts));
				});
				return regProcPacketStatusRequestDto;
			}
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", ex.getMessage() + ExceptionUtils.getStackTrace(ex));
			throwRestExceptions(ex);
		} catch (RestClientException e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw new MasterDataServiceException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage(), e);
		} catch (IOException e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw new MasterDataServiceException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
					PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorMessage(), e);
		}
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.PACKET_STATUS_ERROR,rId));
		return null;

	}

	/**
	 * Throw rest exceptions.
	 *
	 * @param ex
	 *            the ex
	 */
	private void throwRestExceptions(HttpStatusCodeException ex) {
		List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(ex.getResponseBodyAsString());

		if (ex.getRawStatusCode() == 401) {
			if (!validationErrorsList.isEmpty()) {
				auditUtil.setAuditRequestDto(EventEnum.AUTHEN_ERROR_401);
				throw new AuthNException(validationErrorsList);
			} else {
				auditUtil.setAuditRequestDto(EventEnum.AUTH_FAILED_AUTH_MANAGER);
				throw new BadCredentialsException("Authentication failed from AuthManager");
			}
		}
		if (ex.getRawStatusCode() == 403) {
			if (!validationErrorsList.isEmpty()) {
				auditUtil.setAuditRequestDto(EventEnum.AUTHEN_ERROR_403);
				throw new AuthZException(validationErrorsList);
			} else {
				auditUtil.setAuditRequestDto(EventEnum.ACCESS_DENIED);
				throw new AccessDeniedException("Access denied from AuthManager");
			}
		}
		auditUtil.setAuditRequestDto(EventEnum.PACKET_STATUS_ERROR);
		throw new MasterDataServiceException(PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorCode(),
				PacketStatusUpdateErrorCode.CENTER_ID_NOT_PRESENT.getErrorMessage(), ex);

	}

	/**
	 * Authorize rid with zone.
	 *
	 * @param rId
	 *            the r id
	 * @return true, if successful
	 */
	private boolean authorizeRidWithZone(String rId) {
		try {
			HttpHeaders packetHeaders = new HttpHeaders();
			// packetHeaders.set("Cookie",
			// "Authorization=Mosip-TokeneyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTAwMDYiLCJtb2JpbGUiOiI3OTE4MzA5ODYiLCJtYWlsIjoiYXVkcmEuYW1lenF1aXRhQHh5ei5jb20iLCJyb2xlIjoiUkVHSVNUUkFUSU9OX0FETUlOLFJFR0lTVFJBVElPTl9PRkZJQ0VSLFpPTkFMX0FETUlOLFJFR0lTVFJBVElPTl9TVVBFUlZJU09SLEdMT0JBTF9BRE1JTiIsIm5hbWUiOiJ0ZXN0IiwicklkIjoiMjc4NDc2NTczNjAwMDI1MjAxOTA4MjAxMDQ5NTciLCJpYXQiOjE1NzQ1OTkxNzIsImV4cCI6MTU3NDYwNTE3Mn0.va8-7sfCL1XlUcI4soQfy9ulNvFsjjI-H6jna7AMvFFoAPwgb3kYzxwBuFXzJcPHnLXaBBziiJXTHqOUwSph5g");
			packetHeaders.setContentType(MediaType.APPLICATION_JSON);
			UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
					rId);
			HttpEntity<RequestWrapper<String>> httpReq = new HttpEntity<>(null, packetHeaders);
			ResponseEntity<String> response = restTemplate.exchange(uribuilder.toUriString(), HttpMethod.GET, httpReq,
					String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				boolean isAuthorized = getPacketResponse(Boolean.class, response.getBody());
				if(isAuthorized)
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.AUTH_RID_WITH_ZONE_SUCCESS,rId));
				else
					auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.AUTH_RID_WITH_ZONE_FAILURE,rId));
				return isAuthorized;
			}
			auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.AUTH_RID_WITH_ZONE_FAILURE,rId));
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throwRestExceptions(e);
		}
		return false;
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
				auditUtil.setAuditRequestDto(EventEnum.RID_INVALID);
				logger.error("SESSIONID", "ADMIN-SERVICE",
						"ADMIN-SERVICE", PacketStatusUpdateErrorCode.RID_INVALID.getErrorMessage());
				throw new RequestException(PacketStatusUpdateErrorCode.RID_INVALID.getErrorCode(),
						PacketStatusUpdateErrorCode.RID_INVALID.getErrorMessage());
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

	Comparator<PacketStatusUpdateDto> createdDateTimesComparator = new Comparator<PacketStatusUpdateDto>() {

		@Override
		public int compare(PacketStatusUpdateDto o1, PacketStatusUpdateDto o2) {
			LocalDateTime o1CreatedDateTimes = DateUtils.parseToLocalDateTime(o1.getCreatedDateTimes());
			LocalDateTime o2CreatedDateTimes = DateUtils.parseToLocalDateTime(o2.getCreatedDateTimes());
			return o2CreatedDateTimes.compareTo(o1CreatedDateTimes);
		}
	};

	Comparator<PacketStatusUpdateDto> createdDateTimesResultComparator = new Comparator<PacketStatusUpdateDto>() {

		@Override
		public int compare(PacketStatusUpdateDto o1, PacketStatusUpdateDto o2) {
			LocalDateTime o1CreatedDateTimes = DateUtils.parseToLocalDateTime(o1.getCreatedDateTimes());
			LocalDateTime o2CreatedDateTimes = DateUtils.parseToLocalDateTime(o2.getCreatedDateTimes());
			return o1CreatedDateTimes.compareTo(o2CreatedDateTimes);
		}
	};

	@SafeVarargs
	private static <T> Predicate<T> distinctTypeCode(Function<? super T, Object>... keyRetrievers) {

		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> {

			final List<?> keys = Arrays.stream(keyRetrievers).map(key -> key.apply(t)).collect(Collectors.toList());
			return seen.putIfAbsent(keys, Boolean.TRUE) == null;

		};

	}
	
private HttpEntity<Object> setRequestHeader() throws IOException {
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String token="";
		TokenRequestDTO<SecretKeyRequest> tokenRequestDTO = new TokenRequestDTO<SecretKeyRequest>();
		tokenRequestDTO.setId(environment.getProperty("regproc.token.request.id"));
		tokenRequestDTO.setMetadata(new Metadata());

		tokenRequestDTO.setRequesttime(DateUtils.getUTCCurrentDateTimeString());
		tokenRequestDTO.setRequest(setSecretKeyRequestDTO());
		tokenRequestDTO.setVersion(environment.getProperty("regproc.token.request.version"));

		Gson gson = new Gson();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(environment.getProperty("KEYBASEDTOKENAPI"));
		try {
			StringEntity postingString = new StringEntity(gson.toJson(tokenRequestDTO));
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			HttpResponse response = httpClient.execute(post);
			org.apache.http.HttpEntity entity = response.getEntity();
			String responseBody = EntityUtils.toString(entity, "UTF-8");
			Header[] cookie = response.getHeaders("Set-Cookie");
			if (cookie.length == 0)
				throw new MasterDataServiceException(PacketStatusUpdateErrorCode.PACKET_FETCH_EXCEPTION.getErrorCode(),
						 "Token generation failed");
			token = response.getHeaders("Set-Cookie")[0].getValue();
			
		} catch (IOException e) {
			logger.error("SESSIONID", "ADMIN-SERVICE",
					"ADMIN-SERVICE", e.getMessage() + ExceptionUtils.getStackTrace(e));
			throw e;
			}
		headers.add("Cookie", token.substring(0, token.indexOf(';')));
		return new HttpEntity<Object>(headers);
	}

	private SecretKeyRequest setSecretKeyRequestDTO() {
		SecretKeyRequest request = new SecretKeyRequest();
		request.setAppId(environment.getProperty("regproc.token.request.appid"));
		request.setClientId(environment.getProperty("regproc.token.request.clientId"));
		request.setSecretKey(environment.getProperty("regproc.token.request.secretKey"));
		return request;
	}
	
	private Properties getPropertiesByLangCode(String langCode) {
		Properties prop = null;
		ClassLoader classLoader = getClass().getClassLoader();
		String messagesPropertiesFileName = globalPropertyPrefix + langCode + globalPropertySuffix;
		try(
			InputStream inputStream = classLoader.getResourceAsStream(messagesPropertiesFileName);
			InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");) {
			prop = new Properties();
			prop.load(streamReader);
		} catch (IOException e) {
			throw new MasterDataServiceException("RPR-RTS-002",
					"Unknown Exception Occured" + " -->" + e.getMessage());
//			throw new MasterDataServiceException(PlatformErrorMessages.RPR_RTS_UNKNOWN_EXCEPTION.getCode(),
//					PlatformErrorMessages.RPR_RTS_UNKNOWN_EXCEPTION.getMessage() + " -->" + e.getMessage());
		}
		return prop;
	}
	
	private void setStatusMessage(List<PacketStatusUpdateDto> packStautsDtos, String langCode) {
		Properties prop = getPropertiesByLangCode(langCode);
		System.out.println("Prop : " + prop);
		if (null != prop) {
			packStautsDtos.stream().forEach(packStautsDto -> {
				String subStatusCode = packStautsDto.getSubStatusCode();
				if (subStatusCode != null && !subStatusCode.isEmpty()) {
					packStautsDto.setStatusComment(prop.getProperty(subStatusCode));
				}
			});
		}
	}

}
