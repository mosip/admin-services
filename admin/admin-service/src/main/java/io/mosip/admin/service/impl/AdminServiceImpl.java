package io.mosip.admin.service.impl;

import java.util.*;

import io.mosip.admin.constant.ApplicantDetailErrorCode;
import io.mosip.admin.dto.*;
import io.mosip.admin.util.Utility;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.constant.LostRidErrorCode;
import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.service.AdminService;
import io.mosip.kernel.core.util.DateUtils2;


@Service
public class AdminServiceImpl implements AdminService {

	@Value("${mosip.registration.processor.lostrid.version:mosip.registration.processor.workflow.search}")
	private String lostRidReqVersion;

	@Value("${mosip.registration.processor.lostrid.id:mosip.registration.lostrid}")
	private String lostRidRequestId;

	@Value("${mosip.admin.lostrid.details.fields:fullName,dateOfBirth}")
	private String[] fields;

	@Value("${mosip.admin.lostrid.details.name.field:fullName}")
	private String nameField;

	@Value("${mosip.admin.lostrid.details.biometric.field:individualBiometrics}")
	private String biometricField;

	private static final String PROCESS = "NEW";

	private static final String SOURCE = "REGISTRATION_CLIENT";

	private static final String RESPONSE = "response";

	private static final String SEGEMENTS = "segments";

	private static final String VALUE = "value";

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private Utility utility;


	@Autowired
	RestClient restClient;

	@Autowired
	ObjectMapper objectMapper;


	@Override
	public LostRidResponseDto lostRid(SearchInfo searchInfo) {
		LostRidResponseDto lostRidResponseDto = new LostRidResponseDto();
		RegProcRequestWrapper<SearchInfo> procRequestWrapper = new RegProcRequestWrapper<>();
		createLostRidRequest(searchInfo);
		procRequestWrapper.setId(lostRidRequestId);
		procRequestWrapper.setVersion(lostRidReqVersion);
		procRequestWrapper.setRequest(searchInfo);
		String dateTime = DateUtils2.formatToISOString(DateUtils2.getUTCCurrentDateTime());
		procRequestWrapper.setRequesttime(dateTime);
		try {
			String response = restClient.postApi(ApiName.LOST_RID_API, MediaType.APPLICATION_JSON,
					procRequestWrapper, String.class);
			lostRidResponseDto = objectMapper.readValue(response, LostRidResponseDto.class);
		} catch (Exception e) {
			throw new RequestException(LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID.getErrorCode(),
					LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID.getErrorMessage()
							+ e);
		}
		return lostRidResponseDto;
	}

	private void createLostRidRequest(SearchInfo searchInfoRequest) {
		for(FilterInfo fi:searchInfoRequest.getFilters()){
			if(fi.getType().equalsIgnoreCase("contains")) {
				fi.setType("equals");
			}
		}
		if (searchInfoRequest.getSort().isEmpty()) {
			List<SortInfo> sortInfos = searchInfoRequest.getSort();
			SortInfo sortInfo = new SortInfo();
			sortInfo.setSortField("registrationDate");
			sortInfo.setSortType("desc");
			sortInfos.add(sortInfo);
			searchInfoRequest.setSort(sortInfos);
		}

	}

	public String getApplicantPhoto(byte[] isodata) throws Exception {
		    ConvertRequestDto convertRequestDto = new ConvertRequestDto();
		    convertRequestDto.setVersion("ISO19794_5_2011");
			convertRequestDto.setInputBytes(isodata);
			byte[] data = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
			String encodedBytes = new String(data);

		return encodedBytes;
	}

