package io.mosip.hotlist.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.entity.Hotlist;
import io.mosip.hotlist.event.HotlistEventHandler;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.repository.HotlistHistoryRepository;
import io.mosip.hotlist.repository.HotlistRepository;
import io.mosip.hotlist.service.impl.HotlistServiceImpl;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;
import io.mosip.kernel.core.util.DateUtils;

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
	public void testBlockIdAlreadyHotlisted() throws HotlistAppException {
		Hotlist entity = new Hotlist();
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(entity));
		HotlistRequestResponseDTO blockRequest = new HotlistRequestResponseDTO();
		blockRequest.setId("id");
		blockRequest.setIdType("idType");
		HotlistRequestResponseDTO response = service.block(blockRequest);
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.BLOCKED));
	}

	@Test
	public void testBlockIdNotHotlisted() throws HotlistAppException {
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.empty());
		HotlistRequestResponseDTO blockRequest = new HotlistRequestResponseDTO();
		blockRequest.setId("id");
		blockRequest.setIdType("idType");
		HotlistRequestResponseDTO response = service.block(blockRequest);
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.BLOCKED));
	}

	@SuppressWarnings("serial")
	@Test
	public void testBlockTransactionFailed() throws HotlistAppException {
		try {
			when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
					.thenThrow(new DataAccessException("") {
					});
			HotlistRequestResponseDTO blockRequest = new HotlistRequestResponseDTO();
			blockRequest.setId("id");
			blockRequest.setIdType("idType");
			service.block(blockRequest);
		} catch (HotlistAppException e) {
			assertTrue(e.getErrorCode().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
			assertTrue(e.getErrorText().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
		}
	}

	@Test
	public void testRetrieveHotlistIdNotExpired() throws HotlistAppException {
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		entity.setIdType("idType");
		entity.setStatus(HotlistStatus.BLOCKED);
		entity.setExpiryTimestamp(LocalDateTime.MAX.withYear(9999));
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(entity));
		HotlistRequestResponseDTO response = service.retrieveHotlist("id", "idType");
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getIdType().contentEquals("idType"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.BLOCKED));
		assertTrue(response.getExpiryTimestamp().equals(LocalDateTime.MAX.withYear(9999)));
	}

	@SuppressWarnings("static-access")
	@Test
	public void testRetrieveHotlistIdExpired() throws HotlistAppException {
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		entity.setIdType("idType");
		entity.setStatus(HotlistStatus.BLOCKED);
		LocalDateTime expiryTimestamp = DateUtils.getUTCCurrentDateTime().now().withYear(2000);
		entity.setExpiryTimestamp(expiryTimestamp);
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(entity));
		HotlistRequestResponseDTO response = service.retrieveHotlist("id", "idType");
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getIdType().contentEquals("idType"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.UNBLOCKED));
		assertTrue(response.getExpiryTimestamp().equals(expiryTimestamp));
	}

	@Test
	public void testRetrieveHotlistIdNotHotlisted() throws HotlistAppException {
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		entity.setIdType("idType");
		entity.setStatus(HotlistStatus.BLOCKED);
		entity.setExpiryTimestamp(LocalDateTime.MAX.withYear(9999));
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.empty());
		HotlistRequestResponseDTO response = service.retrieveHotlist("id", "idType");
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getIdType().contentEquals("idType"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.UNBLOCKED));
		assertTrue(response.getExpiryTimestamp().equals(LocalDateTime.MAX.withYear(9999)));
	}

	@SuppressWarnings("serial")
	@Test
	public void testRetrieveHotlistTransactionFailed() throws HotlistAppException {
		try {
			when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
					.thenThrow(new DataAccessException("") {
					});
			service.retrieveHotlist("id", "idType");
		} catch (HotlistAppException e) {
			assertTrue(e.getErrorCode().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
			assertTrue(e.getErrorText().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
		}
	}

	@Test
	public void testUnblockIdAlreadyHotlisted() throws HotlistAppException {
		Hotlist entity = new Hotlist();
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(entity));
		HotlistRequestResponseDTO unblockRequest = new HotlistRequestResponseDTO();
		unblockRequest.setId("id");
		unblockRequest.setIdType("idType");
		HotlistRequestResponseDTO response = service.unblock(unblockRequest);
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.UNBLOCKED));
	}

	@Test
	public void testUnblockIdNotHotlisted() throws HotlistAppException {
		when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.empty());
		HotlistRequestResponseDTO unblockRequest = new HotlistRequestResponseDTO();
		unblockRequest.setId("id");
		unblockRequest.setIdType("idType");
		HotlistRequestResponseDTO response = service.unblock(unblockRequest);
		assertTrue(response.getId().contentEquals("id"));
		assertTrue(response.getStatus().contentEquals(HotlistStatus.UNBLOCKED));
	}

	@SuppressWarnings("serial")
	@Test
	public void testUnblockTransactionFailed() throws HotlistAppException {
		try {
			when(hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
					.thenThrow(new DataAccessException("") {
					});
			HotlistRequestResponseDTO unblockRequest = new HotlistRequestResponseDTO();
			unblockRequest.setId("id");
			unblockRequest.setIdType("idType");
			service.unblock(unblockRequest);
		} catch (HotlistAppException e) {
			assertTrue(e.getErrorCode().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
			assertTrue(e.getErrorText().contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
		}
	}
}
