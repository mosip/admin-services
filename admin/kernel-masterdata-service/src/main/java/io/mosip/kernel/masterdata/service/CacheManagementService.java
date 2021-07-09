package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.utils.CacheName;

public interface CacheManagementService {

	void clearCacheByCacheName(CacheName cacheName);
	
	void clearCache();
}
