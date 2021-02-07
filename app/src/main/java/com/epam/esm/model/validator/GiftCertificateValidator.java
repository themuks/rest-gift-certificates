package com.epam.esm.model.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GiftCertificateValidator is a Validator for {@link GiftCertificate} objects.
 * It checks if fields are null, and if they aren't null performs validation for every field.
 * Result of validation will be stored in {@link Errors} object.
 */
@Service
public class GiftCertificateValidator implements Validator {
    private final Validator tagValidator;

    public GiftCertificateValidator(Validator tagValidator) {
        if (tagValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                    "required and must not be null");
        }
        if (!tagValidator.supports(Tag.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                    "support the validation of [Tag] instances.");
        }
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificate.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificate giftCertificate = (GiftCertificate) target;
        Long id = giftCertificate.getId();
        if (id != null) {
            if (id < 1) {
                errors.rejectValue("id", "id.not_positive");
            }
        }
        String name = giftCertificate.getName();
        if (name != null) {
            ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
            if (name.length() > 255) {
                errors.rejectValue("name", "name.too_long");
            }
        }
        String description = giftCertificate.getDescription();
        if (description != null) {
            ValidationUtils.rejectIfEmpty(errors, "description", "description.empty");
            if (description.length() > 65535) {
                errors.rejectValue("description", "description.too_long");
            }
        }
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (price.longValue() < 0) {
                errors.rejectValue("price", "price.negative");
            }
        }
        Integer duration = giftCertificate.getDuration();
        if (duration != null) {
            if (duration < 0) {
                errors.rejectValue("duration", "duration.negative");
            }
        }
        LocalDateTime createDate = giftCertificate.getCreateDate();
        if (createDate != null) {
            if (createDate.getYear() < 1970) {
                errors.rejectValue("createDate", "createDate.too_old");
            }
        }
        LocalDateTime lastUpdateDate = giftCertificate.getLastUpdateDate();
        if (lastUpdateDate != null) {
            if (lastUpdateDate.getYear() < 1970) {
                errors.rejectValue("lastUpdateDate", "lastUpdateDate.too_old");
            }
        }
        List<Tag> tags = giftCertificate.getTags();
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                try {
                    errors.pushNestedPath(String.format("%s[%d]", "tags", i));
                    ValidationUtils.invokeValidator(tagValidator, tags.get(i), errors);
                } finally {
                    errors.popNestedPath();
                }
            }
        }
    }
}
