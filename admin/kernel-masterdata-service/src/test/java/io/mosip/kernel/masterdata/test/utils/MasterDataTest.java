package io.mosip.kernel.masterdata.test.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MasterDataTest {

	public static void checkResponse(MvcResult rst, String actualCode) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map m = mapper.readValue(rst.getResponse().getContentAsString(), Map.class);
			System.out.println("test--"+rst.getResponse().getContentAsString());
			assertEquals(rst.getResponse().getStatus(), 200);
			if (m.containsKey("errors") && null != m.get("errors")) {
				assertEquals(((List<Map<String, String>>) m.get("errors")).get(0).get("errorCode"), actualCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
