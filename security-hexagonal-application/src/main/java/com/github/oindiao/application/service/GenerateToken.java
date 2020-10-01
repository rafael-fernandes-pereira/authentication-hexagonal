package com.github.oindiao.application.service;

import com.github.oindiao.application.config.UserConfig;
import com.github.oindiao.application.domain.User;
import com.github.oindiao.application.port.in.GenerateTokenUseCase;
import com.github.oindiao.application.port.out.*;
import com.github.oindiao.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenerateToken implements GenerateTokenUseCase {

    private final ValidateData validateData;
    private final SearchUser searchUser;
    private final ValidateLogin validateLogin;
    private final CreateToken createToken;
    private final UserConfig config;
    private final CacheToken cacheToken;

    public GenerateToken(ValidateData validateData, SearchUser searchUser, ValidateLogin validateLogin, CreateToken createToken, UserConfig config, CacheToken cacheToken){
        this.validateData = validateData;
        this.searchUser = searchUser;
        this.validateLogin = validateLogin;
        this.createToken = createToken;
        this.config = config;
        this.cacheToken = cacheToken;
    }

    @Override
    public String generate(Input input) {

        try {

            ValidateData.Input validateDataInput = ValidateData.Input.of(input.getEmail());

            this.validateData.validate(validateDataInput);

            SearchUser.Input inputSearchUser = SearchUser.Input.of(input.getEmail());

            SearchUser.Output outputSearchUser = searchUser.searchUser(inputSearchUser);

            User user = User.of(
                    outputSearchUser.getEmail(),
                    outputSearchUser.getPassword(),
                    outputSearchUser.getExpirationPasswordDate(),
                    outputSearchUser.getActive(),
                    outputSearchUser.getProfiles()
            );

            ValidateLogin.Input inputValidateUser = ValidateLogin.Input.of(user, input);

            validateLogin.validate(inputValidateUser);

            CreateToken.Input inputCreateToken = CreateToken.Input.of(user, config.getExpirationDays());

            String token = createToken.create(inputCreateToken);

            CacheToken.Input cacheTokenInput = CacheToken.Input.of(token);

            cacheToken.sendToCache(cacheTokenInput);

            return token;

        } catch (ValidateDataException | SearchUserException | ValidateLoginException | CreateTokenException | CacheTokenException e) {
            log.error("error in {}: {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (UserException e){
            log.error("error in create user domain: {}", e.getMessage());
            throw e;
        } catch (Exception e){
            log.error("error in service: {}", e.getMessage());
            throw new ServiceException("error in service", e);
        }

    }
}


