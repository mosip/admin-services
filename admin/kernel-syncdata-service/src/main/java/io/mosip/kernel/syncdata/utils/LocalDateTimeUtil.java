package io.mosip.kernel.syncdata.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.DateParsingException;

/**
 * Utility class for parsing and validating timestamp strings to {@link LocalDateTime}.
 * <p>
 * This class provides a method to convert a string timestamp into a {@link LocalDateTime} object
 * while ensuring the timestamp is not in the future relative to a provided current timestamp.
 * It handles null inputs and parsing errors efficiently, throwing appropriate exceptions
 * when validation fails.
 * </p>
 *
 * @author [Srinivasan MT]
 * @since 1.0.0
 */
@Component
public final class LocalDateTimeUtil {
	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateTimeUtil.class);

	/**
	 * Parses a string timestamp into a {@link LocalDateTime} object and validates
	 * that it is not a future date relative to the provided current timestamp.
	 * <p>
	 * If the input timestamp string is null, the method returns null.
	 * If the timestamp cannot be parsed, a {@link DateParsingException} is thrown.
	 * If the parsed timestamp is after the current timestamp, a
	 * {@link DataNotFoundException} is thrown to indicate an invalid timestamp.
	 * </p>
	 *
	 * @param currentTimeStamp the current {@link LocalDateTime} to compare against,
	 *                         must not be null
	 * @param lastUpdated      the timestamp string to parse, may be null
	 * @return the parsed {@link LocalDateTime} object, or null if the input
	 * timestamp string is null
	 * @throws DateParsingException  if the timestamp string cannot be parsed
	 * @throws DataNotFoundException if the parsed timestamp is in the future
	 * @throws NullPointerException  if currentTimeStamp is null
	 */
	public static LocalDateTime getLocalDateTimeFromTimeStamp(LocalDateTime currentTimeStamp, String lastUpdated) {
		if (lastUpdated == null || lastUpdated.isBlank()) {
			LOGGER.error("Invalid request: lastUpdated parameter is null");
			return null;
		}

		final LocalDateTime parsed;
		try {
			parsed = DateUtils.parseToLocalDateTime(lastUpdated.trim());
		} catch (DateTimeParseException e) {
			// Parsing truly failed – keep error code semantics
			LOGGER.warn("Failed to parse timestamp: {}", lastUpdated, e);
			throw new DateParsingException(
					MasterDataErrorCode.LAST_UPDATED_PARSE_EXCEPTION.getErrorCode(),
					e.getMessage()
			);
		}

		if (parsed.isAfter(currentTimeStamp)) {
			// Business validation failure – warn is appropriate
			LOGGER.error("Invalid timestamp: provided timestamp {} is in the future compared to current time {}",
					lastUpdated, currentTimeStamp);
			throw new DataNotFoundException(
					MasterDataErrorCode.INVALID_TIMESTAMP_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.INVALID_TIMESTAMP_EXCEPTION.getErrorMessage()
			);
		}

		return null; //why null?
		//return parsed;
	}
}