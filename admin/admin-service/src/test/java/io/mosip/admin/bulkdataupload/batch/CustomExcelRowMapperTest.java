package io.mosip.admin.bulkdataupload.batch;

import java.util.HashSet;
import java.util.Properties;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.batch.core.jsr.configuration.xml.JsrXmlApplicationContext;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomExcelRowMapperTest {

    @Autowired
    private CustomExcelRowMapper customExcelRowMapper;


    @Test(expected = IllegalStateException.class)
    public void testMapRow01() throws BindException {

        ApplicationConversionService conversionService = new ApplicationConversionService();

        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        customExcelRowMapper.setTargetType(Object.class);
        RowSet rowSet = mock(RowSet.class);
        when(rowSet.getProperties()).thenReturn(new Properties());
        customExcelRowMapper.mapRow(rowSet);
    }

    @Test(expected = NotWritablePropertyException.class)
    public void testMapRow02() throws BindException {

        ApplicationConversionService conversionService = new ApplicationConversionService();

        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        customExcelRowMapper.setTargetType(Object.class);

        Properties properties = new Properties();
        properties.setProperty("key", "value");
        RowSet rowSet = mock(RowSet.class);
        when(rowSet.getProperties()).thenReturn(properties);
        customExcelRowMapper.mapRow(rowSet);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testMapRow03() throws BindException {
        ApplicationConversionService conversionService = new ApplicationConversionService();

        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        customExcelRowMapper.setTargetType(Object.class);
        RowSet rowSet = mock(RowSet.class);
        when(rowSet.getProperties()).thenThrow(new ConstraintViolationException(new HashSet<>()));
        customExcelRowMapper.mapRow(rowSet);
        verify(rowSet).getProperties();
    }


    @Test
    public void testCreateBinder01() {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        DataBinder actualCreateBinderResult = (new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()))
                .createBinder("Target");
        assertFalse(actualCreateBinderResult.isIgnoreUnknownFields());
        assertFalse(actualCreateBinderResult.isIgnoreInvalidFields());
        assertTrue(actualCreateBinderResult.isAutoGrowNestedPaths());
        assertNull(actualCreateBinderResult.getValidator());
        assertEquals("Target", actualCreateBinderResult.getTarget());
        assertEquals("target", actualCreateBinderResult.getObjectName());
        assertTrue(actualCreateBinderResult.getBindingErrorProcessor() instanceof DefaultBindingErrorProcessor);
        assertEquals(256, actualCreateBinderResult.getAutoGrowCollectionLimit());
    }

    @Test
    public void testCreateBinder02() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(
                mock(StringValueResolver.class), true);

        DataBinder actualCreateBinderResult = (new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()))
                .createBinder("Target");
        assertFalse(actualCreateBinderResult.isIgnoreUnknownFields());
        assertFalse(actualCreateBinderResult.isIgnoreInvalidFields());
        assertTrue(actualCreateBinderResult.isAutoGrowNestedPaths());
        assertNull(actualCreateBinderResult.getValidator());
        assertEquals("Target", actualCreateBinderResult.getTarget());
        assertEquals("target", actualCreateBinderResult.getObjectName());
        assertTrue(actualCreateBinderResult.getBindingErrorProcessor() instanceof DefaultBindingErrorProcessor);
        assertEquals(256, actualCreateBinderResult.getAutoGrowCollectionLimit());
    }

    @Test
    public void testFindPropertyName01() {
        ApplicationConversionService conversionService = new ApplicationConversionService();

        Object bean = "Bean";
        String key = "Key";

        ReflectionTestUtils.invokeMethod(new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()),
                "findPropertyName", bean, key);

    }

    @Test
    public void testFindPropertyName02() {
        ApplicationConversionService conversionService = new ApplicationConversionService();

        ReflectionTestUtils.invokeMethod(new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()),
                "findPropertyName", null, null);

    }

    @Test
    public void testFindPropertyName03() {
        ApplicationConversionService conversionService = new ApplicationConversionService();

        Object bean = null;
        String key = "Key";

        ReflectionTestUtils.invokeMethod(new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()),
                "findPropertyName", bean, key);
    }

    @Test
    public void testConstructor01() {

        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> actualCustomExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        actualCustomExcelRowMapper.setBeanFactory(new JsrXmlApplicationContext());
        actualCustomExcelRowMapper.setDistanceLimit(1);
        actualCustomExcelRowMapper.setPrototypeBeanName("Name");
        actualCustomExcelRowMapper.setStrict(true);
        actualCustomExcelRowMapper.setTargetType(Object.class);
        actualCustomExcelRowMapper.initBinder(new DataBinder("Target", "Object Name"));
    }

    @Test
    public void testConstructor02() {

        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> actualCustomExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        actualCustomExcelRowMapper.initBinder(new DataBinder("Target", "Object Name"));
    }

    @Test(expected = NotReadablePropertyException.class)
    public void testGetPropertyValue01() {

        ApplicationConversionService conversionService = new ApplicationConversionService();

        ReflectionTestUtils.invokeMethod(new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()),
                "getPropertyValue", "Bean", "nestedName");

    }

    @Test(expected = NullPointerException.class)
    public void testSwitchPropertyNames01() {

        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());

        Properties properties = new Properties();
        properties.setProperty("Key", "Value");

        ReflectionTestUtils.invokeMethod(customExcelRowMapper, "switchPropertyNames", properties, "Old Name", "New Name");

    }
}