	@Override
	public LostRidDetailsDto getLostRidDetails(String rid) {
		LostRidDetailsDto lostRidDetailsDto=new LostRidDetailsDto();
		Map<String,String> lostRidDataMap=new HashMap<>();
		SearchFieldDtos fieldDtos=new SearchFieldDtos();
		RequestWrapper<SearchFieldDtos> fieldDtosRequestWrapper=new RequestWrapper<>();
		ConvertRequestDto convertRequestDto = new ConvertRequestDto();
		try {
			SearchFieldResponseDto fieldResponseDto=new SearchFieldResponseDto();
			buildSearchFieldsRequestDto(fieldDtos,rid);
			fieldDtosRequestWrapper.setRequest(fieldDtos);
			ResponseWrapper<SearchFieldDtos> fieldDtosResponseWrapper = restClient.postApi(ApiName.PACKET_MANAGER_SEARCHFIELDS, MediaType.APPLICATION_JSON,
					fieldDtosRequestWrapper, ResponseWrapper.class);
			fieldResponseDto = objectMapper.readValue(objectMapper.writeValueAsString(fieldDtosResponseWrapper.getResponse()), SearchFieldResponseDto.class);
			for (String field: fields) {
				if (fieldResponseDto.getFields().containsKey(field) && field.equalsIgnoreCase(nameField)) {
					String value = fieldResponseDto.getFields().get(field);
					org.json.JSONArray jsonArray = new org.json.JSONArray(value);
					org.json.JSONObject jsonObject = (org.json.JSONObject) jsonArray.get(0);
					lostRidDataMap.put(field, jsonObject.getString(VALUE));
				} else {
					lostRidDataMap.put(field, fieldResponseDto.getFields().get(field));
				}
			}
			getApplicantPhoto(rid,lostRidDataMap);
			lostRidDetailsDto.setLostRidDataMap(lostRidDataMap);
		} catch (Exception e) {
			logger.error("error is occured while searching fields",e);
			throw new RequestException(LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID_DATA.getErrorCode(),
					LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID_DATA.getErrorMessage()
							, e);
		}
		return lostRidDetailsDto;
	}


	private void getApplicantPhoto(String rid, Map<String, String> lostRidDataMap){
		RequestWrapper<BiometricRequestDto> biometricRequestDtoRequestWrapper=new RequestWrapper<>();
		BiometricRequestDto biometricRequestDto=new BiometricRequestDto();
		ConvertRequestDto convertRequestDto = new ConvertRequestDto();
		try {
			buildBiometricRequestDto(biometricRequestDto,rid);
			biometricRequestDtoRequestWrapper.setRequest(biometricRequestDto);
			String response = restClient.postApi(ApiName.PACKET_MANAGER_BIOMETRIC, MediaType.APPLICATION_JSON,
					biometricRequestDtoRequestWrapper, String.class);
			JSONObject responseObj= objectMapper.readValue(response,JSONObject.class);
			JSONObject responseJsonObj=utility.getJSONObject(responseObj,RESPONSE);
			JSONArray segements=utility.getJSONArray(responseJsonObj,SEGEMENTS);
			JSONObject jsonObject=utility.getJSONObjectFromArray(segements,0);
			convertRequestDto.setVersion("ISO19794_5_2011");
			convertRequestDto.setInputBytes(Base64.decodeBase64((String) jsonObject.get("bdb")));
			byte[] data = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
			String encodedBytes = StringUtils.newStringUtf8(Base64.encodeBase64(data, false));
			String imageData = "data:image/png;base64," + encodedBytes;
			if(response!=null && responseObj.get("response")==null) {
				logger.error("biometric api response is null :  {}",response);
				throw new RequestException(ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorCode(),
						ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorMessage());
			}
			lostRidDataMap.put("applicantPhoto",imageData);
		} catch (Exception e) {
			logger.error("error is occured while getting applicantPhoto",e);
			throw new RequestException(LostRidErrorCode.UNABLE_TO_RETRIEVE_APPLICANT_PHOTO.getErrorCode(),
					LostRidErrorCode.UNABLE_TO_RETRIEVE_APPLICANT_PHOTO.getErrorMessage()
							,e);
		}
	}


	private void buildBiometricRequestDto(BiometricRequestDto biometricRequestDto, String rid) {
		List<String> modalities=new ArrayList<>();
		biometricRequestDto.setSource(SOURCE);
		biometricRequestDto.setId(rid);
		biometricRequestDto.setProcess(PROCESS);
		biometricRequestDto.setPerson(biometricField);
		modalities.add("Face");
		biometricRequestDto.setModalities(modalities);
	}


	private void buildSearchFieldsRequestDto(SearchFieldDtos fieldDtos, String rid) {
		fieldDtos.setSource(SOURCE);
		fieldDtos.setId(rid);
		fieldDtos.setProcess(PROCESS);
		fieldDtos.setFields(Arrays.asList(fields));
		fieldDtos.setBypassCache(false);
	}
}
