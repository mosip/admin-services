package io.mosip.admin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.admin.bulkdataupload.entity.ApplicantUserDetailsEntity;
import io.mosip.admin.bulkdataupload.entity.ApplicantUserDetailsRepository;
import io.mosip.admin.constant.ApplicantDetailErrorCode;
import io.mosip.admin.dto.ApplicantDetailsDto;
import io.mosip.admin.dto.ApplicantUserDetailsDto;
import io.mosip.admin.dto.DigitalCardStatusResponseDto;
import io.mosip.admin.packetstatusupdater.constant.ApiName;

import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.service.ApplicantDetailService;
import io.mosip.admin.util.CbeffToBiometricUtil;
import io.mosip.admin.util.Utility;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.kernel.core.idvalidator.exception.InvalidIDException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApplicantDetailServiceImpl implements ApplicantDetailService {

    private static final String FACE = "Face";

    @Autowired
    RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditUtil auditUtil;

    @Autowired
    CbeffToBiometricUtil cbeffToBiometricUtil;


    @Autowired
    Utility utility;

    @Autowired
    private ApplicantUserDetailsRepository applicantUserDetailsRepository;

    private static final String IDENTITY = "identity";

    private static final String RESPONSE = "response";

    private static final String AVAILABLE = "available";

    private static final String DOCUMENTS="documents";

    private static final String ApplicantPhoto = "applicantPhoto";

    private static final String VALUE = "value";
    private static final String DOB = "dob";

    @Value("${mosip.admin.applicant-details.exposed-identity-fields}")
    private String[] applicantDetails;

    @Value("${mosip.admin.applicant-details.max.login.count:15}")
    private int maxcount;


    @Override
    public ApplicantDetailsDto getApplicantDetails(String rid) throws Exception {
        ApplicantDetailsDto applicantDetailsDto =new ApplicantDetailsDto();
        Map<String,String> applicantDataMap=new HashMap<>();
        List<String> pathsegments=new ArrayList<>();
        String individualBiometrics=null;
        pathsegments.add(rid);
        String imageData=null;
        try {
            String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            long count=applicantUserDetailsRepository.countByUserIdAndLoginDate(userId, LocalDate.now());
            if((int)count>=maxcount){
                throw new RequestException(ApplicantDetailErrorCode.LIMIT_EXCEEDED.getErrorCode(),
                        ApplicantDetailErrorCode.LIMIT_EXCEEDED.getErrorMessage());
            }
            String response = restClient.getApi(ApiName.RETRIEVE_IDENTITY_API,pathsegments,"type","bio",String.class);
            JSONObject responseObj= objectMapper.readValue(response,JSONObject.class);
            if(response!=null && responseObj.get("response")==null) {
                throw new RequestException(ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorCode(),
                        ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorMessage());
            }
            JSONObject responseJsonObj=  utility.getJSONObject(responseObj,RESPONSE);
            JSONObject identityObj=utility.getJSONObject(responseJsonObj,IDENTITY);
            JSONArray documents=utility.getJSONArray(responseJsonObj,DOCUMENTS);
            String idenitityJson=utility.getMappingJson();
            JSONObject idenitityJsonObject=objectMapper.readValue(idenitityJson,JSONObject.class);
            JSONObject mapperIdentity=utility.getJSONObject(idenitityJsonObject,IDENTITY);
            List<String> mapperJsonKeys = new ArrayList<>(mapperIdentity.keySet());
            for(String valueObj: applicantDetails){
                if(valueObj!=null && !valueObj.equalsIgnoreCase(ApplicantPhoto)){
                    LinkedHashMap<String, String> jsonObject = utility.getJSONValue(mapperIdentity, valueObj);
                    String value = jsonObject.get(VALUE);
                    applicantDataMap.put(value,identityObj.get(value).toString());
                } else if (valueObj.equalsIgnoreCase(ApplicantPhoto)) {
                    getImageData(documents,applicantDataMap);
                }
            }
            saveApplicantLoginDetails();
            applicantDetailsDto.setApplicantDataMap(applicantDataMap);
        } catch (ResourceAccessException | JSONException e) {
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR,null);
            throw new RequestException(ApplicantDetailErrorCode.UNABLE_TO_RETRIEVE_RID_DETAILS.getErrorCode(),
                    ApplicantDetailErrorCode.UNABLE_TO_RETRIEVE_RID_DETAILS.getErrorMessage(),e);
        }catch (InvalidIDException | DataNotFoundException e){
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR,null);
            throw new RequestException(e.getErrorCode(), e.getErrorText());
        }
        return applicantDetailsDto;
    }

    @Override
    public byte[] getRIDDigitalCard(String rid, boolean isAcknowledged) throws Exception {
        if(!isAcknowledged){
            throw new MasterDataServiceException(
                    ApplicantDetailErrorCode.DIGITAL_CARD_NOT_ACKNOWLEDGED.getErrorCode(),
                    ApplicantDetailErrorCode.DIGITAL_CARD_NOT_ACKNOWLEDGED.getErrorMessage());
        }
        DigitalCardStatusResponseDto digitalCardStatusResponseDto =getDigitialCardStatus(rid);
        if(!digitalCardStatusResponseDto.getStatusCode().equalsIgnoreCase(AVAILABLE)) {
            auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ_EXCEPTION,null);
            throw new MasterDataServiceException(
                    ApplicantDetailErrorCode.DIGITAL_CARD_RID_NOT_FOUND.getErrorCode(),
                    ApplicantDetailErrorCode.DIGITAL_CARD_RID_NOT_FOUND.getErrorMessage());
        }
        return restClient.getApi(digitalCardStatusResponseDto.getUrl(), byte[].class);
    }

    @Override
    public ApplicantUserDetailsDto getApplicantUserDetails() {
        String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        ApplicantUserDetailsDto applicantUserDetailsDto=new ApplicantUserDetailsDto();
        applicantUserDetailsDto.setMaxCount(maxcount);
        long count=applicantUserDetailsRepository.countByUserIdAndLoginDate(userId,LocalDate.now());
        applicantUserDetailsDto.setCount((int)count);
        return applicantUserDetailsDto;
    }

    private DigitalCardStatusResponseDto getDigitialCardStatus(String rid)
            throws Exception {

        List<String> pathsegments=new ArrayList<>();
        pathsegments.add(rid);
        String response = restClient.getApi(ApiName.DIGITAL_CARD_STATUS_URL,pathsegments,"","",String.class);
        JSONObject responseObj= objectMapper.readValue(response,JSONObject.class);
        if(responseObj.containsKey("response") && responseObj.get("response")==null) {
            throw new MasterDataServiceException(ApplicantDetailErrorCode.REQ_ID_NOT_FOUND.getErrorCode(),
                    ApplicantDetailErrorCode.REQ_ID_NOT_FOUND.getErrorMessage());
        }
        JSONObject responseJsonObj=  utility.getJSONObject(responseObj,RESPONSE);
        DigitalCardStatusResponseDto digitalCardStatusResponseDto = objectMapper.readValue(
                responseJsonObj.toJSONString(), DigitalCardStatusResponseDto.class);
        return digitalCardStatusResponseDto;
    }

    private void getImageData(JSONArray documents, Map<String, String> applicantDataMap) throws Exception {
        ConvertRequestDto convertRequestDto = new ConvertRequestDto();
        JSONObject documentObj=utility.getJSONObjectFromArray(documents,0);
        String individualBiometrics = utility.getJSONValue(documentObj, VALUE);
        List<String> subtype = new ArrayList<>();
        byte[] photoByte = cbeffToBiometricUtil.getImageBytes(individualBiometrics, FACE, subtype);
        if (photoByte != null) {
            convertRequestDto.setVersion("ISO19794_5_2011");
            convertRequestDto.setInputBytes(photoByte);
            byte[] data = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
            String encodedBytes = StringUtils.newStringUtf8(Base64.encodeBase64(data, false));
            String imageData = "data:image/png;base64," + encodedBytes;
            applicantDataMap.put(ApplicantPhoto, imageData);
        } else {
            throw new DataNotFoundException(ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorCode(), ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorMessage());
        }
    }
    public void saveApplicantLoginDetails(){
        String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        ApplicantUserDetailsEntity applicantUserDetailsEntity=new ApplicantUserDetailsEntity();
        applicantUserDetailsEntity.setUserId(userId);
        applicantUserDetailsEntity.setLoginDate(LocalDate.now());
        applicantUserDetailsEntity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        applicantUserDetailsEntity.setCreatedDateTime(LocalDateTime.now());
        applicantUserDetailsEntity.setIsActive(true);
        applicantUserDetailsRepository.save(applicantUserDetailsEntity);
    }

}

