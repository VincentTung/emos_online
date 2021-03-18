package com.vincent.emos.wx.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现shiro的token
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
