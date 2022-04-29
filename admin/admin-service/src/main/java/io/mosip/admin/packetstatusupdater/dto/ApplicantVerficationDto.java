package io.mosip.admin.packetstatusupdater.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantVerficationDto {

    private String applicantPhoto;

    private String dateOfBirth;
}
