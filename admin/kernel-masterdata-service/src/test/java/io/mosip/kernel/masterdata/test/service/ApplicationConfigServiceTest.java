package io.mosip.kernel.masterdata.test.service;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
import io.mosip.kernel.masterdata.test.TestBootApplication;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApplicationConfigServiceTest {

	@Autowired
	private ApplicationConfigService applicationConfigService;

	@Test
	public void getConfigValuesTest() {
		Map<String,String> response = applicationConfigService.getConfigValues();
		assertNotNull(response);
	}

	@Test
	public void getLanguageConfigDetailsTest() {
		ApplicationConfigResponseDto response = applicationConfigService.getLanguageConfigDetails();
		assertNotNull(response);

	}

}
