package io.mosip.kernel.masterdata.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.service.CacheManagementService;
import io.mosip.kernel.masterdata.utils.CacheName;

/**
 * @author GOVINDARAJ VELU
 *
 */
@Service
public class CacheManagementServiceImpl implements CacheManagementService {
	
	private static final Logger log = LoggerFactory.getLogger(CacheManagementServiceImpl.class);
	
	@Autowired
	private CacheManager cacheManager;

	/**
	 * clear the cache by cache name
	 */
	@Override
	public void clearCacheByCacheName(CacheName cacheName) {

		for (String name : cacheManager.getCacheNames()) {
			if (name.equals(cacheName.getName()) && null!=cacheManager.getCache(name)) {
				cacheManager.getCache(name).clear();
			}
		}
		log.info("{} got cleared!", cacheName.name);
	}

	/**
	 * clear the all cache
	 */
	@Override
	public void clearCache() {
		for (String name : cacheManager.getCacheNames()) {
			if(null!=cacheManager.getCache(name))
			cacheManager.getCache(name).clear();
		}
	}

}
