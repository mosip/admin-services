package io.mosip.admin.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.admin.constant.ApiName;
import io.mosip.admin.constant.ApplicantDetailErrorCode;
import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;
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

    private static final String DOCUMENTS="documents";

    private static final String ApplicantPhoto = "applicantPhoto";

    private static final String VALUE = "value";
    private static final String DOB = "dob";

    @Value("${mosip.admin.applicantDetails}")
    private String[] applicantDetails;


    @Override
    public ApplicantDetailsDto getApplicantDetails(String rid) throws Exception {
        ApplicantDetailsDto applicantDetailsDto =new ApplicantDetailsDto();
        Map<String,String> applicantDataMap=new HashMap<>();
        ConvertRequestDto convertRequestDto = new ConvertRequestDto();
        List<String> pathsegments=new ArrayList<>();
        String dateOfBirth=null;
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
            if(response!=null && responseObj.containsKey("response")) {
                JSONObject responseJsonObj=  utility.getJSONObject(responseObj,RESPONSE);
                JSONObject identityObj=utility.getJSONObject(responseJsonObj,IDENTITY);
                JSONArray documents=utility.getJSONArray(responseJsonObj,DOCUMENTS);
                String idenitityJson=utility.getMappingJson();
                JSONObject idenitityJsonObject=objectMapper.readValue(idenitityJson,JSONObject.class);
                JSONObject mapperIdentity=utility.getJSONObject(idenitityJsonObject,IDENTITY);
                List<String> mapperJsonKeys = new ArrayList<>(mapperIdentity.keySet());
                for (String key: mapperJsonKeys){
                    for(String valueObj: applicantDetails){
                        if(valueObj.equalsIgnoreCase(key)){
                            LinkedHashMap<String, String> jsonObject = utility.getJSONValue(mapperIdentity, key);
                            String value = jsonObject.get(VALUE);
                            applicantDataMap.put(value,identityObj.get(value).toString());
                        }

                    }
                }
                for (String applicantDetail: applicantDetails) {
                    if (applicantDetail.equalsIgnoreCase(ApplicantPhoto)) {
                        JSONObject documentObj=utility.getJSONObjectFromArray(documents,0);
                        individualBiometrics = utility.getJSONValue(documentObj, VALUE);
                        List<String> subtype = new ArrayList<>();
                        byte[] photoByte = cbeffToBiometricUtil.getImageBytes(individualBiometrics, FACE, subtype);
                        if (photoByte != null) {
                            convertRequestDto.setVersion("ISO19794_5_2011");
                            convertRequestDto.setInputBytes(photoByte);
                            byte[] data = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
                            String encodedBytes = StringUtils.newStringUtf8(Base64.encodeBase64(data, false));
                            imageData = "data:image/png;base64," + encodedBytes;
                            applicantDataMap.put(ApplicantPhoto, imageData);
                        } else {
                            throw new DataNotFoundException(ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorCode(), ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorMessage());
                        }
                    }
                }
                applicantDetailsDto.setApplicantDataMap(applicantDataMap);
            }else{
                throw new RequestException(ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorCode(),
                        ApplicantDetailErrorCode.RID_NOT_FOUND.getErrorMessage());
            }
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

}

