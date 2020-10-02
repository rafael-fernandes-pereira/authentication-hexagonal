package com.github.oindiao.application.port.out;

import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.NotEmpty;

public interface CacheToken {

    void sendToCache(Input input);

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

}
