package io.mosip.admin.util;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AdminDataUtil {

	public static void checkResponse(MvcResult rst, String expectedCode) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (rst.getResponse().getContentAsString().isEmpty() && rst.getResponse().getStatus() == 404) {
				assertEquals(404,rst.getResponse().getStatus());

			} else {
				Map m = mapper.readValue(rst.getResponse().getContentAsString(), Map.class);
				assertEquals(200,rst.getResponse().getStatus());
				assertEquals(expectedCode, getErrorCode(m));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static String getErrorCode(Map map){
		if(map.containsKey("errors") && null != map.get("errors")){
			List<Map<String ,String>> data=(List<Map<String, String>>) map.get("errors");
			return data.isEmpty()?null:data.get(0).get("errorCode");
		}
		return null;
	}
	
	public static void checkErrorResponse(MvcResult rst, String s) {
		assertEquals(rst.getResponse().getStatus(), 500);
	}

}
