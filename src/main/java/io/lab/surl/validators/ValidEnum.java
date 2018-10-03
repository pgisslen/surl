package io.lab.surl.validators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.lab.surl.validators.ValidEnum.EnumValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Documented
public @interface ValidEnum {

    String message() default "{Unknown value for ENUM}";

    Class<?>[] groups() default {};

    Class<? extends Enum<?>> targetClassType();

    Class<? extends Payload>[] payload() default {};

    class EnumValidator implements ConstraintValidator<ValidEnum, String> {

        private Set<String> allowedValues;

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(final ValidEnum targetEnum) {
            final Class<? extends Enum> enumSelected = targetEnum.targetClassType();
            allowedValues =
                (Set<String>) EnumSet.allOf(enumSelected).stream().map(e -> ((Enum<? extends Enum<?>>) e).name())
                    .collect(Collectors.toSet());
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext context) {

            final boolean valid = value == null || allowedValues.contains(value);
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate(value + " is not a valid Enum value")
                    .addConstraintViolation();
            }
            return valid;
        }
    }

}

