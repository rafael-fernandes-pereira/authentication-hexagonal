package com.github.oindiao.application.service;

import com.github.oindiao.application.config.UserConfig;
import com.github.oindiao.application.domain.Profile;
import com.github.oindiao.application.port.GenericInterface;
import com.github.oindiao.application.port.in.GenerateTokenUseCase;
import com.github.oindiao.application.port.out.*;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    @DisplayName("When invalid method from validateData return false then get notification")
    void invalidValidateData() {

        // Arrange

        GenericInterface.Notification notification = GenericInterface.Notification.create();
        ValidateData.Output validateDataOutput = ValidateData.Output.of(notification);

        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());

    }

    @Test
    @DisplayName("When has error in searchUser then get notification")
    void errorSearchUser() {

        // Arrange

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        GenericInterface.Notification notification = GenericInterface.Notification.create();
        notification.addError("Error");

        SearchUser.Output searchUserOuput = SearchUser.Output.error(notification);
        when(this.searchUser.execute(any())).thenReturn(searchUserOuput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());
    }

    @Test
    @DisplayName("When has error in create user domain then get notification")
    void errorCreateUserDomain() {

        // Arrange

        when(config.getExpirationTokenDays()).thenReturn(120);

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.FALSE);

        when(this.searchUser.execute(any())).thenReturn(outputSearchUser);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());
        assertThat(generateTokenUseCaseOutput.getNotification().getErrors(), hasItems("User inactive."));
    }

    @Test
    @DisplayName("When invalid method from validateLogin return false then get notification")
    void invalidValidateLogin() {

        // Arrange

        when(config.getExpirationTokenDays()).thenReturn(120);

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE);

        when(this.searchUser.execute(any())).thenReturn(outputSearchUser);

        GenericInterface.Notification notification = GenericInterface.Notification.create();
        ValidateLogin.Output validateLoginOutput = ValidateLogin.Output.of(notification);

        when(this.validateLogin.execute(any())).thenReturn(validateLoginOutput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());

    }

    @Test
    @DisplayName("When has error in create token then get notification")
    void errorCreateToken() {

        // Arrange

        when(config.getExpirationTokenDays()).thenReturn(120);

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE);
        when(this.searchUser.execute(any())).thenReturn(outputSearchUser);

        ValidateLogin.Output validateLoginOutput = ValidateLogin.Output.of(null);
        when(this.validateLogin.execute(any())).thenReturn(validateLoginOutput);

        GenericInterface.Notification notification = GenericInterface.Notification.create();
        notification.addError("Error");

        CreateToken.Output createTokenOutput = CreateToken.Output.error(notification);
        when(this.createToken.execute(any())).thenReturn(createTokenOutput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());

    }

    @Test
    @DisplayName("When has error in cache token then get notification")
    void errorCacheToken() {

        // Arrange

        when(config.getExpirationTokenDays()).thenReturn(120);

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE);
        when(this.searchUser.execute(any())).thenReturn(outputSearchUser);

        ValidateLogin.Output validateLoginOutput = ValidateLogin.Output.of(null);
        when(this.validateLogin.execute(any())).thenReturn(validateLoginOutput);

        CreateToken.Output createTokenOutput = CreateToken.Output.of(easyRandom.nextObject(String.class));
        when(this.createToken.execute(any())).thenReturn(createTokenOutput);

        GenericInterface.Notification notification = GenericInterface.Notification.create();
        CacheToken.Output cacheTokenOutput = CacheToken.Output.of(notification);
        when(this.cacheToken.execute(any())).thenReturn(cacheTokenOutput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertNull(generateTokenUseCaseOutput.getToken());

    }

    @Test
    @DisplayName("Valid if create token")
    void validToken(){

        // Arrange

        when(config.getExpirationTokenDays()).thenReturn(120);

        String token = easyRandom.nextObject(String.class);

        ValidateData.Output validateDataOutput = ValidateData.Output.of(null);
        when(this.validateData.execute(any())).thenReturn(validateDataOutput);

        SearchUser.Output outputSearchUser = SearchUser.Output.of(
                easyRandom.email(),
                easyRandom.password(),
                Arrays.asList(easyRandom.nextObject(Profile.class).name()),
                this.today.minusDays(30),
                Boolean.TRUE);
        when(this.searchUser.execute(any())).thenReturn(outputSearchUser);

        ValidateLogin.Output validateLoginOutput = ValidateLogin.Output.of(null);
        when(this.validateLogin.execute(any())).thenReturn(validateLoginOutput);

        CreateToken.Output createTokenOutput = CreateToken.Output.of(token);
        when(this.createToken.execute(any())).thenReturn(createTokenOutput);

        CacheToken.Output cacheTokenOutput = CacheToken.Output.of(null);
        when(this.cacheToken.execute(any())).thenReturn(cacheTokenOutput);

        // Act

        GenerateTokenUseCase.Output generateTokenUseCaseOutput = this.generateToken.execute(defaultInput);

        // Assert

        assertEquals(token, generateTokenUseCaseOutput.getToken());

    }


}
