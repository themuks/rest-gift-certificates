package com.epam.esm.model.service.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DataBinder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GiftCertificateValidatorTest {
    private final GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(new TagValidator());

    @Test
    void supports_GiftCertificateClassGiven_ShouldReturnTrue() {
        assertTrue(giftCertificateValidator.supports(GiftCertificate.class));
    }

    @Test
    void supports_ObjectClassGiven_ShouldReturnFalse() {
        assertFalse(giftCertificateValidator.supports(Object.class));
    }

    @Test
    void validate_ValidGiftCertificateGiven_ShouldNotMakeErrors() {
        DataBinder dataBinder = new DataBinder(new GiftCertificate());
        dataBinder.addValidators(giftCertificateValidator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validate_InvalidGiftCertificateGiven_ShouldMakeErrors() {
        LocalDateTime localDateTime = LocalDateTime.of(1, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(-1L)
                .name("")
                .description("")
                .duration(-1)
                .price(new BigDecimal(-10))
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime)
                .tags(null)
                .build();
        DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(giftCertificateValidator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
    }
}
