package io.mosip.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.mosip.admin.dto.RoleExtnDto;
import io.mosip.admin.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Value("${mosip.admin-services.required.roles}")
	private String requiredRoles;

	@Override
	public RoleExtnDto getRequiredRoles() {
		RoleExtnDto roleExtnDto = new RoleExtnDto();
		String[] requiredRole = requiredRoles.split(",");
		List<String> listRequiredRoles = new ArrayList<String>();
		for (String role : requiredRole) {
			listRequiredRoles.add(role);
		}
		roleExtnDto.setRoles(listRequiredRoles);
		return roleExtnDto;
	}
}
