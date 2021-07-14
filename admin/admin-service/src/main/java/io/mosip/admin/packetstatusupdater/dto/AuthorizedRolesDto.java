package io.mosip.admin.packetstatusupdater.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin.packetstatusupdater")
@Getter
@Setter
public class AuthorizedRolesDto {

	//Audit manager proxy controller
	private List<String> postauditmanagerlog;
	//packet status update controller
	
	private List<String> getpacketstatusupdate;
	
	//Sync Auth Token Controller

	//private List<String> postauthenticateuseridpwd;

	//private List<String> postauthenticatesendotp;

	
}