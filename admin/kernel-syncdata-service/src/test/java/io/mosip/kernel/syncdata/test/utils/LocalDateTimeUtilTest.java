package io.mosip.kernel.syncdata.test.utils;

import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.DateParsingException;
import io.mosip.kernel.syncdata.utils.LocalDateTimeUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeUtilTest {

	@Mock
	LocalDateTimeUtil localDateTimeUtil;

	@Test(expected = DateParsingException.class)
	public void getLocalDateTimeFailureTest() {
		String errorCode = "DATE_PARSING_ERROR";
		String errorMessage = "Invalid date format";
		Throwable rootCause = new RuntimeException("Parsing error occurred");
		when(localDateTimeUtil.getLocalDateTimeFromTimeStamp(any(LocalDateTime.class), anyString()))
				.thenThrow(new DateParsingException(errorCode, errorMessage, rootCause));
		localDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2019-09-09T09.000");
	}

	@Test
	public void getLocalDateTimeTest() {
		localDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2019-01-09T09:00:00.000Z");
		Assert.assertNotNull(localDateTimeUtil);
	}

	@Ignore
	@Test(expected = DataNotFoundException.class)
	public void getLocalDateTimeExceptionTest() {
		localDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2015-08-09T09:00:00.000Z");
	}

	@Test()
	public void getLocalDateTimeNullTest() {
		localDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), null);
	}
}
