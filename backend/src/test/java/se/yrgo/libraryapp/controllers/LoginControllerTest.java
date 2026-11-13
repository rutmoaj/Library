package se.yrgo.libraryapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jooby.*;
import se.yrgo.libraryapp.entities.*;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

public class LoginControllerTest {
    private SessionDao sessionDao;
    private RoleDao roleDao;
    private Context context;
    private UserService userService;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        sessionDao = mock(SessionDao.class);
        roleDao = mock(RoleDao.class);
        context = mock(Context.class);
        userService = mock(UserService.class);
        controller = new LoginController(userService, roleDao, sessionDao);
    }

    // login

    @Test
    void login_InvalidSessionValidLoginInfo() {
        String sessionCookie = "bom";
        LoginData login = new LoginData("hej", "hejj");
        UUID sessionId = UUID.randomUUID();
        UserId userId = UserId.of("123");
        Role role = Role.USER;

        when(userService.validate(login.getUsername(), login.getPassword())).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);
        when(roleDao.get(userId)).thenReturn(List.of(role));

        List<Role> result = controller.login(context, sessionCookie, login);

        assertEquals(List.of(role), result);
    }

    @Test
    void login_InvalidSessionWrongLoginInfo() {
        String sessionCookie = "bom";
        LoginData login = new LoginData("hej", "hejj");
        UUID sessionId = UUID.randomUUID();
        UserId userId = UserId.of("123");
        Role role = Role.USER;

        when(userService.validate(login.getUsername(), "bam")).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);
        when(roleDao.get(userId)).thenReturn(List.of(role));

        List<Role> result = controller.login(context, sessionCookie, login);

        assertEquals(List.of(), result);
    }

    @Test
    void login_ValidSessionReturnEmptyList() {
        UUID sessionId = UUID.randomUUID();
        String sessionCookie = sessionId.toString();
        LoginData login = new LoginData("hej", "hejj");
        List<Role> result = controller.login(context, sessionCookie, login);

        assertEquals(List.of(), result);
    }

    // isloggedin

    @Test
    void isLoggedIn_ValidSessionReturnsUser() {
        UUID sessionId = UUID.randomUUID();
        String sessionCookie = sessionId.toString();
        UserId userId = UserId.of("123");
        Role role = Role.USER;

        when(sessionDao.validate(sessionId)).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(List.of(role));

        List<Role> result = controller.isLoggedIn(sessionCookie);

        assertEquals(List.of(role), result);
    }

    @Test
    void isLoggedIn_InvalidSession() {
        String sessionCookie = "bom";

        List<Role> result = controller.isLoggedIn(sessionCookie);

        assertEquals(List.of(), result);
    }
}
