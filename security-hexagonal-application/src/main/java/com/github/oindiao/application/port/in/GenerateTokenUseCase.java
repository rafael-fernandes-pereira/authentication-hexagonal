package com.github.oindiao.application.port.in;

import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface GenerateTokenUseCase {

    String generate(Input input);

    class Input extends SelfValidating<GenerateTokenUseCase.Input> {

        @NotEmpty
        @Email
        private final String email;

        @NotEmpty
        private final String password;

        private Input(@NotEmpty @Email String email, @NotEmpty String password) {
            this.email = email;
            this.password = password;
            this.validateSelf();
        }

        public static Input of(String email, String password) {
            return new Input(email, password);
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

}
