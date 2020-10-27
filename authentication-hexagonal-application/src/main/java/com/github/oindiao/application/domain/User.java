package com.github.oindiao.application.domain;

import com.github.oindiao.application.port.GenericInterface;
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
    private String email;

    @NotEmpty	
    private String password;

    private Boolean active;

    @NotNull
    private List<Profile> profiles;

    private GenericInterface.Notification notification;

    private User(String email, String password, LocalDate expirationPasswordDate, Boolean active, List<Profile> profiles) {
        this.email = email;
        this.password = password;
        this.active = active;
        this.profiles = profiles;
        this.notification = GenericInterface.Notification.create();
        this.validateSelf();
    }

    private User(GenericInterface.Notification notification){
        this.notification = notification;
    }


    public static User of(String email, String password, LocalDate expirationPasswordDate, Integer expirationTokenDays, Boolean active, List<String> profiles) {

        GenericInterface.Notification notification = GenericInterface.Notification.create();

        if (active == Boolean.FALSE) {
            notification.addError("User inactive.");
        }

        LocalDate todayMinus120Days = LocalDate.now().minusDays(expirationTokenDays);

        if (expirationPasswordDate.isBefore(todayMinus120Days) || expirationPasswordDate.isEqual(todayMinus120Days)) {
            notification.addError("Password is expired");
        }

        ArrayList<Profile> profilesEnum = new ArrayList<>();

        for (String profile : profiles) {
            try {
                profilesEnum.add(Profile.valueOf(profile));
            } catch (IllegalArgumentException e ){
                notification.addError(String.format("Wrong profile: %s", profile));
            }

        }

        if (notification.hasError()) {
            return new User(notification);
        }
        return new User(email, password, expirationPasswordDate, active, profilesEnum);

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public GenericInterface.Notification getNotification() {
        return notification;
    }
}