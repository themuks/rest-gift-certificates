package com.epam.esm.model.validator;

import com.epam.esm.entity.Tag;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * ProxyTagValidator is a Validator for {@link Tag} objects.
 * It checks if fields are null, and if they aren't null performs validation for every field.
 * Performs additional check that name is not null.
 * Result of validation will be stored in {@link Errors} object.
 */
public class ProxyTagValidator implements Validator {
    private final TagValidator tagValidator;

    public ProxyTagValidator() {
        this.tagValidator = new TagValidator();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return tagValidator.supports(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.invokeValidator(tagValidator, tag, errors);
    }
}
