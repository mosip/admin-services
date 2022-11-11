package io.mosip.kernel.masterdata.validator;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ValueValidator implements ConstraintValidator<DynamicValueValidator, JsonNode> {
    @Value("${mosip.kernel.masterdata.name.validate.regex}")
    private String allowedCharactersRegex;


    @Override
    public boolean isValid(JsonNode jsonValue, ConstraintValidatorContext context) {
        JsonNode val=jsonValue.get("value");
        String value= val.asText();
        if(value!=null && !value.isEmpty()){
            Pattern p= Pattern.compile(allowedCharactersRegex,Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(value.trim());
            return !(m.find());
        }
        return true;
    }
}
