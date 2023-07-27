package io.mosip.admin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin")
@Getter
@Setter
public class AuthorizedRolesDto {

	//Bulk data upload controller
	private List<String> postbulkupload;
	
	private List<String> getbulkuploadtranscationtranscationid;
	
	private List<String> getbulkuploadgetalltransactions;

	//Audit manager proxy controller
	private List<String> postauditmanagerlog;

	//packet status update controller
	private List<String> getpacketstatusupdate;


	//admin lostRid controller

	private List<String> getlostRiddetailsrid;
	private List<String> postlostRid;

	//applicant Details controller

	private List<String> getapplicantDetailsrid;
	private List<String> getapplicantDetailsgetLoginDetails;
	private List<String> getriddigitalcardrid;


	// keymanager controller
	private List<String> getgeneratecsrcertificateapplicationidreferenceid;
	private List<String> postuploadcertificate;
	private List<String> postgeneratecsr;
	private List<String> postuploadotherdomaincertificate;

	
}