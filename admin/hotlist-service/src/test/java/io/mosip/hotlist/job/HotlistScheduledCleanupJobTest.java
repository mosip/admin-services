package io.mosip.hotlist.job;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.hotlist.config.job.HotlistScheduledCleanupJob;
import io.mosip.hotlist.repository.HotlistRepository;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(SpringRunner.class)
@WebMvcTest
public class HotlistScheduledCleanupJobTest {

	@InjectMocks
	private HotlistScheduledCleanupJob job;

	@Mock
	private HotlistRepository repo;

	@Test
	public void cleanupUnblockedIdsTest() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenReturn(Collections.emptyList());
		job.cleanupUnblockedIds();
	}

	@Test
	public void cleanupUnblockedIdsTestException() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenThrow(new NullPointerException());
		job.cleanupUnblockedIds();
	}

	@Test
	public void cleanupExpiredIdsTest() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenReturn(Collections.emptyList());
		job.cleanupUnblockedIds();
	}

	@Test
	public void cleanupExpiredIdsTestException() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenThrow(new NullPointerException());
		job.cleanupUnblockedIds();
	}

	@Test
	public void cleanupDeletedIdsTest() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenReturn(Collections.emptyList());
		job.cleanupUnblockedIds();
	}

	@Test
	public void cleanupDeletedIdsException() {
		when(repo.findByStatusAndExpiryTimestampAndIsDeleted(any(), any(), any())).thenThrow(new NullPointerException());
		job.cleanupUnblockedIds();
	}

}
