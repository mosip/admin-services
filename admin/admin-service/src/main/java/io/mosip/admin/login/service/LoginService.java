package io.mosip.admin.login.service;

import io.mosip.admin.login.dto.AccessTokenResponseDTO;

/**
 * The Interface LoginService.
 * 
 * @author Sasikumar Ganesan
 */
public interface LoginService {

	AccessTokenResponseDTO loginRedirect(String state, String sessionState, String code, String stateCookie,
			String redirectURI);

	String getKeycloakURI(String redirectURI, String state);

	String getAuthTokenHeader();

	Boolean getCookieSecurity();

}