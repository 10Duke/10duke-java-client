/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeConfig;
import java.net.URI;
import java.security.Key;
import javax.annotation.Nullable;

/**
 * OAuth 2 configuration for OpenId Connect with Authorization Code flow.
 *
 */
public class OpenIdAuthorizationCodeConfig extends AuthorizationCodeConfig {

    /** Issuer. */
    private final String issuer;

    /** Key for verifying the ID-token signature. */
    private final Key signatureVerificationKey;

    /**
     * Constructs new instance.
     *
     * @param clientId -
     * @param authorizationEndpoint -
     * @param redirectUri -
     * @param tokenEndpoint -
     * @param clientSecret -
     * @param issuer -
     * @param signatureVerificationKey -
     */
    public OpenIdAuthorizationCodeConfig(
            final String clientId,
            final URI authorizationEndpoint,
            final URI redirectUri,
            final URI tokenEndpoint,
            final @Nullable String clientSecret,
            final String issuer,
            final Key signatureVerificationKey
    ) {
        super(clientId, authorizationEndpoint, redirectUri, tokenEndpoint, clientSecret);

        this.issuer = issuer;
        this.signatureVerificationKey = signatureVerificationKey;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public String getIssuer() {
        return issuer;
    }

    public Key getSignatureVerificationKey() {
        return signatureVerificationKey;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
