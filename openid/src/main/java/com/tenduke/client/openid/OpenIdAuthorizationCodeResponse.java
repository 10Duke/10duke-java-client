/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;

/**
 * Success-response return by OpenId Connect with Authorization Code flow.
 *
 */
public class OpenIdAuthorizationCodeResponse extends AuthorizationCodeResponse implements OpenIdConnectResponse {

    private static final long serialVersionUID = 1L;

    /** ID-token. */
    private final IdToken idToken;

    /**
     * No-arg constructor. Needed by certain JSON deserializers.
     *
     */
    protected OpenIdAuthorizationCodeResponse() {
        this(null, null, -1, null, null);
    }

    /**
     * Constructs new instance.
     *
     * @param accessToken -
     * @param refreshToken -
     * @param expiresIn -
     * @param tokenType -
     * @param idToken -
     */
    public OpenIdAuthorizationCodeResponse(
            final String accessToken,
            final String refreshToken,
            final long expiresIn,
            final String tokenType,
            final IdToken idToken
    ) {
        super(accessToken, refreshToken, expiresIn, tokenType);

        this.idToken = idToken;
    }

    /**
     * Constructs new instance.
     *
     * <p>
     * Copies the {@code copy} and adds id token.
     *
     * @param copy -
     * @param idToken -
     */
    public OpenIdAuthorizationCodeResponse(
            final AuthorizationCodeResponse copy,
            final IdToken idToken
    ) {
        this(
                copy.getAccessToken(),
                copy.getRefreshToken(),
                copy.getExpiresIn(),
                copy.getTokenType(),
                idToken
        );
        super.zetAdditionalProperties(copy.getAdditionalProperties());
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    @Override
    public IdToken getIdToken() {
        return idToken;
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
        return "OpenIdAuthorizationCodeResponse{"
                + "accessToken=" + getAccessToken()
                + ", refreshToken=" + getRefreshToken()
                + ", expiresIn=" + getExpiresIn()
                + ", tokenType=" + getTokenType()
                + ", idToken=" + idToken
                + '}';
    }

    // </editor-fold>



}
