package io.mosip.kernel.masterdata.dto;

import lombok.Data;

@Data
public class UserDetailsCenterMapping extends UserDetailsGetExtnDto {

	private String zoneName;
	private String regCenterName;

	public void buildUserDetailsGetExtDto(UserDetailsGetExtnDto ud) {
		//this.setMobile(ud.getMobile());
		this.setId(ud.getId());
		this.setIsActive(ud.getIsActive());
		this.setLangCode(ud.getLangCode());
		//this.setEmail(ud.getEmail());
		this.setName(ud.getName());
		this.setRegCenterId(ud.getRegCenterId());
		this.setStatusCode(ud.getStatusCode());
		//this.setUin(ud.getUin());
		this.setZoneCode(ud.getZoneCode());

	}

}
