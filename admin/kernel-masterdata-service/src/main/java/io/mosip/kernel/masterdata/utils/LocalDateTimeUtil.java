package io.mosip.kernel.masterdata.utils;

import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.RequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Component
public class LocalDateTimeUtil {

    /**
     * It will parse string timestamp to localdatetime. It also validates if the
     * lastupdatedtime is not future date.
     *
     * @param currentTimeStamp - current time stamp
     * @param lastUpdated      - last updated time stamp
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getLocalDateTimeFromTimeStamp(LocalDateTime currentTimeStamp, String lastUpdated) {
        LocalDateTime timeStamp = null;
        if (lastUpdated != null) {
            try {
                timeStamp = MapperUtils.parseToLocalDateTime(lastUpdated);
                if (timeStamp.isAfter(currentTimeStamp)) {
                    throw new DataNotFoundException(MasterdataSearchErrorCode.INVALID_TIMESTAMP_EXCEPTION.getErrorCode(),
                            MasterdataSearchErrorCode.INVALID_TIMESTAMP_EXCEPTION.getErrorMessage());
                }
            } catch (DateTimeParseException e) {
                throw new RequestException(MasterdataSearchErrorCode.LAST_UPDATED_PARSE_EXCEPTION.getErrorCode(),
                        e.getMessage());
            }
        }

        return timeStamp;
    }
}
