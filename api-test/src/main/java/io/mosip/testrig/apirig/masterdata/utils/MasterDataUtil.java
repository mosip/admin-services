package io.mosip.testrig.apirig.masterdata.utils;

import org.apache.log4j.Logger;
import org.testng.SkipException;

import io.mosip.testrig.apirig.dto.TestCaseDTO;
import io.mosip.testrig.apirig.utils.AdminTestUtil;
import io.mosip.testrig.apirig.utils.GlobalConstants;
import io.mosip.testrig.apirig.utils.SkipTestCaseHandler;

public class MasterDataUtil extends AdminTestUtil {

	private static final Logger logger = Logger.getLogger(MasterDataUtil.class);
	
	public static String isTestCaseValidForExecution(TestCaseDTO testCaseDTO) {
		String testCaseName = testCaseDTO.getTestCaseName();
		
		if (SkipTestCaseHandler.isTestCaseInSkippedList(testCaseName)) {
			throw new SkipException(GlobalConstants.KNOWN_ISSUES);
		}
		return testCaseName;
	}
	
}