package io.mosip.hotlist.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.service.HotlistService;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;

@RestController
public class HotlistController {

	@Autowired
	private HotlistService hotlistService;

	@PostMapping(path = "/block", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<HotlistRequestResponseDTO>> block(
			@RequestBody RequestWrapper<List<HotlistRequestResponseDTO>> request) {
		ResponseWrapper<List<HotlistRequestResponseDTO>> response = new ResponseWrapper<>();
		response.setResponse(request.getRequest().stream().map(hotlistRequest -> {
			try {
				return hotlistService.block(hotlistRequest);
			} catch (HotlistAppException e) {
				HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
				errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
				return errorResponse;
			}
		}).collect(Collectors.toList()));
		return response;
	}

	@GetMapping(path = "/{idType}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<HotlistRequestResponseDTO> retrieveHotlist(@PathVariable String id,
			@PathVariable String idType) {
		ResponseWrapper<HotlistRequestResponseDTO> response = new ResponseWrapper<>();
		try {
			response.setResponse(hotlistService.retrieveHotlist(id, idType));
		} catch (HotlistAppException e) {
			HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
			errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
			response.setResponse(errorResponse);
		}
		return response;
	}

	@PatchMapping(path = "/updateHotlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<HotlistRequestResponseDTO>> update(
			@RequestBody RequestWrapper<List<HotlistRequestResponseDTO>> request) {
		ResponseWrapper<List<HotlistRequestResponseDTO>> response = new ResponseWrapper<>();
		response.setResponse(request.getRequest().stream().map(t -> {
			try {
				return hotlistService.updateHotlist(t);
			} catch (HotlistAppException e) {
				HotlistRequestResponseDTO errorResponse = new HotlistRequestResponseDTO();
				errorResponse.setError(new ServiceError(e.getErrorCode(), e.getErrorText()));
				return errorResponse;
			}
		}).collect(Collectors.toList()));
		return response;
	}
}
