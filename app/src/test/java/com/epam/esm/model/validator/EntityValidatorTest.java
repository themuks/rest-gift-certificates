package com.epam.esm.model.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityValidatorTest {
    @Test
    void isIdValid_ValidIdGiven_ShouldReturnTrue() {
        assertTrue(EntityValidator.isIdValid(1));
    }

    @Test
    void isIdValid_InvalidIdGiven_ShouldReturnFalse() {
        assertFalse(EntityValidator.isIdValid(-1));
    }
}
