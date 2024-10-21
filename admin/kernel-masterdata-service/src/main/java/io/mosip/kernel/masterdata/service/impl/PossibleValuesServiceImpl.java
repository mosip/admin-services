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

    private Map<String, List<PossibleValueDto>> getAllValuesOfDefaultField(@NonNull String fieldName, String[] langCodes) {
        Map<String, List<PossibleValueDto>> result = new HashMap<>();

        Integer level = locationHierarchyRepository.findByheirarchyLevalName(fieldName);
        if(level == null)
            return result;

        for(String lang : langCodes) {
            LOGGER.debug("Identified field name as default field", fieldName.replaceAll("[^a-zA-Z0-9_]", "_"));
            List<Location> locations = locationRepository.getAllLocationsByLangCodeWithHierarchyLevel(lang, level.shortValue());
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

    private Map<String, List<PossibleValueDto>> getAllValuesOfDynamicField(@NonNull String fieldName, String[] langCodes) {
        Map<String, List<PossibleValueDto>> result = new HashMap<>();
        for(String lang : langCodes) {
            List<DynamicField> list = dynamicFieldRepository.findAllDynamicFieldValuesByNameAndLangCode(fieldName, lang);

            if(list == null || list.isEmpty())
                continue;

            List<PossibleValueDto> valueDtos = new ArrayList<>();
            LOGGER.debug("Identified field name as dynamic field", fieldName.replaceAll("[^a-zA-Z0-9_]", "_"));
            list.forEach(e -> {
                    PossibleValueDto possibleValueDto = new PossibleValueDto();
                    try {
                        JSONObject jsonObject = new JSONObject(e.getValueJson());
                        if (jsonObject.has(CODE))
                            possibleValueDto.setCode(jsonObject.getString(CODE));
                        if (jsonObject.has(VALUE))
                            possibleValueDto.setValue(jsonObject.getString(VALUE));
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

    @Override
    public Map<String, List<PossibleValueDto>> getAllValuesOfField(@NonNull String fieldName, @NonNull String langCode) {
        String[] langCodes = langCode.split(",");

        if(langCodes.length == 0)
            return null;

        Map<String, List<PossibleValueDto>> result = getAllValuesOfDefaultField(fieldName, langCodes);

        if(!result.isEmpty())
            return result;

        return getAllValuesOfDynamicField(fieldName, langCodes);
    }
}
