package com.github.oindiao.application.domain;

import com.github.oindiao.common.exception.UserException;
import com.github.oindiao.common.validation.SelfValidating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends SelfValidating<User>{

    @NotEmpty
    @Email
    private final String email;

    @NotEmpty	
    private final String password;

    @NotNull
    private final LocalDate expirationPasswordDate;

    private final Boolean active;

    @NotNull
    private List<Profile> profiles;

    private User(String email, String password, LocalDate expirationPasswordDate, Boolean active, List<Profile> profiles) {
        this.email = email;
        this.password = password;
        this.expirationPasswordDate = expirationPasswordDate;
        this.active = active;
        this.profiles = profiles;
        this.validateSelf();
    }

    public static User of(String email, String password, LocalDate expirationPasswordDate, Integer expirationTokenDays, Boolean active, List<String> profiles) {

        if (active == Boolean.FALSE) {
            throw new UserException("User inactive.");
        }

        LocalDate todayMinus120Days = LocalDate.now().minusDays(120);

        if (expirationPasswordDate.isBefore(todayMinus120Days) || expirationPasswordDate.isEqual(todayMinus120Days)) {
            throw new UserException("Password expiration");
        }

        ArrayList<Profile> profilesEnum = new ArrayList<>();

        for (String profile : profiles) {
            try {
                profilesEnum.add(Profile.valueOf(profile));
            } catch (IllegalArgumentException e ){
                throw new UserException("Wrong profile.", e);
            }

        }

        return new User(email, password, expirationPasswordDate, active, profilesEnum);

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getExpirationPasswordDate() {
        return expirationPasswordDate;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }
}