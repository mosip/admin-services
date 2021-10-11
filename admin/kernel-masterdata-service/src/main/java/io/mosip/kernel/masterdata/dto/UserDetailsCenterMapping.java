package io.mosip.kernel.masterdata.dto;

import lombok.Data;

@Data
public class UserDetailsCenterMapping extends UserDetailsGetExtnDto {

	private String zoneName;
	private String regCenterName;

	public void buildUserDetailsGetExtDto(UserDetailsGetExtnDto ud) {
		this.setId(ud.getId());
		this.setIsActive(ud.getIsActive());
		this.setLangCode(ud.getLangCode());
		this.setName(ud.getName());
		this.setRegCenterId(ud.getRegCenterId());
		this.setStatusCode(ud.getStatusCode());
		this.setZoneCode(ud.getZoneCode());

	}

}
