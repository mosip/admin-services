package io.mosip.admin.service;

import io.mosip.admin.dto.AccessTokenResponseDTO;

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