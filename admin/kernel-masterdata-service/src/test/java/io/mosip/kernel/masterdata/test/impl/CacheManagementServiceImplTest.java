package io.mosip.kernel.masterdata.test.impl;

import io.mosip.kernel.masterdata.service.impl.CacheManagementServiceImpl;
import io.mosip.kernel.masterdata.utils.CacheName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CacheManagementServiceImplTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache1;

    @Mock
    private Cache cache2;

    @InjectMocks
    private CacheManagementServiceImpl cacheManagementService;


    @Test
    public void clearCacheByCacheNameShouldClearMatchingCache() {
        // given
        CacheName cacheName = CacheName.DOCUMENT_CATEGORY;
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(cacheName.getName());
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache(cacheName.getName())).thenReturn(cache1);
        // when
        cacheManagementService.clearCacheByCacheName(cacheName);
        // then
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache(cacheName.getName());
        verify(cache1, times(1)).clear();
        verifyNoMoreInteractions(cache1);
    }

    @Test
    public void clearCacheByCacheNameShouldNotFailWhenCacheIsNull() {
        // given
        CacheName cacheName = CacheName.BLOCK_LISTED_WORDS;
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(cacheName.getName());
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache(cacheName.getName())).thenReturn(null);
        // when
        cacheManagementService.clearCacheByCacheName(cacheName);
        // then
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache(cacheName.getName());
        verifyNoInteractions(cache1, cache2);
    }

    @Test
    public void clearCacheByCacheNameShouldDoNothingWhenNoNameMatches() {
        // given
        CacheName cacheName = CacheName.DYNAMIC_FIELD;
        Set<String> cacheNames = new HashSet<>(Arrays.asList("OTHER_CACHE_1", "OTHER_CACHE_2"));
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache("OTHER_CACHE_1")).thenReturn(cache1);
        when(cacheManager.getCache("OTHER_CACHE_2")).thenReturn(cache2);
        // when
        cacheManagementService.clearCacheByCacheName(cacheName);
        // then
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache("OTHER_CACHE_1");
        verify(cacheManager, times(1)).getCache("OTHER_CACHE_2");
        verify(cache1, never()).clear();
        verify(cache2, never()).clear();
    }

    @Test
    public void clearCacheShouldClearAllNonNullCaches() {
        // given
        Set<String> cacheNames = new HashSet<>(Arrays.asList("cache1", "cache2"));
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache("cache1")).thenReturn(cache1);
        when(cacheManager.getCache("cache2")).thenReturn(cache2);
        // when
        cacheManagementService.clearCache();
        // then
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache("cache1");
        verify(cacheManager, times(1)).getCache("cache2");
        verify(cache1, times(1)).clear();
        verify(cache2, times(1)).clear();
    }

    @Test
    public void clearCacheShouldIgnoreNullCaches() {
        // given
        Set<String> cacheNames = new HashSet<>(Arrays.asList("cache1", "cache2"));
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache("cache1")).thenReturn(cache1);
        when(cacheManager.getCache("cache2")).thenReturn(null);
        // when
        cacheManagementService.clearCache();
        // then
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache("cache1");
        verify(cacheManager, times(1)).getCache("cache2");
        verify(cache1, times(1)).clear();  // seul cache1 non null
        verifyNoMoreInteractions(cache1);
    }

}