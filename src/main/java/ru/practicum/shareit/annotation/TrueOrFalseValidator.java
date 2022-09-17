package ru.practicum.shareit.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class TrueOrFalseValidator implements ConstraintValidator<TrueOrFalse, Boolean> {

    public void initialize(TrueOrFalse annotation) {
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {

        if (value != null && !Optional.of(value).isEmpty()) {
            return true;
        }

        return false;
    }
}
