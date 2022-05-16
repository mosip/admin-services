package io.mosip.admin.packetstatusupdater.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantDetailsDto {

  Map<String , String> applicantDataMap=new HashMap<>();

}
