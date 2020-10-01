package com.github.oindiao.application.port.out;

import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface ValidateData {

    void validate(Input input);

    class Input extends SelfValidating<ValidateData.Input>{

        @NotEmpty
        @Email
        private final String email;

        private Input(String email){
            this.email = email;
            this.validateSelf();
        }

        public static Input of(String email){
            return new Input(email);
        }

        public String getEmail() {
            return email;
        }
    }




}
