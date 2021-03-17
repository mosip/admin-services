package io.mosip.hotlist.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.hotlist.event.HotlistEventHandler;
import io.mosip.hotlist.repository.HotlistHistoryRepository;
import io.mosip.hotlist.repository.HotlistRepository;
import io.mosip.hotlist.service.impl.HotlistServiceImpl;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(SpringRunner.class)
@WebMvcTest
public class HotlistServiceTest {
	
	@InjectMocks
	private HotlistServiceImpl service;

	@Mock
	private HotlistRepository hotlistRepo;

	/** The hotlist H repo. */
	@Mock
	private HotlistHistoryRepository hotlistHRepo;

	/** The mapper. */
	@Mock
	private ObjectMapper mapper;

	@Mock
	private HotlistEventHandler eventHandler;
	
	@Test
	public void testBlock() {
		
	}
}
