package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RealNameTest {

    @ParameterizedTest
    @ValueSource(strings = { "dumbom", "attans", "TONÅRSFLINT" })
    void validateRejectsBadWords(String name) {
        assertFalse(RealName.validate(name));
    }

    @Test
    void validateAcceptValidNames() {
        assertTrue(RealName.validate("Moa Johansson"));
    }

}
