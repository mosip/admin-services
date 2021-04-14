package io.mosip.kernel.masterdata.service;

import java.util.List;

public interface GenericService {

    List<String> getMissingData(Class entity, String langCode);
}
