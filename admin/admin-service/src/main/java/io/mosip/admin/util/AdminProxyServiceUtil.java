package io.mosip.admin.util;

import io.mosip.admin.packetstatusupdater.exception.AdminServiceException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URLDecoder;

@Component
public class AdminProxyServiceUtil {

	@Value("${mosip.admin.base.url}")
	private String baseUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	AuditUtil auditUtil;

	private static final Logger logger = LoggerFactory.getLogger(AdminProxyServiceUtil.class);


	@SuppressWarnings("deprecation")
	public URI getUrl(HttpServletRequest request,String version) {

		logger.info("getUrl method of adminProxyServiceUtil");

		String query = request.getQueryString();
		String requestUrl = request.getRequestURI();
		requestUrl = URLDecoder.decode(requestUrl);
		String url = null;
		URI uri = null;
		if (query != null) {
			String decodedQuery = URLDecoder.decode(query);
			url = baseUrl + "/" + version
					+ requestUrl.replace(request.getContextPath() , "").strip().toString();
			uri = UriComponentsBuilder.fromHttpUrl(url).query(decodedQuery).build().toUri();
			logger.info("Requested Url is: {}" , uri);
		} else {
			url = baseUrl + "/" + version
					+ requestUrl.replace(request.getContextPath(), "").strip().toString();
			uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();

			logger.info("Requested Url is: {}", uri);
		}
		return uri;
	}

	public HttpMethod getHttpMethodType(HttpServletRequest request) {

		logger.info("getHttpMethodType method of proxyMasterDataServiceUtil");

		HttpMethod httpMethod = null;

		logger.info(" Request Method Type: {}" + request.getMethod());

		switch (request.getMethod()) {
			case "GET":
				httpMethod = HttpMethod.GET;
				break;

			case "POST":
				httpMethod = HttpMethod.POST;
				break;

			case "DELETE":
				httpMethod = HttpMethod.DELETE;
				break;

			case "PUT":
				httpMethod = HttpMethod.PUT;
				break;
			case "PATCH":
				httpMethod = HttpMethod.PATCH;
				break;
		}
		return httpMethod;

	}

	
	public Object adminRestCall(URI uri, String body, HttpMethod methodType) {

		logger.info("adminRestCall method with request url {}", uri);
      
		ResponseEntity<?> response = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		logger.info("httpEntity : {}" + entity);

		try {

			response = restTemplate.exchange(uri, methodType, entity, String.class);

			logger.info("adminRestCall response for :{}" , uri);

		} catch (Exception e) {
			auditUtil.setAuditRequestDto(EventEnum.ADMIN_PROXY_ERROR,null);
			logger.error("Proxy Admin Call Exception response for url {}, {} ", uri, ExceptionUtils.getStackTrace(e));
			throw new AdminServiceException("ADM-MSD-001", "Failed to call api");

		}

		return response.getBody();

	}

}
