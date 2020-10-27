package com.github.oindiao.application.domain;

import com.github.oindiao.application.config.UserConfig;
import com.github.oindiao.common.util.EasyRandomWithEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserTest {

	EasyRandomWithEmail easyRandom = new EasyRandomWithEmail();

    LocalDate today;

    UserConfig config = Mockito.mock(UserConfig.class);

    @BeforeEach
    void setup() {
        today = LocalDate.now();
        when(config.getExpirationTokenDays()).thenReturn(120);
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 121 days then user can't create")
    void verifyExpirationDateTodayMinus121Days() {

        // Arrange
        LocalDate todayMinus121Days = this.today.minusDays(121);

        // Act

        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                todayMinus121Days,
                config.getExpirationTokenDays(),
                Boolean.TRUE,
                Arrays.asList(easyRandom.nextObject(Profile.class).name())
        );

        // Assert

        assertTrue(user.getNotification().hasError());
        assertThat(user.getNotification().getErrors(), hasItems("Password is expired"));
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 120 days then user can't create")
    void verifyExpirationDateTodayMinus120Days() {

        // Arrange
        LocalDate todayMinus120Days = this.today.minusDays(120);

        // Act

        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                todayMinus120Days,
                config.getExpirationTokenDays(),
                Boolean.TRUE,
                Arrays.asList(easyRandom.nextObject(Profile.class).name())
        );

        // Assert

        assertTrue(user.getNotification().hasError());
        assertThat(user.getNotification().getErrors(), hasItems("Password is expired"));
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 119 days then user can create")
    void verifyExpirationDateTodayMinus119Days() {

        // Arrange
        LocalDate todayMinus119Days = this.today.minusDays(119);

        // Act
        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                todayMinus119Days,
                config.getExpirationTokenDays(),
                Boolean.TRUE,
                Arrays.asList(easyRandom.nextObject(Profile.class).name())
        );

        // Assert
        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("Don't create user with wrong profile")
    void verifyNotCreateUserWithWrongProfile() {
        // Arrange

        String profile = this.easyRandom.nextObject(String.class);

        // Act

        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                LocalDate.now(),
                config.getExpirationTokenDays(),
                Boolean.TRUE,
                Arrays.asList(profile)
        );

        // Assert

        assertTrue(user.getNotification().hasError());
        assertThat(user.getNotification().getErrors(), hasItems(String.format("Wrong profile: %s", profile)));

    }

    @Test
    @DisplayName("Create user with valid profile")
    void verifyCreateUserWithValidProfile() {
        // Arrange

        String profile = "ROLE_ADMIN";

        // Act

        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                LocalDate.now(),
                config.getExpirationTokenDays(),
                Boolean.TRUE,
                Arrays.asList(profile)
        );

        // Assert
        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("Don't create user inactive")
    void dontCreateUserInactive(){

        // Arrange

        Boolean active = Boolean.FALSE;

        // Assert

        User user = User.of(easyRandom.email(),
                easyRandom.nextObject(String.class),
                LocalDate.now(),
                config.getExpirationTokenDays(),
                active,
                Arrays.asList(easyRandom.nextObject(Profile.class).name())
        );

        // Assert

        assertTrue(user.getNotification().hasError());
        assertThat(user.getNotification().getErrors(), hasItems("User inactive."));

    }

    @Test
    @DisplayName("Create user active")
    void verifyCreateUserWithActive() {
        // Arrange

        Boolean active = Boolean.TRUE;

        // Act

        User user = User.of(easyRandom.email(),
                easyRandom.email(),
                LocalDate.now(),
                config.getExpirationTokenDays(),
                active,
                Arrays.asList(easyRandom.nextObject(Profile.class).name())
        );

        // Assert
        assertTrue(user.getActive());
    }

    

}
