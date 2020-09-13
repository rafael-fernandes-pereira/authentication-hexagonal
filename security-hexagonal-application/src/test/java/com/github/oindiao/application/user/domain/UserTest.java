package com.github.oindiao.application.user.domain;

import com.github.oindiao.common.exception.UserException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    EasyRandom easyRandom = new EasyRandom();

    LocalDate today;

    @BeforeEach
    void setup() {
        today = LocalDate.now();
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 121 days then user can't create")
    void verifyExpirationDateTodayMinus121Days() {

        // Arrange
        LocalDate todayMinus121Days = this.today.minusDays(121);

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.nextObject(String.class),
                        easyRandom.nextObject(String.class),
                        todayMinus121Days,
                        Boolean.TRUE,
                        easyRandom.nextObject(Profile.class).name()
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
                User.of(easyRandom.nextObject(String.class),
                        easyRandom.nextObject(String.class),
                        todayMinus120Days,
                        Boolean.TRUE,
                        easyRandom.nextObject(Profile.class).name()
                )
        );
    }

    @Test
    @DisplayName("If expiration password date has small that today minus 119 days then user can create")
    void verifyExpirationDateTodayMinus119Days() {

        // Arrange
        LocalDate todayMinus119Days = this.today.minusDays(119);

        // Act
        User user = User.of(easyRandom.nextObject(String.class),
                easyRandom.nextObject(String.class),
                todayMinus119Days,
                Boolean.TRUE,
                easyRandom.nextObject(Profile.class).name()
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
                User.of(easyRandom.nextObject(String.class),
                        easyRandom.nextObject(String.class),
                        LocalDate.now(),
                        Boolean.TRUE,
                        profile
                )
        );

    }

    @Test
    @DisplayName("Create user with valid profile")
    void verifyCreateUserWithValidProfile() {
        // Arrange

        String profile = "ROLE_ADMIN";

        // Act

        User user = User.of(easyRandom.nextObject(String.class),
                easyRandom.nextObject(String.class),
                LocalDate.now(),
                Boolean.TRUE,
                profile
        );

        // Assert
        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("Dont create user inactive")
    void dontCreateUserInactive(){

        // Arrange

        Boolean active = Boolean.FALSE;

        // Act - Assert
        assertThrows(UserException.class, () ->
                User.of(easyRandom.nextObject(String.class),
                        easyRandom.nextObject(String.class),
                        LocalDate.now(),
                        active,
                        easyRandom.nextObject(Profile.class).name()
                )
        );

    }

    @Test
    @DisplayName("Create user active")
    void verifyCreateUserWithActive() {
        // Arrange

        Boolean active = Boolean.TRUE;

        // Act

        User user = User.of(easyRandom.nextObject(String.class),
                easyRandom.nextObject(String.class),
                LocalDate.now(),
                active,
                easyRandom.nextObject(Profile.class).name()
        );

        // Assert
        assertTrue(user.getActive());
    }

    

}
