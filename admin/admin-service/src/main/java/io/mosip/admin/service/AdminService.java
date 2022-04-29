package io.mosip.admin.service;

import io.mosip.admin.packetstatusupdater.dto.ApplicantVerficationDto;

public interface AdminService {

    ApplicantVerficationDto getApplicantVerficationDetails(String rid) throws Exception;

}
