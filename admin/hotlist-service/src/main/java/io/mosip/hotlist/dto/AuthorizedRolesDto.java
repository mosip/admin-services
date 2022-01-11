package io.mosip.hotlist.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin.hotlist")
@Data
public class AuthorizedRolesDto {

  //Hotlist controller
	
	private List<String> postHotlistBlock;
	
    private List<String> getHotlistStatus;	
	
	private List<String> postHotlistUnblock; 
	 
}