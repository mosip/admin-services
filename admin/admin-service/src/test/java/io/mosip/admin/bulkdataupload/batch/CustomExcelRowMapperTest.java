package io.mosip.admin.bulkdataupload.batch;

import org.junit.Test;
import org.springframework.batch.core.jsr.configuration.xml.JsrXmlApplicationContext;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomExcelRowMapperTest {

    @Autowired
    private CustomExcelRowMapper customExcelRowMapper;

    @Test(expected = Exception.class)
    public void mapRow_withEmptyRowSet_throwException() throws BindException {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        customExcelRowMapper.setTargetType(Object.class);
        RowSet rowSet = mock(RowSet.class);
        when(rowSet.getProperties()).thenReturn(new Properties());
        customExcelRowMapper.mapRow(rowSet);
    }

    @Test(expected = NotWritablePropertyException.class)
    public void mapRow_withProperties_throwNotWritablePropertyException() throws BindException {
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
    public void mapRow_withConstraintViolation_throwConstraintViolationException() throws BindException {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> customExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        customExcelRowMapper.setTargetType(Object.class);
        RowSet rowSet = mock(RowSet.class);
        when(rowSet.getProperties()).thenThrow(new ConstraintViolationException(new HashSet<>()));
        customExcelRowMapper.mapRow(rowSet);

    }

    @Test
    public void createBinder_withApplicationConversionService_returnSuccessResponse() {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        DataBinder actualCreateBinderResult = (new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()))
                .createBinder("Target");

        assertNotNull(actualCreateBinderResult);
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
    public void createBinder_withDefaultFormattingConversionService_returnSuccessResponse() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        DataBinder actualCreateBinderResult = (new CustomExcelRowMapper<>(conversionService, new CustomValidatorBean()))
                .createBinder("Target");

        assertNotNull(actualCreateBinderResult);
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
    public void customExcelRowMapper_withSetValues_returnSuccessResponse() {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> actualCustomExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        actualCustomExcelRowMapper.setBeanFactory(new JsrXmlApplicationContext());
        actualCustomExcelRowMapper.setDistanceLimit(1);
        actualCustomExcelRowMapper.setPrototypeBeanName("Name");
        actualCustomExcelRowMapper.setStrict(true);
        actualCustomExcelRowMapper.setTargetType(Object.class);
        DataBinder dataBinder = new DataBinder("Target", "Object Name");
        actualCustomExcelRowMapper.initBinder(dataBinder);
        assertNotNull(dataBinder);
    }

    @Test
    public void customExcelRowMapper_withInitBinder_returnSuccessResponse() {
        ApplicationConversionService conversionService = new ApplicationConversionService();
        CustomExcelRowMapper<Object> actualCustomExcelRowMapper = new CustomExcelRowMapper<>(conversionService,
                new CustomValidatorBean());
        DataBinder dataBinder = new DataBinder("Target", "Object Name");
        actualCustomExcelRowMapper.initBinder(dataBinder);
        assertNotNull(dataBinder);
    }

}
