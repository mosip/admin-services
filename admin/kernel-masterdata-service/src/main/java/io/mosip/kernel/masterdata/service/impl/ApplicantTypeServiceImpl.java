package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.mosip.kernel.core.applicanttype.exception.InvalidApplicantArgumentException;
import io.mosip.kernel.core.applicanttype.spi.ApplicantType;
import io.mosip.kernel.masterdata.constant.ApplicantTypeErrorCode;
import io.mosip.kernel.masterdata.dto.KeyValues;
import io.mosip.kernel.masterdata.dto.request.RequestDTO;
import io.mosip.kernel.masterdata.dto.response.ApplicantTypeCodeDTO;
import io.mosip.kernel.masterdata.dto.response.ResponseDTO;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.service.ApplicantTypeService;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ApplicantTypeServiceImpl implements ApplicantTypeService {

	@Autowired
	private ApplicantType applicantCodeService;
	@Value("${mosip.kernel.masterdata.individualTypeCode}")
	private String individualTypeCode;

	@Value("${mosip.kernel.masterdata.genderCode}")
	private String genderCode;

	@Value("${mosip.kernel.masterdata.biometricAvailable}")
	private String biometricAvailable;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.mosip.kernel.applicanttype.service.ApplicantTypeService#getApplicantType(
	 * io.mosip.kernel.applicanttype.dto.RequestDTO)
	 */
	@Override
	public ResponseDTO getApplicantType(RequestDTO dto) {
		ResponseDTO response = new ResponseDTO();

		List<KeyValues<String, Object>> list = dto.getAttributes();
		Map<String, Object> map = new HashMap<>();
		for (KeyValues<String, Object> keyValues : list) {
			String attribute = keyValues.getAttribute();
			Object value = keyValues.getValue();

			if ("residenceStatus".equals(attribute)) {
				if (value instanceof String && !individualTypeCode.contains((String) value)) {
					throw new DataNotFoundException(ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorCode(),
							ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorMessage()+attribute);
				}
			} else if ("gender".equals(attribute)) {
				if (value instanceof String && !genderCode.contains((String) value)) {
					throw new DataNotFoundException(ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorCode(),
							ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorMessage()+attribute);
				}
			} else if ("biometricAvailable".equals(attribute)) {
				if (value instanceof String && !biometricAvailable.contains((String) value)) {
					throw new DataNotFoundException(ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorCode(),
							ApplicantTypeErrorCode.INVALID_ATTRIBUTE.getErrorMessage()+attribute);
				}
			}

			map.put(attribute, value);
		}

		ApplicantTypeCodeDTO appDto = new ApplicantTypeCodeDTO();
		try {
			appDto.setApplicantTypeCode(applicantCodeService.getApplicantType(map));
		} catch (InvalidApplicantArgumentException e) {
			throw new RequestException(e.getErrorCode(), e.getErrorText(), e);
		}
		if (appDto.getApplicantTypeCode() == null || appDto.getApplicantTypeCode().trim().length() == 0) {
			throw new DataNotFoundException(ApplicantTypeErrorCode.NO_APPLICANT_FOUND_EXCEPTION.getErrorCode(),
					ApplicantTypeErrorCode.NO_APPLICANT_FOUND_EXCEPTION.getErrorMessage());
		}
		response.setApplicantType(appDto);
		return response;
	}

}
