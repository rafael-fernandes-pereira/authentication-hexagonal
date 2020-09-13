package com.github.oindiao.application.user.domain;

import com.github.oindiao.common.exception.UserException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class User {

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private LocalDate expirationPasswordDate;

    @NotNull
    private Boolean active;

    @NotNull
    private Profile profile;

    private User(String email, String password, LocalDate expirationPasswordDate, Boolean active, Profile profile) {
        this.email = email;
        this.password = password;
        this.expirationPasswordDate = expirationPasswordDate;
        this.active = active;
        this.profile = profile;
    }

    public static User of(String email, String password, LocalDate expirationPasswordDate, Boolean active, String profile) {

        if (active == Boolean.FALSE) {
            throw new UserException("User inactive.");
        }

        LocalDate todayMinus120Days = LocalDate.now().minusDays(120);

        if (expirationPasswordDate.isBefore(todayMinus120Days) || expirationPasswordDate.isEqual(todayMinus120Days)) {
            throw new UserException("Password expiration");
        }

        Profile profileEnum;

        try {
            profileEnum = Profile.valueOf(profile);
        } catch (IllegalArgumentException e ){
            throw new UserException("Wrong profile.", e);
        }

        return new User(email, password, expirationPasswordDate, active, profileEnum);

    }

}