package io.mosip.admin.service.impl;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.mosip.admin.constant.AdminErrorCode;
import io.mosip.admin.dto.*;
import io.mosip.admin.packetstatusupdater.constant.PacketStatusUpdateErrorCode;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.util.CbeffToBiometricUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.kernel.biometrics.spi.CbeffUtil;
import io.mosip.kernel.core.idvalidator.exception.InvalidIDException;
import io.mosip.kernel.core.idvalidator.spi.RidValidator;
import io.mosip.kernel.core.util.JsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.constant.LostRidErrorCode;
import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.service.AdminService;
import io.mosip.kernel.core.util.DateUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


@Service
public class AdminServiceImpl implements AdminService {

	@Value("${mosip.registration.processor.lostrid.version:mosip.registration.processor.workflow.search}")
	private String lostRidReqVersion;

	@Value("${mosip.registration.processor.lostrid.id:mosip.registration.lostrid}")
	private String lostRidRequestId;

	private static final String FACE = "Face";

	@Autowired
	RestClient restClient;

	@Autowired
	private RidValidator<String> ridValidator;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private AuditUtil auditUtil;

	@Override
	public LostRidResponseDto lostRid(SearchInfo searchInfo) {
		LostRidResponseDto lostRidResponseDto = new LostRidResponseDto();
		RegProcRequestWrapper<SearchInfo> procRequestWrapper = new RegProcRequestWrapper<>();
		createLostRidRequest(searchInfo);
		procRequestWrapper.setId(lostRidRequestId);
		procRequestWrapper.setVersion(lostRidReqVersion);
		procRequestWrapper.setRequest(searchInfo);
		String dateTime = DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime());
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

	@Override
	public ApplicantVerficationDto getApplicantVerficationDetails(String rid) throws Exception {
		ApplicantVerficationDto applicantVerficationDto=new ApplicantVerficationDto();
		ConvertRequestDto convertRequestDto = new ConvertRequestDto();
		List<String> pathsegments=new ArrayList<>();
		String dateOfBirth=null;
		String individualBiometrics=null;
		pathsegments.add(rid);
		String imageData=null;
		try {
			if (!ridValidator.validateId(rid)) {
				throw new RequestException(AdminErrorCode.RID_INVALID.getErrorCode(),
						AdminErrorCode.RID_INVALID.getErrorMessage());
			}
			String response = restClient.getApi(ApiName.RETRIEVE_IDENTITY_API,pathsegments,"type","bio",String.class);
			if(response!=null && !new JSONObject(response).isNull("response")) {
				dateOfBirth = new JSONObject(response).getJSONObject("response").getJSONObject("identity").get("dateOfBirth").toString();
				individualBiometrics = new JSONObject(response).getJSONObject("response").getJSONArray("documents").getJSONObject(0).get("value").toString();
				CbeffToBiometricUtil util = new CbeffToBiometricUtil();
				List<String> subtype = new ArrayList<>();
				byte[] photoByte = util.getImageBytes(individualBiometrics, FACE, subtype);
				if (photoByte != null) {
					convertRequestDto.setVersion("ISO19794_5_2011");
					convertRequestDto.setInputBytes(photoByte);
					byte[] data=FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
					String encodedBytes = StringUtils.newStringUtf8(Base64.encodeBase64(data, false));
					imageData="data:image/png;base64," + encodedBytes;
				}
				applicantVerficationDto.setDateOfBirth(dateOfBirth);
				applicantVerficationDto.setApplicantPhoto(imageData);
			}else{
				throw new RequestException(AdminErrorCode.RID_NOT_FOUND.getErrorCode(),
						AdminErrorCode.RID_NOT_FOUND.getErrorMessage());
			}
		} catch (ResourceAccessException | JSONException e) {
			auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR,null);
			throw new RequestException(AdminErrorCode.UNABLE_TO_RETRIEVE_RID.getErrorCode(),
					AdminErrorCode.UNABLE_TO_RETRIEVE_RID.getErrorMessage(),e);
		}catch (InvalidIDException | DataNotFoundException e){
			auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR,null);
			throw new RequestException(e.getErrorCode(), e.getErrorText());
		}
		return applicantVerficationDto;
	}
}
