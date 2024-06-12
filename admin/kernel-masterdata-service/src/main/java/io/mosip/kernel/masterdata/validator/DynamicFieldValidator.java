package io.mosip.kernel.masterdata.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldValidator.class )
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicFieldValidator {
    String message() default "Special characters are not allowed in code and value fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}