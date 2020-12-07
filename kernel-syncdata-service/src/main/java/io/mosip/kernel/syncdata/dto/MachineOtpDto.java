package io.mosip.kernel.syncdata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class MachineOtpDto {

    private String userId;
    private List<String> otpChannel;
    private String appId;
    private String useridtype;
    private Map<String, Object> templateVariables;
    private String context;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;
}
