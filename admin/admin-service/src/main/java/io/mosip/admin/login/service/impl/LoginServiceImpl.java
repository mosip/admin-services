package io.mosip.admin.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.mosip.admin.login.constant.OpenIDConstant;
import io.mosip.admin.login.dto.AccessTokenResponse;
import io.mosip.admin.login.dto.AccessTokenResponseDTO;
import io.mosip.admin.login.service.LoginService;

/**
 * 
 * @author Sasikumar Ganesan
 *
 */
//@Service
public class LoginServiceImpl implements LoginService {

	private static Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Value("${mosip.admin-services.audit.manager.api}")
	String auditmanagerapi;

	@Value("${mosip.admin-services.open-id.realmid}")
	String realmID;

	@Value("${mosip.admin-services.open-id.login_flow.name}")
	private String loginFlowName;

	@Value("${mosip.admin-services.open-id.clientid}")
	String clientID;

	@Value("${mosip.admin-services.open-id.clientsecret}")
	private String clientSecret;

	@Value("${mosip.admin-services.redirecturi}")
	private String redirectURI;

	@Value("${mosip.admin-services.open-id.login_flow.scope}")
	private String scope;

	@Value("${mosip.admin-services.open-id.login_flow.response_type}")
	private String responseType;

	@Value("${mosip.admin-services.open-id.authorization_endpoint}")
	private String authorizationEndpoint;

	@Value("${mosip.admin-services.open-id.token_endpoint}")
	private String tokenEndpoint;

	@Value("${mosip.admin-services.open-id.token.header:Authorization}")
	private String tokenHeader;

	@Value("${mosip.admin-services.cookie.security:false}")
	private Boolean cookieSecurity;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ObjectMapper objectmapper;
	
	@Override
	public String getKeycloakURI(String redirectURI, String state) {
		Map<String, String> pathParam = new HashMap<>();
		pathParam.put("realmId", realmID);
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(authorizationEndpoint);
		uriComponentsBuilder.queryParam(OpenIDConstant.CLIENT_ID, clientID);
		uriComponentsBuilder.queryParam(OpenIDConstant.REDIRECT_URI, this.redirectURI + redirectURI);
		uriComponentsBuilder.queryParam(OpenIDConstant.STATE, state);
		uriComponentsBuilder.queryParam(OpenIDConstant.RESPONSE_TYPE, responseType);
		uriComponentsBuilder.queryParam(OpenIDConstant.SCOPE, scope);

		return uriComponentsBuilder.buildAndExpand(pathParam).toString();
	}

	@Override
	public AccessTokenResponseDTO loginRedirect(String state, String sessionState, String code, String stateCookie,
			String redirectURI) {
		// Compare states
		if (!stateCookie.equals(state)) {
			LOGGER.error("given state does not match " + state + " cookie state " + stateCookie);
			throw new RuntimeException("State Mismatch");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add(OpenIDConstant.GRANT_TYPE, loginFlowName);
		map.add(OpenIDConstant.CLIENT_ID, clientID);
		map.add(OpenIDConstant.CLIENT_SECRET, clientSecret);
		map.add(OpenIDConstant.CODE, code);
		map.add(OpenIDConstant.REDIRECT_URI, this.redirectURI + redirectURI);
		Map<String, String> pathParam = new HashMap<>();
		pathParam.put(OpenIDConstant.REALM_ID, realmID);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(tokenEndpoint);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(uriBuilder.buildAndExpand(pathParam).toUriString(), HttpMethod.POST,
					entity, String.class);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			LOGGER.error("Open-id system returned http exception ", e);
			throw e;
		}
		AccessTokenResponse accessTokenResponse = null;
		try {
			accessTokenResponse = objectmapper.readValue(responseEntity.getBody(), AccessTokenResponse.class);
		} catch (IOException exception) {
			LOGGER.error("Access Token IOException for state " + state + " cookie " + stateCookie);
			throw new RuntimeException(exception);
		}
		AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
		accessTokenResponseDTO.setAccessToken(accessTokenResponse.getAccess_token());
		accessTokenResponseDTO.setExpiresIn(accessTokenResponse.getExpires_in());
		return accessTokenResponseDTO;
	}

	@Override
	public String getAuthTokenHeader(){
		return tokenHeader;
	}

	@Override
	public Boolean getCookieSecurity(){
		return cookieSecurity;
	}

}