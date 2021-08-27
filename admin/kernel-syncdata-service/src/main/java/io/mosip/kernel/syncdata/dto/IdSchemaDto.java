package io.mosip.kernel.syncdata.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IdSchemaDto {
	
	private String id;
	private double idVersion;
	private JsonNode schema;
	private String schemaJson;
	private LocalDateTime effectiveFrom;
}
