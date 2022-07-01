package io.mosip.admin.packetstatusupdater.dto;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
public class ApplicantUserDetailsDto {

    private int maxCount;

    private int count;
}
