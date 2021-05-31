package io.mosip.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.admin.dto.RoleExtnDto;
import io.mosip.admin.service.RoleService;
import io.mosip.kernel.core.http.ResponseWrapper;

@RestController
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@GetMapping("/roles")
	private ResponseWrapper<RoleExtnDto> getRequiredRoles() {
		ResponseWrapper<RoleExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(roleService.getRequiredRoles());
		return responseWrapper;
	}
}
