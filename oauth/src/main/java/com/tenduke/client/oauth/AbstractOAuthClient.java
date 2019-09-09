/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

/**
 * Abstract base of {@link OAuthClient}.
 *
 * @param <C> type of config used by this client
 */
public abstract class AbstractOAuthClient<C extends OAuthConfig> implements OAuthClient {

    /**
     * OAuth configuration used by this client instance.
     */
    private final C config;

    /**
     * Constructs new instance.
     *
     * @param config -
     */
    protected AbstractOAuthClient(final C config) {
        this.config = config;
    }

    /**
     * Returns the configuration used by this client instance.
     *
     * @return -
     */
    public C getConfig() {
        return config;
    }

}
