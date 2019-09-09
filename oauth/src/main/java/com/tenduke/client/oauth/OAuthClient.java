/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

/**
 * OAuth-client.
 *
 */
public interface OAuthClient {

    /**
     * Starts building new {@link OAuthFlow}.
     *
     * @return -
     */
    OAuthFlowBuilder request();

}
