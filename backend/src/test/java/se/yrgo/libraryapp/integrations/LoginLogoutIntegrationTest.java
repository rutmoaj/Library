package se.yrgo.libraryapp.integrations;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;

import org.junit.jupiter.api.Test;

@Tag("bombim")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");

    @Test
    public void alwaysOkToLogout() throws IOException {
    }

    public static App createApp() {
        System.setProperty("db.url", mySQLContainer.getJdbcUrl());
        System.setProperty("db.user", mySQLContainer.getUsername());
        System.setProperty("db.password", mySQLContainer.getPassword());
        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }
}
