package io.mosip.admin.controller;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.AdminProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/keymanager/")
@Tag(name = "keymanager-proxy-controller", description = "KeyManager Proxy Controller")
public class KeyManagerProxyController {

	@Autowired
	AuditUtil auditUtil;

	@Autowired
	private AdminProxyService service;

	@Value("${mosip.admin.keymanager.service.url}")
	private String url;

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@Operation(summary = "KeyManager proxy", description = "KeyManager proxy", tags = "KeyManager-controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
	})
	public ResponseEntity<?> getKeyManagerProxyController(@RequestBody(required = false) String body, HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.KEYMANAGER_PROXY_API_CALLED, null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(null, request, url));
	}

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@Operation(summary = "KeyManager proxy", description = "KeyManager proxy", tags = "KeyManager-controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
	})
	public ResponseEntity<?> postKeyManagerProxyController(@RequestBody(required = false) String body, HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.MASTERDATA_PROXY_API_CALLED, null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(body, request, url));
	}

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	@Operation(summary = "KeyManager proxy", description = "KeyManager proxy", tags = "KeyManager-controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
	})
	public ResponseEntity<?> deleteKeyManagerProxyController(@RequestBody(required = false) String body, HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.MASTERDATA_PROXY_API_CALLED, null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(null, request, url));
	}

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	@Operation(summary = "KeyManager proxy", description = "KeyManager proxy", tags = "KeyManager-controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
	})
	public ResponseEntity<?> putKeyManagerProxyController(@RequestBody(required = false) String body, HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.MASTERDATA_PROXY_API_CALLED, null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(body, request, url));
	}

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PATCH)
	@Operation(summary = "KeyManager proxy", description = "KeyManager proxy for PATCH requests", tags = "KeyManager-controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
	})
	public ResponseEntity<?> patchKeyManagerProxyController(@RequestBody(required = false) String body, HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.MASTERDATA_PROXY_API_CALLED, null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(body, request, url));
	}

}

