package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@Testcontainers
public class UserDaoIntegrationTest {
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");
    private static DataSource ds;

    @BeforeAll
    static void setup() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mysql.getJdbcUrl());
        hikariConfig.setUsername(mysql.getUsername());
        hikariConfig.setPassword(mysql.getPassword());
        hikariConfig.setDriverClassName(mysql.getDriverClassName());
        UserDaoIntegrationTest.ds = new HikariDataSource(hikariConfig);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();
    }

    @Test
    void getUserById() {
        // this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getUserByIdNotFound() {
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get("9999");

        assertThat(maybeUser).isEmpty();
    }

    @Test
    void getLoginInfo() {
        UserDao userDao = new UserDao(ds);
        LoginInfo info = userDao.getLoginInfo("test").get();

        final UserId userId = UserId.of(1);
        final String passwordHash = "$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ";

        assertThat(info.getUserId()).isEqualTo(userId);
        assertThat(info.getPasswordHash())
                .isEqualTo(passwordHash);
    }

    @Test
    void getLoginInfoNotFound() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.getLoginInfo("Asmodeus")).isEmpty();
    }

    @Test
    void registerUser() {
        UserDao userDao = new UserDao(ds);
        assertTrue(userDao.register("Bim", "bom", "bam"));

        Optional<LoginInfo> maybeUser = userDao.getLoginInfo("Bim");

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getPasswordHash()).isEqualTo("bam");
    }

}
