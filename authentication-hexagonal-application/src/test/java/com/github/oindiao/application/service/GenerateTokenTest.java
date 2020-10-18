package com.github.oindiao.application.service;

import com.github.oindiao.application.config.UserConfig;
import com.github.oindiao.application.domain.Profile;
import com.github.oindiao.application.port.in.GenerateTokenUseCase;
import com.github.oindiao.application.port.out.*;
import com.github.oindiao.common.exception.*;
import com.github.oindiao.common.util.EasyRandomWithEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class GenerateTokenTest {

    EasyRandomWithEmail easyRandom = new EasyRandomWithEmail();

    @Mock
    private ValidateData validateData;

    @Mock
    private SearchUser searchUser;

    @Mock
    private ValidateLogin validateLogin;

    @Mock
    private CreateToken createToken;

    @Mock
    private UserConfig config;

    @Mock
    private CacheToken cacheToken;

    @InjectMocks
    private GenerateToken generateToken;

    private GenerateTokenUseCase.Input defaultInput;

    LocalDate today;

    @BeforeEach
    void setup() {
        defaultInput = GenerateTokenUseCase.Input.of(easyRandom.email(), easyRandom.password());
        today = LocalDate.now();
    }

    @Test
    @DisplayName("When return of validateData having error then generateToken throw exception")
    void whenValidateDataError_ThenThrowException() {

        // Arrange

        doThrow(ValidateDataException.class).when(this.validateData).validate(any());

        // Act - Assert

        assertThrows(ValidateDataException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });

    }

    @Test
    @DisplayName("When return of searchUser having error then generateToken throw exception")
    void whenSearchUserError_ThenThrowException() {

        // Arrange

        doThrow(SearchUserException.class).when(this.searchUser).searchUser(any());

        // Act - Assert

        assertThrows(SearchUserException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });

    }

    @Test
    @DisplayName("When error in create user domain throw exception")
    void whenErrorUserDomain_throwException(){

        // Arrange
        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.FALSE
        );

        doReturn(outputSearchUser).when(searchUser).searchUser(any());

        // Act - Assert

        assertThrows(UserException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });
    }

    @Test
    @DisplayName("When return of validateLogin having error then generateToken throw exception")
    void whenValidateLoginError_ThenThrowException() {

        // Arrange

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE
        );

        doReturn(outputSearchUser).when(searchUser).searchUser(any());

        doThrow(ValidateLoginException.class).when(this.validateLogin).validate(any());

        // Act - Assert

        assertThrows(ValidateLoginException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });

    }

    @Test
    @DisplayName("When exception in create token then throw")
    void whenExceptionCreateToken_thenThrow() {

        // Arrange

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE
        );

        doReturn(outputSearchUser).when(searchUser).searchUser(any());

        doReturn(10).when(config).getExpirationTokenDays();

        doThrow(CreateTokenException.class).when(this.createToken).create(any());

        // Act - Assert

        assertThrows(CreateTokenException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });

    }

    @Test
    @DisplayName("When exception in cache token then throw")
    void whenExceptionCacheToken_thenThrow() {

        // Arrange

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE
        );

        doReturn(outputSearchUser).when(searchUser).searchUser(any());

        doReturn(10).when(config).getExpirationTokenDays();

        doReturn(easyRandom.nextObject(String.class)).when(createToken).create(any());

        doThrow(CacheTokenException.class).when(this.cacheToken).sendToCache(any());

        // Act - Assert

        assertThrows(CacheTokenException.class, () -> {
            this.generateToken.generate(this.defaultInput);
        });

    }

    @Test
    @DisplayName("When send valid input then return token")
    void whenSendValidInput_thenReturnToken(){

        // Arrange

        String tokenFake = easyRandom.nextObject(String.class);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE
        );

        doReturn(outputSearchUser).when(searchUser).searchUser(any());

        doReturn(10).when(config).getExpirationTokenDays();

        doReturn(tokenFake).when(createToken).create(any());

        // Act

        String token = generateToken.generate(defaultInput);

        // Assert

        assertEquals(token, tokenFake);
    }


}
