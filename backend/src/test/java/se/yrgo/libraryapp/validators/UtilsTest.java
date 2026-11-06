package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UtilsTest {

    @ParameterizedTest
    @CsvSource(value = { "Hello, World!123:hello world", "inget konstigt här:inget konstigt här",
            "AJ:aj", "go-go:gogo" }, delimiter = ':')
    void onlyLettersAndWhitespaceKeepOnlyLettersAndWhitespace(String input, String expected) {
        String actualValue = Utils.onlyLettersAndWhitespace(input);
        assertEquals(expected, actualValue);
    }

    @ParameterizedTest
    @CsvSource(value = { "'':''", "' ':' '", "'    ':'    '", "'a b':'a b'" }, delimiter = ':')
    void onlyLettersAndWhitespaceWhitespaceBehaviour(String input, String expected) {
        String actualValue = Utils.onlyLettersAndWhitespace(input);
        assertEquals(expected, actualValue);
    }

    @ParameterizedTest 
    @CsvSource(value={"'halÖÄäå':halöääå", "'😴':''"}, delimiter = ':')
    void onlyLettersAndWhitespaceNonASCII(String input, String expected){
        String actualValue = Utils.onlyLettersAndWhitespace(input);
        assertEquals(expected, actualValue);
    }

    @Test
    void onlyLettersAndWhitespaceNullThrows() {
        assertThrows(NullPointerException.class, () -> Utils.onlyLettersAndWhitespace(null));
    }

    @ParameterizedTest
    @CsvSource(value = { "'1337':'leet'", "'h3ll0 w0rld':'hello world'", "'7r4n5l4t3':'translate'",
            "'':''", "'5t4r W4r5!':'star wars'", "'😔':''" }, delimiter = ':')
    void cleanAndUnLeetTranslateLeet(String input, String expected) {
        String actualValue = Utils.cleanAndUnLeet(input);
        assertEquals(expected, actualValue);
    }

    @Test
    void cleanAndUnLeetNullThrows() {
        assertThrows(NullPointerException.class, () -> Utils.cleanAndUnLeet(null));
    }

}
