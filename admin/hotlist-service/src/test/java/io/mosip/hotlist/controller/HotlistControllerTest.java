package io.mosip.hotlist.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.helper.AuditHelper;
import io.mosip.hotlist.service.HotlistService;
import io.mosip.hotlist.validator.HotlistValidator;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(SpringRunner.class)
@WebMvcTest
public class HotlistControllerTest {

	@InjectMocks
	private HotlistController controller;

	@Mock
	private HotlistValidator validator;

	@Mock
	private HotlistService service;

	@Mock
	private AuditHelper auditHelper;

	@Test
	public void blockIdTest() throws HotlistAppException {
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		when(service.block(Mockito.any())).thenReturn(request);
		RequestWrapper<HotlistRequestResponseDTO> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.block(requestWrapper);
		assertTrue(response.getResponse().getId().contentEquals("id"));
		assertTrue(response.getResponse().getIdType().contentEquals("idType"));
	}

	@Test
	public void blockIdTestException() throws HotlistAppException {
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		when(service.block(Mockito.any()))
				.thenThrow(new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR));
		RequestWrapper<HotlistRequestResponseDTO> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.block(requestWrapper);
		assertTrue(response.getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
		assertTrue(response.getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
	}

	@Test
	public void retrieveBlockedIdTest() throws HotlistAppException, MethodArgumentNotValidException {
		HotlistRequestResponseDTO serviceResponse = new HotlistRequestResponseDTO();
		serviceResponse.setId("id");
		serviceResponse.setIdType("idType");
		when(service.retrieveHotlist(Mockito.any(), Mockito.any())).thenReturn(serviceResponse);
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.retrieveHotlist("id", "idType");
		assertTrue(response.getResponse().getId().contentEquals("id"));
		assertTrue(response.getResponse().getIdType().contentEquals("idType"));
	}

	@Test
	public void retrieveBlockedIdTestException() throws HotlistAppException, MethodArgumentNotValidException {
		HotlistRequestResponseDTO serviceResponse = new HotlistRequestResponseDTO();
		serviceResponse.setId("id");
		serviceResponse.setIdType("idType");
		when(service.retrieveHotlist(Mockito.any(), Mockito.any()))
				.thenThrow(new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR));
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.retrieveHotlist("id", "idType");
		assertTrue(response.getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
		assertTrue(response.getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
	}

	@Test
	public void updateBlockedIdTest() throws HotlistAppException {
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		when(service.unblock(Mockito.any())).thenReturn(request);
		RequestWrapper<HotlistRequestResponseDTO> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.unblock(requestWrapper);
		assertTrue(response.getResponse().getId().contentEquals("id"));
		assertTrue(response.getResponse().getIdType().contentEquals("idType"));
	}

	@Test
	public void updateBlockedIdTestException() throws HotlistAppException {
		HotlistRequestResponseDTO request = new HotlistRequestResponseDTO();
		request.setId("id");
		request.setIdType("idType");
		when(service.unblock(Mockito.any()))
				.thenThrow(new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR));
		RequestWrapper<HotlistRequestResponseDTO> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(request);
		ResponseWrapper<HotlistRequestResponseDTO> response = controller.unblock(requestWrapper);
		assertTrue(response.getErrors().get(0).getErrorCode()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorCode()));
		assertTrue(response.getErrors().get(0).getMessage()
				.contentEquals(HotlistErrorConstants.DATABASE_ACCESS_ERROR.getErrorMessage()));
	}
}
