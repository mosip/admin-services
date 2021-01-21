package io.mosip.kernel.syncdata.websub.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Event {

    private String id;
    private String transactionId;
    private Type type;
    private String timestamp;
    private String dataShareUri;
    private Map<String, Object> data;
}
