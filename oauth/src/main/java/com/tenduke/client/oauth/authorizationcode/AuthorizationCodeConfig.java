/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.OAuthConfig;
import java.net.URI;
import javax.annotation.Nullable;

/**
 * Configuration for Authorization Code flow.
 *
 */
public class AuthorizationCodeConfig extends OAuthConfig {

    /** Client secret. */
    private @Nullable final String clientSecret;

    /** Token endpoint URI. */
    private final URI tokenEndpoint;

    /**
     * Constructs new instance.
     *
     * @param clientId -
     * @param authorizationEndpoint -
     * @param redirectUri -
     * @param tokenEndpoint -
     * @param clientSecret -
     */
    public AuthorizationCodeConfig(
            final String clientId,
            final URI authorizationEndpoint,
            final URI redirectUri,
            final URI tokenEndpoint,
            final @Nullable String clientSecret
    ) {
        super(clientId, authorizationEndpoint, redirectUri);

        this.clientSecret = clientSecret;
        this.tokenEndpoint = tokenEndpoint;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public @Nullable String getClientSecret() {
        return clientSecret;
    }

    public URI getTokenEndpoint() {
        return tokenEndpoint;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
