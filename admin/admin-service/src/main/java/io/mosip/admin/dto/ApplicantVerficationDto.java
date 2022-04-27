package io.mosip.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantVerficationDto {

    private String applicantPhoto;

    private String dateOfBirth;
}
