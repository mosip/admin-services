package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.utils.CacheName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CacheNameTest {

    @Test
    public void testGetName_Success() {
        assertEquals("blocklisted-words", CacheName.BLOCK_LISTED_WORDS.getName());
        assertEquals("document-category", CacheName.DOCUMENT_CATEGORY.getName());
    }

    @Test
    public void testCacheNameByNameValidInput_Success() {
        assertEquals(CacheName.BLOCK_LISTED_WORDS, CacheName.cacheNameByName("blocklisted-words"));
        assertEquals(CacheName.DOCUMENT_CATEGORY, CacheName.cacheNameByName("document-category"));
    }

    @Test
    public void testCacheNameByNameInvalidInputCaseSensitivity_Success() {
        assertNull(CacheName.cacheNameByName("BLOCKLISTED-WORDS"));
    }

    @Test
    public void testCacheNameByNameInvalidInputNonExistentName_Success() {
        assertNull(CacheName.cacheNameByName("invalid-name"));
    }

    @Test
    public void testAllEnumValuesPresent_Success() {
        List<String> expectedValues = Arrays.asList("blocklisted-words", "document-category", "document-type", "dynamic-field", "exceptional-holiday", "gender-type", "id-type",
                "individual-type", "languages", "locations", "location-hierarchy", "templates", "template-type", "titles", "ui-spec", "valid-document", "working-day", "zones");
        List<String> actualValues = Stream.of(CacheName.values()).map(CacheName::getName).toList();
        assertEquals(expectedValues.size(), actualValues.size());
        assertTrue(expectedValues.containsAll(actualValues));
    }

}
