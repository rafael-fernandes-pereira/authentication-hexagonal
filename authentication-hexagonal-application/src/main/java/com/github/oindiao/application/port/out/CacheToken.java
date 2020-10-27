package com.github.oindiao.application.port.out;

import com.github.oindiao.application.port.GenericInterface;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.NotEmpty;

public interface CacheToken extends GenericInterface<CacheToken.Input, CacheToken.Output>{

    class Input extends SelfValidating<CacheToken.Input> {

        @NotEmpty
        private final String token;

        private Input (String token){
            this.token = token;
            this.validateSelf();
        }

        public static Input of (String token){
            return new Input(token);
        }

        public String getToken() {
            return token;
        }
    }

    class Output {

        private Notification notification;

        private Output(Notification notification){
            this.notification = notification;
        }

        public static Output of(Notification notification){
            return new Output(notification);
        }

        public Boolean invalid() {
            return this.notification != null;
        }

        public Notification getNotification() {
            return notification;
        }

    }

}
