package io.mosip.admin.packetstatusupdater.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import io.mosip.admin.packetstatusupdater.constant.ApiName;



/**
 * 
 * @author Dhanendra
 *
 */
@Component
public class RestClient {

	/** The environment. */
	@Autowired
	private Environment environment;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * Post api.
	 *
	 * @param <T>             the generic type
	 * @param apiName         the api name
	 * @param pathsegments    the pathsegments
	 * @param queryParamName  the query param name
	 * @param queryParamValue the query param value
	 * @param mediaType       the media type
	 * @param requestType     the request type
	 * @param responseClass   the response class
	 * @return the t
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T postApi(ApiName apiName, List<String> pathsegments, String queryParamName, String queryParamValue,
			MediaType mediaType, Object requestType, Class<?> responseClass) throws Exception {
		T result = null;
		String apiHostIpPort = environment.getProperty(apiName.name());
		UriComponentsBuilder builder = null;
		if (apiHostIpPort != null)
			builder = UriComponentsBuilder.fromUriString(apiHostIpPort);
		if (builder != null) {

			if (!((pathsegments == null) || (pathsegments.isEmpty()))) {
				for (String segment : pathsegments) {
					if (!((segment == null) || (("").equals(segment)))) {
						builder.pathSegment(segment);
					}
				}

			}
			if (!((queryParamName == null) || (("").equals(queryParamName)))) {
				String[] queryParamNameArr = queryParamName.split(",");
				String[] queryParamValueArr = queryParamValue.split(",");

				for (int i = 0; i < queryParamNameArr.length; i++) {
					builder.queryParam(queryParamNameArr[i], queryParamValueArr[i]);
				}
			}

			try {
				result = (T) restTemplate.postForObject(builder.toUriString(), setRequestHeader(requestType, mediaType),
						responseClass);
			} catch (Exception e) {
				throw new Exception(e);
			}
		}
		return result;
	}

	/**
	 * Gets the api.
	 *
	 * @param <T>             the generic type
	 * @param apiName         the api name
	 * @param pathsegments    the pathsegments
	 * @param queryParamName  the query param name
	 * @param queryParamValue the query param value
	 * @param responseType    the response type
	 * @return the api
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T getApi(ApiName apiName, List<String> pathsegments, String queryParamName, String queryParamValue,
			Class<?> responseType) throws Exception {

		String apiHostIpPort = environment.getProperty(apiName.name());
		T result = null;
		UriComponentsBuilder builder = null;
		UriComponents uriComponents = null;
		if (apiHostIpPort != null) {

			builder = UriComponentsBuilder.fromUriString(apiHostIpPort);
			if (!((pathsegments == null) || (pathsegments.isEmpty()))) {
				for (String segment : pathsegments) {
					if (!((segment == null) || (("").equals(segment)))) {
						builder.pathSegment(segment);
					}
				}

			}

			if (!((queryParamName == null) || (("").equals(queryParamName)))) {

				String[] queryParamNameArr = queryParamName.split(",");
				String[] queryParamValueArr = queryParamValue.split(",");
				for (int i = 0; i < queryParamNameArr.length; i++) {
					builder.queryParam(queryParamNameArr[i], queryParamValueArr[i]);
				}

			}
			uriComponents = builder.build(false).encode();

			try {
				result = (T) restTemplate
						.exchange(uriComponents.toUri(), HttpMethod.GET, setRequestHeader(null, null), responseType)
						.getBody();
			} catch (Exception e) {
				throw new Exception(e);
			}

		}
		return result;
	}

	/**
	 * Gets the rest template.
	 *
	 * @return the rest template
	 * @throws KeyManagementException   the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException        the key store exception
	 */
	public RestTemplate getRestTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new RestTemplate(requestFactory);

	}

	/**
	 * Sets the request header.
	 *
	 * @param requestType the request type
	 * @param mediaType   the media type
	 * @return the http entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	private HttpEntity<Object> setRequestHeader(Object requestType, MediaType mediaType) throws IOException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		//headers.add("Cookie", getToken());
		if (mediaType != null) {
			headers.add("Content-Type", mediaType.toString());
		}
		if (requestType != null) {
			try {
				HttpEntity<Object> httpEntity = (HttpEntity<Object>) requestType;
				HttpHeaders httpHeader = httpEntity.getHeaders();
				Iterator<String> iterator = httpHeader.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					if (!(headers.containsKey("Content-Type") && key == "Content-Type"))
						headers.add(key, httpHeader.get(key).get(0));
				}
				return new HttpEntity<Object>(httpEntity.getBody(), headers);
			} catch (ClassCastException e) {
				return new HttpEntity<Object>(requestType, headers);
			}
		} else
			return new HttpEntity<Object>(headers);
	}

}
