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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/masterdata")
@Tag(name = "masterdata-proxy-controller", description = "Proxy Masterdata Controller")
public class MasterdataProxyController {

	@Autowired
	AuditUtil auditUtil;

	@Autowired
	private AdminProxyService service;

	@Value("${mosip.admin.masterdata.service.version}")
	private String version;

	@RequestMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.GET,
			RequestMethod.POST, RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.PUT })
	@Operation(summary  = "Master data proxy", description = "Master data proxy", tags = "proxy-masterdata-controller")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found" ,content = @Content(schema = @Schema(hidden = true)))})
	public ResponseEntity<?> masterDataProxyController(@RequestBody(required = false) String body,
			HttpServletRequest request) {
		auditUtil.setAuditRequestDto(EventEnum.MASTERDATA_PROXY_API_CALLED,null);
		return ResponseEntity.status(HttpStatus.OK).body(service.getResponse(body, request,version));
	}

}
