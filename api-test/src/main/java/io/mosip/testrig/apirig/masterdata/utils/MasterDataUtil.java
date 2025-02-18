package io.mosip.testrig.apirig.masterdata.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.SkipException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.mosip.testrig.apirig.dto.TestCaseDTO;
import io.mosip.testrig.apirig.utils.AdminTestUtil;
import io.mosip.testrig.apirig.utils.GlobalConstants;
import io.mosip.testrig.apirig.utils.SkipTestCaseHandler;

public class MasterDataUtil extends AdminTestUtil {

	private static final Logger logger = Logger.getLogger(MasterDataUtil.class);
	public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	public static String infantDob = LocalDateTime.now().minusYears(getInfantMaxAge()).format(dateFormatter);
	
	public static void setLogLevel() {
		if (MasterDataConfigManager.IsDebugEnabled())
			logger.setLevel(Level.ALL);
		else
			logger.setLevel(Level.ERROR);
	}

	public static String isTestCaseValidForExecution(TestCaseDTO testCaseDTO) {
		String testCaseName = testCaseDTO.getTestCaseName();

		if (SkipTestCaseHandler.isTestCaseInSkippedList(testCaseName)) {
			throw new SkipException(GlobalConstants.KNOWN_ISSUES);
		}
		return testCaseName;
	}

	public String inputJsonKeyWordHandeler(String jsonString, String testCaseName) {

		if (jsonString.contains("$INFANT$")) {
			jsonString = replaceKeywordWithValue(jsonString, "$INFANT$", infantDob);
		}

		return jsonString;
	}

	private static int getInfantMaxAge() {
		HashMap<String, String> map = null;
		String ageGroup = getValueFromRegprocActuator("systemProperties",
				"mosip.regproc.packet.classifier.tagging.agegroup.ranges");

		try {
			map = new Gson().fromJson(ageGroup, new TypeToken<HashMap<String, String>>() {
			}.getType());
		} catch (Exception e) {
			logger.error(
					GlobalConstants.ERROR_STRING_1 + ageGroup + GlobalConstants.EXCEPTION_STRING_1 + e.getMessage());
		}

		String infantAgeGroup = map.get("INFANT").toString();

		String[] parts = infantAgeGroup.split("-");
		return Integer.parseInt(parts[1]);
	}

}