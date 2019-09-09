/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

/**
 * OpenId Connect success-response.
 *
 */
public interface OpenIdConnectResponse {

    /**
     * Returns Id-token.
     *
     * @return -
     */
    IdToken getIdToken();
}
