package io.mosip.kernel.masterdata.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.DeviceSpecificationErrorCode;
import io.mosip.kernel.masterdata.dto.DeviceSpecificationDto;
import io.mosip.kernel.masterdata.dto.DeviceSpecificationPutDto;
import io.mosip.kernel.masterdata.entity.DeviceType;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.DeviceTypeRepository;

/**
 * The Class DeviceSpecificationValidator.
 */
@Component
public class DeviceSpecificationValidator {

	/** The device type repository. */
	@Autowired
	DeviceTypeRepository deviceTypeRepository;

	/**
	 * Validate.
	 *
	 * @param request the request
	 */
	public void validate(DeviceSpecificationDto request) {

		List<DeviceType> entities = deviceTypeRepository.findtoUpdateDeviceTypeByCode(request.getDeviceTypeCode());
		if (EmptyCheckUtils.isNullEmpty(entities)) {
			throw new RequestException(DeviceSpecificationErrorCode.INVALID_DEVICE_TYPE_CODE__EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.INVALID_DEVICE_TYPE_CODE__EXCEPTION.getErrorMessage());
		}
	}

	public void validate(DeviceSpecificationPutDto request) {

		List<DeviceType> entities = deviceTypeRepository.findtoUpdateDeviceTypeByCode(request.getDeviceTypeCode());
		if (EmptyCheckUtils.isNullEmpty(entities)) {
			throw new RequestException(DeviceSpecificationErrorCode.INVALID_DEVICE_TYPE_CODE__EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.INVALID_DEVICE_TYPE_CODE__EXCEPTION.getErrorMessage());
		}
	}
}
