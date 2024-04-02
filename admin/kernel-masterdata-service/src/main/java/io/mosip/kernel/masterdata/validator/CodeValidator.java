package io.mosip.kernel.masterdata.validator;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class CodeValidator implements ConstraintValidator<DynamicCodeValidator, JsonNode> {
    @Value("${mosip.kernel.masterdata.code.validate.regex}")
    private String allowedCharactersRegex;


    @Override
    public boolean isValid(JsonNode value, ConstraintValidatorContext context) {
        JsonNode val = value.get("code");
        String code = val.asText();
        if (code == null && code.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(allowedCharactersRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(code.trim());
        return m.find() ? true : false;
    }
}