package io.mosip.hotlist.validator;

import static io.mosip.hotlist.constant.HotlistErrorConstants.ID_TYPE_NOT_ALLOWED;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_INPUT_PARAMETER;
import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_REQUEST;
import static io.mosip.hotlist.constant.HotlistErrorConstants.MISSING_INPUT_PARAMETER;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.hotlist.constant.HotlistStatus;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.exception.HotlistAppUncheckedException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.StringUtils;

/**
 * The Class HotlistValidator.
 *
 * @author Manoj SP
 */
@Component
public class HotlistValidator implements Validator {
	
	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistValidator.class);

	/** The Constant PATH. */
	private static final String PATH = "request/%s/%s";

	/** The allowed id types. */
	@Value("#{'${mosip.hotlist.allowedIdTypes:}'.split(',')}")
	private List<String> allowedIdTypes;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		allowedIdTypes.remove("");
	}

	/**
	 * Supports.
	 *
	 * @param clazz the clazz
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(RequestWrapper.class);
	}

	/**
	 * Validate.
	 *
	 * @param target the target
	 * @param errors the errors
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void validate(Object target, Errors errors) {
		RequestWrapper requestWrapper = (RequestWrapper) target;
		List<HotlistRequestResponseDTO> request = convertToHotlistRequest(requestWrapper);
		IntStream.range(0, request.size()).forEach(index -> validateRequest(index, request.get(index), errors));
	}

	/**
	 * Validate request.
	 *
	 * @param index the index
	 * @param request the request
	 * @param errors the errors
	 */
	private void validateRequest(int index, HotlistRequestResponseDTO request, Errors errors) {
		validateId(index, request.getId(), errors);
		validateIdType(index, request.getIdType(), errors);
		validateStatus(index, request.getStatus(), errors);
	}

	/**
	 * Validate id.
	 *
	 * @param index the index
	 * @param id the id
	 * @param errors the errors
	 */
	private void validateId(int index, String id, Errors errors) {
		if (Objects.isNull(id)) {
			errors.reject(MISSING_INPUT_PARAMETER.getErrorCode(),
					String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), String.format(PATH, index, "id")));
		} else if (StringUtils.isBlank(id)) {
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), String.format(PATH, index, "id")));
		}
	}

	/**
	 * Validate id type.
	 *
	 * @param index the index
	 * @param idType the id type
	 * @param errors the errors
	 */
	private void validateIdType(int index, String idType, Errors errors) {
		if (Objects.isNull(idType)) {
			errors.reject(MISSING_INPUT_PARAMETER.getErrorCode(),
					String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), String.format(PATH, index, "idType")));
		} else if (StringUtils.isBlank(idType)) {
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), String.format(PATH, index, "idType")));
		} else if (!allowedIdTypes.contains(idType)) {
			errors.reject(ID_TYPE_NOT_ALLOWED.getErrorCode(), ID_TYPE_NOT_ALLOWED.getErrorMessage());
		}
	}

	/**
	 * Validate status.
	 *
	 * @param index the index
	 * @param status the status
	 * @param errors the errors
	 */
	private void validateStatus(int index, String status, Errors errors) {
		if (Objects.nonNull(status) && (StringUtils.isBlank(status)
				|| (!HotlistStatus.BLOCKED.contentEquals(status) && !HotlistStatus.UNBLOCKED.contentEquals(status)))) {
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), String.format(PATH, index, "status")));
		}
	}

	/**
	 * Convert to hotlist request.
	 *
	 * @param requestWrapper the request wrapper
	 * @return the list
	 */
	@SuppressWarnings("rawtypes")
	private List<HotlistRequestResponseDTO> convertToHotlistRequest(RequestWrapper requestWrapper) {
		try {
			return mapper.convertValue(requestWrapper.getRequest(),
					new TypeReference<List<HotlistRequestResponseDTO>>() {
					});
		} catch (Exception e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "updateHotlist", e.getMessage());
			throw new HotlistAppUncheckedException(INVALID_REQUEST, e);
		}
	}

}
