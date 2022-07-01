package io.mosip.admin.service;

import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;
import io.mosip.admin.packetstatusupdater.dto.ApplicantUserDetailsDto;

public interface ApplicantDetailService {

    ApplicantDetailsDto getApplicantDetails(String rid) throws Exception;

    byte[] getRIDDigitalCard(String rid, boolean isAcknowledged) throws Exception;

    ApplicantUserDetailsDto getApplicantUserDetails();
}
