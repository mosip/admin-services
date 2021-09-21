package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.dto.DocMissingData;
import io.mosip.kernel.masterdata.dto.MissingDataDto;
import io.mosip.kernel.masterdata.entity.DocumentCategory;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.service.GenericService;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenericServiceImpl implements GenericService {

	@Value("#{'${mosip.mandatory-languages:}'.concat(',').concat('${mosip.optional-languages:}')}")
	private String supportedLanguages;

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Override
	public List<MissingDataDto> getMissingData(Class entity, String langCode, String idFieldName, String fieldName) {
		List<MissingDataDto> list = new ArrayList<>();

		if (!supportedLanguages.contains(langCode)) {
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

	public List<DocMissingData> getMissingsDetails(Class entity, String langCode, String fieldName) {

		List<MissingDataDto> lst = getMissingData(entity, langCode, "name", fieldName);
		List<DocMissingData> doc = new ArrayList<>();
		for (int i = 0; i < lst.size(); i++) {

			DocMissingData d = new DocMissingData(
					null == lst.get(i).getFieldValue() ? lst.get(i).getId() : lst.get(i).getFieldValue(),
					null == lst.get(i).getFieldValue() ? lst.get(i).getFieldValue() : lst.get(i).getId(),
					lst.get(i).getLangCode());
			doc.add(d);

		}

		return doc;

	}
}
