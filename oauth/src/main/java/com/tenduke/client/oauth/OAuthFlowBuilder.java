/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.util.Collection;
import java.util.Map;

/**
 * Builds OAuth flow.
 *
 *
 */
public interface OAuthFlowBuilder {

    /**
     * Adds a custom parameter to the request.
     *
     * @param name -
     * @param value -
     * @return -
     */
    OAuthFlowBuilder parameter(String name, String value);

    /**
     * Adds given custom parameters to the request.
     *
     * @param values -
     * @return -
     */
    OAuthFlowBuilder parameters(Map<String, String> values);

    /**
     * Adds a scope to the request.
     *
     * @param scope -
     * @return -
     */
    OAuthFlowBuilder scope(String scope);

    /**
     * Adds given scopes to the request.
     *
     * @param scopes -
     * @return -
     */
    OAuthFlowBuilder scopes(Collection<String> scopes);

    /**
     * Adds given scopes to the request.
     *
     * @param scopes -
     * @return -
     */
    OAuthFlowBuilder scopes(String... scopes);

    /**
     * Sets the request state, i.e.&nbsp;identifier for the communication.
     *
     * @param state -
     * @return -
     */
    OAuthFlowBuilder state(String state);

    /**
     * Starts the flow.
     *
     * @return -
     */
    OAuthFlow start();
}
