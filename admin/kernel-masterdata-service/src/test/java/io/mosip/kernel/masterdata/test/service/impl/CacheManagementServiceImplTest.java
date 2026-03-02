package io.mosip.kernel.masterdata.test.service.impl;

import io.mosip.kernel.masterdata.service.impl.CacheManagementServiceImpl;
import io.mosip.kernel.masterdata.utils.CacheName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CacheManagementServiceImplTest {

    @InjectMocks
    private CacheManagementServiceImpl cacheService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache mockCache1;

    @Mock
    private Cache mockCache2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void clearCacheByCacheName_existingCache_clearsOnlyMatched() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList("blocklisted-words", "other-cache"));
        when(cacheManager.getCache("blocklisted-words")).thenReturn(mockCache1);
        when(cacheManager.getCache("other-cache")).thenReturn(mockCache2);

        cacheService.clearCacheByCacheName(CacheName.BLOCK_LISTED_WORDS);

        verify(mockCache1).clear();
        verify(mockCache2, never()).clear();
    }

    @Test
    void clearCacheByCacheName_cacheNotFound_doesNotThrow() {
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("CACHE2"));
        when(cacheManager.getCache("CACHE2")).thenReturn(mockCache2);

        assertDoesNotThrow(() -> cacheService.clearCacheByCacheName(CacheName.BLOCK_LISTED_WORDS));

        verify(mockCache2, never()).clear();
    }

    @Test
    void clearCacheByCacheName_nullCache_ignored() {
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("CACHE1"));
        when(cacheManager.getCache("CACHE1")).thenReturn(null);

        assertDoesNotThrow(() -> cacheService.clearCacheByCacheName(CacheName.BLOCK_LISTED_WORDS));
    }

    @Test
    void clearCache_clearsAllCaches() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList("CACHE1", "CACHE2"));
        when(cacheManager.getCache("CACHE1")).thenReturn(mockCache1);
        when(cacheManager.getCache("CACHE2")).thenReturn(mockCache2);

        cacheService.clearCache();

        verify(mockCache1).clear();
        verify(mockCache2).clear();
    }

    @Test
    void clearCache_nullCaches_ignored() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList("CACHE1", "CACHE2"));
        when(cacheManager.getCache("CACHE1")).thenReturn(null);
        when(cacheManager.getCache("CACHE2")).thenReturn(mockCache2);

        cacheService.clearCache();

        verify(mockCache2).clear();
    }
}