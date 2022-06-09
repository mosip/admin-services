package io.mosip.admin.service;

import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;

public interface ApplicantDetailService {

    ApplicantDetailsDto getApplicantDetails(String rid) throws Exception;

    byte[] getRIDDigitalCard(String rid, boolean isAcknowledged) throws Exception;
}
