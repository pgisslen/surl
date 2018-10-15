package io.lab.surl.validators;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.lab.surl.validators.ValidUrl.CustomUrlValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.apache.commons.validator.routines.UrlValidator;

@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CustomUrlValidator.class)
@Documented
public @interface ValidUrl {

    String message() default "{Is not an valid URL}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomUrlValidator implements ConstraintValidator<ValidUrl, String> {

        @Override
        public void initialize(final ValidUrl constraintAnnotation) {
            //Nothing todo.
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext context) {
            final boolean valid = UrlValidator.getInstance().isValid(value);
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(value + " is not a valid URL")
                    .addConstraintViolation();
            }
            return valid;
        }
    }

}
