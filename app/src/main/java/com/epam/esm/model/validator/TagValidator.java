package com.epam.esm.model.validator;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * TagValidator is a Validator for {@link Tag} objects.
 * Validator checks id value and name value to be correct and not null.
 * Result of validation will be stored in {@link Errors} object.
 */
@Service
public class TagValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
        Long id = tag.getId();
        if (id != null) {
            if (id < 1) {
                errors.rejectValue("id", "id.not_positive");
            }
        }
        String name = tag.getName();
        if (name != null) {
            ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
            if (name.length() > 255) {
                errors.rejectValue("name", "name.too_long");
            }
        }
    }
}
