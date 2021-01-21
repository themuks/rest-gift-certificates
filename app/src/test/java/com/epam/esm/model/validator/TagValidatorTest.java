package com.epam.esm.model.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DataBinder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagValidatorTest {
    private final TagValidator tagValidator = new TagValidator();

    @Test
    void supports_TagClassGiven_ShouldReturnTrue() {
        assertTrue(tagValidator.supports(Tag.class));
    }

    @Test
    void supports_ObjectClassGiven_ShouldReturnFalse() {
        assertFalse(tagValidator.supports(Object.class));
    }

    @Test
    void validate_ValidTagGiven_ShouldNotMakeErrors() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("name")
                .build();
        DataBinder dataBinder = new DataBinder(tag);
        dataBinder.addValidators(tagValidator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validate_InvalidTagGiven_ShouldMakeErrors() {
        Tag tag = Tag.builder()
                .id(-1L)
                .name("")
                .build();
        DataBinder dataBinder = new DataBinder(tag);
        dataBinder.addValidators(tagValidator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
    }
}
