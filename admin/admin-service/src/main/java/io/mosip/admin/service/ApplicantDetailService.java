package io.mosip.admin.service;

import io.mosip.admin.dto.ApplicantDetailsDto;
import io.mosip.admin.dto.ApplicantUserDetailsDto;

public interface ApplicantDetailService {

    ApplicantDetailsDto getApplicantDetails(String rid) throws Exception;

    byte[] getRIDDigitalCard(String rid, boolean isAcknowledged) throws Exception;

    ApplicantUserDetailsDto getApplicantUserDetails();
}
