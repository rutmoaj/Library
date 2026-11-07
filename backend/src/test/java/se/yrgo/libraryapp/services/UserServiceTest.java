package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private UserService userService;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    void testRegisterNameWithApostrophe() {

    }

    @Test
    void testCorrectlyHashedPassword() {
        String rawPassword = "mickmous";
        final PasswordEncoder encoder = new BCryptPasswordEncoder();
        final String hash = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, hash);
        assertTrue(encoder.matches(rawPassword, hash));
    }

    // isNameAvailable
    @ParameterizedTest
    @NullSource  
    void testIsNameAvailableNullEmptyAndWhitespaceValue(String name){
        assertFalse(userService.isNameAvailable(name));
    }

    @ParameterizedTest 
    @EmptySource 
    void testIsNameAvailableEmptyValue(String name){
        assertFalse(userService.isNameAvailable(name));
    }

    @Test 
    void testIsNameAvailableTooShortValue(){
        assertFalse(userService.isNameAvailable("Bo"));
    }

    @Test 
    void testIsNameAvailableJustOverLimitValue(){
        assertTrue(userService.isNameAvailable("Bob"));
    }
}
