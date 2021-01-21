package com.epam.esm.model.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class ProxyGiftCertificateValidator implements Validator {
    private final Validator giftCertificateValidator;

    public ProxyGiftCertificateValidator(Validator tagValidator) {
        if (tagValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                    "required and must not be null");
        }
        if (!tagValidator.supports(Tag.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                    "support the validation of [Tag] instances.");
        }
        this.giftCertificateValidator = new GiftCertificateValidator(tagValidator);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return giftCertificateValidator.supports(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificate giftCertificate = (GiftCertificate) target;
        if (giftCertificate.getName() == null) {
            ValidationUtils.rejectIfEmpty(errors, "name", "name.null");
        }
        if (giftCertificate.getDescription() == null) {
            ValidationUtils.rejectIfEmpty(errors, "description", "description.null");
        }
        if (giftCertificate.getPrice() == null) {
            ValidationUtils.rejectIfEmpty(errors, "price", "price.null");
        }
        if (giftCertificate.getDuration() == null) {
            ValidationUtils.rejectIfEmpty(errors, "duration", "duration.null");
        }
        if (giftCertificate.getCreateDate() == null) {
            ValidationUtils.rejectIfEmpty(errors, "createDate", "createDate.null");
        }
        if (giftCertificate.getLastUpdateDate() == null) {
            ValidationUtils.rejectIfEmpty(errors, "lastUpdateDate", "lastUpdateDate.null");
        }
        giftCertificateValidator.validate(target, errors);
    }
}
