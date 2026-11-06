package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import net.bytebuddy.implementation.bind.annotation.Empty;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = { "bosse", "boss", "b.o_b-@", "1234", "bob12" })
    void correctUsername(String username) {
        assertTrue(Username.validate(username));
    }

    @ParameterizedTest
    @ValueSource(strings = { "b o b", "böb", "bob[{?", "", "bob", "bo" })
    void incorrectUsername(String username) {
        assertFalse(Username.validate(username));
    }

    @ParameterizedTest
    @EmptySource
    void testRejectEmptyUsername(String username) {
        assertFalse(Username.validate(username));
    }

    @Test
    void testRejectNullUsername() {
        assertDoesNotThrow(() -> assertFalse(Username.validate(null)));
    }
}
