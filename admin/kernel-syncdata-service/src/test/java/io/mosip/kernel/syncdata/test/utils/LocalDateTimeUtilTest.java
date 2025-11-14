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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeUtilTest {

	@Test
	public void getLocalDateTimeFailureTest() {
		LocalDateTimeUtil util = new LocalDateTimeUtil();
		assertThrows(DateParsingException.class, () ->
				util.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2019-09-09T09.000")
		);
	}

	@Test
	public void getLocalDateTimeTest() {
		Assert.assertNotNull(LocalDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2019-01-09T09:00:00.000Z"));
	}

	@Ignore
	@Test(expected = DataNotFoundException.class)
	public void getLocalDateTimeExceptionTest() {
		LocalDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), "2015-08-09T09:00:00.000Z");
	}

	@Test()
	public void getLocalDateTimeNullTest() {
		LocalDateTimeUtil.getLocalDateTimeFromTimeStamp(LocalDateTime.now(), null);
	}
}
