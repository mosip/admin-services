package io.mosip.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.mosip.admin.dto.GenerateCsrResponseDto;
import io.mosip.admin.dto.UploadCertificateDto;
import io.mosip.admin.dto.UploadCertificateResponseDto;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.KeyManagerService;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.admin.dto.GenerateCsrDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/keymanager/")
@Api(tags = { "KeyManager" })
public class KeyManagerController {

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private KeyManagerService keyManagerService;

	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostgeneratecsr())")
	@PostMapping("generatecsr")
	@ApiOperation(value = "Service to generate csr ", notes = "Generates csr by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When CSR is generated successfully"),
			@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
			@ApiResponse(code = 500, message = "While generating CSR any error occured") })

	public ResponseWrapper<GenerateCsrResponseDto> generateCsr(
			@Valid @RequestBody RequestWrapper<GenerateCsrDto> generateCSRDto) {

		auditUtil.setAuditRequestDto(EventEnum.GENERATE_CSR, null);
		return keyManagerService.generateCsr(generateCSRDto);
	}

	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgeneratecsrcertificateapplicationidreferenceid())")
	@GetMapping("generatecsrcertificate")
	@ApiOperation(value = "Service to generte csr certificate ", notes = "Generates csr certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  csr certificate is obtained successfully"),
			@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
			@ApiResponse(code = 500, message = "While getting certificate error occurred") })

	public ResponseWrapper<GenerateCsrResponseDto> generateCsrCertificate(
			@RequestParam(name = "applicationid", required = true) String applicationid,
			@RequestParam(name = "referenceid", required = false) String referenceid) {
		auditUtil.setAuditRequestDto(EventEnum.GENERATE_CSR_CERTIFICATE, null);
		ResponseWrapper<GenerateCsrResponseDto> a = keyManagerService.generateCsrCertificate(applicationid,
				referenceid);
		return a;
	}

	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostuploadcertificate())")
	@PostMapping("uploadcertificate")
	@ApiOperation(value = "Service to upload certificate ", notes = "Uploads certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  csr certificate is obtained successfully"),
			@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
			@ApiResponse(code = 500, message = "While getting certificate error occurred") })

	public ResponseWrapper<UploadCertificateResponseDto> uploadCertificate(
			@Valid @RequestBody RequestWrapper<UploadCertificateDto> uploadCertificateDto) {

		auditUtil.setAuditRequestDto(EventEnum.UPLOAD_CERTIFICATE, null);
		return keyManagerService.uploadCertificate(uploadCertificateDto, false);
	}

	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostuploadotherdomaincertificate())")
	@PostMapping("uploadotherdomaincertificate")
	@ApiOperation(value = "Service to upload other domain certificate ", notes = "Uploads other domain certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  upoad is successfully"),
			@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
			@ApiResponse(code = 500, message = "While uploading othe domain certificate error occurred") })

	public ResponseWrapper<UploadCertificateResponseDto> uploadOtherDomainCertificate(
			@RequestBody @Valid RequestWrapper<UploadCertificateDto> uploadCertificateDto) {
		
		auditUtil.setAuditRequestDto(EventEnum.UPLOAD_OTHER_DOMAIN_CERTIFICATE, null);
		return keyManagerService.uploadCertificate(uploadCertificateDto, true);
	}

}
