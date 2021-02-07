package com.epam.esm.model.validator;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * GiftCertificateValidator is a Validator for {@link GiftCertificate} objects.
 * It checks if fields are null, and if they aren't null performs validation for every field.
 * Performs additional check that name is not null.
 * Result of validation will be stored in {@link Errors} object.
 */
@Service
public class ProxyGiftCertificateValidator implements Validator {
    private final GiftCertificateValidator giftCertificateValidator;

    public ProxyGiftCertificateValidator(Validator tagValidator) {
        this.giftCertificateValidator = new GiftCertificateValidator(tagValidator);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return giftCertificateValidator.supports(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificate giftCertificate = (GiftCertificate) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.invokeValidator(giftCertificateValidator, giftCertificate, errors);
    }
}
