package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.masterdata.dto.PossibleValueDto;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.repository.LocationHierarchyRepository;
import io.mosip.kernel.masterdata.repository.LocationRepository;
import io.mosip.kernel.masterdata.service.PossibleValuesService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PossibleValuesServiceImpl implements PossibleValuesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleValuesServiceImpl.class);
    private static final String CODE = "code";
    private static final String VALUE = "value";

    @Autowired
    private DynamicFieldRepository dynamicFieldRepository;

    @Autowired
    private LocationHierarchyRepository locationHierarchyRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Map<String, List<PossibleValueDto>> getAllValuesOfDefaultField(@NonNull String fieldName, @NonNull String langCode) {
        Map<String, List<PossibleValueDto>> result = new HashMap<>();
        String[] langCodes = langCode.split(",");
        for(String lang : langCodes) {
            //assuming only location hierarchy is part of default fields
            Integer level = locationHierarchyRepository.findByheirarchyLevalNameAndLangCode(fieldName, lang);
            if(level == null)
                continue;

            List<Location> locations = locationRepository.getAllLocationsByLangCodeAndLevel(lang, level.shortValue());
            if(locations == null || locations.isEmpty())
                continue;

            List<PossibleValueDto> possibleValueDtos = new ArrayList<>();
            locations.forEach(l -> {
                PossibleValueDto possibleValueDto = new PossibleValueDto();
                possibleValueDto.setCode(l.getCode());
                possibleValueDto.setValue(l.getName());
                possibleValueDto.setDataType("string");
                possibleValueDtos.add(possibleValueDto);
            });
            result.put(lang, possibleValueDtos);
        }
        return result;
    }

    @Override
    public Map<String, List<PossibleValueDto>> getAllValuesOfDynamicField(@NonNull String fieldName, @NonNull String langCode) {
        Map<String, List<PossibleValueDto>> result = new HashMap<>();
        String[] langCodes = langCode.split(",");
        for(String lang : langCodes) {
            List<PossibleValueDto> valueDtos = new ArrayList<>();
            List<DynamicField> list = dynamicFieldRepository.findAllActiveDynamicFieldByNameAndLangCode(fieldName, langCode);

            if(list == null || list.isEmpty())
                continue;

            list.forEach(e -> {
                    PossibleValueDto possibleValueDto = new PossibleValueDto();
                    try {
                        JSONObject jsonObject = new JSONObject(e.getValueJson());
                        if (jsonObject.has(CODE))
                            possibleValueDto.setCode(jsonObject.getString(CODE));
                        if (jsonObject.has(VALUE))
                            possibleValueDto.setCode(jsonObject.getString(VALUE));
                    } catch (JSONException jsonException) {
                        LOGGER.error("Failed to parse valueJson", jsonException);
                    }
                    possibleValueDto.setDataType(e.getDataType());
                    valueDtos.add(possibleValueDto);
            });
            result.put(lang, valueDtos);
        }
        return result;
    }
}
