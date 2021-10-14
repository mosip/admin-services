package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.utils.LanguageUtils;
import org.junit.Assert;
import org.junit.Test;

public class LanguageUtilsTest {

    @Test
    public void testNullProperties() {
        LanguageUtils languageUtils = new LanguageUtils(null, null);
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals("eng", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testEmptyProperties() {
        LanguageUtils languageUtils = new LanguageUtils("", "");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals("eng", languageUtils.getDefaultLanguage());
    }


    @Test
    public void testOptionalNullProperties() {
        LanguageUtils languageUtils = new LanguageUtils("", null);
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals("eng", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testMandatoryNullProperties() {
        LanguageUtils languageUtils = new LanguageUtils(null, "");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals("eng", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testSingleLangProperties() {
        LanguageUtils languageUtils = new LanguageUtils("fra", "");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testSingleLangWithOptionalNullProperties() {
        LanguageUtils languageUtils = new LanguageUtils("fra", null);
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testSingleLangWithInvalidOptionalProperties() {
        LanguageUtils languageUtils = new LanguageUtils("fra", ",   ,");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testOptionalSingleLangProperties() {
        LanguageUtils languageUtils = new LanguageUtils("", "fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testOptionalSingleLangWithMandatoryNullProperties() {
        LanguageUtils languageUtils = new LanguageUtils(null, "fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testOptionalSingleLangWithInvalidMandatoryProperties() {
        LanguageUtils languageUtils = new LanguageUtils(",   ,","fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(1, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("fra", languageUtils.getDefaultLanguage());
    }


    @Test
    public void testMultiLangProperties() {
        LanguageUtils languageUtils = new LanguageUtils("ara,kan","fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(3, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("ara", languageUtils.getDefaultLanguage());
    }


    @Test
    public void testMultiLangOrderCheck() {
        LanguageUtils languageUtils = new LanguageUtils("kan","ara,fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(3, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("kan", languageUtils.getDefaultLanguage());
    }

    @Test
    public void testOnlyOptionalMultiLangOrderCheck() {
        LanguageUtils languageUtils = new LanguageUtils("","kan,fra");
        Assert.assertNotNull(languageUtils.getConfiguredLanguages());
        Assert.assertEquals(2, languageUtils.getConfiguredLanguages().size());
        Assert.assertEquals("kan", languageUtils.getDefaultLanguage());
    }
}
