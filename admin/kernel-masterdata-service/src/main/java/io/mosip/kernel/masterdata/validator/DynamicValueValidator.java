package io.mosip.kernel.masterdata.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValueValidator.class )
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicValueValidator {
    String message() default "Numbers and Special characters not allowed in value field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}