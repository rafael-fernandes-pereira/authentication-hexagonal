package com.github.oindiao.application.port.out;

import com.github.oindiao.application.domain.User;
import com.github.oindiao.application.port.in.GenerateTokenUseCase;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface ValidateLogin {

    void validate(ValidateLogin.Input input);

    class Input extends SelfValidating<ValidateLogin.Input> {

        @Email
        @NotEmpty
        private final String email;

        @NotEmpty
        private final String password;

        @NotNull
        private final GenerateTokenUseCase.Input genereteTokenUseCaseInput;

        private Input(@NotNull String email, @NotNull String password, GenerateTokenUseCase.@NotNull Input genereteTokenUseCaseInput) {
            this.email = email;
            this.password = password;
            this.genereteTokenUseCaseInput = genereteTokenUseCaseInput;
            this.validateSelf();
        }

        public static Input of (User user, GenerateTokenUseCase.Input genereteTokenUseCaseInput) {
            return new Input(user.getEmail(), user.getPassword(), genereteTokenUseCaseInput);
        }

        public GenerateTokenUseCase.Input getGenereteTokenUseCaseInput() {
            return genereteTokenUseCaseInput;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

}
