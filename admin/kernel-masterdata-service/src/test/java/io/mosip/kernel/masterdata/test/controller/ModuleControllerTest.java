package io.mosip.kernel.masterdata.test.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.mosip.kernel.masterdata.constant.ModuleErrorCode;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;

/**
 * @author GOVINDARAJ VELU
 * @implSpec ModuleController Test-cases
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModuleControllerTest extends AbstractTest {

	@Test
	@WithUserDetails("global-admin")
	public void getModuleIdandLangCodeNotFound() throws Exception {
		//given
		String id = "10001", langcode = "eng";
		//when
		String uri = "/modules/" + id + "/" + langcode;
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn(), 
				ModuleErrorCode.MODULE_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getModuleLangCodeNotFound() throws Exception {
		//given
		String langcode = "eng";
		//when
		String uri = "/modules/" + langcode;
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn(), 
				ModuleErrorCode.MODULE_NOT_FOUND_EXCEPTION.getErrorCode());
	}
	
}
