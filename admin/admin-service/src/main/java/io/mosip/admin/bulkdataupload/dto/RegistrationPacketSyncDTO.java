package io.mosip.admin.bulkdataupload.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationPacketSyncDTO {

    private String id;
    private String requesttime;
    private String version;
    private List<RIDSyncDto> request;
}
