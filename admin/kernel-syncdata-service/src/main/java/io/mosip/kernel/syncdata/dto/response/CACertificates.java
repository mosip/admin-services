package io.mosip.kernel.syncdata.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CACertificates {

    private String lastSyncTime;
    private List<CACertificateDTO> certificateDTOList;

}
