package io.mosip.admin.bulkdataupload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketUploadStatus {

    private String message;
    private boolean isFailed;
}
