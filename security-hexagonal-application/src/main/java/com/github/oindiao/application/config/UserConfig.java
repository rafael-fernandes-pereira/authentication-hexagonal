package com.github.oindiao.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserConfig {

    @Value("${expiration.days}")
    private Integer expirationDays;

    public Integer getExpirationDays() {
        return expirationDays;
    }
}
