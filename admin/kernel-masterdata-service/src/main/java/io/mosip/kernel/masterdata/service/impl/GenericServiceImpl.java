package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.dto.MissingDataDto;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.service.GenericService;
import io.mosip.kernel.masterdata.utils.LanguageUtils;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenericServiceImpl implements GenericService {

	private static final Logger logger = LoggerFactory.getLogger(GenericService.class);

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Autowired
	private DynamicFieldRepository dynamicFieldRepository;

	@Autowired
	private LanguageUtils languageUtils;

	@Override
	public List<MissingDataDto> getMissingData(Class entity, String langCode, String idFieldName, String fieldName) {
		List<MissingDataDto> list = new ArrayList<>();

		if (!languageUtils.getConfiguredLanguages().contains(langCode)) {
			throw new MasterDataServiceException(MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorCode(),
					MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorMessage());
		}

		List<Object[]> resultSet = masterdataSearchHelper.fetchMissingValues(entity, langCode, idFieldName, fieldName);
		for (Object[] obj : resultSet) {
			String identifier = obj[0] instanceof String ? (String) obj[0] : String.valueOf(obj[0]);
			if (!list.stream().anyMatch(dto -> dto.getId().equals(identifier))) {
				MissingDataDto dto = new MissingDataDto();
				dto.setId(identifier);
				dto.setLangCode((String) obj[1]);
				if (idFieldName.equalsIgnoreCase(fieldName)) {
					dto.setFieldValue(identifier);
				}
				if (obj.length > 2) {
					dto.setFieldValue((String) obj[2]);
				}
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public List<MissingDataDto> getMissingDynamicData(String langCode, String name) {
		List<MissingDataDto> list = new ArrayList<>();

		if (!languageUtils.getConfiguredLanguages().contains(langCode)) {
			throw new MasterDataServiceException(MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorCode(),
					MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorMessage());
		}

		List<DynamicField> allFields = dynamicFieldRepository.findAllDynamicFieldValuesByName(name);
		if(allFields != null) {
			HashMap<String, String> codes = new HashMap<>();
			allFields.stream()
			.filter( f -> !f.getLangCode().equals(langCode) )
			.forEach(f -> {
				try {
					String value = new JSONObject(f.getValueJson()).getString("code");
					codes.put(value,f.getLangCode());
				} catch (JSONException e) {
					logger.error("Failed to parse field {} value json {}", name, f.getValueJson(), e);
				}
			});

			allFields.stream()
			.filter( f -> f.getLangCode().equals(langCode) )
			.forEach( f-> {
				try {
					codes.remove(new JSONObject(f.getValueJson()).getString("code"));
				} catch (JSONException e) {
					logger.error("Failed to parse field {} value json {}", name, f.getValueJson(), e);
				}
			});

			for (Entry<String, String> c : codes.entrySet()) {
				list.add(new MissingDataDto(c.getKey(), name, c.getValue()));
			}

		}
		return list;
	}
}
