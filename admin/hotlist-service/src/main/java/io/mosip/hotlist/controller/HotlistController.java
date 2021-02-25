package io.mosip.hotlist.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.hotlist.constant.AuditEvents;
import io.mosip.hotlist.constant.AuditModules;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.helper.AuditHelper;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.hotlist.service.HotlistService;
import io.mosip.hotlist.validator.HotlistValidator;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * The Class HotlistController.
 *
 * @author Manoj SP
 */
@RestController
public class HotlistController {

	private static final String BLOCK = "block";

	private static final String HOTLIST_CONTROLLER = "HotlistController";

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistController.class);

	/** The validator. */
	@Autowired
	private HotlistValidator validator;

	/**
	 * Inits the binder.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validator);
	}

	/** The hotlist service. */
	@Autowired
	private HotlistService hotlistService;

	@Autowired
	private AuditHelper auditHelper;

	/**
	 * Block.
	 *
	 * @param request the request
	 * @return the response wrapper
	 */
	@PostMapping(path = "/block", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<HotlistRequestResponseDTO>> block(
			@Validated @RequestBody RequestWrapper<List<HotlistRequestResponseDTO>> request) {
		ResponseWrapper<List<HotlistRequestResponseDTO>> response = new ResponseWrapper<>();
		response.setResponse(request.getRequest().stream().map(hotlistRequest -> {
			try {
				HotlistRequestResponseDTO blockResponse = hotlistService.block(hotlistRequest);
				auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
						HotlistSecurityManager.hash(hotlistRequest.getId().getBytes()), hotlistRequest.getIdType(),
						"HOTLIST REQUESTED");
				return blockResponse;
			} catch (HotlistAppException e) {
				mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, BLOCK, e.getMessage());
				HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
				errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
				auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
						HotlistSecurityManager.hash(hotlistRequest.getId().getBytes()), hotlistRequest.getIdType(), e);
				return errorResponse;
			}
		}).collect(Collectors.toList()));
		return response;
	}

	/**
	 * Retrieve hotlist.
	 *
	 * @param id     the id
	 * @param idType the id type
	 * @return the response wrapper
	 */
	@GetMapping(path = "/{idType}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<HotlistRequestResponseDTO> retrieveHotlist(@PathVariable String id,
			@PathVariable String idType) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		try {
			response.setResponse(hotlistService.retrieveHotlist(id, idType));
			auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
					HotlistSecurityManager.hash(id.getBytes()), idType, "RETRIEVE HOTLIST REQUESTED");
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, BLOCK, e.getMessage());
			HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
			errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
			response.setResponse(errorResponse);
			auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
					HotlistSecurityManager.hash(id.getBytes()), idType, e);
		}
		return response;
	}

	/**
	 * Update.
	 *
	 * @param request the request
	 * @return the response wrapper
	 */
	@PatchMapping(path = "/updateHotlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<HotlistRequestResponseDTO>> update(
			@RequestBody RequestWrapper<List<HotlistRequestResponseDTO>> request) {
		ResponseWrapper<List<HotlistRequestResponseDTO>> response = new ResponseWrapper<>();
		response.setResponse(request.getRequest().stream().map(hotlistRequest -> {
			try {
				HotlistRequestResponseDTO updateHotlistResponse = hotlistService.updateHotlist(hotlistRequest);
				auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
						HotlistSecurityManager.hash(hotlistRequest.getId().getBytes()), hotlistRequest.getIdType(),
						"UPDATE HOTLIST REQUESTED");
				return updateHotlistResponse;
			} catch (HotlistAppException e) {
				mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, BLOCK, e.getMessage());
				HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
				errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
				auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
						HotlistSecurityManager.hash(hotlistRequest.getId().getBytes()), hotlistRequest.getIdType(), e);
				return errorResponse;
			}
		}).collect(Collectors.toList()));
		return response;
	}
}
