/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.authorizationcode.AbstractAuthorizationCodeRequest;
import java.util.Map;
import java.util.Set;

/**
 * OAuth 2 request for OpenId Connect with Authorization Code flow.
 *
 */
public class OpenIdAuthorizationCodeRequest extends AbstractAuthorizationCodeRequest<OpenIdAuthorizationCodeConfig> {

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param parameters -
     * @param scopes -
     * @param state -
     */
    public OpenIdAuthorizationCodeRequest(
            final OpenIdAuthorizationCodeConfig config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state
    ) {
        super(config, parameters, scopes, state);
    }

}
