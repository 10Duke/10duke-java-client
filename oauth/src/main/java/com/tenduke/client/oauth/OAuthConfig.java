/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.net.URI;

/**
 * Base OAuth-configuration.
 *
 * <p>
 * Instances are immutable.
 *
 */
public class OAuthConfig {

    /** Authorization endpoint. */
    private final URI authorizationEndpoint;

    /** Client id. */
    private final String clientId;

    /** Redirect URI. */
    private final URI redirectUri;

    /**
     * Constructs new instance.
     *
     * @param clientId -
     * @param authorizationEndpoint -
     * @param redirectUri -
     */
    public OAuthConfig(
            final String clientId,
            final URI authorizationEndpoint,
            final URI redirectUri
    ) {
        this.authorizationEndpoint = authorizationEndpoint;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public URI getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public URI getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
