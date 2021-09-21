package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.DocMissingData;
import io.mosip.kernel.masterdata.dto.MissingDataDto;

import java.util.List;

public interface GenericService {

    List<MissingDataDto> getMissingData(Class entity, String langCode, String idFieldName, String fieldName);
    
    public List<DocMissingData> getMissingsDetails(Class entity,String langCode, String fieldName);
}
