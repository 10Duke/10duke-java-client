/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.OAuthRequest;
import java.util.Map;
import java.util.Set;

/**
 * Base for Authorization Code requests.
 *
 * @param <C> type of the OAuth configuration
 */
public class AbstractAuthorizationCodeRequest<C extends AuthorizationCodeConfig> extends OAuthRequest<C> {

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param parameters -
     * @param scopes -
     * @param state -
     */
    public AbstractAuthorizationCodeRequest(
            final C config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state
    ) {
        super(config, parameters, "code", scopes, state);
    }

}
