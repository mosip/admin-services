package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.PossibleValueDto;

import java.util.List;
import java.util.Map;

public interface PossibleValuesService {

    Map<String, List<PossibleValueDto>> getAllValuesOfField(String fieldName, String langCode);
}
