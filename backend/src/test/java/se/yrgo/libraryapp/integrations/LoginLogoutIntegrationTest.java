package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";
    private static final String REGISTER = "/register";
    private static final HttpClient client = HttpClient.newHttpClient();

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

    @Test
    public void alwaysOkToLogout(int serverPort) throws IOException {
        try {
            HttpResponse<String> resp = tryHttpRequest(serverPort, LOGOUT);
            assertEquals("", resp.body());
            assertEquals(HttpURLConnection.HTTP_OK, resp.statusCode());
        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void loginLogoutNoError(int serverPort) throws IOException {
        try {

            HttpResponse<String> createUser = tryPost(serverPort, REGISTER,
                    "{\"name\":\"chevy\",\"realName\":\"devil\",\"password\":\"weewoo\"}");
            assertEquals(HttpURLConnection.HTTP_OK, createUser.statusCode());

            HttpResponse<String> respLogin = tryPost(serverPort, LOGIN,
                    "{\"username\":\"chevy\",\"password\":\"weewoo\"}");
            assertEquals(HttpURLConnection.HTTP_OK, respLogin.statusCode());

            HttpResponse<String> respLogout = tryHttpRequest(serverPort, LOGOUT);
            assertEquals("", respLogout.body());
            assertEquals(HttpURLConnection.HTTP_OK, respLogout.statusCode());
        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void loginInvalidUser(int serverPort) throws IOException {
        try {

            HttpResponse<String> respLogin = tryPost(serverPort, LOGIN,
                    "{\"username\":\"Booz\",\"password\":\"weewoo\"}");
            assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, respLogin.statusCode());

        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    public HttpResponse<String> tryHttpRequest(int serverPort, String endpoint)
            throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" +
                serverPort + endpoint)).build();
        return client.send(req, BodyHandlers.ofString());
    }

    public HttpResponse<String> tryPost(int serverPort, String endpoint, String body)
            throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" + serverPort + endpoint))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();
        return client.send(req, BodyHandlers.ofString());
    }
}
