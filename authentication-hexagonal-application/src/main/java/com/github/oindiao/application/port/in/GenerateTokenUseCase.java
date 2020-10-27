package com.github.oindiao.application.port.in;

import com.github.oindiao.application.port.GenericInterface;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface GenerateTokenUseCase extends GenericInterface<GenerateTokenUseCase.Input, GenerateTokenUseCase.Output> {

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

    class Output extends SelfValidating<GenerateTokenUseCase.Output> {

        @NotEmpty
        private String token;

        private Notification notification;

        private Output(String token) {
            this.token = token;
            this.validateSelf();
        }

        private Output(Notification notification) {
            this.notification = notification;
        }

        public static Output of (String token){
            return new Output(token);
        }

        public static Output error (Notification notification){
            return new Output(notification);
        }

        public String getToken() {
            return token;
        }

        public Notification getNotification() {
            return notification;
        }
    }

}
