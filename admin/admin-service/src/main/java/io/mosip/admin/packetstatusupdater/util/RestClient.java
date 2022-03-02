package io.mosip.admin.packetstatusupdater.util;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
	 * @param mediaType       the media type
	 * @param requestType     the request type
	 * @param responseClass   the response class
	 * @return the t
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T postApi(ApiName apiName, MediaType mediaType, Object requestType, Class<?> responseClass) throws Exception {
		T result = null;
		String apiHostIpPort = environment.getProperty(apiName.name());
		UriComponentsBuilder builder = null;
		if (apiHostIpPort != null)
			builder = UriComponentsBuilder.fromUriString(apiHostIpPort);
		if (builder != null) {
			try {
				result = (T) restTemplate.postForObject(builder.toUriString(), setRequestHeader(requestType, mediaType),
						responseClass);
			} catch (Exception e) {
				throw e;
			}
		}
		return result;
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
	private HttpEntity<Object> setRequestHeader(Object requestType, MediaType mediaType) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String contentType="Content-Type";
		if (mediaType != null) {
			headers.add(contentType, mediaType.toString());
		}
		if (requestType != null) {
			try {
				HttpEntity<Object> httpEntity = (HttpEntity<Object>) requestType;
				HttpHeaders httpHeader = httpEntity.getHeaders();
				Iterator<String> iterator = httpHeader.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					if ( !(headers.containsKey(contentType) && key.equalsIgnoreCase(contentType)) && null!=httpHeader.get(key) && httpHeader.get(key).size()>0)
						headers.add(key, httpHeader.get(key).get(0));
				}
				return new HttpEntity<>(httpEntity.getBody(), headers);
			} catch (ClassCastException e) {
				return new HttpEntity<>(requestType, headers);
			}
		} else
			return new HttpEntity<>(headers);
	}

}
