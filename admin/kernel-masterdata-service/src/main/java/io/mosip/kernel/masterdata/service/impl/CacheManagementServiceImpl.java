package io.mosip.kernel.masterdata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.service.CacheManagementService;
import io.mosip.kernel.masterdata.utils.CacheName;

@Service
public class CacheManagementServiceImpl implements CacheManagementService {
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public void clearCacheByCacheName(CacheName cacheName) {

		for (String name : cacheManager.getCacheNames()) {
			if (name.equals(cacheName.getName())) {
				cacheManager.getCache(name).clear();
			}
		}
		System.out.println(cacheName.name + " got cleared!");
	}

	@Override
	public void clearCache() {
		for (String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear();
		}
	}

}
