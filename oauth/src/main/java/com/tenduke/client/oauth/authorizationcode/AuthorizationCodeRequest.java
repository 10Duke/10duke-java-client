/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Request for initiating OAuth 2 Authorization Code flow.
 *
 */
public class AuthorizationCodeRequest extends AbstractAuthorizationCodeRequest<AuthorizationCodeConfig> {

    /** Constructs new instance.
     *
     * @param config configuration
     * @param parameters custom parameters
     * @param scopes requested scopes
     * @param state OAuth state
     */
    public AuthorizationCodeRequest(
            final AuthorizationCodeConfig config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state
    ) {
        this(config, parameters, scopes, state, null);
    }

    /** Constructs new instance.
     *
     * @param config configuration
     * @param parameters custom parameters
     * @param scopes requested scopes
     * @param state OAuth state
     * @param codeVerifier PKCE code verifier (RFC 7637). If {@code null}, PKCE is not used.
     */
    public AuthorizationCodeRequest(
            final AuthorizationCodeConfig config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state,
            @Nullable final String codeVerifier
    ) {
        super(config, parameters, scopes, state, codeVerifier);
    }

}
