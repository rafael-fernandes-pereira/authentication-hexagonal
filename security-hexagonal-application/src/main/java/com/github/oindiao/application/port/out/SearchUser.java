package com.github.oindiao.application.port.out;

import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface SearchUser {

    Output searchUser(Input input);

    class Input extends SelfValidating<SearchUser.Input> {

        @Email
        @NotEmpty
        private final String email;

        private Input(String email) {
            this.email = email;
            this.validateSelf();
        }

        public static Input of(String email) {
            return new Input(email);
        }

        public String getEmail() {
            return email;
        }
    }

    class Output extends SelfValidating<SearchUser.Output> {

        @NotEmpty
        @Email
        private final String email;

        @NotEmpty
        private final String password;

        @NotNull
        private final List<String> profiles;

        @NotNull
        private final LocalDate expirationPasswordDate;

        private final Boolean active;

        private Output(String email, String password, List<String> profiles, @NotNull LocalDate expirationPasswordDate, Boolean active) {
            this.email = email;
            this.password = password;
            this.profiles = profiles;
            this.expirationPasswordDate = expirationPasswordDate;
            this.active = active;
            this.validateSelf();

        }

        public static Output of(String email, String password, List<String> profiles, LocalDate expirationPasswordDate, Boolean active){
            return new Output(email, password, profiles, expirationPasswordDate, active);
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public List<String> getProfiles() {
            return profiles;
        }

        public Boolean getActive() {
            return active;
        }

        public LocalDate getExpirationPasswordDate() {
            return expirationPasswordDate;
        }

    }

}
