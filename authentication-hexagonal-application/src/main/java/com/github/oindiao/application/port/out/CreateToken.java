package com.github.oindiao.application.port.out;

import com.github.oindiao.application.domain.Profile;
import com.github.oindiao.application.domain.User;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface CreateToken {

    String create(CreateToken.Input input);

    class Input extends SelfValidating<CreateToken.Input> {

        @NotEmpty
        @Email
        private final String email;

        @NotNull
        private final List<String> profiles;

        @NotNull
        private final LocalDate expirationToken;

        private Input(@NotEmpty @Email String email, @NotNull List<String> profiles, @NotNull LocalDate expirationToken) {
            this.email = email;
            this.profiles = profiles;
            this.expirationToken = expirationToken;
            this.validateSelf();
        }

        public static Input of(User user, Integer days) {

            List<String> profilesString = user.getProfiles()
                .stream()
                .map(Profile::name)
            .collect(Collectors.toList());

            LocalDate expirationToken = LocalDate.now().plusDays(days);

            return new Input(user.getEmail(), profilesString, expirationToken);
        }

        public String getEmail() {
            return email;
        }

        public List<String> getProfiles() {
            return profiles;
        }

        public LocalDate getExpirationToken() {
            return expirationToken;
        }
    }

}
