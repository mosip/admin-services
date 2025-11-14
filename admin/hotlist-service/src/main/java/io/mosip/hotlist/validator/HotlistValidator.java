package io.mosip.hotlist.validator;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils2;
import io.mosip.kernel.core.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static io.mosip.hotlist.constant.HotlistErrorConstants.*;

/**
 * The Class HotlistValidator.
 *
 * @author Manoj SP
 */
@Component
public class HotlistValidator implements Validator {

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistValidator.class);

	/** The allowed id types. */
	@Value("#{'${mosip.hotlist.allowedIdTypes:}'.split(',')}")
	private List<String> allowedIdTypes;

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
	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors) {
		HotlistRequestResponseDTO request = ((RequestWrapper<HotlistRequestResponseDTO>) target).getRequest();
		validateId(request.getId(), errors);
		validateIdType(request.getIdType(), errors);
		validateStatus(request.getStatus(), errors);
		validateExpiryTimestamp(request.getExpiryTimestamp(), errors);
	}

	/**
	 * Validate id.
	 *
	 * @param index  the index
	 * @param id     the id
	 * @param errors the errors
	 */
	public void validateId(String id, Errors errors) {
		if (Objects.isNull(id)) {
			mosipLogger.debug("Input ID is null");
			errors.reject(MISSING_INPUT_PARAMETER.getErrorCode(),
					String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "id"));
		} else if (StringUtils.isBlank(id)) {
			mosipLogger.debug("Input ID is blank");
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "id"));
		}
	}

	/**
	 * Validate id type.
	 *
	 * @param index  the index
	 * @param idType the id type
	 * @param errors the errors
	 */
	public void validateIdType(String idType, Errors errors) {
		if (Objects.isNull(idType)) {
			mosipLogger.debug("Input IDType is null");
			errors.reject(MISSING_INPUT_PARAMETER.getErrorCode(),
					String.format(MISSING_INPUT_PARAMETER.getErrorMessage(), "idType"));
		} else if (StringUtils.isBlank(idType)) {
			mosipLogger.debug("Input IDType is blank");
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "idType"));
		} else if (!allowedIdTypes.contains(idType)) {
			mosipLogger.debug("Input IDType is not allowed as per configuration");
			errors.reject(ID_TYPE_NOT_ALLOWED.getErrorCode(), ID_TYPE_NOT_ALLOWED.getErrorMessage());
		}
	}

	/**
	 * Validate status.
	 *
	 * @param index  the index
	 * @param status the status
	 * @param errors the errors
	 */
	private void validateStatus(String status, Errors errors) {
		if (Objects.nonNull(status)
				&& (!HotlistStatus.BLOCKED.contentEquals(status) && !HotlistStatus.UNBLOCKED.contentEquals(status))) {
			mosipLogger.debug("Input status is invalid");
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "status"));
		}
	}

	private void validateExpiryTimestamp(LocalDateTime expiryTimestamp, Errors errors) {
		if (Objects.nonNull(expiryTimestamp) && expiryTimestamp.isBefore(DateUtils2.getUTCCurrentDateTime())) {
			mosipLogger.debug("Expiry Timestamp is past dated");
			errors.reject(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "expiryTimestamp"));
		}
	}
}
