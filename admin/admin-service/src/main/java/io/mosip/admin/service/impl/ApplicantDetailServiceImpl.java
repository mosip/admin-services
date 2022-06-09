package io.mosip.admin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.admin.constant.ApiName;
import io.mosip.admin.constant.ApplicantDetailErrorCode;
import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;
import io.mosip.admin.packetstatusupdater.dto.DigitalCardStatusResponseDto;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.ApplicantDetailService;
import io.mosip.admin.util.CbeffToBiometricUtil;
import io.mosip.admin.util.RestClient;
import io.mosip.admin.util.Utility;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.idvalidator.exception.InvalidIDException;
import io.mosip.kernel.core.idvalidator.spi.RidValidator;
import io.mosip.kernel.core.util.JsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
public class ApplicantDetailServiceImpl implements ApplicantDetailService {

    private static final String FACE = "Face";

    @Autowired
    RestClient restClient;

    @Autowired
    private RidValidator<String> ridValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditUtil auditUtil;

    @Autowired
    CbeffToBiometricUtil cbeffToBiometricUtil;


    @Autowired
    Utility utility;

    private static final String IDENTITY = "identity";

    private static final String RESPONSE = "response";

    private static final String AVAILABLE = "available";

    private static final String DOCUMENTS="documents";

    private static final String ApplicantPhoto = "applicantPhoto";

    private static final String VALUE = "value";
    private static final String DOB = "dob";

    @Value("${mosip.admin.applicant-details.exposed-identity-fields}")
    private String[] applicantDetails;


    @Override
    public ApplicantDetailsDto getApplicantDetails(String rid) throws Exception {
        ApplicantDetailsDto applicantDetailsDto =new ApplicantDetailsDto();
        Map<String,String> applicantDataMap=new HashMap<>();
        List<String> pathsegments=new ArrayList<>();
        String individualBiometrics=null;
        pathsegments.add(rid);
        String imageData=null;
        try {
			if (!ridValidator.validateId(rid)) {
				throw new RequestException(ApplicantDetailErrorCode.RID_INVALID.getErrorCode(),
                        ApplicantDetailErrorCode.RID_INVALID.getErrorMessage());
			}
            String response = restClient.getApi(ApiName.RETRIEVE_IDENTITY_API,pathsegments,"type","bio",String.class);
            JSONObject responseObj= objectMapper.readValue(response,JSONObject.class);
            if(response!=null && !responseObj.containsKey("response")) {
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
            applicantDetailsDto.setApplicantDataMap(applicantDataMap);
        } catch (ResourceAccessException | JSONException e) {
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR);
            throw new RequestException(ApplicantDetailErrorCode.UNABLE_TO_RETRIEVE_RID_DETAILS.getErrorCode(),
                    ApplicantDetailErrorCode.UNABLE_TO_RETRIEVE_RID_DETAILS.getErrorMessage(),e);
        }catch (InvalidIDException | DataNotFoundException e){
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR);
            throw new RequestException(e.getErrorCode(), e.getErrorText());
        }
        return applicantDetailsDto;
    }

    @Override
    public byte[] getRIDDigitalCard(String rid, boolean isAcknowledged) throws Exception {
        String data=null;
        if(!isAcknowledged){
            throw new RequestException(
                    ApplicantDetailErrorCode.DIGITAL_CARD_NOT_ACKNOWLEDGED.getErrorCode(),
                    ApplicantDetailErrorCode.DIGITAL_CARD_NOT_ACKNOWLEDGED.getErrorMessage());
        }
        DigitalCardStatusResponseDto digitalCardStatusResponseDto = getDigitialCardStatus(rid);
        if(!digitalCardStatusResponseDto.getStatusCode().equals(AVAILABLE)) {
            auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ_EXCEPTION);
            throw new RequestException(
                    ApplicantDetailErrorCode.DIGITAL_CARD_RID_NOT_FOUND.getErrorCode(),
                    ApplicantDetailErrorCode.DIGITAL_CARD_RID_NOT_FOUND.getErrorMessage());
        }
        data = restClient.getForObject(digitalCardStatusResponseDto.getUrl(), String.class);
        return data.getBytes();
    }

    private DigitalCardStatusResponseDto getDigitialCardStatus(String rid)
            throws Exception {
        List<String> pathsegments=new ArrayList<>();
        pathsegments.add(rid);
        String response = restClient.getApi(ApiName.DIGITAL_CARD_STATUS_URL,pathsegments,"","",String.class);
        JSONObject responseObj= objectMapper.readValue(response,JSONObject.class);
        if(responseObj.containsKey("response") && responseObj.get("response")==null) {
            throw new RequestException(ApplicantDetailErrorCode.REQ_ID_NOT_FOUND.getErrorCode(),
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

}

