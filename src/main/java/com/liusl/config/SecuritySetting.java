package com.liusl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther liusl12
 * @date 2018/5/8.
 */
@Component
@ConfigurationProperties(prefix = "jwt.security")
@EnableConfigurationProperties(SecuritySetting.class)
public class SecuritySetting {
    private String securityToken;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
