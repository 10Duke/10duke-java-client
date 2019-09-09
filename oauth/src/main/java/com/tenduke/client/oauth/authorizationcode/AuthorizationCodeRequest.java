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
        super(config, parameters, scopes, state);
    }

}
