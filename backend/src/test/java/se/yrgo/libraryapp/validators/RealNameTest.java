package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RealNameTest {

    @Test 
    void testValidName(){
        assertTrue(RealName.validate("Chevy Johansson"));
    }

    @ParameterizedTest 
    @ValueSource(strings={"dumbom", "attans", "fasen"})
    void testInvalidNames(String name){
        assertFalse(RealName.validate(name));
    }

    @Test
    void testInvalidNameLast(){
        assertFalse(RealName.validate("Chevy Dumbom"));
    }

    @Test 
    void testInvalidNameFirst(){
        assertFalse(RealName.validate("Stökis Devil"));
    }

    @Test 
    void testInvalidWordInAnotherWord(){
        assertFalse(RealName.validate("Maja Fyfasensson"));
    }

    @Test 
    void testInvalidBigLetters(){
        assertFalse(RealName.validate("ATTANS"));
    }
}
