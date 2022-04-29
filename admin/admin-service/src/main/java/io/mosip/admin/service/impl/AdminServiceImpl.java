package io.mosip.admin.service.impl;

import io.mosip.admin.constant.AdminErrorCode;
import io.mosip.admin.constant.ApiName;
import io.mosip.admin.packetstatusupdater.dto.ApplicantVerficationDto;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.AdminService;
import io.mosip.admin.util.CbeffToBiometricUtil;
import io.mosip.admin.util.RestClient;
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
public class AdminServiceImpl implements AdminService {

    private static final String FACE = "Face";

    @Autowired
    RestClient restClient;

    @Autowired
    private RidValidator<String> ridValidator;

    @Autowired
    private AuditUtil auditUtil;


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
                    byte[] data= FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
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
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR);
            throw new RequestException(AdminErrorCode.UNABLE_TO_RETRIEVE_RID.getErrorCode(),
                    AdminErrorCode.UNABLE_TO_RETRIEVE_RID.getErrorMessage(),e);
        }catch (InvalidIDException | DataNotFoundException e){
            auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_ERROR);
            throw new RequestException(e.getErrorCode(), e.getErrorText());
        }
        return applicantVerficationDto;
    }

}

