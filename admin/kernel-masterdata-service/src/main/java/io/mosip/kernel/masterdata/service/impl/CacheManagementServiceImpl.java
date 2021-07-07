package io.mosip.kernel.masterdata.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.service.CacheManagementService;
import io.mosip.kernel.masterdata.utils.CacheName;

@Service
public class CacheManagementServiceImpl implements CacheManagementService {
	
	private static final Logger log = LoggerFactory.getLogger(CacheManagementServiceImpl.class);
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public void clearCacheByCacheName(CacheName cacheName) {

		for (String name : cacheManager.getCacheNames()) {
			if (name.equals(cacheName.getName())) {
				cacheManager.getCache(name).clear();
			}
		}
		log.info("{} got cleared!", cacheName.name);
	}

	@Override
	public void clearCache() {
		for (String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear();
		}
	}

}
