package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.service.GenericService;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericServiceImpl implements GenericService {

    @Value("#{'${mosip.mandatory-languages:}'.concat('${mosip.optional-languages:}')}")
    private String supportedLanguages;

    @Autowired
    private MasterdataSearchHelper masterdataSearchHelper;

    @Override
    public List<String> getMissingData(Class entity, String langCode) {

        if (!supportedLanguages.contains(langCode)) {
            throw new MasterDataServiceException(MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorCode(),
                    MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorMessage());
        }

        return masterdataSearchHelper.fetchMissingValues(entity, langCode);
    }
}
