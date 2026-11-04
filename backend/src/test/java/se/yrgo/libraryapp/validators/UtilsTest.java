package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UtilsTest {

    @ParameterizedTest
    @CsvSource(value = { "Hello, World!123:hello world", "inget konstigt här:inget konstigt här",
            "AJ:aj", "go-go:gogo" }, delimiter = ':')
    void testReturnExpectedValueBasicCases(String input, String expected) {
        String actualValue = Utils.onlyLettersAndWhitespace(input);
        assertEquals(expected, actualValue);
    }

    @ParameterizedTest
    @CsvSource(value = { "'':''", "'':''", "' o ':' o '", "'a   b':'a   b'" }, delimiter = ':')
    void testReturnExpectedValueGreyZone(String input, String expected) {
        String actualValue = Utils.onlyLettersAndWhitespace(input);
        assertEquals(expected, actualValue);
    }
}
