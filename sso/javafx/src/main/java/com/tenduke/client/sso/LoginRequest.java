/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso;

import com.tenduke.client.openid.OpenIdAuthorizationCodeClient;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Login request.
 *
 * <p>
 * Encapsulates a login request, containing OpenID Connect -client, requested scopes and additional parameters. To configure
 * the {@link OpenIdAuthorizationCodeClient}, see the package level documentation.
 *
 * <p>
 * Scopes are the OAuth-scopes caller wishes to request authorization for. Note that {@link OpenIdAuthorizationCodeClient} adds the
 * {@code openid}-scope automatically.
 *
 * <p>
 * Parameters allow additional parameters to be added to the request. {@link OpenIdAuthorizationCodeClient} automatically adds
 * certain required parameters. A randomly-generated nonce is also automatically added.
 *
 * <p>
 * Scopes and parameters are automatically URL-encoded by {@link OpenIdAuthorizationCodeClient}.
 */
public class LoginRequest {

    /** The client. */
    private final OpenIdAuthorizationCodeClient client;

    /** Requested scopes. */
    private final LinkedHashSet<String> scopes = new LinkedHashSet<>();

    /** Additional request parameters. */
    private final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    /**
     * Constructs new instance.
     *
     * @param client -
     * @param scopes -
     */
    public LoginRequest(
            final OpenIdAuthorizationCodeClient client,
            final String... scopes
    ) {
        this(
                client,
                Collections.emptyMap(),
                new LinkedHashSet<String>(Arrays.asList(scopes))
        );
    }

    /**
     * Constructs new instance.
     *
     * @param client -
     * @param parameters -
     * @param scopes -
     */
    public LoginRequest(
            final OpenIdAuthorizationCodeClient client,
            final Map<String, String> parameters,
            final Set<String> scopes
    ) {
        this.client = client;
        this.scopes.addAll(scopes);
        this.parameters.putAll(parameters);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public OpenIdAuthorizationCodeClient getClient() {
        return client;
    }

    public LinkedHashSet<String> getScopes() {
        return scopes;
    }

    public LinkedHashMap<String, String> getParameters() {
        return parameters;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
