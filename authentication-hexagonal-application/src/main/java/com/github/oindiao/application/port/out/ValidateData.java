package com.github.oindiao.application.port.out;

import com.github.oindiao.application.port.GenericInterface;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface ValidateData extends GenericInterface<ValidateData.Input, ValidateData.Output> {

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

    class Output {

        private final Notification notification;

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
