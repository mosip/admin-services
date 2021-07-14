package io.mosip.admin.bulkdataupload.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin.bulkdataupload")
@Getter
@Setter
public class AuthorizedRolesDto {

	//Bulk data upload controller
	private List<String> postbulkupload;
	
	private List<String> getbulkuploadtranscationtranscationid;
	
	private List<String> getbulkuploadgetalltransactions;

	//Sync Auth Token Controller

	//private List<String> postauthenticateuseridpwd;

	//private List<String> postauthenticatesendotp;

	
}