package io.mosip.kernel.masterdata.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.MachineSpecificationErrorCode;
import io.mosip.kernel.masterdata.dto.MachineSpecificationDto;
import io.mosip.kernel.masterdata.dto.MachineSpecificationPutDto;
import io.mosip.kernel.masterdata.entity.MachineType;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.MachineTypeRepository;

@Component
public class MachineSpecificationValidator {

	/** The device type repository. */
	@Autowired
	MachineTypeRepository machineTypeRepository;

	/**
	 * Validate.
	 *
	 * @param request the request
	 */
	public void validate(MachineSpecificationDto request) {

		List<MachineType> entities = machineTypeRepository.findtoUpdateMachineTypeByCode(request.getMachineTypeCode());
		if (EmptyCheckUtils.isNullEmpty(entities)) {
			throw new RequestException(
					MachineSpecificationErrorCode.INVALID_MACHINE_TYPE_CODE__EXCEPTION.getErrorCode(),
					MachineSpecificationErrorCode.INVALID_MACHINE_TYPE_CODE__EXCEPTION.getErrorMessage());
		}
	}

	/**
	 * Validate.
	 *
	 * @param request the request
	 */
	public void validate(MachineSpecificationPutDto request) {

		List<MachineType> entities = machineTypeRepository.findtoUpdateMachineTypeByCode(request.getMachineTypeCode());
		if (EmptyCheckUtils.isNullEmpty(entities)) {
			throw new RequestException(
					MachineSpecificationErrorCode.INVALID_MACHINE_TYPE_CODE__EXCEPTION.getErrorCode(),
					MachineSpecificationErrorCode.INVALID_MACHINE_TYPE_CODE__EXCEPTION.getErrorMessage());
		}
	}
}
