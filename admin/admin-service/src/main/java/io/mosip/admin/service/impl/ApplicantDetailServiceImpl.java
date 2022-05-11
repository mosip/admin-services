package io.mosip.admin.service.impl;

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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicantDetailServiceImpl implements ApplicantDetailService {

    private static final String FACE = "Face";

    @Autowired
    RestClient restClient;

    @Autowired
    private RidValidator<String> ridValidator;

    @Autowired
    private AuditUtil auditUtil;

    @Autowired
    CbeffToBiometricUtil cbeffToBiometricUtil;

    @Autowired
    Utility utility;

    private static final String IDENTITY = "identity";
    private static final String VALUE = "value";
    private static final String DOB = "dob";



    @Override
    public ApplicantDetailsDto getApplicantDetails(String rid) throws Exception {
        ApplicantDetailsDto applicantDetailsDto =new ApplicantDetailsDto();
        ConvertRequestDto convertRequestDto = new ConvertRequestDto();
        List<String> pathsegments=new ArrayList<>();
        String dateOfBirth=null;
        String individualBiometrics=null;
        pathsegments.add(rid);
        String imageData=null;
        try {
			/*if (!ridValidator.validateId(rid)) {
				throw new RequestException(ApplicantDetailErrorCode.RID_INVALID.getErrorCode(),
                        ApplicantDetailErrorCode.RID_INVALID.getErrorMessage());
			}*/
            JSONObject mappingJson=utility.getMappingJson();
            String response = restClient.getApi(ApiName.RETRIEVE_IDENTITY_API,pathsegments,"type","bio",String.class);
            if(response!=null && !new JSONObject(response).isNull("response")) {
                JSONObject responseJsonObj=new JSONObject(response).getJSONObject("response");
                dateOfBirth = responseJsonObj.getJSONObject("identity").get(mappingJson.getJSONObject(IDENTITY).getJSONObject(DOB).get(VALUE).toString()).toString();
                individualBiometrics = responseJsonObj.getJSONArray("documents").getJSONObject(0).get("value").toString();
                List<String> subtype = new ArrayList<>();
                byte[] photoByte = cbeffToBiometricUtil.getImageBytes(individualBiometrics, FACE, subtype);
                if (photoByte != null) {
                    convertRequestDto.setVersion("ISO19794_5_2011");
                    convertRequestDto.setInputBytes(photoByte);
                    byte[] data= FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
                    String encodedBytes = StringUtils.newStringUtf8(Base64.encodeBase64(data, false));
                    imageData="data:image/png;base64," + encodedBytes;
                    applicantDetailsDto.setApplicantPhoto(imageData);
                }else{
                    throw new DataNotFoundException(ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorCode(),ApplicantDetailErrorCode.DATA_NOT_FOUND.getErrorMessage());
                }
                applicantDetailsDto.setDateOfBirth(dateOfBirth);
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

