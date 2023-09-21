package io.mosip.kernel.masterdata.dto.getresponse.extn;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DynamicFieldExtnDto {

    private String id;
    private String name;
    private String langCode;
    private String dataType;
    private String description;
    private List<JsonNode> fieldVal;
    private Boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
