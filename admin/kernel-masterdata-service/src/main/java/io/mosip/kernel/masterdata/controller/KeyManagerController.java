package io.mosip.kernel.masterdata.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.GenerateCsrDto;
import io.mosip.kernel.masterdata.dto.UploadCertificateDto;
import io.mosip.kernel.masterdata.dto.getresponse.GenerateCsrResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.UploadCertificateResponseDto;
import io.mosip.kernel.masterdata.service.KeyManagerService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
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

	public ResponseWrapper<GenerateCsrResponseDto> generateCsr( @Valid @RequestBody RequestWrapper<GenerateCsrDto>  generateCSRDto ) {
		

		auditUtil.auditRequest(MasterDataConstant.GENERATE_API_IS_CALLED + KeyManagerController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.GENERATE_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(), "ADM-KMS-001");
	
		return keyManagerService.generateCsr(generateCSRDto);
	}
	
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgeneratecsrcertificateapplicationidreferenceid())")
	@GetMapping("generatecsrcertificate/{applicationid}/{referenceid}")
	@ApiOperation(value = "Service to generte csr certificate ", notes = "Generates csr certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  csr certificate is obtained successfully"),
		@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
		@ApiResponse(code = 500, message = "While getting certificate error occurred") })

	public ResponseWrapper<GenerateCsrResponseDto> generateCsrCertificate(@RequestParam(name="applicationid",required=true) String applicationid,
			@RequestParam(name = "referenceid" ,required = false)String referenceid  ) {
		auditUtil.auditRequest(MasterDataConstant.GENERATE_CERTIFICATE_API_IS_CALLED + KeyManagerController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.GENERATE_CERTIFICATE_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(), "ADM-KMS-002");
	 ResponseWrapper<GenerateCsrResponseDto> a=keyManagerService.generateCsrCertificate(applicationid, referenceid);
	 return a;
	}
	
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostuploadcertificate())")
	@PostMapping("uploadcertificate")
	@ApiOperation(value = "Service to upload certificate ", notes = "Uploads certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  csr certificate is obtained successfully"),
		@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
		@ApiResponse(code = 500, message = "While getting certificate error occurred") })

	public ResponseWrapper<UploadCertificateResponseDto> uploadCertificate(@Valid @RequestBody RequestWrapper<UploadCertificateDto> uploadCertificateDto) {
	
		auditUtil.auditRequest(MasterDataConstant.UPLOAD_CERTIFICATE_API_IS_CALLED + KeyManagerController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPLOAD_CERTIFICATE_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(), "ADM-KMS-003");
	
		return keyManagerService.uploadCertificate(uploadCertificateDto,false);
	}
	
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostuploadotherdomaincertificate())")
	@PostMapping("uploadotherdomaincertificate")
	@ApiOperation(value = "Service to upload other domain certificate ", notes = "Uploads other domain certificate by delegating request to keymanager")
	@ApiResponses({ @ApiResponse(code = 201, message = "When  upoad is successfully"),
		@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
		@ApiResponse(code = 500, message = "While uploading othe domain certificate error occurred") })

	public ResponseWrapper<UploadCertificateResponseDto> uploadOtherDomainCertificate( @RequestBody @Valid RequestWrapper<UploadCertificateDto> uploadCertificateDto) {
		auditUtil.auditRequest(MasterDataConstant.UPLOAD_CERTIFICATE_API_IS_CALLED + KeyManagerController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPLOAD_CERTIFICATE_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(), "ADM-KMS-004");
		
		return keyManagerService.uploadCertificate(uploadCertificateDto,true);
	}

}
