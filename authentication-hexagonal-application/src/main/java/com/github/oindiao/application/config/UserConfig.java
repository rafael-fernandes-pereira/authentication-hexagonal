package com.github.oindiao.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserConfig {

    @Value("${expiration.token.days}")
    private Integer expirationTokenDays;

    public Integer getExpirationTokenDays() {
        return expirationTokenDays;
    }
}
