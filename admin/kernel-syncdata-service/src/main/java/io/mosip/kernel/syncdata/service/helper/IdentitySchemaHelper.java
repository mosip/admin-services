package io.mosip.kernel.syncdata.service.helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.IdSchemaDto;
import io.mosip.kernel.syncdata.dto.PageDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

@Component
public class IdentitySchemaHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySchemaHelper.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${mosip.kernel.syncdata-service-idschema-url}")
	private String idSchemaUrl;

	public JsonNode getLatestIdentitySchema(LocalDateTime lastUpdated, double schemaVersion, String domain,
			String type) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(idSchemaUrl);
			builder.queryParam("schemaVersion", schemaVersion);
			if (domain != null) {
				builder.queryParam("domain", domain);
			}
			if (type != null) {
				builder.queryParam("type", type);
			}
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

			objectMapper.registerModule(new JavaTimeModule());
			ResponseWrapper<JsonNode> resp = objectMapper.readValue(responseEntity.getBody(),
					new TypeReference<ResponseWrapper<JsonNode>>() {
					});

			if (resp.getErrors() != null && !resp.getErrors().isEmpty())
				throw new SyncInvalidArgumentException(resp.getErrors());
			return resp.getResponse();
		} catch (Exception e) {
			LOGGER.error("Failed to fetch latest schema", e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCHEMA_FETCH_FAILED.getErrorCode(),
					MasterDataErrorCode.SCHEMA_FETCH_FAILED.getErrorMessage() + " : "
							+ ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}
}
