package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.applicanttype.exception.InvalidApplicantArgumentException;
import io.mosip.kernel.core.applicanttype.spi.ApplicantType;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.KeyValues;
import io.mosip.kernel.masterdata.dto.request.RequestDTO;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantTypeControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private ApplicantType applicantCodeService;
	
	
	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;
	
	private ObjectMapper mapper;
	RequestWrapper<RequestDTO> reqDto=new RequestWrapper<>();
	
	@Value("${mosip.kernel.config.server.file.storage.uri}")
	private String url;
	
	@Autowired
	RestTemplate restTemplate;
	MockRestServiceServer mockRestServiceServer;

	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	
		RequestDTO dto=new RequestDTO();
		List<KeyValues<String,Object>> lst=new ArrayList<>();
		KeyValues<String, Object> keyValue1=new KeyValues<>();
		keyValue1.setAttribute("individualTypeCode");
		keyValue1.setValue("FR");
		lst.add(keyValue1);
		
		KeyValues<String, Object> keyValue2=new KeyValues<>();
		keyValue2.setAttribute("biometricAvailable");
		keyValue2.setValue("True");
		lst.add(keyValue2);
		
		KeyValues<String, Object> keyValue3=new KeyValues<>();
		keyValue3.setAttribute("dateofbirth");
		keyValue3.setValue("2018-01-01T10:00:00.00Z");
		lst.add(keyValue3);
		
		KeyValues<String, Object> keyValue4=new KeyValues<>();
		keyValue4.setAttribute("genderCode");
		keyValue4.setValue("MLE");
		lst.add(keyValue4);
		
		dto.setAttributes(lst);
		reqDto.setRequest(dto);
		
		 mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		
		 String res="import java.time.LocalDate; import java.time.LocalDateTime; import java.time.format.DateTimeFormatter; import java.time.Period; import java.util.List; import java.time.ZoneId; import java.time.temporal.ValueRange; import java.util.regex.Matcher; import java.util.regex.Pattern; String CHILD = \"CHL\"; String ADULT = \"ADL\"; String MALE = 'MLE'; String FEMALE = 'FLE'; String NonResident = \"FR\"; String Resident = \"NFR\"; String Others = \"OTH\"; String DATE_PATTERN = \"yyyy/MM/dd\"; String regex = \"^\\\\d{4}(\\\\/)(((0){0,1}[1-9])|((1)[0-2]))(\\\\/)([0-2][0-9]|(3)[0-1])$\"; Pattern pattern = Pattern.compile(regex); def isUpdateFlow(identity) { Object val = identity.getOrDefault('_flow', null); return (val == 'Update') ? true : false; } def getResidenceStatus(identity) { if(identity.containsKey('residenceStatusCode')) { return identity.getOrDefault('residenceStatusCode', null); } if(identity.containsKey('residenceStatus')) { Object val = identity.getOrDefault('residenceStatus', null); return val == null ? null : (val instanceof String ? ; (String)val : (String) ((List)val).get(0).value); } return null; } def getGenderType(identity) { if(identity.containsKey('genderCode')) { return identity.getOrDefault('genderCode', null); } if(identity.containsKey('gender')) { Object val = identity.getOrDefault('gender', null); return val == null ? null : (val instanceof String ? ; (String)val : (String) ((List)val).get(0).value); } return null; } def getAgeCode(identity) { if(ageGroups == null || !identity.containsKey('dateOfBirth')) return null; String dob = identity.get('dateOfBirth'); if(!pattern.matcher(dob).matches()) return null; LocalDate date = LocalDate.parse(dob, DateTimeFormatter.ofPattern(DATE_PATTERN)); LocalDate currentDate = LocalDate.now(ZoneId.of(\"UTC\")); if(date.isAfter(currentDate)) { return 'KER-MSD-151'; } int ageInYears = Period.between(date, currentDate).getYears(); String ageGroup = null; for(String groupName : ageGroups.keySet()) { String[] range = ((String)ageGroups.get(groupName)).split('-'); if(ValueRange.of(Long.valueOf(range[0]), Long.valueOf(range[1])).isValidIntValue(ageInYears)) { ageGroup = groupName; } } return ageGroup == null ? null : (ageGroup == 'INFANT' ? CHILD : ADULT); } def getBioExceptionFlag(identity) { if(!identity.containsKey('isBioException')) { return false; } Object val = identity.getOrDefault('isBioException', null); return (val == 'true') ? true : (( val == 'false' ) ? false : null); } def getApplicantType() { String itc = getResidenceStatus(identity); String genderType = getGenderType(identity); String ageCode = getAgeCode(identity); boolean isBioExPresent = getBioExceptionFlag(identity); if( ageCode == 'KER-MSD-151' ) { return \"KER-MSD-151\"; } if(itc == null || genderType == null || ageCode == null || isBioExPresent == null ) { return isUpdateFlow(identity) ? \"000\" : \"KER-MSD-147\"; } System.out.println(itc + \" - \" + genderType + \" - \" + ageCode + \" - \" + isBioExPresent); if (itc == NonResident && genderType == MALE && ageCode == CHILD && !isBioExPresent) { return \"001\"; } else if (itc == NonResident && genderType == MALE && ageCode == ADULT && !isBioExPresent) { return \"002\"; } else if (itc == Resident && genderType == MALE && ageCode == CHILD && !isBioExPresent) { return \"003\"; } else if (itc == Resident && genderType == MALE && ageCode == ADULT && !isBioExPresent) { return \"004\"; } else if (itc == NonResident && genderType == FEMALE && ageCode == CHILD && !isBioExPresent) { return \"005\"; } else if (itc == NonResident && genderType == FEMALE && ageCode == ADULT && !isBioExPresent) { return \"006\"; } else if (itc == Resident && genderType == FEMALE && ageCode == CHILD && !isBioExPresent) { return \"007\"; } else if (itc == Resident && genderType == FEMALE && ageCode == ADULT && !isBioExPresent) { return \"008\"; } else if (itc == NonResident && genderType == Others && ageCode == CHILD && !isBioExPresent) { return \"005\"; } else if (itc == NonResident && genderType == Others && ageCode == ADULT && !isBioExPresent) { return \"006\"; } else if (itc == Resident && genderType == Others && ageCode == CHILD && !isBioExPresent) { return \"007\"; } else if (itc == Resident && genderType == Others && ageCode == ADULT && !isBioExPresent) { return \"008\"; } else if (itc == NonResident && genderType == MALE && ageCode == CHILD && isBioExPresent) { return \"009\"; } else if (itc == NonResident && genderType == MALE && ageCode == ADULT && isBioExPresent) { return \"010\"; } else if (itc == Resident && genderType == MALE && ageCode == CHILD && isBioExPresent) { return \"011\"; } else if (itc == Resident && genderType == MALE && ageCode == ADULT && isBioExPresent) { return \"012\"; } else if (itc == NonResident && genderType == FEMALE && ageCode == CHILD && isBioExPresent) { return \"013\"; } else if (itc == NonResident && genderType == FEMALE && ageCode == ADULT && isBioExPresent) { return \"014\"; } else if (itc == Resident && genderType == FEMALE && ageCode == CHILD && isBioExPresent) { return \"015\"; } else if (itc == Resident && genderType == FEMALE && ageCode == ADULT && isBioExPresent) { return \"016\"; } else if (itc == NonResident && genderType == Others && ageCode == CHILD && isBioExPresent) { return \"013\"; } else if (itc == NonResident && genderType == Others && ageCode == ADULT && isBioExPresent) { return \"014\"; } else if (itc == Resident && genderType == Others && ageCode == CHILD && isBioExPresent) { return \"015\"; } else if (itc == Resident && genderType == Others && ageCode == ADULT && isBioExPresent) { return \"016\"; } return \"000\"; }";
			mockRestServiceServer.expect(requestTo(url))
			.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
	
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest() throws Exception {
		Mockito.when(applicantCodeService.getApplicantType(Mockito.anyMap())).thenReturn("009");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reqDto))).andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest1() throws Exception {
		Mockito.when(applicantCodeService.getApplicantType(Mockito.anyMap())).thenThrow(InvalidApplicantArgumentException.class);
		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reqDto))).andReturn());

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest3() throws Exception {
		Mockito.when(applicantCodeService.getApplicantType(Mockito.anyMap())).thenReturn(null);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reqDto))).andReturn(),"KER-MSD-147");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest4() throws Exception {
		Mockito.when(applicantCodeService.getApplicantType(Mockito.anyMap())).thenThrow(new InvalidApplicantArgumentException("KER-MSD-149",""));
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reqDto))).andReturn(),"KER-MSD-149");

	}

	
}
