package io.mosip.kernel.syncdata.service.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class IdentitySchemaHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySchemaHelper.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${mosip.kernel.syncdata-service-idschema-url}")
	private String idSchemaUrl;

	private List<String> responseProperties115 = Arrays.asList("schema", "schemaJson", "id", "idVersion", "effectiveFrom");


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

			ResponseWrapper<ObjectNode> resp = objectMapper.readValue(responseEntity.getBody(),
					new TypeReference<ResponseWrapper<ObjectNode>>() {
					});

			if (resp.getErrors() != null && !resp.getErrors().isEmpty())
				throw new SyncInvalidArgumentException(resp.getErrors());

			// This if-block is added for backward-compatibility for registration-clients with version < 1.2.0.*
			if ("schema".equals(type) && "registration-client".equals(domain)) {
				return resp.getResponse().retain(responseProperties115);
			}

			return resp.getResponse();
		} catch (Exception e) {
			LOGGER.error("Failed to fetch latest schema", e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCHEMA_FETCH_FAILED.getErrorCode(),
					MasterDataErrorCode.SCHEMA_FETCH_FAILED.getErrorMessage() + " : "
							+ ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}
}
