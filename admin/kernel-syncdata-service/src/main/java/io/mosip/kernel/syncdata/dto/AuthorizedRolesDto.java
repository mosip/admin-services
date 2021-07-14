package io.mosip.kernel.syncdata.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin.syncdata")
@Getter
@Setter
public class AuthorizedRolesDto {

	//Sync data controller
	
	private List<String> getconfigs;
	
	private List<String> getglobalconfigs;
	
	private List<String> getregistrationcenterconfigregistrationcenterid;
	
	private List<String> getconfigurationregistrationcenterid;
	
	private List<String> getclientsettings;
	
	private List<String> getclientsettingsregcenterid;
	
	private List<String> getroles;
	
	private List<String> getuserdetailsregcenterid;
	
	private List<String> getusersaltregid;
	
	private List<String> getpublickeyapplicationid;
	
	private List<String> gettpmpublickeyverify;
	
	private List<String> getlatestidschema;
	
	private List<String> getgetcertificate;
	
	private List<String> gettpmpublickeymachineid;
	
	private List<String> getconfigsmachinename;
	
	private List<String> getuserdetails;
	
	private List<String> getgetcacertificates;
	
	
}