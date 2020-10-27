package com.github.oindiao.application.service;

import com.github.oindiao.application.config.UserConfig;
import com.github.oindiao.application.domain.User;
import com.github.oindiao.application.port.in.GenerateTokenUseCase;
import com.github.oindiao.application.port.out.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenerateToken implements GenerateTokenUseCase {

    private final ValidateData validateData;
    private final SearchUser searchUser;
    private final ValidateLogin validateLogin;
    private final CreateToken createToken;
    private final CacheToken cacheToken;
    private final UserConfig config;

    public GenerateToken(ValidateData validateData, SearchUser searchUser, ValidateLogin validateLogin, CreateToken createToken, UserConfig config, CacheToken cacheToken){
        this.validateData = validateData;
        this.searchUser = searchUser;
        this.validateLogin = validateLogin;
        this.createToken = createToken;
        this.config = config;
        this.cacheToken = cacheToken;
    }

    @Override
    public GenerateTokenUseCase.Output execute(Input input) {

        ValidateData.Input validateDataInput = ValidateData.Input.of(input.getEmail());
        ValidateData.Output validateDataOutput = this.validateData.execute(validateDataInput);

        if (validateDataOutput.invalid()){
            return GenerateTokenUseCase.Output.error(validateDataOutput.getNotification());
        }

        SearchUser.Input searchUserInput = SearchUser.Input.of(input.getEmail());
        SearchUser.Output outputSearchUser = this.searchUser.execute(searchUserInput);

        if (outputSearchUser.getNotification().hasError()){
            return GenerateTokenUseCase.Output.error(outputSearchUser.getNotification());
        }

        User user = User.of(
                outputSearchUser.getEmail(),
                outputSearchUser.getPassword(),
                outputSearchUser.getExpirationPasswordDate(),
                this.config.getExpirationTokenDays(),
                outputSearchUser.getActive(),
                outputSearchUser.getProfiles()
        );

        if (user.getNotification().hasError()){
            return GenerateTokenUseCase.Output.error(user.getNotification());
        }

        ValidateLogin.Input validateLoginInput = ValidateLogin.Input.of(user, input);
        ValidateLogin.Output validateLoginOutput = this.validateLogin.execute(validateLoginInput);

        if (validateLoginOutput.invalid()){
            return GenerateTokenUseCase.Output.error(validateLoginOutput.getNotification());
        }

        CreateToken.Input createTokenInput = CreateToken.Input.of(user, config.getExpirationTokenDays());
        CreateToken.Output createTokenOutput = this.createToken.execute(createTokenInput);

        if (createTokenOutput.getNotification().hasError()){
            return GenerateTokenUseCase.Output.error(createTokenOutput.getNotification());
        }

        CacheToken.Input cacheTokenInput = CacheToken.Input.of(createTokenOutput.getToken());
        CacheToken.Output cacheTokenOutput = this.cacheToken.execute(cacheTokenInput);

        if (cacheTokenOutput.invalid()){
            return GenerateTokenUseCase.Output.error(cacheTokenOutput.getNotification());
        }

        return GenerateTokenUseCase.Output.of(createTokenOutput.getToken());

    }
}


