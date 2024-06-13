package io.mosip.hotlist.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import jakarta.validation.constraints.Pattern;
import java.util.Map;

/**
 * Rest Request Object which is provided as input to RestHelper for HTTP/HTTPS Request
 * and response.
 * 
 * @author Manoj SP
 *
 */
@Data
public class RestRequestDTO {

	@Pattern(regexp = "<\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>", message = "{mosip.rest.request.uri.message}")
	@NotNull
	private String uri;

	MultiValueMap<String, String> params;

	Map<String, String> pathVariables;

	@NotNull
	private HttpMethod httpMethod;

	private Object requestBody;

	@NotNull
	private Class<?> responseType;

	@NotNull
	private HttpHeaders headers;

	@Pattern(regexp = "^[0-9]*$", message = "{mosip.rest.request.timeout.message}")
	private Integer timeout;
}