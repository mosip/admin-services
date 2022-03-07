package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.dto.GenerateCsrDto;
import io.mosip.kernel.masterdata.dto.UploadCertificateDto;
import io.mosip.kernel.masterdata.dto.getresponse.GenerateCsrResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.UploadCertificateResponseDto;

public interface KeyManagerService {
	
	public ResponseWrapper<GenerateCsrResponseDto> generateCsr( RequestWrapper<GenerateCsrDto> generateCSRDto);

	public ResponseWrapper<GenerateCsrResponseDto> generateCsrCertificate(String applicationId, String referenceId);

	public ResponseWrapper<UploadCertificateResponseDto> uploadCertificate(RequestWrapper< UploadCertificateDto> uploadCertificateDto,boolean otherDomain);

	
	

}
