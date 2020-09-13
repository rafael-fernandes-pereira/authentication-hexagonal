package com.github.oindiao.application.user.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface GenerateTokenUserPort {

    Optional<Output> gerar(Input input);

    @Builder
    @AllArgsConstructor
    @Getter
    class Input {

        @NotNull
        @NotEmpty
        @Email
        private final String email;

        @NotNull
        private final String password;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    class Output {

        @NotNull
        private final String token;

    }

}
