package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @SuppressWarnings("deprecation")
    private final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

    private UserService userService;

    @Test
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);

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
        final String hash = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, hash);
        assertTrue(encoder.matches(rawPassword, hash));
    }

    // isNameAvailable
    @ParameterizedTest
    @NullSource
    void testIsNameAvailableNullEmptyAndWhitespaceValue(String name) {
        userService = new UserService(userDao, encoder);
        assertFalse(userService.isNameAvailable(name));
    }

    @ParameterizedTest
    @EmptySource
    void testIsNameAvailableEmptyValue(String name) {
        userService = new UserService(userDao, encoder);
        assertFalse(userService.isNameAvailable(name));
    }

    @Test
    void testIsNameAvailableTooShortValue() {
        userService = new UserService(userDao, encoder);
        assertFalse(userService.isNameAvailable("Bo"));
    }

    @Test
    void testIsNameAvailableJustOverLimitValue() {
        userService = new UserService(userDao, encoder);
        when(userDao.isNameAvailable("bob")).thenReturn(true);
        assertTrue(userService.isNameAvailable("bob"));
    }
}
