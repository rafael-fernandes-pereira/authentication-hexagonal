package com.github.oindiao.application.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import com.github.oindiao.application.config.UserConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.oindiao.common.exception.UserException;
import com.github.oindiao.common.util.EasyRandomWithEmail;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.email(),
                        easyRandom.nextObject(String.class),
                        todayMinus121Days,
                        config.getExpirationTokenDays(),
                        Boolean.TRUE,
                        Arrays.asList(easyRandom.nextObject(Profile.class).name())
                )
        );
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 120 days then user can't create")
    void verifyExpirationDateTodayMinus120Days() {

        // Arrange
        LocalDate todayMinus120Days = this.today.minusDays(120);

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.email(),
                        easyRandom.nextObject(String.class),
                        todayMinus120Days,
                        config.getExpirationTokenDays(),
                        Boolean.TRUE,
                        Arrays.asList(easyRandom.nextObject(Profile.class).name())
                )
        );
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

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.email(),
                        easyRandom.nextObject(String.class),
                        LocalDate.now(),
                        config.getExpirationTokenDays(),
                        Boolean.TRUE,
                        Arrays.asList(profile)
                )
        );

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

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.email(),
                        easyRandom.nextObject(String.class),
                        LocalDate.now(),
                        config.getExpirationTokenDays(),
                        active,
                        Arrays.asList(easyRandom.nextObject(Profile.class).name())
                )
        );

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
