package io.mosip.admin.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.mosip.admin.dto.AccessTokenResponseDTO;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.service.LoginService;

/**
 * The Class PacketUpdateStatusController.
 * 
 * @author Srinivasan
 */
//@RestController
public class LoginController {

	private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private LoginService loginService;

	// @GetMapping(value = "/login/{redirectURI}")
	public void login(@CookieValue("state") String state, @PathVariable("redirectURI") String redirectURI,
			HttpServletResponse res) throws IOException {
		String uri = loginService.getKeycloakURI(redirectURI, state);
		LOGGER.info("redirect open id login uri " + uri);
		res.setStatus(302);
		res.sendRedirect(uri);
	}

	// @GetMapping(value = "/login-redirect/{redirectURI}")
	public void loginRedirect(@PathVariable("redirectURI") String redirectURI, @RequestParam("state") String state,
			@RequestParam("session_state") String sessionState, @RequestParam("code") String code,
			@CookieValue("state") String stateCookie, HttpServletResponse res) throws IOException {
		AccessTokenResponseDTO jwtResponseDTO = loginService.loginRedirect(state, sessionState, code, stateCookie,
				redirectURI);
		String uri = new String(Base64.decodeBase64(redirectURI.getBytes()));
		LOGGER.info("login-redirect open id login uri " + uri);
		Cookie cookie = createCookie(jwtResponseDTO.getAccessToken(), Integer.parseInt(jwtResponseDTO.getExpiresIn()));
		// auditUtil.auditRequest(AuditConstant., eventType, description, eventId);
		res.addCookie(cookie);
		res.setStatus(302);
		res.sendRedirect(uri);
	}

	/**
	 * API to validate token
	 * 
	 * 
	 * @return ResponseEntity with MosipUserDto
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	/*
	 * @ResponseFilter
	 * 
	 * @GetMapping(value = "/authorize/validateToken") public ResponseEntity
	 * validateAdminToken(HttpServletRequest request, HttpServletResponse res) {
	 * 
	 * String body = IOUtils.toString(request.getInputStream(),
	 * Charset.forName(request.getCharacterEncoding())); try {
	 * ResponseEntity<Object> exchange = restTemplate.exchange(firstUrl +
	 * request.getRequestURI(), HttpMethod.valueOf(request.getMethod()), new
	 * HttpEntity<>(body), Object.class, request.getParameterMap()); return
	 * exchange; } catch (final HttpClientErrorException e) { return new
	 * ResponseEntity<>(e.getResponseBodyAsByteArray(), e.getResponseHeaders(),
	 * e.getStatusCode()); } }
	 */

	private Cookie createCookie(final String content, final int expirationTimeSeconds) {
		final Cookie cookie = new Cookie(loginService.getAuthTokenHeader(), content);
		cookie.setMaxAge(expirationTimeSeconds);
		cookie.setHttpOnly(true);
		cookie.setSecure(loginService.getCookieSecurity());
		cookie.setPath("/");
		return cookie;
	}
}
