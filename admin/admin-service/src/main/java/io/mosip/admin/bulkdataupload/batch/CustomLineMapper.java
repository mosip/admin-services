package io.mosip.admin.bulkdataupload.batch;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomLineMapper <T> implements LineMapper<T>, InitializingBean {

    private LineTokenizer tokenizer;

    private FieldSetMapper<T> fieldSetMapper;

    private List<String> supportedLanguages;

    private Validator validator;

    public CustomLineMapper(List<String> languages, Validator validator) {
        this.supportedLanguages = languages;
        this.validator = validator;
    }

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        FieldSet fieldSet = tokenizer.tokenize(line);
        String[] values = fieldSet.getValues();

        if(Arrays.stream(Objects.requireNonNull(values)).anyMatch( v -> v.isBlank()))
            throw new Exception("Blank values are not allowed");

        for(int i = 0; i < fieldSet.getNames().length; i++) {
            String columnName = fieldSet.getNames()[i];
            if( (columnName.equals("langCode") || columnName.equals("lang_code")) && !supportedLanguages.contains(values[i])) {
                throw new Exception("Invalid language code provided");
            }
        }
        Set<ConstraintViolation<T>> violations = validator.validate(fieldSetMapper.mapFieldSet(tokenizer.tokenize(line)));
	    if (!violations.isEmpty()) {
	      throw new ConstraintViolationException(violations);
	    }

        return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    }

    public void setLineTokenizer(LineTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
        this.fieldSetMapper = fieldSetMapper;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(tokenizer, "The LineTokenizer must be set");
        Assert.notNull(fieldSetMapper, "The FieldSetMapper must be set");
    }
}