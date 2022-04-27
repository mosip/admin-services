package io.mosip.admin.service;

import io.mosip.admin.dto.ApplicantVerficationDto;
import io.mosip.admin.dto.LostRidResponseDto;
import io.mosip.admin.dto.SearchInfo;

public interface AdminService {


	LostRidResponseDto lostRid(SearchInfo searchInfo);

    ApplicantVerficationDto getApplicantVerficationDetails(String rid) throws Exception;
}
