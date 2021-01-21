package com.epam.esm.model.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DataBinder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProxyGiftCertificateValidatorTest {
    private final ProxyGiftCertificateValidator proxyGiftCertificateValidator = new ProxyGiftCertificateValidator(new TagValidator());

    @Test
    void supports_GiftCertificateClassGiven_ShouldReturnTrue() {
        assertTrue(proxyGiftCertificateValidator.supports(GiftCertificate.class));
    }

    @Test
    void supports_ObjectClassGiven_ShouldReturnFalse() {
        assertFalse(proxyGiftCertificateValidator.supports(Object.class));
    }

    @Test
    void validate_ValidGiftCertificateGiven_ShouldNotMakeErrors() {
        LocalDateTime localDateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(1)
                .price(new BigDecimal(10))
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(proxyGiftCertificateValidator);
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
        dataBinder.addValidators(proxyGiftCertificateValidator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
    }
}
