package io.mosip.kernel.masterdata.validator;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class FieldValidator implements ConstraintValidator<DynamicFieldValidator, JsonNode> {
    @Value("${mosip.kernel.masterdata.code.validate.regex}")
    private String allowedCharactersRegex;

    @Override
    public boolean isValid(JsonNode jsonValue, ConstraintValidatorContext context) {
        JsonNode val = jsonValue.get("value");
        String value = val.asText();
        val = jsonValue.get("code");
        String code = val.asText();
        if (value == null || value.isEmpty() || code == null || code.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(allowedCharactersRegex, Pattern.CASE_INSENSITIVE);
        Matcher mCode = p.matcher(code.trim());
        Matcher mValue = p.matcher(value.trim());
        return (mCode.find() || mValue.find()) ? false : true;
    }
}