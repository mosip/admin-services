package io.mosip.hotlist.controller;

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
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * The Class HotlistController.
 *
 * @author Manoj SP
 */
@RestController
public class HotlistController {

	private static final String BLOCK = "block";

	private static final String UNBLOCK = "unblock";

	private static final String RETRIEVE = "retrieveHotlist";

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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostHotlistBlock())")
	@PostMapping(path = "/block", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<HotlistRequestResponseDTO> block(
			@Validated @RequestBody RequestWrapper<HotlistRequestResponseDTO> request) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		try {
			HotlistRequestResponseDTO blockResponse = hotlistService.block(request.getRequest());
			response.setResponse(blockResponse);
			auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
					HotlistSecurityManager.hash(request.getRequest().getId().getBytes()), request.getRequest().getIdType(),
					"BLOCK HOTLIST REQUESTED");
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, BLOCK, e.getMessage());
			response.setErrors(Collections.singletonList(new ServiceError(e.getErrorCode(), e.getErrorText())));
			auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST,
					HotlistSecurityManager.hash(request.getRequest().getId().getBytes()), request.getRequest().getIdType(), e);
		}
		return response;
	}

	/**
	 * Retrieve hotlist.
	 *
	 * @param id     the id
	 * @param idType the id type
	 * @return the response wrapper
	 * @throws MethodArgumentNotValidException
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetHotlistStatus())")
	@GetMapping(path = "/status/{idType}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<HotlistRequestResponseDTO> retrieveHotlist(
			@ApiParam("Id") @PathVariable("id") String id,
			@ApiParam("Id type") @PathVariable("idType") String idType)
			throws MethodArgumentNotValidException {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new HotlistRequestResponseDTO(), "request");
		validator.validateId(id, errors);
		validator.validateIdType(idType, errors);
		if (errors.hasErrors()) {
			throw new MethodArgumentNotValidException(null, errors);
		}
		try {
			response.setResponse(hotlistService.retrieveHotlist(id, idType));
			auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.RETRIEVE_HOTLIST,
					HotlistSecurityManager.hash(id.getBytes()), idType, "RETRIEVE HOTLIST REQUESTED");
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, RETRIEVE, e.getMessage());
			response.setErrors(Collections.singletonList(new ServiceError(e.getErrorCode(), e.getErrorText())));
			auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.RETRIEVE_HOTLIST,
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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostHotlistUnblock())")
	@PostMapping(path = "/unblock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<HotlistRequestResponseDTO> unblock(
			@Validated @RequestBody RequestWrapper<HotlistRequestResponseDTO> request) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		try {
			HotlistRequestResponseDTO updateHotlistResponse = hotlistService.unblock(request.getRequest());
			response.setResponse(updateHotlistResponse);
			auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.UNBLOCK_HOTLIST,
					HotlistSecurityManager.hash(request.getRequest().getId().getBytes()), request.getRequest().getIdType(),
					"UNBLOCK HOTLIST REQUESTED");
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_CONTROLLER, UNBLOCK, e.getMessage());
			response.setErrors(Collections.singletonList(new ServiceError(e.getErrorCode(), e.getErrorText())));
			auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.UNBLOCK_HOTLIST,
					HotlistSecurityManager.hash(request.getRequest().getId().getBytes()), request.getRequest().getIdType(), e);
		}
		return response;
	}
}
