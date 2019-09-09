/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.OAuthResponse;

/**
 * Success-response for Authorization Code flow.
 *
 */
public class AuthorizationCodeResponse extends OAuthResponse {

    private static final long serialVersionUID = 1L;

    // NOTE: Do not change names of these fields. They are mapped from JSON.

    /** Refresh token. */
    private final String refreshToken;

    /**
     * No-arg constructor. Some JSON-serializers need this.
     *
     */
    protected AuthorizationCodeResponse() {
        this(null, null, -1, null);
    }

    /**
     * Constructs new instance.
     *
     * @param accessToken -
     * @param refreshToken -
     * @param expiresIn -
     * @param tokenType -
     */
    public AuthorizationCodeResponse(
            final String accessToken,
            final String refreshToken,
            final long expiresIn,
            final String tokenType
    ) {
        super(accessToken, expiresIn, tokenType);
        this.refreshToken = refreshToken;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public String getRefreshToken() {
        return refreshToken;
    }

    // CSON: JavadocMethod
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString()">

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public String toString() {
        return "AuthorizationCodeResponse{"
                + "accessToken=" + getAccessToken()
                + ", refreshToken=" + refreshToken
                + ", expiresIn=" + getExpiresIn()
                + ", tokenType=" + getTokenType()
                + '}';
    }

    // </editor-fold>

}
