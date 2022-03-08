package io.mosip.admin.service;

import io.mosip.admin.dto.GenerateCsrDto;
import io.mosip.admin.dto.GenerateCsrResponseDto;
import io.mosip.admin.dto.UploadCertificateDto;
import io.mosip.admin.dto.UploadCertificateResponseDto;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;

public interface KeyManagerService {
	
	public ResponseWrapper<GenerateCsrResponseDto> generateCsr( RequestWrapper<GenerateCsrDto> generateCSRDto);

	public ResponseWrapper<GenerateCsrResponseDto> generateCsrCertificate(String applicationId, String referenceId);

	public ResponseWrapper<UploadCertificateResponseDto> uploadCertificate(RequestWrapper< UploadCertificateDto> uploadCertificateDto,boolean otherDomain);

	
	

}
